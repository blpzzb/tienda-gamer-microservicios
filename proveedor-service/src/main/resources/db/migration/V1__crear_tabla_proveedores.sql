CREATE TABLE proveedores (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL
);

INSERT INTO proveedores (nombre, email)
VALUES ('Gaming Supply Chile', 'ventas@gamingsupply.cl');
