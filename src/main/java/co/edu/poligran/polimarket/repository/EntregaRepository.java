package co.edu.poligran.polimarket.repository;

import co.edu.poligran.polimarket.domain.Entrega;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Entrega entity.
 */
@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Long>, JpaSpecificationExecutor<Entrega> {
    default Optional<Entrega> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Entrega> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Entrega> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select entrega from Entrega entrega left join fetch entrega.tarea left join fetch entrega.cliente left join fetch entrega.producto",
        countQuery = "select count(entrega) from Entrega entrega"
    )
    Page<Entrega> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select entrega from Entrega entrega left join fetch entrega.tarea left join fetch entrega.cliente left join fetch entrega.producto"
    )
    List<Entrega> findAllWithToOneRelationships();

    @Query(
        "select entrega from Entrega entrega left join fetch entrega.tarea left join fetch entrega.cliente left join fetch entrega.producto where entrega.id =:id"
    )
    Optional<Entrega> findOneWithToOneRelationships(@Param("id") Long id);
}
