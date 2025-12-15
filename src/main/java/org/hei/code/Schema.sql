CREATE TYPE continent_enum AS ENUM('AFRICA', 'EUROPA', 'ASIA', 'AMERICA');

CREATE TABLE team (
    id INT PRIMARY KEY,
    name VARCHAR,
    continent continent_enum
);

CREATE TYPE position_enum AS ENUM('GK', 'DEF', 'MIDF', 'STR');

CREATE TABLE player (
    id INT PRIMARY KEY,
    name VARCHAR(150),
    age INT,
    id_team INT,
    position position_enum,
    CONSTRAINT fk_team FOREIGN KEY(id_team) REFERENCES team(id)
);