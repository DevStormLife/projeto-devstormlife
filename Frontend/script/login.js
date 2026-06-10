const API_URL = "http://localhost:8080/usuarios/login";

document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.querySelector('.login-form');

    loginForm.addEventListener('submit', (event) => {
        // impede que a página recarregue ao clicar no botão
        event.preventDefault();

        // captura os valores dos inputs
        const inputs = document.querySelectorAll('.login-input');
        const loginValue = inputs[0].value.trim();
        const passwordValue = inputs[1].value.trim();

        // validação simples
        if (loginValue === '' || passwordValue === '') {
            alert('Por favor, preencha todos os campos.');
            return;
        }

        console.log('Tentativa de login com:', loginValue);

        // se os dados estiverem corretos
        if (loginValue === 'admin' && passwordValue === '1234') {
            alert('Acesso concedido! Bem-vindo ao Storm Life.');
        } else {
            alert('Usuário ou senha incorretos.');
        }
    });
});