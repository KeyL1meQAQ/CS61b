package lab14;

import lab14lib.Generator;

public class AcceleratingSawToothGenerator implements Generator {
    private int period;
    private double factor;
    private int state;
    private int stateInPeriod;

    public AcceleratingSawToothGenerator(int period, double factor) {
        this.period = period;
        this.factor = factor;
        state = 0;
        stateInPeriod = 0;
    }

    @Override
    public double next() {
        state++;
        if (stateInPeriod == period + 1) {
            stateInPeriod = 0;
            period = (int) Math.round(period * factor);
        } else {
            stateInPeriod++;
        }
        if (stateInPeriod == 0) {
            return -1;
        }
        return convert(stateInPeriod, period);
    }

    private double convert(int stateInPeriod, int period) {
        return 2 / (double) period * stateInPeriod - 1;
    }
}
