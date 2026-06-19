const API_URL = "http://localhost:8080/funcionarios/cadastro";

document.addEventListener('DOMContentLoaded', () => {
    const cadastroForm = document.querySelector('#cadastro-form');

    cadastroForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const nome = document.querySelector('#nome').value.trim();
        const cpf = document.querySelector('#cpf').value.trim();
        const email = document.querySelector('#email').value.trim();
        const senha = document.querySelector('#senha').value.trim();
        const cargo = document.querySelector('#cargo').value.trim();

        if (!nome || !cpf || !email || !senha || !cargo) {
            alert('Por favor, preencha todos os campos do cadastro.');
            return;
        }

        if (cpf.length < 11) {
            alert('Por favor, insira um CPF válido.');
            return;
        }

        if (senha.length < 8) {
            alert('A senha deve ter no mínimo 8 caracteres.');
            return;
        }


        const funcionario = {
            nome: nome,
            email: email,
            senha: senha,
            cpf: cpf,
            cargo: cargo,
            subestacao: { id: 1 }
        };
        
        try {
            const resposta = await fetch(API_URL, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(funcionario)
            });

            if (resposta.ok) {
                alert('Cadastro realizado com sucesso!');
                window.location.href = '../index.html';
            } else {
                const erroDados = await resposta.text();
                alert('Erro no cadastro: ' + erroDados);
            }
        } catch (erro) {
            console.error('Erro ao conectar com a API:', erro);
            alert('Não foi possível conectar ao servidor.');
        }
    });
});