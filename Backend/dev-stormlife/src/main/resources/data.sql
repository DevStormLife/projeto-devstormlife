-- Garante que o banco comece com categorias padrão se estiver vazio
INSERT IGNORE INTO tb_usuario (id, email, senha, tipo) VALUES
(1, 'usereletrica@gmail.com', 'Userç@1234', 'padrão');

INSERT IGNORE INTO subestacoes (id, nome_estacao, codigo_subestacao, localizacao) VALUES
(1, 'Subestação Bulls', 'SUBEST-8934-91', 'Mauá');

INSERT IGNORE INTO cartoes_acesso (id, codigo_serial, subestacao_id) VALUES
(1, '12345', 1);
<<<<<<< HEAD
=======

INSERT IGNORE INTO funcionarios (id, nome, email, senha, cpf, cargo, cartao_acesso_id, fk_subestacao) VALUES
(1, "Aranha come-come", 'aranhacomecome@gmail.com', 'Comecomeç@1234', '976.437.620-74', 'Operador', 1, 1);
>>>>>>> b12d4ccdf08d8a2c36c9bac953254c9e8c9e7157
