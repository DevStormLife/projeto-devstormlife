const API_URL = window.STORMLIFE_API_ORIGIN + "/usuarios/login";

const formLogin = document.getElementById("form-login");
const mensagemErro = document.getElementById("mensagem-erro");

formLogin.addEventListener("submit", async function(event){
    // Evita que o navegador recarregue a página ao clicar no botão "Entrar"
    event.preventDefault();

    // Captura os valores digitados nos inputs
    const loginInput = document.getElementById("login").value;
    const senhaInput = document.getElementById("senha").value;

    try {
        // Dispara um POST enviando o email e senha no "corpo" (body) da requisição
        const resposta = await window.stormFetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" }, // Avisa que estamos a enviar um JSON
            body: JSON.stringify({
                email: loginInput, // CORREÇÃO: Alterado de 'login' para 'email'
                senha: senhaInput
            })
        });

        // Se a API retornar um status de sucesso (200 OK)
        if (resposta.ok) {
            // Tenta ler os dados, mas protege o código caso o backend não devolva um JSON
            let dadosUsuario = {};
            try {
                dadosUsuario = await resposta.json();
            } catch (e) {
                console.log("Login realizado com sucesso, mas o servidor não retornou JSON.");
            }

            // Grava os dados do utilizador na memória do navegador (localStorage)
            localStorage.setItem("usuarioSessao", JSON.stringify(dadosUsuario));

            // Redireciona o utilizador para a página principal
            window.location.href = "/pages/homePage.html";
        } else {
            mensagemErro.innerText = "Utilizador ou senha inválidos!";
        }
    } catch (erro) {
        console.error(erro);
        mensagemErro.innerText = "Erro ao conectar com o servidor.";
    }
});
