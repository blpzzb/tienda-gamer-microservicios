package cl.duoc.tienda.categoria.repository;
import cl.duoc.tienda.categoria.model.Categoria; import org.springframework.data.jpa.repository.JpaRepository;
public interface CategoriaRepository extends JpaRepository<Categoria,Long> {}
