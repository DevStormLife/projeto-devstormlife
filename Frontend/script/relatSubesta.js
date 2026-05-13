const logsAcesso = [
    { nome: "Pedro Cunha", rf: "99281", data: "13/05/2026 08:30", cod: "Setor-1" },
    { nome: "Victor Gabriel", rf: "88273", data: "13/05/2026 09:15", cod: "Setor-2" },
    { nome: "Guilherme Melos", rf: "77122", data: "13/05/2026 10:05", cod: "Setor-6" },
    { nome: "Miguel Lopes", rf: "66100", data: "13/05/2026 11:40", cod: "Setor-1" },
    { nome: "Miguel Narvais", rf: "44500", data: "13/05/2026 12:20", cod: "Setor-1" },
    { nome: "Pedro Santos", rf: "77861", data: "13/05/2026 13:10", cod: "Setor-3" }
];


function atualizarTabela() {
    const corpoTabela = document.querySelector("#tabelaRelatorio tbody");

    corpoTabela.innerHTML = "";

    logsAcesso.forEach(item => {
        const tr = document.createElement("tr");

        tr.innerHTML = `
            <td>${item.nome}</td>
            <td>${item.rf}</td>
            <td>${item.data}</td>
            <td>${item.cod}</td>
        `;

        corpoTabela.appendChild(tr);
    });
}

document.addEventListener("DOMContentLoaded", () => {
    atualizarTabela();

    const btnHome = document.getElementById('btnHome');
    if (btnHome) {
        btnHome.onclick = () => window.location.href = "home.html";
    }
});