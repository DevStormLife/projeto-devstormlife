document.addEventListener('DOMContentLoaded', () => {
    const cadastroForm = document.querySelector('.login-form');

    cadastroForm.addEventListener('submit', (event) => {
        // Impede o envio imediato para validar os dados primeiro
        event.preventDefault();

        // Captura dos campos
        const inputs = document.querySelectorAll('.login-input');
        const nome = inputs[0].value.trim();
        const cpf = inputs[1].value.trim();
        const email = inputs[2].value.trim();
        const senha = inputs[3].value.trim();

        // 1. Validação de Campos Vazios
        if (!nome, !cpf, !email, !senha) {
            alert('Por favor, preencha todos os campos do cadastro.');
            return;
        }

        // 2. Validação básica de CPF
        if (cpf.length < 11) {
            alert('Por favor, insira um CPF válido.');
            return;
        }

        // 3. Validação de Senha
        if (senha.length < 8) {
            alert('A senha deve ter no mínimo 8 caracteres.');
            return;
        }

        // --- SIMULAÇÃO DE SALVAMENTO ---
        console.log('Dados cadastrados:', { nome, cpf, email, senha });

        // Feedback visual para o usuário
        alert('Cadastro realizado com sucesso!');

        window.location.href = 'pgInicial.html';
    });
});