const API_URL = "http://localhost:8080/usuarios/cadastro";

document.addEventListener('DOMContentLoaded', () => {
    const cadastroForm = document.querySelector('.login-form');

    cadastroForm.addEventListener('submit', (event) => {
        event.preventDefault();

        const inputs = document.querySelectorAll('.login-input');
        const nome = inputs[0].value.trim();
        const cpf = inputs[1].value.trim();
        const email = inputs[2].value.trim();
        const senha = inputs[3].value.trim();

        if (!nome, !cpf, !email, !senha) {
            alert('Por favor, preencha todos os campos do cadastro.');
            return;
        }

        // validação do CPF
        if (cpf.length < 11) {
            alert('Por favor, insira um CPF válido.');
            return;
        }

        // validação da senha
        if (senha.length < 8) {
            alert('A senha deve ter no mínimo 8 caracteres.');
            return;
        }

        console.log('Dados cadastrados:', { nome, cpf, email, senha });

        alert('Cadastro realizado com sucesso!');

        window.location.href = '../index.html';
    });
});