package cl.duoc.tienda.orden.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrdenRequest(
    @NotNull(message = "El usuarioId es obligatorio")
    @Positive(message = "El usuarioId debe ser mayor a 0")
    Long usuarioId,

    @NotNull(message = "El productoId es obligatorio")
    @Positive(message = "El productoId debe ser mayor a 0")
    Long productoId,

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a 0")
    Integer cantidad
) {}
