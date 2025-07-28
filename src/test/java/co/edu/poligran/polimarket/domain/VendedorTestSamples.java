package co.edu.poligran.polimarket.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class VendedorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Vendedor getVendedorSample1() {
        return new Vendedor().id(1L).nombre("nombre1");
    }

    public static Vendedor getVendedorSample2() {
        return new Vendedor().id(2L).nombre("nombre2");
    }

    public static Vendedor getVendedorRandomSampleGenerator() {
        return new Vendedor().id(longCount.incrementAndGet()).nombre(UUID.randomUUID().toString());
    }
}
