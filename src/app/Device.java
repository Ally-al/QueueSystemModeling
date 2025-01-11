package app;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Random;

public class Device {
    private final double alpha;
    private final double beta;
    private final int deviceCount;
    private ArrayList<Pair<Double, Request>> devices; //list of time executed and request

    private final Random random = new Random();

    public Device() {
        alpha = 0;
        beta = 0;
        deviceCount = 0;
        devices = new ArrayList<>();
    }

    public Device(double alpha, double beta, int deviceCount) {
        this.alpha = alpha;
        this.beta = beta;
        this.deviceCount = deviceCount;
        this.devices = new ArrayList<>();

        for (int i = 0; i < deviceCount; i++) {
            devices.add(null);
        }
    }

    public boolean isFreeDevice() {
        for (int i = 0; i < deviceCount; i++) {
            if (devices.get(i) == null) {
                return true;
            }
        }
        return false;
    }

    //return device number that will execute the request and request
    //or null (not free device)
    public Pair<Integer, Request> put(double currentTime, Request request) {
        double timeExecuting = currentTime + ((beta - alpha) * (random.nextDouble()) + alpha);
        for (int i = 0; i < deviceCount; i++) {
            if (devices.get(i) == null) {
                devices.set(i, new Pair<>(timeExecuting, new Request(currentTime, request.getSourceNumber(), request.getRequestNumber())));
                return new Pair<>(i, new Request(currentTime, request.getSourceNumber(), request.getRequestNumber()));
            }
        }
        //return new Pair<>(-1, new Request(request));
        return null;
    }

    //return list of device number, time executing and request
    public ArrayList<Pair<Integer, Pair<Double, Request>>> getDoneDevice(double currentTime) {
        ArrayList<Pair<Integer, Pair<Double, Request>>> result = new ArrayList<>();
        ArrayList<Pair<Double, Request>> updateDevicesList = new ArrayList<>();

        for (int i = 0; i < deviceCount; i++) {
            Pair<Double, Request> currentDevice = devices.get(i);
            if (currentDevice != null) {
                if (currentDevice.getKey() < currentTime) {
                    result.add(new Pair<>(
                            i,
                            new Pair<>(
                                    currentDevice.getKey(),
                                    new Request(currentDevice.getValue())
                            )
                    ));
                    updateDevicesList.add(null);
                } else {
                    updateDevicesList.add(currentDevice);
                }
            } else {
                updateDevicesList.add(null);
            }
        }
        devices = updateDevicesList;
        return result;
    }
}
