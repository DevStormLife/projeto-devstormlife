const BASE_URL = 'http://localhost:8080/subestacoes';

const SubestacaoService = {
    async listarTodas() {
        const response = await fetch(`${BASE_URL}/subestacoes`);
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

    const tabelaCorpo = document.querySelector('.table-container tbody') || document.querySelector('table tbody');
    
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
