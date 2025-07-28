package co.edu.poligran.polimarket.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AreaDeNegocioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AreaDeNegocio getAreaDeNegocioSample1() {
        return new AreaDeNegocio().id(1L).nombre("nombre1").descripcion("descripcion1");
    }

    public static AreaDeNegocio getAreaDeNegocioSample2() {
        return new AreaDeNegocio().id(2L).nombre("nombre2").descripcion("descripcion2");
    }

    public static AreaDeNegocio getAreaDeNegocioRandomSampleGenerator() {
        return new AreaDeNegocio()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .descripcion(UUID.randomUUID().toString());
    }
}
