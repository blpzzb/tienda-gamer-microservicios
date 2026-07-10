package cl.duoc.tienda.notificacion.dto;
import jakarta.validation.constraints.*;
public record NotificacionRequest(@NotNull @Positive Long usuarioId, @NotBlank String mensaje, @NotNull Boolean leida) {}
