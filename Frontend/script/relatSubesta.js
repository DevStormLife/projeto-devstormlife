const API_URL = "http://localhost:8080/subestacoes";

async function carregarRelatorioSubestacoes() {
    const corpoTabela = document.querySelector("#tabelaRelatorio tbody");
    
    corpoTabela.innerHTML = `<tr><td colspan="4" style="text-align: center;">Carregando dados...</td></tr>`;

    try {
        const resposta = await fetch(API_URL);
        
        if (!resposta.ok) {
            throw new Error(`Erro na requisição: ${resposta.status}`);
        }

        const subestacoes = await resposta.json();
        
    
        corpoTabela.innerHTML = "";

        if (subestacoes.length === 0) {
            corpoTabela.innerHTML = `<tr><td colspan="4" style="text-align: center;">Nenhuma subestação encontrada.</td></tr>`;
            return;
        }

        subestacoes.forEach(subestacao => {
            const tr = document.createElement("tr");

            
            tr.innerHTML = `
                <td>${subestacao.nome || "Sem Nome"}</td>
                <td>${subestacao.localizacao || "Não informada"}</td>
                <td>ID: ${subestacao.id}</td>
                <td>${subestacao.codigoSubestacao || "Sem Código"}</td>
            `;

            corpoTabela.appendChild(tr);
        });

    } catch (erro) {
        console.error("Erro ao buscar dados do servidor:", erro);
        corpoTabela.innerHTML = `
            <tr>
                <td colspan="4" style="text-align: center; color: red; font-weight: bold;">
                    Erro ao carregar o relatório.
                </td>
            </tr>`;
    }
}

document.addEventListener("DOMContentLoaded", () => {
   
    carregarRelatorioSubestacoes();

    const btnHome = document.getElementById('btnHome');
    if (btnHome) {
        btnHome.onclick = () => {
            window.location.href = "home.html";
        };
    }
});