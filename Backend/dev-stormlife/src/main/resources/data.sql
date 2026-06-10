-- Garante que o banco comece com categorias padrão se estiver vazio
INSERT IGNORE INTO tb_usuario (id, email, senha, tipo) VALUES
(1, 'usereletrica@gmail.com', 'Userç@1234', 'padrão');