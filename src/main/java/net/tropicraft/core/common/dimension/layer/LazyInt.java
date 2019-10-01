package net.tropicraft.core.common.dimension.layer;

import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class LazyInt implements IntSupplier {
    
    private final IntSupplier generator;
    private volatile int value;
    private volatile boolean resolved = false;
    
    public LazyInt(IntSupplier generator) {
        this.generator = generator;
    }
    
    public LazyInt(Supplier<Integer> generator) {
        this((IntSupplier) generator::get);
    }

    @Override
    public int getAsInt() {
        if (!resolved) {
            synchronized (this) {
                if (!resolved) {
                    this.value = generator.getAsInt();
                    this.resolved = true;
                }
            }
        }
        return value;
    }
    
    public synchronized void invalidate() {
        this.resolved = false;
    }
}
