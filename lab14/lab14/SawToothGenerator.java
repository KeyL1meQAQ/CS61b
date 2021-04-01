package lab14;

import lab14lib.Generator;

public class SawToothGenerator implements Generator {
    private int period;
    private int state;

    public SawToothGenerator(int period) {
        this.period = period;
        state = 0;
    }

    @Override
    public double next() {
        state++;
        return convert(state % period, period);
    }

    private double convert(int state, int period) {
        return 2 / (double) period * state - 1;
    }
}
