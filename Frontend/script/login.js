const API_URL = "http://localhost:8080/usuarios/login";

const formLogin = document.getElementById("form-login");
const mensagemErro = document.getElementById("mensagem-erro");

formLogin.addEventListener("submit", async function(event) {
    event.preventDefault();

    const loginInput = document.getElementById("login").value;
    const senhaInput = document.getElementById("senha").value;

    try {
        const resposta = await fetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                login: loginInput,
                senha: senhaInput
            })
        });

        if (resposta.ok) {
            let dadosUsuario = {};
            
            const contentType = resposta.headers.get("content-type");
            if (contentType && contentType.includes("application/json")) {
                dadosUsuario = await resposta.json();
            }

            // Grava os dados da sessão
            localStorage.setItem("usuarioSessao", JSON.stringify(dadosUsuario));

            // REDIRECIONAMENTO CORRETO:
            window.location.href = "pages/homePage.html";
        } else {
            mensagemErro.innerText = "Utilizador ou senha inválidos!";
        }
    } catch (erro) {
        console.error("Erro no login:", erro);
        mensagemErro.innerText = "Erro ao conectar com o servidor.";
    }
});