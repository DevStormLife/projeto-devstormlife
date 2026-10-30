import cv2
import time
import requests
import serial
from ultralytics import YOLO

# ==========================================
# CONFIGURAÇÕES DO SISTEMA
# ==========================================
MODELO_PATH = 'melhor_modelo_epi.pt'
BACKEND_URL = 'http://localhost:5000/api/acesso/validar'
PORTA_SERIAL = 'COM3'  # Altere para a porta do seu Arduino (ex: '/dev/ttyUSB0' no Linux)
BAUD_RATE = 9600
TEMPO_VALIDACAO_SEGUNDOS = 3.0  # Tempo de permanência exigido com os 5 EPIs

# Conjunto de EPIs obrigatórios para a Subestação
EPI_OBRIGATORIOS = {
    'capacete',
    'oculos_protecao',
    'botina_seguranca',
    'luvas',
    'vestimenta_atpv'
}

# ==========================================
# INICIALIZAÇÃO DE HARDWARE E IA
# ==========================================
print("[INICIALIZANDO] Carregando modelo YOLO...")
model = YOLO(MODELO_PATH)

# Conexão Serial com o Arduino da Maquete
try:
    arduino = serial.Serial(PORTA_SERIAL, BAUD_RATE, timeout=0.1)
    time.sleep(2)  # Aguarda estabilização da conexão serial
    hardware_conectado = True
    print(f"[HARDWARE] Arduino conectado na porta {PORTA_SERIAL}.")
except Exception as e:
    hardware_conectado = False
    arduino = None
    print(f"[AVISO] Arduino não detectado na porta {PORTA_SERIAL}. Rodando em modo simulação. Erro: {e}")

# Inicialização da Câmera do Notebook
cap = cv2.VideoCapture(0)
if not cap.isOpened():
    print("[ERRO] Não foi possível acessar a câmera do notebook.")
    exit()

# Variáveis de Controle de Estado
tempo_inicio_aprovacao = None
rfid_habilitado = False
status_mensagem = "Aguardando operador..."
cor_status = (255, 255, 255)

def enviar_requisicao_backend(cartao_uid, status_liberacao, epis_detectados):
    """Envia o log de acesso completo para a API Backend."""
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
        print(f"[ERRO API] Falha ao comunicar com o Backend: {err}")

# ==========================================
# LOOP PRINCIPAL DE PROCESSAMENTO
# ==========================================
print("[SISTEMA PRONTO] Pressione 'q' na janela do vídeo para encerrar.")

while cap.isOpened():
    success, frame = cap.read()
    if not success:
        print("[ERRO] Falha ao ler frame da câmera.")
        break

    # 1. Detectar EPIs no frame atual
    results = model(frame, conf=0.50, verbose=False)
    detectados = set()

    for r in results:
        for box in r.boxes:
            cls_id = int(box.cls[0])
            label_name = model.names[cls_id].lower()
            detectados.add(label_name)

    # Validar presença dos 5 EPIs obrigatórios
    epis_presentes = detectados.intersection(EPI_OBRIGATORIOS)
    epis_faltantes = EPI_OBRIGATORIOS - epis_presentes
    checklist_completo = (len(epis_faltantes) == 0)

    # 2. Lógica do Temporizador de Estabilidade
    if checklist_completo:
        if tempo_inicio_aprovacao is None:
            tempo_inicio_aprovacao = time.time()
        
        tempo_decorrido = time.time() - tempo_inicio_aprovacao

        if tempo_decorrido >= TEMPO_VALIDACAO_SEGUNDOS:
            if not rfid_habilitado:
                rfid_habilitado = True
                status_mensagem = "EPIs OK! APROXIME O CARTAO RFID"
                cor_status = (0, 255, 0) # Verde
                if hardware_conectado:
                    arduino.write(b"HABILITAR_RFID\n")
        else:
            progresso = int(tempo_decorrido)
            status_mensagem = f"MANTENHA A POSICAO ({progresso}/{int(TEMPO_VALIDACAO_SEGUNDOS)}s)..."
            cor_status = (0, 255, 255) # Amarelo
    else:
        tempo_inicio_aprovacao = None
        rfid_habilitado = False
        status_mensagem = "EPIs INCOMPLETOS - ACESSO BLOQUEADO"
        cor_status = (0, 0, 255) # Vermelho

    # 3. Comunicação Serial (Leitura de comandos vindos do Arduino)
    if hardware_conectado and arduino.in_waiting > 0:
        try:
            linha_serial = arduino.readline().decode('utf-8', errors='ignore').strip()
            
            if linha_serial == "PRESENCA_DETECTADA":
                print("[MAQUETE] Sensor ultrassônico detectou pessoa próxima.")

            elif linha_serial.startswith("CARTAO_LIDO:"):
                uid_cartao = linha_serial.split(":")[1]
                print(f"[MAQUETE] Cartão RFID Lido: {uid_cartao}")

                # Se o checklist visual estiver 100% OK, libera a porta
                if checklist_completo and rfid_habilitado:
                    print("[ACESSO PERMITIDO] Liberando trava da porta...")
                    arduino.write(b"LIBERAR_PORTA\n")
                    enviar_requisicao_backend(uid_cartao, True, epis_presentes)
                    status_mensagem = f"ACESSO LIBERADO! (TAG: {uid_cartao})"
                    cor_status = (0, 255, 0)
                    tempo_inicio_aprovacao = None
                    rfid_habilitado = False
                else:
                    print("[ACESSO NEGADO] Cartão lido sem todos os EPIs.")
                    arduino.write(b"NEGADO\n")
                    enviar_requisicao_backend(uid_cartao, False, epis_presentes)
                    status_mensagem = "ACESSO NEGADO - EPIs FALTANTES"
                    cor_status = (0, 0, 255)

        except Exception as e:
            print(f"[ERRO SERIAL] Erro ao ler porta serial: {e}")

    # 4. Interface Gráfica (OpenCV)
    annotated_frame = results[0].plot()

    # Barra de Status Superior
    cv2.rectangle(annotated_frame, (0, 0), (annotated_frame.shape[1], 45), (30, 30, 30), -1)
    cv2.putText(annotated_frame, status_mensagem, (15, 30),
                cv2.FONT_HERSHEY_SIMPLEX, 0.7, cor_status, 2)

    # Painel com Checklist
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

    # Renderiza o frame na tela
    cv2.imshow("Scanner de Acesso a Subestacao - SENAI", annotated_frame)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

# Finalização
cap.release()
if hardware_conectado and arduino:
    arduino.close()
cv2.destroyAllWindows()