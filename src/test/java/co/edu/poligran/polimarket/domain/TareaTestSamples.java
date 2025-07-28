package co.edu.poligran.polimarket.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TareaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Tarea getTareaSample1() {
        return new Tarea().id(1L).descripcion("descripcion1").estado("estado1");
    }

    public static Tarea getTareaSample2() {
        return new Tarea().id(2L).descripcion("descripcion2").estado("estado2");
    }

    public static Tarea getTareaRandomSampleGenerator() {
        return new Tarea().id(longCount.incrementAndGet()).descripcion(UUID.randomUUID().toString()).estado(UUID.randomUUID().toString());
    }
}
