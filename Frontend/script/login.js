document.addEventListener('DOMContentLoaded', () => {
    const loginForm = document.querySelector('.login-form');

    loginForm.addEventListener('submit', (event) => {
        // Impede que a página recarregue ao clicar no botão
        event.preventDefault();

        // Captura os valores dos inputs
        const inputs = document.querySelectorAll('.login-input');
        const loginValue = inputs[0].value.trim();
        const passwordValue = inputs[1].value.trim();

        // Validação Simples
        if (loginValue === '' || passwordValue === '') {
            alert('Por favor, preencha todos os campos.');
            return;
        }

        console.log('Tentativa de login com:', loginValue);

        // Se os dados estiverem corretos
        if (loginValue === 'admin' && passwordValue === '1234') {
            alert('Acesso concedido! Bem-vindo ao Storm Life.');
        } else {
            alert('Usuário ou senha incorretos.');
        }
    });
});