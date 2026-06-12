const API_URL = "http://localhost:8080/funcionarios";

document.addEventListener('DOMContentLoaded', () => {
    const tabela = document.querySelector('tbody');

    // --- 1. BUSCAR E RENDERIZAR FUNCIONÁRIOS DA API ---
    const carregarFuncionarios = async () => {
        try {
            const resposta = await fetch(API_URL);
            const funcionarios = await resposta.json();

            // Limpa o conteúdo estático atual da tabela
            tabela.innerHTML = "";

            // Se não houver funcionários cadastrados no banco
            if (!funcionarios || funcionarios.length === 0) {
                tabela.innerHTML = `
                    <tr>
                        <td colspan="4" style="text-align: center; color: #6c757d; font-style: italic;">
                            Nenhum funcionário cadastrado no sistema.
                        </td>
                    </tr>
                `;
                return;
            }

            // Se houver funcionários, renderiza cada um deles com status e ações
            funcionarios.forEach(func => {
                const tr = document.createElement('tr');
                
                // Define a cor do status dinamicamente
                const statusTexto = func.status ? func.status.toUpperCase() : 'OFFLINE';
                const statusColor = statusTexto === 'ONLINE' ? '#28a745' : '#6c757d';

                tr.innerHTML = `
                    <td>${func.nome.toUpperCase()}</td>
                    <td style="font-weight: bold; color: ${statusColor};">${statusTexto}</td>
                    <td class="linha-divisoria col-cpf-conteudo">${func.cpf || 'XX.XX.XX-XX'}</td>
                    <td class="linha-divisoria">
                        <div class="acoes" data-id="${func.id}">
                            <i class="fas fa-pen" style="cursor:pointer;" title="Editar"></i>
                            <i class="far fa-times-circle" style="cursor:pointer; color:red;" title="Excluir"></i>
                        </div>
                    </td>
                `;
                tabela.appendChild(tr);
            });

        } catch (erro) {
            console.error("Erro ao buscar funcionários:", erro);
            tabela.innerHTML = `
                <tr>
                    <td colspan="4" style="text-align: center; color: #dc3545;">
                        Erro ao conectar com o servidor.
                    </td>
                </tr>
            `;
        }
    };

    // --- 2. DELEGAÇÃO DE EVENTOS (CLIQUES) ---
    tabela.addEventListener('click', async (e) => {
        const elementoClicado = e.target;
        const containerAcoes = elementoClicado.closest('.acoes');
        
        // Se clicou fora de uma ação ou em uma linha vazia, ignora
        if (!containerAcoes) return; 
        
        const idFuncionario = containerAcoes.dataset.id;
        const linha = elementoClicado.closest('tr');
        const nome = linha.cells[0].innerText;

        // --- AÇÃO DE EXCLUIR ---
        if (elementoClicado.classList.contains('fa-times-circle')) {
            if (confirm(`Deseja realmente excluir o funcionário ${nome}?`)) {
                try {
                    // Envia a requisição de exclusão para a API
                    const resposta = await fetch(`${API_URL}/${idFuncionario}`, {
                        method: 'DELETE'
                    });

                    if (resposta.ok) {
                        linha.style.opacity = '0';
                        linha.style.transition = 'opacity 0.3s ease';
                        setTimeout(() => {
                            linha.remove();
                            // Se era o último funcionário, recarrega para mostrar a mensagem de "Nenhum cadastrado"
                            if (tabela.querySelectorAll('tr').length === 0) {
                                carregarFuncionarios();
                            }
                        }, 300);
                    } else {
                        alert("Não foi possível excluir o funcionário no servidor.");
                    }
                } catch (erro) {
                    console.error("Erro ao deletar:", erro);
                }
            }
        }

        // --- AÇÃO DE EDITAR ---
        if (elementoClicado.classList.contains('fa-pen')) {
            const celulaNome = linha.cells[0];
            const nomeAtual = celulaNome.innerText;
            
            const novoNome = prompt("Editar nome do funcionário:", nomeAtual);
            
            if (novoNome !== null && novoNome.trim() !== "") {
                try {
                    // Envia a atualização para a API (ajuste os campos conforme seu backend)
                    const resposta = await fetch(`${API_URL}/${idFuncionario}`, {
                        method: 'PUT',
                        headers: { 'Content-Type': 'application/json' },
                        body: JSON.stringify({ nome: novoNome.toUpperCase() })
                    });

                    if (resposta.ok) {
                        celulaNome.innerText = novoNome.toUpperCase();
                    } else {
                        alert("Não foi possível atualizar o nome no servidor.");
                    }
                } catch (erro) {
                    console.error("Erro ao atualizar:", erro);
                }
            }
        }
    });

    // Executa a busca no banco de dados assim que a página carrega
    carregarFuncionarios();
});