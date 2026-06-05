CREATE TABLE usuarios (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          nombre VARCHAR(100) NOT NULL,
                          correo VARCHAR(100) NOT NULL UNIQUE,
                          rut VARCHAR(12) NOT NULL UNIQUE
);

-- Datos de prueba!!
INSERT INTO usuarios (nombre, correo, rut) VALUES ('Niko', 'niko@correo.cl', '20.123.456-7');
INSERT INTO usuarios (nombre, correo, rut) VALUES ('Admin', 'admin@tienda.cl', '10.987.654-3');