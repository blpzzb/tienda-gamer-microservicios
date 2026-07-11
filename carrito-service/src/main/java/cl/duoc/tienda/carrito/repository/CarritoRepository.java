package cl.duoc.tienda.carrito.repository;
import cl.duoc.tienda.carrito.model.Carrito; import org.springframework.data.jpa.repository.JpaRepository;
public interface CarritoRepository extends JpaRepository<Carrito,Long> {}
