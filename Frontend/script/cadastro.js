const API_URL = "http://localhost:8080/funcionarios/cadastro";

document.addEventListener('DOMContentLoaded', () => {
<<<<<<< HEAD
    // CORREÇÃO: Mudado de '.login-form' para '.cadastro-form'
    const cadastroForm = document.querySelector('.cadastro-form');

    // Se o formulário não for encontrado, avisa no console para não quebrar silenciosamente
    if (!cadastroForm) {
        console.error("Erro: Formulário '.cadastro-form' não foi encontrado no HTML.");
        return;
    }

    cadastroForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        // CORREÇÃO: Mudado de '.login-input' para '.cadastro-input'
        const inputs = document.querySelectorAll('.cadastro-input');
        
        const nome = inputs[0]?.value.trim();
        const cpf = inputs[1]?.value.trim();
        const email = inputs[2]?.value.trim();
        const senha = inputs[3]?.value.trim();

        if (!nome || !cpf || !email || !senha) {
=======
    const cadastroForm = document.querySelector('#cadastro-form');

    cadastroForm.addEventListener('submit', async (e) => {
        e.preventDefault();

        const nome = document.querySelector('#nome').value.trim();
        const cpf = document.querySelector('#cpf').value.trim();
        const email = document.querySelector('#email').value.trim();
        const senha = document.querySelector('#senha').value.trim();
        const cargo = document.querySelector('#cargo').value.trim();

        if (!nome || !cpf || !email || !senha || !cargo) {
>>>>>>> 951b7f24789d7f133013ea787b7fa7d30dc77666
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

<<<<<<< HEAD
        const dadosUsuario = { nome, cpf, email, senha };

        console.log('Tentando enviar dados:', dadosUsuario);

        try {
            const resposta = await fetch(API_URL, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(dadosUsuario)
            });

            if (resposta.ok) {
                alert('Cadastro realizado com sucesso!');
                window.location.href = '../index.html'; 
            } else {
                const erroTexto = await resposta.text();
                alert(`Erro no cadastro: ${erroTexto || 'Erro desconhecido no servidor.'}`);
            }

        } catch (error) {
            console.error('Erro ao conectar com o Back-End:', error);
            alert('Não foi possível conectar ao servidor. Verifique se o Back-End está rodando.');
=======

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
>>>>>>> 951b7f24789d7f133013ea787b7fa7d30dc77666
        }
    });
});