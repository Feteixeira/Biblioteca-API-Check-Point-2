

IF DB_ID('biblioteca') IS NULL
    CREATE DATABASE biblioteca;
GO

USE biblioteca;
GO

IF OBJECT_ID('dbo.autores', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.autores (
        id            BIGINT IDENTITY(1,1) NOT NULL,
        nome          VARCHAR(150)         NOT NULL,
        nacionalidade VARCHAR(80)          NULL,
        CONSTRAINT pk_autores PRIMARY KEY (id)
    );
END
GO

IF OBJECT_ID('dbo.livros', 'U') IS NULL
BEGIN
    CREATE TABLE dbo.livros (
        id                    BIGINT IDENTITY(1,1) NOT NULL,
        titulo                VARCHAR(200)         NOT NULL,
        isbn                  VARCHAR(20)          NOT NULL,
        ano_publicacao        INT                  NULL,
        quantidade_disponivel INT                  NOT NULL CONSTRAINT df_livros_quantidade DEFAULT 0,
        autor_id              BIGINT               NOT NULL,
        CONSTRAINT pk_livros PRIMARY KEY (id),
        CONSTRAINT uk_livros_isbn UNIQUE (isbn),
        CONSTRAINT ck_livros_quantidade CHECK (quantidade_disponivel >= 0),
        CONSTRAINT fk_livros_autores FOREIGN KEY (autor_id) REFERENCES dbo.autores (id)
    );
END
GO

IF NOT EXISTS (SELECT 1 FROM dbo.autores)
BEGIN
    INSERT INTO dbo.autores (nome, nacionalidade) VALUES
        ('Machado de Assis', 'Brasileira'),
        ('Clarice Lispector', 'Brasileira'),
        ('George Orwell', 'Britânica');
END
GO

IF NOT EXISTS (SELECT 1 FROM dbo.livros)
BEGIN
    INSERT INTO dbo.livros (titulo, isbn, ano_publicacao, quantidade_disponivel, autor_id) VALUES
        ('Dom Casmurro',                     '9788535910663', 1899, 3, (SELECT id FROM dbo.autores WHERE nome = 'Machado de Assis')),
        ('Memórias Póstumas de Brás Cubas',  '9788535911664', 1881, 2, (SELECT id FROM dbo.autores WHERE nome = 'Machado de Assis')),
        ('A Hora da Estrela',                '9788532508126', 1977, 4, (SELECT id FROM dbo.autores WHERE nome = 'Clarice Lispector')),
        ('1984',                             '9788535914849', 1949, 5, (SELECT id FROM dbo.autores WHERE nome = 'George Orwell'));
END
GO
