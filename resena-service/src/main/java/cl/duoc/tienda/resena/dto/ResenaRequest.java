package cl.duoc.tienda.resena.dto;
import jakarta.validation.constraints.*;
public record ResenaRequest(@NotNull @Positive Long usuarioId, @NotNull @Positive Long productoId, @Min(1) @Max(5) Integer calificacion, @NotBlank String comentario) {}
