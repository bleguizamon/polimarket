package co.edu.poligran.polimarket.repository;

import co.edu.poligran.polimarket.domain.AreaDeNegocio;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AreaDeNegocio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AreaDeNegocioRepository extends JpaRepository<AreaDeNegocio, Long>, JpaSpecificationExecutor<AreaDeNegocio> {}
