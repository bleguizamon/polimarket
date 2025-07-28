package co.edu.poligran.polimarket.repository;

import co.edu.poligran.polimarket.domain.Tarea;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Tarea entity.
 */
@Repository
public interface TareaRepository extends JpaRepository<Tarea, Long>, JpaSpecificationExecutor<Tarea> {
    default Optional<Tarea> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Tarea> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Tarea> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select tarea from Tarea tarea left join fetch tarea.vendedor", countQuery = "select count(tarea) from Tarea tarea")
    Page<Tarea> findAllWithToOneRelationships(Pageable pageable);

    @Query("select tarea from Tarea tarea left join fetch tarea.vendedor")
    List<Tarea> findAllWithToOneRelationships();

    @Query("select tarea from Tarea tarea left join fetch tarea.vendedor where tarea.id =:id")
    Optional<Tarea> findOneWithToOneRelationships(@Param("id") Long id);
}
