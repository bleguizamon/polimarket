package co.edu.poligran.polimarket.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MovimientoStockTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static MovimientoStock getMovimientoStockSample1() {
        return new MovimientoStock().id(1L).tipo("tipo1").cantidad(1).referencia("referencia1");
    }

    public static MovimientoStock getMovimientoStockSample2() {
        return new MovimientoStock().id(2L).tipo("tipo2").cantidad(2).referencia("referencia2");
    }

    public static MovimientoStock getMovimientoStockRandomSampleGenerator() {
        return new MovimientoStock()
            .id(longCount.incrementAndGet())
            .tipo(UUID.randomUUID().toString())
            .cantidad(intCount.incrementAndGet())
            .referencia(UUID.randomUUID().toString());
    }
}
