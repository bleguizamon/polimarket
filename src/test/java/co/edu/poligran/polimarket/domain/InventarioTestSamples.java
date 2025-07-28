package co.edu.poligran.polimarket.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class InventarioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Inventario getInventarioSample1() {
        return new Inventario().id(1L).cantidad(1).ubicacion("ubicacion1");
    }

    public static Inventario getInventarioSample2() {
        return new Inventario().id(2L).cantidad(2).ubicacion("ubicacion2");
    }

    public static Inventario getInventarioRandomSampleGenerator() {
        return new Inventario()
            .id(longCount.incrementAndGet())
            .cantidad(intCount.incrementAndGet())
            .ubicacion(UUID.randomUUID().toString());
    }
}
