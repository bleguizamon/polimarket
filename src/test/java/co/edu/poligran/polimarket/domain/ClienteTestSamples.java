package co.edu.poligran.polimarket.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ClienteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Cliente getClienteSample1() {
        return new Cliente().id(1L).nombre("nombre1").contacto("contacto1");
    }

    public static Cliente getClienteSample2() {
        return new Cliente().id(2L).nombre("nombre2").contacto("contacto2");
    }

    public static Cliente getClienteRandomSampleGenerator() {
        return new Cliente().id(longCount.incrementAndGet()).nombre(UUID.randomUUID().toString()).contacto(UUID.randomUUID().toString());
    }
}
