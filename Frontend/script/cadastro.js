const API_URL = "http://localhost:8080/usuarios/cadastro";

document.addEventListener('DOMContentLoaded', () => {
    const cadastroForm = document.querySelector('.login-form');

    // Usamos async aqui para poder usar o await no fetch depois
    cadastroForm.addEventListener('submit', async (event) => {
        event.preventDefault();

        const inputs = document.querySelectorAll('.login-input');
        const nome = inputs[0].value.trim();
        const cpf = inputs[1].value.trim();
        const email = inputs[2].value.trim();
        const senha = inputs[3].value.trim();

        // CORREÇÃO: O operador correto para "OU" é ||. O uso de vírgulas aqui não funcionava como esperado.
        if (!nome || !cpf || !email || !senha) {
            alert('Por favor, preencha todos os campos do cadastro.');
            return;
        }

        // Validação do CPF
        if (cpf.length < 11) {
            alert('Por favor, insira um CPF válido.');
            return;
        }

        // Validação da senha
        if (senha.length < 8) {
            alert('A senha deve ter no mínimo 8 caracteres.');
            return;
        }

        // 1. Montando o objeto unificado para enviar ao backend
        // Aqui, dividimos o que é do funcionário e o que é do usuário de forma organizada
        const dadosCadastro = {
            funcionario: {
                nome: nome,
                cpf: cpf
            },
            usuario: {
                email: email,
                senha: senha
            }
        };

        console.log('Enviando dados:', dadosCadastro);

        // 2. Enviando os dados para a API
        try {
            const response = await fetch(API_URL, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(dadosCadastro) // Transforma o objeto JS em texto JSON
            });

            if (response.ok) {
                // Se o backend responder com status 200 a 299
                alert('Cadastro de funcionário e usuário realizado com sucesso!');
                window.location.href = '../index.html';
            } else {
                // Se o backend retornar algum erro (ex: email já cadastrado, erro 400, 500)
                const erroData = await response.json().catch(() => ({}));
                alert(`Erro no cadastro: ${erroData.mensagem || 'Verifique os dados enviados.'}`);
            }

        } catch (error) {
            // Se o servidor estiver fora do ar ou houver erro de rede
            console.error('Erro na requisição:', error);
            alert('Não foi possível conectar ao servidor. Tente novamente mais tarde.');
        }
    });
});