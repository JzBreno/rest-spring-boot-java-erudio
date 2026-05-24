-- Arquivo: V3__Remove_constraints_e_colunas_antigas.sql

-- 1. Remove a chave estrangeira que liga a tabela intermediária à Person
-- (Substitua 'fk_person_books_person' pelo nome real da sua FK se for diferente)
ALTER TABLE person_books DROP CONSTRAINT IF EXISTS fk_person_books_person;

-- 2. Remove a chave primária antiga da tabela person (que era baseada no ID Long)
-- (Geralmente o nome padrão da PK é 'person_pkey')
ALTER TABLE person DROP CONSTRAINT IF EXISTS person_pkey;

-- 3. Agora o banco permite deletar as colunas antigas sem dar erro de integridade
ALTER TABLE person_books DROP COLUMN person_id;
ALTER TABLE person DROP COLUMN id;