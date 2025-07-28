package co.edu.poligran.polimarket.repository;

import co.edu.poligran.polimarket.domain.Inventario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Inventario entity.
 */
@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long>, JpaSpecificationExecutor<Inventario> {
    default Optional<Inventario> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Inventario> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Inventario> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select inventario from Inventario inventario left join fetch inventario.producto",
        countQuery = "select count(inventario) from Inventario inventario"
    )
    Page<Inventario> findAllWithToOneRelationships(Pageable pageable);

    @Query("select inventario from Inventario inventario left join fetch inventario.producto")
    List<Inventario> findAllWithToOneRelationships();

    @Query("select inventario from Inventario inventario left join fetch inventario.producto where inventario.id =:id")
    Optional<Inventario> findOneWithToOneRelationships(@Param("id") Long id);
}
