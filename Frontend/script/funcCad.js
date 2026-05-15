
const API_URL = "http://localhost:8080/usuarios/cadastro";



document.addEventListener('DOMContentLoaded', () => {
    // Seleciona o corpo da tabela onde os dados estão localizados
    const tabela = document.querySelector('tbody');

    /**
      DELEGAÇÃO DE EVENTOS
      Em vez de criar um evento para cada botão em cada linha, ouvimos os cliques 
      na tabela inteira e identificamos o que foi clicado através do 'e.target'.
     */
    tabela.addEventListener('click', (e) => {
        const elementoClicado = e.target;

        // --- AÇÃO DE EXCLUIR ---
        // Verifica se o clique ocorreu em um elemento com a classe de ícone de fechar/excluir
        if (elementoClicado.classList.contains('fa-times-circle')) {
            const linha = elementoClicado.closest('tr'); // Sobe na hierarquia até encontrar a linha (tr)
            const nome = linha.cells[0].innerText;      // Pega o texto da primeira célula (nome)
            
            // Exibe caixa de confirmação nativa do navegador
            if (confirm(`Deseja realmente excluir o funcionário ${nome}?`)) {
                linha.style.opacity = '0'; // Inicia transição visual (requer CSS de transition)
                
                // Remove o elemento do DOM após 300ms para permitir que a animação ocorra
                setTimeout(() => linha.remove(), 300); 
            }
        }

        // --- AÇÃO DE EDITAR ---
        // Verifica se o clique ocorreu no ícone de edição (caneta)
        if (elementoClicado.classList.contains('fa-pen')) {
            const linha = elementoClicado.closest('tr');
            const celulaNome = linha.cells[0];          // Referência direta à célula do nome
            const nomeAtual = celulaNome.innerText;
            
            // Abre uma caixa de entrada com o valor atual preenchido
            const novoNome = prompt("Editar nome do funcionário:", nomeAtual);
            
            // Validação: só atualiza se o usuário não cancelar (null) e não deixar vazio (.trim())
            if (novoNome !== null && novoNome.trim() !== "") {
                celulaNome.innerText = novoNome.toUpperCase(); // Salva em maiúsculas
            }
        }
    });

    /**
     * 2. ESTILIZAÇÃO DINÂMICA DE STATUS
     * Percorre as linhas existentes para aplicar cores baseadas no texto do status.
     */
    const aplicarEstiloStatus = () => {
        const linhas = tabela.querySelectorAll('tr'); // Pega todas as linhas atuais
        
        linhas.forEach(linha => {
            const statusCel = linha.cells[1]; // Assume que o Status é a segunda coluna (índice 1)
            const statusTexto = statusCel.innerText.toUpperCase();

            statusCel.style.fontWeight = 'bold'; // Aplica negrito via JS
            
            // Lógica condicional de cores (Verde para Online, Cinza para o resto)
            if (statusTexto === 'ONLINE') {
                statusCel.style.color = '#28a745'; 
            } else {
                statusCel.style.color = '#6c757d'; 
            }
        });
    };

    // Executa a estilização assim que a página carrega
    aplicarEstiloStatus();
});