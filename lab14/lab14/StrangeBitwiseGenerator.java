package lab14;

import lab14lib.Generator;

public class StrangeBitwiseGenerator implements Generator {
    private int period;
    private int state;

    public StrangeBitwiseGenerator(int period) {
        this.period = period;
        state = 0;
    }

    @Override
    public double next() {
        state++;
        int weirdState = state & (state >> 3) & (state >> 8) % period;
        return convert(weirdState, period);
    }

    private double convert(int state, int period) {
        return 2 / (double) period * state - 1;
    }

}
