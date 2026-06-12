const API_URL = "http://localhost:8080/usuarios/login";

document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.querySelector('.login-form');

    loginForm.addEventListener('submit', (event) => {
        // impede que a página recarregue ao clicar no botão
        event.preventDefault();

        // captura os valores dos inputs
        const inputs = document.querySelectorAll('.login-input');
        const loginValido = inputs[0].value.trim();
        const SenhaValida = inputs[1].value.trim();

        // validação simples
        if (loginValido === '' || senhaValida === '') {
            alert('Por favor, preencha todos os campos.');
            return;
        }

        console.log('Tentativa de login com:', loginValido);

        // se os dados estiverem corretos
        if (loginValido === 'admin' && senhaValida === '1234') {
            alert('Acesso concedido! Bem-vindo ao Storm Life.');
        } else {
            alert('Usuário ou senha incorretos.');
        }
    });
});