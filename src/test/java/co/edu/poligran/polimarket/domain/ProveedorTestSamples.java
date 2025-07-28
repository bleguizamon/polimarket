package co.edu.poligran.polimarket.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ProveedorTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Proveedor getProveedorSample1() {
        return new Proveedor().id(1L).nombre("nombre1").telefono("telefono1");
    }

    public static Proveedor getProveedorSample2() {
        return new Proveedor().id(2L).nombre("nombre2").telefono("telefono2");
    }

    public static Proveedor getProveedorRandomSampleGenerator() {
        return new Proveedor().id(longCount.incrementAndGet()).nombre(UUID.randomUUID().toString()).telefono(UUID.randomUUID().toString());
    }
}
