import cv2
import time
import requests
import serial
from ultralytics import YOLO

# ==========================================
# CONFIGURAÇÕES DO SISTEMA
# ==========================================
# Modelo de teste padrão (o download é feito automaticamente na 1ª vez)
# Quando tiver o arquivo treinado do TCC, altere para: 'melhor_modelo_epi.pt'
MODELO_PATH = 'yolov8n.pt' 

BACKEND_URL = 'http://localhost:5000/api/acesso/validar'
PORTA_SERIAL = 'COM3'  # Ajuste para a porta do seu Arduino (ou '/dev/ttyACM0' no Raspberry)
BAUD_RATE = 9600
TEMPO_VALIDACAO_SEGUNDOS = 3.0

# EPIs obrigatórios exigidos pelo sistema
EPI_OBRIGATORIOS = {
    'capacete',
    'oculos_protecao',
    'botina_seguranca',
    'luvas',
    'vestimenta_atpv'
}

# ==========================================
# INICIALIZAÇÃO
# ==========================================
print("[INICIALIZANDO] Carregando modelo YOLO...")
model = YOLO(MODELO_PATH)

# Conexão Serial com o Arduino
try:
    arduino = serial.Serial(PORTA_SERIAL, BAUD_RATE, timeout=0.1)
    time.sleep(2)
    hardware_conectado = True
    print(f"[HARDWARE] Arduino conectado na porta {PORTA_SERIAL}.")
except Exception as e:
    hardware_conectado = False
    arduino = None
    print(f"[AVISO] Arduino não detectado na porta {PORTA_SERIAL}. Rodando sem comunicação Serial.")

# Inicialização da Câmera
cap = cv2.VideoCapture(0)
if not cap.isOpened():
    print("[ERRO] Não foi possível acessar a webcam.")
    exit()

tempo_inicio_aprovacao = None
rfid_habilitado = False
status_mensagem = "Aguardando operador..."
cor_status = (255, 255, 255)

def enviar_requisicao_backend(cartao_uid, status_liberacao, epis_detectados):
    """Envia os registros de acesso para a API Backend."""
    payload = {
        "cartao_uid": cartao_uid,
        "status": "LIBERADO" if status_liberacao else "NEGADO",
        "epis_detectados": list(epis_detectados),
        "timestamp": time.time()
    }
    try:
        res = requests.post(BACKEND_URL, json=payload, timeout=2)
        print(f"[API] Resposta do Backend: {res.status_code}")
    except Exception as err:
        print(f"[ERRO API] Falha de comunicação com o Backend: {err}")

# ==========================================
# LOOP PRINCIPAL
# ==========================================
print("[SISTEMA PRONTO] Pressione 'q' na janela do vídeo para fechar.")

while cap.isOpened():
    success, frame = cap.read()
    if not success:
        print("[ERRO] Falha ao ler frame da câmera.")
        break

    # 1. Detecção com IA
    results = model(frame, conf=0.50, verbose=False)
    detectados = set()

    for r in results:
        for box in r.boxes:
            cls_id = int(box.cls[0])
            label_name = model.names[cls_id].lower()
            detectados.add(label_name)

    # 2. Checklist
    epis_presentes = detectados.intersection(EPI_OBRIGATORIOS)
    epis_faltantes = EPI_OBRIGATORIOS - epis_presentes
    checklist_completo = (len(epis_faltantes) == 0)

    # 3. Lógica do Temporizador de Estabilidade (3 segundos)
    if checklist_completo:
        if tempo_inicio_aprovacao is None:
            tempo_inicio_aprovacao = time.time()
        
        tempo_decorrido = time.time() - tempo_inicio_aprovacao

        if tempo_decorrido >= TEMPO_VALIDACAO_SEGUNDOS:
            if not rfid_habilitado:
                rfid_habilitado = True
                status_mensagem = "EPIs OK! APROXIME O CARTAO RFID"
                cor_status = (0, 255, 0)
                if hardware_conectado:
                    arduino.write(b"HABILITAR_RFID\n")
        else:
            progresso = int(tempo_decorrido)
            status_mensagem = f"MANTENHA A POSICAO ({progresso}/{int(TEMPO_VALIDACAO_SEGUNDOS)}s)..."
            cor_status = (0, 255, 255)
    else:
        tempo_inicio_aprovacao = None
        rfid_habilitado = False
        status_mensagem = "EPIs INCOMPLETOS - ACESSO BLOQUEADO"
        cor_status = (0, 0, 255)

    # 4. Leitura do Leitor RFID / Arduino
    if hardware_conectado and arduino.in_waiting > 0:
        try:
            linha_serial = arduino.readline().decode('utf-8', errors='ignore').strip()
            
            if linha_serial == "PRESENCA_DETECTADA":
                print("[MAQUETE] Sensor ultrassônico detectou operador.")

            elif linha_serial.startswith("CARTAO_LIDO:"):
                uid_cartao = linha_serial.split(":")[1]
                print(f"[MAQUETE] Cartão RFID Lido: {uid_cartao}")

                if checklist_completo and rfid_habilitado:
                    print("[ACESSO PERMITIDO] Liberando trava...")
                    arduino.write(b"LIBERAR_PORTA\n")
                    enviar_requisicao_backend(uid_cartao, True, epis_presentes)
                    status_mensagem = f"ACESSO LIBERADO! ({uid_cartao})"
                    cor_status = (0, 255, 0)
                    tempo_inicio_aprovacao = None
                    rfid_habilitado = False
                else:
                    print("[ACESSO NEGADO] EPIs incompletos.")
                    arduino.write(b"NEGADO\n")
                    enviar_requisicao_backend(uid_cartao, False, epis_presentes)
                    status_mensagem = "ACESSO NEGADO - EPIs FALTANTES"
                    cor_status = (0, 0, 255)

        except Exception as e:
            print(f"[ERRO SERIAL] {e}")

    # 5. Interface Visual (OpenCV)
    annotated_frame = results[0].plot()

    # Barra superior
    cv2.rectangle(annotated_frame, (0, 0), (annotated_frame.shape[1], 45), (30, 30, 30), -1)
    cv2.putText(annotated_frame, status_mensagem, (15, 30),
                cv2.FONT_HERSHEY_SIMPLEX, 0.7, cor_status, 2)

    # Checklist na tela
    y_offset = 75
    cv2.putText(annotated_frame, "CHECKLIST SUBESTACAO:", (10, y_offset),
                cv2.FONT_HERSHEY_SIMPLEX, 0.6, (255, 255, 255), 2)

    for epi in sorted(EPI_OBRIGATORIOS):
        y_offset += 25
        if epi in epis_presentes:
            txt = f"[OK] {epi.upper()}"
            color = (0, 255, 0)
        else:
            txt = f"[X]  {epi.upper()}"
            color = (0, 0, 255)
        
        cv2.putText(annotated_frame, txt, (15, y_offset),
                    cv2.FONT_HERSHEY_SIMPLEX, 0.5, color, 2)

    # Exibe a janela de vídeo
    cv2.imshow("Scanner de Acesso - SENAI", annotated_frame)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
if hardware_conectado and arduino:
    arduino.close()
cv2.destroyAllWindows()