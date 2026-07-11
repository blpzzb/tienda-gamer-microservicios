package cl.duoc.tienda.proveedor.dto;
import jakarta.validation.constraints.*;
public record ProveedorRequest(@NotBlank(message="El nombre es obligatorio") String nombre, @Email(message="Correo invalido") @NotBlank(message="El correo es obligatorio") String email) {}
