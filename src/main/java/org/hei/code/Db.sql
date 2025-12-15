CREATE USER mini_football_db_manager WITH PASSWORD '123456';

CREATE DATABASE mini_football_db;

\c mini_football_db;

GRANT ALL PRIVILEGES ON DATABASE mini_football_db TO mini_football_db_manager;

GRANT SELECT, INSERT, UPDATE, DELETE ON ALL tables IN SCHEMA public TO mini_football_db_manager;

GRANT CREATE, USAGE ON SCHEMA public TO mini_football_db_manager;

