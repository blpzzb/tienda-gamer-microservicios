package cl.duoc.tienda.orden.dto;

import lombok.Data;

@Data
public class InventarioDTO {

    private Long id;
    private Long productoId;
    private Integer stockActual;
    private String ubicacionBodega;
}