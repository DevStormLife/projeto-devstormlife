const API_URL = "http://localhost:8080/funcionarios";

document.addEventListener('DOMContentLoaded', () => {
    const tabela = document.querySelector('tbody');
    const modal = document.getElementById('modalEditar');
    const formEditar = document.getElementById('formEditar');
    const btnFecharModal = document.querySelector('.close-modal');
    const btnCancelar = document.getElementById('btnCancelar');

    // Armazena temporariamente a lista de funcionários para consulta local rápida
    let listaFuncionariosCache = [];

    // --- 1. CARREGAR FUNCIONÁRIOS ---
    const carregarFuncionarios = async () => {
        try {
            const resposta = await fetch(API_URL);
            if (!resposta.ok) throw new Error("Erro ao listar funcionários");
            
            const funcionarios = await resposta.json();
            listaFuncionariosCache = funcionarios; // Salva em cache

            tabela.innerHTML = "";

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

            funcionarios.forEach(func => {
                const tr = document.createElement('tr');
                
                const statusTexto = func.status ? func.status.toUpperCase() : 'OFFLINE';
                const statusColor = statusTexto === 'ONLINE' ? '#28a745' : '#6c757d';

                tr.innerHTML = `
                    <td>${func.nome ? func.nome.toUpperCase() : ''}</td>
                    <td style="font-weight: bold; color: ${statusColor};">${statusTexto}</td>
                    <td class="linha-divisoria col-cpf-conteudo">${func.cpf || 'XX.XX.XX-XX'}</td>
                    <td class="linha-divisoria">
                        <div class="acoes" data-id="${func.id}">
                            <button type="button" class="btn-acao btn-editar" title="Editar">
                                <i class="fas fa-edit"></i> Editar
                            </button>
                            <button type="button" class="btn-acao btn-excluir" title="Excluir">
                                <i class="fas fa-trash"></i> Excluir
                            </button>
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

    // --- 2. CLIQUE NA TABELA ---
    tabela.addEventListener('click', async (e) => {
        const btnEditar = e.target.closest('.btn-editar');
        const btnExcluir = e.target.closest('.btn-excluir');

        if (!btnEditar && !btnExcluir) return;

        const containerAcoes = e.target.closest('.acoes');
        const idFuncionario = containerAcoes.dataset.id;
        const linha = e.target.closest('tr');
        const nome = linha.cells[0].innerText;

        // EXCLUIR
        if (btnExcluir) {
            if (confirm(`Deseja realmente excluir o funcionário ${nome}?`)) {
                try {
                    const resposta = await fetch(`${API_URL}/${idFuncionario}`, {
                        method: 'DELETE'
                    });

                    if (resposta.ok) {
                        linha.style.opacity = '0';
                        linha.style.transition = 'opacity 0.3s ease';
                        setTimeout(() => {
                            linha.remove();
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

        // EDITAR (Abre o modal diretamente com os dados carregados)
        if (btnEditar) {
            try {
                // Tenta buscar no cache ou via API
                let funcionario = listaFuncionariosCache.find(f => f.id == idFuncionario);

                if (!funcionario) {
                    const resposta = await fetch(`${API_URL}/${idFuncionario}`);
                    if (resposta.ok) {
                        funcionario = await resposta.json();
                    }
                }

                if (funcionario) {
                    document.getElementById('edit-id').value = funcionario.id;
                    document.getElementById('edit-nome').value = funcionario.nome || '';
                    document.getElementById('edit-email').value = funcionario.email || '';
                    document.getElementById('edit-cpf').value = funcionario.cpf || '';
                    document.getElementById('edit-cargo').value = funcionario.cargo || '';

                    // Força a exibição do modal
                    modal.style.display = 'flex';
                } else {
                    alert("Não foi possível localizar os dados do funcionário.");
                }
            } catch (erro) {
                console.error("Erro ao carregar dados no modal:", erro);
            }
        }
    });

    // --- 3. SALVAR (PUT) ---
    formEditar.addEventListener('submit', async (e) => {
        e.preventDefault();

        const id = document.getElementById('edit-id').value;
        
        // Monta o payload respeitando a obrigatoriedade de @NotNull subestacao do Spring Boot
        const dadosAtualizados = {
            nome: document.getElementById('edit-nome').value,
            email: document.getElementById('edit-email').value,
            cpf: document.getElementById('edit-cpf').value,
            cargo: document.getElementById('edit-cargo').value.toUpperCase(),
            subestacao: {
                id: 1 // Mantém um ID de subestação padrão válido para não falhar na validação do Backend
            }
        };

        try {
            const resposta = await fetch(`${API_URL}/${id}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(dadosAtualizados)
            });

            if (resposta.ok) {
                fecharModal();
                carregarFuncionarios();
            } else {
                const erroServidor = await resposta.text();
                console.error("Resposta do servidor:", erroServidor);
                alert("Erro ao atualizar o funcionário. Verifique os campos preenchidos.");
            }
        } catch (erro) {
            console.error("Erro ao salvar alterações:", erro);
        }
    });

    // --- 4. FECHAR MODAL ---
    const fecharModal = () => {
        modal.style.display = 'none';
        formEditar.reset();
    };

    btnFecharModal.addEventListener('click', fecharModal);
    btnCancelar.addEventListener('click', fecharModal);
    
    window.addEventListener('click', (e) => {
        if (e.target === modal) fecharModal();
    });

    carregarFuncionarios();
});