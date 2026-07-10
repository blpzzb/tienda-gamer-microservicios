package cl.duoc.tienda.notificacion.repository;
import cl.duoc.tienda.notificacion.model.Notificacion; import org.springframework.data.jpa.repository.JpaRepository;
public interface NotificacionRepository extends JpaRepository<Notificacion,Long> {}
