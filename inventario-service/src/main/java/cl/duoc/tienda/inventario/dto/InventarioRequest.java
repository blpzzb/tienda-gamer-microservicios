package cl.duoc.tienda.inventario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record InventarioRequest(
    @NotNull(message = "El productoId es obligatorio")
    @Positive(message = "El productoId debe ser mayor a 0")
    Long productoId,

    @NotNull(message = "El stock actual es obligatorio")
    @PositiveOrZero(message = "El stock actual no puede ser negativo")
    Integer stockActual,

    @NotBlank(message = "La ubicacion de bodega es obligatoria")
    String ubicacionBodega
) {}
