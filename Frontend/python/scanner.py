import cv2
import requests
import time
import serial  # Comunicação com Arduino/ESP32 para abrir a trava física
from ultralytics import YOLO

# 1. Inicialização da IA e Configurações
model = YOLO('melhor_modelo_epi.pt')

EPI_OBRIGATORIOS = {
    'capacete',
    'oculos_protecao',
    'botina_seguranca',
    'luvas',
    'vestimenta_atpv'
}

BACKEND_URL = "http://localhost:5000/api/acesso/liberar-cartao"

# Opcional: Comunicação Serial com Arduino/ESP32 (substitua 'COM3' ou '/dev/ttyUSB0')
try:
    arduino = serial.Serial('COM3', 9600, timeout=1)
    time.sleep(2) # Tempo para estabilizar a conexão serial
    hardware_conectado = True
    print("[HARDWARE] Conectado ao leitor/trava via Serial.")
except Exception as e:
    hardware_conectado = False
    print(f"[HARDWARE WARN] Sem conexão Serial (Modo Simulação): {e}")

def liberar_porta_e_cartao():
    """Aciona a API do backend e dispara a trava física."""
    print("\n[CHECKLIST APROVADO] Liberando cartão e acionando trava...")
    
    # 1. Envia comando ao Backend para habilitar a leitura do cartão
    try:
        payload = {"status": "CHECKLIST_OK", "cartao_habilitado": True}
        res = requests.post(BACKEND_URL, json=payload, timeout=2)
        print(f"[API] Backend notificado: {res.status_code}")
    except Exception as e:
        print(f"[API ERROR] Falha ao comunicar com backend: {e}")

    # 2. Envia sinal elétrico para o Arduino/ESP32 acionar o relé da porta
    if hardware_conectado:
        arduino.write(b'ABRIR_PORTA\n') # Envia comando pela serial

# 2. Variáveis de Controle de Tempo para Estabilidade
tempo_inicio_aprovacao = None
TEMPO_NECESSARIO_SEGUNDOS = 3 # Precisa ficar 3s seguidos com todos os EPIs
porta_liberada = False

cap = cv2.VideoCapture(0)

while cap.isOpened():
    success, frame = cap.read()
    if not success:
        break

    results = model(frame, conf=0.50, verbose=False)
    detectados = set()

    for r in results:
        for box in r.boxes:
            cls_id = int(box.cls[0])
            label_name = model.names[cls_id].lower()
            detectados.add(label_name)

    epis_presentes = detectados.intersection(EPI_OBRIGATORIOS)
    epis_faltantes = EPI_OBRIGATORIOS - epis_presentes
    checklist_completo = (len(epis_faltantes) == 0)

    # Lógica do Temporizador de Validação
    if checklist_completo:
        if tempo_inicio_aprovacao is None:
            tempo_inicio_aprovacao = time.time()
        
        tempo_decorrido = time.time() - tempo_inicio_aprovacao

        if tempo_decorrido >= TEMPO_NECESSARIO_SEGUNDOS and not porta_liberada:
            liberar_porta_e_cartao()
            porta_liberada = True
    else:
        # Reseta o temporizador e bloqueia se a pessoa tirar algum EPI
        tempo_inicio_aprovacao = None
        porta_liberada = False

    # Interface Visual (OpenCV)
    annotated_frame = results[0].plot()
    
    # Exibir Status na Tela
    if porta_liberada:
        cv2.rectangle(annotated_frame, (0, 0), (640, 80), (0, 255, 0), -1)
        cv2.putText(annotated_frame, "PORTA LIBERADA! APROXIME O CARTAO", (20, 50),
                    cv2.FONT_HERSHEY_SIMPLEX, 0.8, (0, 0, 0), 2)
    elif checklist_completo:
        progresso = int(tempo_decorrido)
        cv2.putText(annotated_frame, f"MANTENHA A POSICAO: {progresso}/{TEMPO_NECESSARIO_SEGUNDOS}s", (10, 50),
                    cv2.FONT_HERSHEY_SIMPLEX, 0.8, (0, 255, 255), 2)
    else:
        cv2.putText(annotated_frame, "EPIs FALTANTES - PORTA BLOQUEADA", (10, 50),
                    cv2.FONT_HERSHEY_SIMPLEX, 0.8, (0, 0, 255), 2)

    cv2.imshow("Scanner de Acesso - Subestacao SENAI", annotated_frame)

    if cv2.waitKey(1) & 0xFF == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()