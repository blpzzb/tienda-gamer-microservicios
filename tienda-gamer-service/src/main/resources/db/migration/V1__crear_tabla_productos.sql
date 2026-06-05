CREATE TABLE productos (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           nombre VARCHAR(100) NOT NULL,
                           categoria VARCHAR(50),
                           precio DECIMAL(10, 2) NOT NULL,
                           stock INT DEFAULT 0
);

-- Datos iniciales para que la tienda no esté vacía!!
INSERT INTO productos (nombre, categoria, precio, stock) VALUES ('Mouse Gamer RGB', 'Periféricos', 25000, 15);
INSERT INTO productos (nombre, categoria, precio, stock) VALUES ('Teclado Mecánico', 'Periféricos', 45000, 10);
INSERT INTO productos (nombre, categoria, precio, stock) VALUES ('Monitor 144Hz', 'Pantallas', 180000, 5);