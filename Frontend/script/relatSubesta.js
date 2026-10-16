const BASE_URL = 'http://localhost:8080/subestacoes';

const SubestacaoService = {
    async listarTodas() {
        // CORREÇÃO: Removido o '/subestacoes' extra do final
        const response = await fetch(BASE_URL); 
        if (!response.ok) throw new Error('Erro ao buscar subestações.');
        return await response.json();
    }
};

const FuncionarioService = {
    async criar(funcionario, idSubestacao) {
        const payload = {
            ...funcionario,
            subestacao: { id: idSubestacao }
        };

        // Se a rota do backend for http://localhost:8080/funcionarios, você deve ajustar aqui.
        // Se for http://localhost:8080/subestacoes/funcionarios, então está correto.
        const response = await fetch(`${BASE_URL}/funcionarios`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (!response.ok) {
            const erroData = await response.json();
            throw new Error(erroData.message || 'Erro ao cadastrar funcionário.');
        }
        return await response.json();
    }
};

async function renderizarTabela() {
    // Melhorado: usando o ID direto da sua tabela para garantir que pegue o elemento certo
    const tabelaCorpo = document.querySelector('#tabelaRelatorio tbody');
    
    if (!tabelaCorpo) {
        console.error("Elemento da tabela não foi encontrado no HTML.");
        return;
    }

    try {
        const subestacoes = await SubestacaoService.listarTodas();
        tabelaCorpo.innerHTML = ''; 

        if (subestacoes.length === 0) {
            tabelaCorpo.innerHTML = `
                <tr>
                    <td colspan="4" style="text-align: center; padding: 15px;">
                        Nenhuma subestação cadastrada no sistema.
                    </td>
                </tr>`;
            return;
        }

        subestacoes.forEach(subestacao => {
            const linha = document.createElement('tr');
            linha.innerHTML = `
                <td>${subestacao.nome}</td>
                <td>${subestacao.localizacao}</td>
                <td>${subestacao.id}</td>
                <td>${subestacao.codigoSubestacao}</td>
            `;
            tabelaCorpo.appendChild(linha);
        });

    } catch (error) {
        console.error("Falha ao carregar o relatório:", error);

        tabelaCorpo.innerHTML = `
            <tr>
                <td colspan="4" style="color: #ff0000; text-align: center; font-weight: bold; padding: 15px;">
                    Erro ao carregar o relatório.
                </td>
            </tr>`;
    }
}

document.addEventListener('DOMContentLoaded', renderizarTabela);    