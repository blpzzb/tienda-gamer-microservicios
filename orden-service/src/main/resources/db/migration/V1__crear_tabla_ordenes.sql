CREATE TABLE ordenes (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         usuario_id BIGINT NOT NULL,
                         producto_id BIGINT NOT NULL,
                         cantidad INT NOT NULL,
                         fecha_orden TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Simulamos que el Usuario 1 compró 2 unidades del Producto 1
INSERT INTO ordenes (usuario_id, producto_id, cantidad) VALUES (1, 1, 2);