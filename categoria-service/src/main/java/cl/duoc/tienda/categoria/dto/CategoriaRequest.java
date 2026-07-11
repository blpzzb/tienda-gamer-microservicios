package cl.duoc.tienda.categoria.dto;
import jakarta.validation.constraints.*;
public record CategoriaRequest(@NotBlank(message="El nombre es obligatorio") String nombre, String descripcion) {}
