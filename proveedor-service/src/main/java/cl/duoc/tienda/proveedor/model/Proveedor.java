package cl.duoc.tienda.proveedor.model;
import jakarta.persistence.*;
@Entity @Table(name="proveedores") public class Proveedor { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id; private String nombre; private String email; public Proveedor(){} public Proveedor(String nombre, String email){this.nombre=nombre; this.email=email;} public Long getId(){return id;} public void setId(Long v){id=v;} public String getNombre(){return nombre;} public void setNombre(String v){nombre=v;} public String getEmail(){return email;} public void setEmail(String v){email=v;} }
