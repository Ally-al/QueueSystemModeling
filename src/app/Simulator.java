package app;

import app.analytics.Analytics;
import app.analytics.SourceAnalytics;
import app.timeDiagram.CoordinateInfo;
import javafx.util.Pair;

import java.util.ArrayList;

public class Simulator {
    private Settings settings;
    private Source source;
    private Buffer buffer;
    private Device device;
    private Analytics analytics;

    private CoordinateInfo coordinateInfo;

    public Simulator(Settings settings) {
        this.settings = settings;
        analytics = new Analytics(settings);
        source = new Source(settings.getLambda(), settings.getSourceCount());
        buffer = new Buffer(settings.getBufferSize(), settings.getSourceCount());
        device = new Device(settings.getAlpha(), settings.getBeta(), settings.getDeviceCount());
        coordinateInfo = new CoordinateInfo(settings.getSourceCount(), settings.getBufferSize(), settings.getDeviceCount());
    }

    public void simulate() {
        double currentTime = 0.0;

        for (int i = 0; i < settings.getRequestsAmount(); i++) {
            //generate request
            Request request = source.generate();
            //coordinateInfo.addImpulse("S" + request.getSourceNumber(), request.getTime(), request.getSourceNumber().toString() + '.' + request.getRequestNumber().toString());

            analytics.setSourceRequestGenerated(request.getSourceNumber(), request.getRequestNumber());
            currentTime += request.getTime();
            request.setTime(currentTime);

            //get number of Free Devices and Completed request whit time executing
            ArrayList<Pair<Integer, Pair<Double, Request>>> doneDevices = device.getDoneDevice(currentTime);
            //sort from an earlier request to a later one
            doneDevices.sort((o1, o2) -> {
                if (o1.getValue().getKey() < o2.getValue().getKey()) {
                    return -1;
                } else if (o1.getValue().getKey().equals(o2.getValue().getKey())) {
                    return 0;
                } else {
                    return 1;
                }
            });
            for (Pair<Integer, Pair<Double, Request>> deviceItem : doneDevices) {
                analytics.removeRequestFromDevice(deviceItem.getValue().getKey(), new Pair<>(deviceItem.getKey(), deviceItem.getValue().getValue()));
                analytics.increaseSourceRequestDone(deviceItem.getValue().getValue().getSourceNumber());
                analytics.increaseSourceTimeOfProcess(
                        deviceItem.getValue().getValue().getSourceNumber(),
                        deviceItem.getValue().getKey() - deviceItem.getValue().getValue().getTime()
                );
                analytics.increaseDeviceTimeOfWork(deviceItem.getKey(), deviceItem.getValue().getKey() - deviceItem.getValue().getValue().getTime());

                addRequestToDeviceFromBuffer(deviceItem.getValue().getKey());
            }

            analytics.newRequestGenerate(currentTime, new Pair<>(request.getSourceNumber(), new Request(request)));


            //add request to buffer
            Pair<Boolean, Pair<Integer, Request>> tmpRequest = buffer.put(request);
            if (tmpRequest.getKey()) {
                analytics.addRequestToBuffer(currentTime, new Pair<>(tmpRequest.getValue().getKey(), tmpRequest.getValue().getValue()));
            } else {
                analytics.removeRequestFromBuffer(currentTime, new Pair<>(tmpRequest.getValue().getKey(), tmpRequest.getValue().getValue()));
                analytics.increaseSourceRequestRejected(tmpRequest.getValue().getValue().getSourceNumber());
                analytics.increaseSourceTimeOfWait(tmpRequest.getValue().getValue().getSourceNumber(), currentTime - tmpRequest.getValue().getValue().getTime());
                analytics.addRequestToBuffer(currentTime, new Pair<>(tmpRequest.getValue().getKey(), new Request(request)));
            }
            addRequestToDeviceFromBuffer(currentTime);
        }
        analytics.setAllTimeOfWork(currentTime);
        analytics.setDeviceAllTimeOfWork();

//        if (analytics.getSourceRejectProbability() < 0.05 && analytics.getSourceAverageTimeWaiting() < 0.7 &&
//                analytics.getUtilizationRate() > 90 && analytics.getSourceAverageTimeWaiting() >= 0 &&
//                analytics.getUtilizationRate() < 100) {
            System.out.print(settings.getSourceCount() + " " + settings.getBufferSize() + " " + settings.getDeviceCount() + "   ");
            System.out.print("Pотк = " + analytics.getSourceRejectProbabilityS() + "    ");
            System.out.print("Tбп = " + analytics.getSourceAverageTimeWaitingS() + "    ");
            System.out.print("Кисп = " + analytics.getUtilizationRateS() + "    ");
            System.out.println("прибыль = " + analytics.getPriceRateS() + "    ");
        //}
    }

    public Analytics getAnalytics() {
        return analytics;
    }

    public Integer getStepCount() {
        return analytics.getStepCount();
    }

    private void addRequestToDeviceFromBuffer(Double currentTime) {
        while (device.isFreeDevice()) {
            Pair<Integer, Request> requestFromBuffer = buffer.get();
            if (requestFromBuffer != null && device.isFreeDevice()) {
                analytics.getRequestFromBuffer(currentTime, new Pair<>(requestFromBuffer.getKey(), requestFromBuffer.getValue()));
                analytics.increaseSourceTimeOfWait(requestFromBuffer.getValue().getSourceNumber(), currentTime - requestFromBuffer.getValue().getTime());

                Pair<Integer, Request> requestToDevice = device.put(currentTime, requestFromBuffer.getValue());
                analytics.addRequestToDevice(currentTime, requestToDevice);
            } else {
                break;
            }
        }
    }

    public CoordinateInfo getCoordinateInfo() {
        return coordinateInfo;
    }
}
