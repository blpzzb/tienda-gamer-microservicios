package cl.duoc.tienda.orden.dto;

import lombok.Data;

@Data
public class ProductoDTO {

    private Long id;
    private String nombre;
    private String categoria;
    private Double precio;
    private Integer stock;
}