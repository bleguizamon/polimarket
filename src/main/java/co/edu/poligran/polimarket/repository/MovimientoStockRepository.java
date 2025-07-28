package co.edu.poligran.polimarket.repository;

import co.edu.poligran.polimarket.domain.MovimientoStock;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MovimientoStock entity.
 */
@Repository
public interface MovimientoStockRepository extends JpaRepository<MovimientoStock, Long>, JpaSpecificationExecutor<MovimientoStock> {
    default Optional<MovimientoStock> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<MovimientoStock> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<MovimientoStock> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select movimientoStock from MovimientoStock movimientoStock left join fetch movimientoStock.producto left join fetch movimientoStock.inventario",
        countQuery = "select count(movimientoStock) from MovimientoStock movimientoStock"
    )
    Page<MovimientoStock> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select movimientoStock from MovimientoStock movimientoStock left join fetch movimientoStock.producto left join fetch movimientoStock.inventario"
    )
    List<MovimientoStock> findAllWithToOneRelationships();

    @Query(
        "select movimientoStock from MovimientoStock movimientoStock left join fetch movimientoStock.producto left join fetch movimientoStock.inventario where movimientoStock.id =:id"
    )
    Optional<MovimientoStock> findOneWithToOneRelationships(@Param("id") Long id);
}
