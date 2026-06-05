CREATE TABLE inventario (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            producto_id BIGINT NOT NULL,
                            stock_actual INT NOT NULL,
                            ubicacion_bodega VARCHAR(100)
);

-- Stock inicial para el Producto 1
INSERT INTO inventario (producto_id, stock_actual, ubicacion_bodega) VALUES (1, 50, 'Pasillo A-12');