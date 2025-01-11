package app;

import javafx.util.Pair;

import java.util.Random;

public class Source {
    private final double lambda;
    private final int sourceCount;
    private final double[] times;
    private final int[] requestsNumbers;

    private final Random random = new Random();

    public Source() {
        lambda = 0.0;
        sourceCount = 0;
        times = new double[sourceCount];
        requestsNumbers = new int[sourceCount];
    }

    public Source(Double lambda, Integer sourceCount) {
        this.lambda = lambda;
        this.sourceCount = sourceCount;
        times = new double[sourceCount];
        requestsNumbers = new int[sourceCount];

        for (int i = 0; i < sourceCount; i++) {
            requestsNumbers[i] = 0;
            times[i] = -1.0;
        }
    }

    public Request generate() {
        fillTimes();
        Pair<Integer, Double> nearestRequest = getMinTimeAndSourceNumber();
        removeMinTime(nearestRequest.getValue());

        return new Request(nearestRequest.getValue(), nearestRequest.getKey(), requestsNumbers[nearestRequest.getKey()]);
    }

    private void fillTimes() {
        for (int i = 0; i < sourceCount; i++) {
            if (times[i] <= 0) {
                requestsNumbers[i]++;
                times[i] = (-1 / lambda) * Math.log(random.nextDouble()); // пуассоновский закон распределения
            }
        }
    }

    private Pair<Integer, Double> getMinTimeAndSourceNumber() {
        double minTime = times[0];
        int minSourceNumber = 0;
        for (int i = 0; i < sourceCount; i++) {
            if (times[i] < minTime) {
                minTime = times[i];
                minSourceNumber = i;
            }
        }
        return new Pair<>(minSourceNumber, minTime);
    }

    private void removeMinTime(double minTime) {
        for (int i = 0; i < sourceCount; i++) {
            times[i] -= minTime;
        }
    }
}
