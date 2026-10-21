import cv2
import requests
import time
from ultralytics import YOLO

# 1. Carregar o modelo YOLO treinado para EPIs
# Substitua 'melhor_modelo_epi.pt' pelo caminho do arquivo de pesos treinado
model = YOLO('melhor_modelo_epi.pt')

# 2. Mapeamento das classes exigidas pela Subestação
EPI_OBRIGATORIOS = {
    'capacete',
    'oculos_protecao',
    'botina_seguranca',
    'luvas',
    'vestimenta_atpv'
}

# Configuração da Câmera (0 é a webcam padrão ou insira a URL da câmera IP)
cap = cv2.VideoCapture(0)

# URL da sua API Backend (exemplo)
BACKEND_URL = "http://localhost:5000/api/acesso/validar"

def enviar_status_backend(liberado: bool, epis_detectados: list, epis_faltantes: list):
    """Envia o resultado do checkout para o servidor BackEnd."""
    payload = {
        "status": "APROVADO" if liberado else "REPROVADO",
        "epis_detectados": list(epis_detectados),
        "epis_faltantes": list(epis_faltantes),
        "timestamp": time.time()
    }
    try:
        response = requests.post(BACKEND_URL, json=payload, timeout=2)
        print(f"[API] Status enviado: {response.status_code}")
    except Exception as e:
        print(f"[API ERROR] Falha ao comunicar com o backend: {e}")

print("Iniciando Scanner de EPIs...")

while cap.isOpened():
    success, frame = cap.read()
    if not success:
        print("Erro ao acessar a câmera.")
        break

    # Realizar a inferência com limiar de confiança de 50% (0.50)
    results = model(frame, conf=0.50, verbose=False)
    
    # Conjunto para armazenar o que foi detectado no frame atual
    detectados = set()

    # Extrair rótulos dos objetos detectados
    for r in results:
        for box in r.boxes:
            cls_id = int(box.cls[0])
            label_name = model.names[cls_id].lower()
            detectados.add(label_name)

    # Identificar quais EPIs faltam
    epis_presentes = detectados.intersection(EPI_OBRIGATORIOS)
    epis_faltantes = EPI_OBRIGATORIOS - epis_presentes
    acesso_liberado = len(epis_faltantes) == 0

    # Renderizar as caixas de detecção no frame
    annotated_frame = results[0].plot()

    # Exibir Checklist na tela em tempo real
    y_offset = 30
    cv2.putText(annotated_frame, "CHECKLIST DE SEGURANCA:", (10, y_offset),
                cv2.FONT_HERSHEY_SIMPLEX, 0.7, (255, 255, 255), 2)
    
    for epi in EPI_OBRIGATORIOS:
        y_offset += 25
        if epi in epis_presentes:
            status_text = f"[OK] {epi}"
            color = (0, 255, 0) # Verde
        else:
            status_text = f"[X]  {epi}"
            color = (0, 0, 255) # Vermelho
            
        cv2.putText(annotated_frame, status_text, (10, y_offset),
                    cv2.FONT_HERSHEY_SIMPLEX, 0.6, color, 2)

    # Mensagem Final de Acesso
    if acesso_liberado:
        cv2.putText(annotated_frame, "ACESSO LIBERADO", (10, y_offset + 40),
                    cv2.FONT_HERSHEY_SIMPLEX, 1.0, (0, 255, 0), 3)
    else:
        cv2.putText(annotated_frame, "ACESSO BLOQUEADO", (10, y_offset + 40),
                    cv2.FONT_HERSHEY_SIMPLEX, 1.0, (0, 0, 255), 3)

    # Exibir janela do scanner
    cv2.imshow("Scanner de Acesso - Subestacao SENAI", annotated_frame)

    # Pressione 'c' para acionar o checkout e enviar a validação ao BackEnd
    key = cv2.waitKey(1) & 0xFF
    if key == ord('c'):
        print("\n--- Processando Checkout de Entrada ---")
        enviar_status_backend(acesso_liberado, epis_presentes, epis_faltantes)
    elif key == ord('q'):
        break

cap.release()
cv2.destroyAllWindows()