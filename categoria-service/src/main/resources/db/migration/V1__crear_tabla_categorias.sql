CREATE TABLE categorias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255)
);

INSERT INTO categorias (nombre, descripcion)
VALUES ('Perifericos', 'Accesorios para computadores y consolas');
