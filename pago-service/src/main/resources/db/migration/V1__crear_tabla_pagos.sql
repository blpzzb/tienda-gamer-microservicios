CREATE TABLE pagos (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       orden_id BIGINT NOT NULL,
                       monto DOUBLE NOT NULL,
                       estado VARCHAR(50), -- 'PENDIENTE', 'COMPLETADO'
                       fecha_pago DATETIME DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO pagos (orden_id, monto, estado) VALUES (1, 150000.0, 'COMPLETADO');