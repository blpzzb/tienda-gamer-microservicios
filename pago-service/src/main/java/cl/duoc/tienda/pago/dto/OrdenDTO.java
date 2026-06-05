package cl.duoc.tienda.pago.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrdenDTO {

    private Long id;
    private Long usuarioId;
    private Long productoId;
    private Integer cantidad;
    private LocalDateTime fechaOrden;
}