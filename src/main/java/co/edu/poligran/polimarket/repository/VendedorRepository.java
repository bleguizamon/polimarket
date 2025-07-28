package co.edu.poligran.polimarket.repository;

import co.edu.poligran.polimarket.domain.Vendedor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Vendedor entity.
 */
@Repository
public interface VendedorRepository extends JpaRepository<Vendedor, Long>, JpaSpecificationExecutor<Vendedor> {
    default Optional<Vendedor> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Vendedor> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Vendedor> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select vendedor from Vendedor vendedor left join fetch vendedor.areaDeNegocio",
        countQuery = "select count(vendedor) from Vendedor vendedor"
    )
    Page<Vendedor> findAllWithToOneRelationships(Pageable pageable);

    @Query("select vendedor from Vendedor vendedor left join fetch vendedor.areaDeNegocio")
    List<Vendedor> findAllWithToOneRelationships();

    @Query("select vendedor from Vendedor vendedor left join fetch vendedor.areaDeNegocio where vendedor.id =:id")
    Optional<Vendedor> findOneWithToOneRelationships(@Param("id") Long id);
}
