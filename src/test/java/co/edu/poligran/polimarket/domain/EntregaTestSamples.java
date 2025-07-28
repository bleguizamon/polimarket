package co.edu.poligran.polimarket.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EntregaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Entrega getEntregaSample1() {
        return new Entrega().id(1L).estado("estado1");
    }

    public static Entrega getEntregaSample2() {
        return new Entrega().id(2L).estado("estado2");
    }

    public static Entrega getEntregaRandomSampleGenerator() {
        return new Entrega().id(longCount.incrementAndGet()).estado(UUID.randomUUID().toString());
    }
}
