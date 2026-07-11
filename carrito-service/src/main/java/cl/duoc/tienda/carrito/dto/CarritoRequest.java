package cl.duoc.tienda.carrito.dto;
import jakarta.validation.constraints.*;
public record CarritoRequest(@NotNull @Positive Long usuarioId, @NotNull @Positive Long productoId, @NotNull @Positive Integer cantidad) {}
