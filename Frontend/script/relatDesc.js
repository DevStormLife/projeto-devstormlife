const API_URL = "http://localhost:8080/usuarios/cadastro";

let registrosAcesso = [];

const tbody = document.querySelector('.tabela tbody');
const btnFiltros = document.querySelector('.filtros');
const cardHeader = document.querySelector('.card-header');

const btnAdicionar = document.createElement('span');
btnAdicionar.textContent = "Adicionar";
btnAdicionar.className = "filtros"; 
btnAdicionar.style.cursor = "pointer";
btnAdicionar.style.marginLeft = "10px";
cardHeader.appendChild(btnAdicionar);

function renderizarTabela(dados) {
    tbody.innerHTML = "";

    if (dados.length === 0) {
        tbody.innerHTML = `<tr><td colspan="5" style="text-align:center">Nenhum registro encontrado.</td></tr>`;
        return;
    }

    dados.forEach(item => {
        const linha = document.createElement('tr');
        linha.innerHTML = `
            <td>${item.nome}</td>
            <td>${item.rf}</td>
            <td>${item.dataHora}</td>
            <td>${item.local}</td>
            <td>${item.codSubestacao}</td>
        `;
        tbody.appendChild(linha);
    });
}

renderizarTabela(registrosAcesso);

btnAdicionar.addEventListener('click', () => {
    const nome = prompt("Digite o Nome:");
    if (!nome) return;

    const rf = prompt("Digite o RF:");
    const dataHora = prompt("Digite a Data/Hora (Ex: 12/06/2026 15:30):");
    const local = prompt("Digite o Local:");
    const codSubestacao = prompt("Digite o Cód. Subestação:");

    const novoRegistro = {
        nome: nome,
        rf: rf || "-",
        dataHora: dataHora || "-",
        local: local || "-",
        codSubestacao: codSubestacao || "-"
    };

    registrosAcesso.push(novoRegistro);
    renderizarTabela(registrosAcesso);
});

btnFiltros.style.cursor = "pointer"; 
btnFiltros.addEventListener('click', () => {
    const termoBusca = prompt("Digite o Nome ou RF para filtrar:");
    
    if (termoBusca !== null) {
        const filtrados = registrosAcesso.filter(reg => 
            reg.nome.toLowerCase().includes(termoBusca.toLowerCase()) || 
            reg.rf.includes(termoBusca)
        );
        renderizarTabela(filtrados);
    }
});
const tituloRelatorio = document.querySelector('.card-header h2');
tituloRelatorio.style.cursor = "pointer";
tituloRelatorio.title = "Clique para imprimir este relatório";

tituloRelatorio.addEventListener('click', () => {
    window.print();
});