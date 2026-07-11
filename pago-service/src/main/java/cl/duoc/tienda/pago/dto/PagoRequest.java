package cl.duoc.tienda.pago.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PagoRequest(
    @NotNull(message = "El ordenId es obligatorio")
    @Positive(message = "El ordenId debe ser mayor a 0")
    Long ordenId,

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a 0")
    Double monto
) {}
