document.addEventListener('DOMContentLoaded', () => {
    
    const registrosAcesso = [
         { nome: "Guilherme Melos", rf: "0001", data: "13/05/2026 7:00", local: "Setor Sul", sub: "SE-03" },
        { nome: "Miguel Narvais", rf: "0014", data: "13/05/2026 08:45", local: "Sala de Máquinas", sub: "SE-01" },
        { nome: "Miguel Lopes", rf: "0015", data: "13/05/2026 09:12", local: "Setor Norte", sub: "SE-04" },
        { nome: "Pedro Santos", rf: "0021", data: "13/05/2026 10:05", local: "Painel Central", sub: "SE-01" },
        { nome: "Victor Braga", rf: "0030", data: "13/05/2026 10:30", local: "Estacionamento", sub: "SE-02" }
    ];

    const tbody = document.querySelector('.tabela tbody');
    const btnFiltros = document.querySelector('.filtros');

    function renderizarTabela(dados) {
        tbody.innerHTML = "";

        if (dados.length === 0) {
            tbody.innerHTML = <tr><td colspan="5" style="text-align:center">Nenhum registro encontrado.</td></tr>;
            return;
        }

        dados.forEach(item => {
            const linha = document.createElement('tr');
            linha.innerHTML = `
                <td>${item.nome}</td>
                <td>${item.rf}</td>
                <td>${item.data}</td>
                <td>${item.local}</td>
                <td>${item.sub}</td>
            `;
            tbody.appendChild(linha);
        });
    }

    renderizarTabela(registrosAcesso);

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
});