package app.analytics;

import app.Request;
import app.Settings;
import javafx.util.Pair;

import java.util.ArrayList;

public class Analytics {
    private ArrayList<StepModel> steps;
    private ArrayList<SourceAnalytics> sourcesAnalytics;
    private ArrayList<DeviceAnalytics> deviceAnalytics;
    private Double allTimeOfWork;

    public Analytics(Settings settings) {
        steps = new ArrayList<>();
        sourcesAnalytics = new ArrayList<>();
        for (int i = 0; i < settings.getSourceCount(); i++) {
            sourcesAnalytics.add(new SourceAnalytics(i + 1));
        }
        deviceAnalytics = new ArrayList<>();
        for (int i = 0; i < settings.getDeviceCount(); i++) {
            deviceAnalytics.add(new DeviceAnalytics(i + 1));
        }
    }

    public void newRequestGenerate(double currentTime, Pair<Integer, Request> data) {
        steps.add(new StepModel(currentTime, Actions.NEW_REQUEST, data));
    }

    public void addRequestToBuffer(double currentTime, Pair<Integer, Request> data) {
        steps.add(new StepModel(currentTime, Actions.ADD_TO_BUFFER, data));
    }

    public void removeRequestFromBuffer(double currentTime, Pair<Integer, Request> data) {
        steps.add(new StepModel(currentTime, Actions.REMOVE_FROM_BUFFER, data));
    }

    public void getRequestFromBuffer(double currentTime, Pair<Integer, Request> data) {
        steps.add(new StepModel(currentTime, Actions.GET_FROM_BUFFER, data));
    }

    public void addRequestToDevice(double currentTime, Pair<Integer, Request> data) {
        steps.add(new StepModel(currentTime, Actions.ADD_TO_DEVICE, data));
    }

    public void removeRequestFromDevice(double currentTime, Pair<Integer, Request> data) {
        steps.add(new StepModel(currentTime, Actions.REMOVE_FROM_DEVICE, data));
    }

    public void setSourceRequestGenerated(Integer sourceId, Integer requestAmount) {
        sourcesAnalytics.get(sourceId).setRequestGenerated(requestAmount);
    }

    public void increaseSourceRequestDone(Integer sourceId) {
        sourcesAnalytics.get(sourceId).setRequestDone(sourcesAnalytics.get(sourceId).getRequestDone() + 1);
    }

    public void increaseSourceRequestRejected(Integer sourceId) {
        sourcesAnalytics.get(sourceId).setRequestReject(sourcesAnalytics.get(sourceId).getRequestReject() + 1);
    }

    public void increaseSourceTimeOfWait(Integer sourceId, Double timeOfWait) {
        sourcesAnalytics.get(sourceId).increaseRequestTimeOfWait(timeOfWait);
    }

    public void increaseSourceTimeOfProcess(Integer sourceId, Double timeOfProcess) {
        sourcesAnalytics.get(sourceId).increaseRequestTimeOfProcess(timeOfProcess);
    }

    public ArrayList<Double> getSourceAvarageTimeOfWaiting() {
        ArrayList<Double> result = new ArrayList<>();
        for (SourceAnalytics item : sourcesAnalytics) {
            result.add(item.getAverageTimeOfWaiting());
        }
        return result;
    }

    public ArrayList<Double> getSourceAvarageTimeOfProcess() {
        ArrayList<Double> result = new ArrayList<>();
        for (SourceAnalytics item : sourcesAnalytics) {
            result.add(item.getAverageTimeOfProcess());
        }
        return result;
    }

    public String getSourceAverageTimeWaitingS() {
        double sum = 0;
        for (SourceAnalytics item : sourcesAnalytics) {
            sum += item.getAverageTimeOfWaiting();
        }
        return String.format("%.3f", sum / sourcesAnalytics.size());
    }
    public double getSourceAverageTimeWaiting() {
        double sum = 0;
        for (SourceAnalytics item : sourcesAnalytics) {
            sum += item.getAverageTimeOfWaiting();
        }
        return sum / sourcesAnalytics.size();
    }

    public String getSourceRejectProbabilityS() {
        double sum = 0;
        for (SourceAnalytics item : sourcesAnalytics) {
            sum += item.getRequestRejectProbability();
        }
        return String.format("%.2f", sum / sourcesAnalytics.size() * 100) + "%";
        //return sum / sourcesAnalytics.size();
    }

    public double getSourceRejectProbability() {
        double sum = 0;
        for (SourceAnalytics item : sourcesAnalytics) {
            sum += item.getRequestRejectProbability();
        }
        //return String.format("%.2f", sum / sourcesAnalytics.size() * 100) + "%";
        return sum / sourcesAnalytics.size();
    }

//    public ArrayList<Double> getSourceRejectProbability() {
//        ArrayList<Double> result = new ArrayList<>();
//        for (SourceAnalytics item : sourcesAnalytics) {
//            result.add(item.getRequestRejectProbability());
//        }
//        return result;
//    }

    public ArrayList<Double> getSourceDispersionTimeOfWait() {
        ArrayList<Double> result = new ArrayList<>();
        for (SourceAnalytics item : sourcesAnalytics) {
            result.add(item.getDispersionTimeOfWait());
        }
        return result;
    }

    public ArrayList<Double> getSourceDispersionTimeOfProcess() {
        ArrayList<Double> result = new ArrayList<>();
        for (SourceAnalytics item : sourcesAnalytics) {
            result.add(item.getDispersionTimeOfProcess());
        }
        return result;
    }

    public void increaseDeviceTimeOfWork(Integer deviceId, Double timeOfWork) {
        deviceAnalytics.get(deviceId).increaseTimeOfWork(timeOfWork);
    }

    public String getPriceRateS() {
        double requestCount = 0;
        for (SourceAnalytics item : sourcesAnalytics) {
            requestCount += item.getRequestGenerated();
        }
        double income =  requestCount * 800000 / allTimeOfWork;
        double expense = 0;
        double podkormka = requestCount * 5000 / allTimeOfWork;
        double inventar = 50000 / allTimeOfWork;
        double vspashka = 1000 * sourcesAnalytics.size() * (requestCount / sourcesAnalytics.size() / 25) / allTimeOfWork;
        double kusti = sourcesAnalytics.size() * 52000 * (requestCount / deviceAnalytics.size() / 25) / allTimeOfWork;
        double chaniSum = 0;
        for (int i = 0; i < deviceAnalytics.size(); i++) {
            chaniSum += (120000 - i * 10000);
        }
        double chani = chaniSum / allTimeOfWork;
        expense += podkormka + inventar + vspashka + kusti + chani;
        return String.format("%.0f", income - expense);
    }


    public String getUtilizationRateS() {
        double sum = 0;
        for (DeviceAnalytics item : deviceAnalytics) {
            sum += item.getUtilizationRate();
        }
        double result = sum / deviceAnalytics.size() * 100;
        if (result > 100) {
            result = 100;
        }
        return String.format("%.2f", result) + "%";
    }
    public double getUtilizationRate() {
        double sum = 0;
        for (DeviceAnalytics item : deviceAnalytics) {
            sum += item.getUtilizationRate();
        }
        double result = sum / deviceAnalytics.size() * 100;
        if (result > 100) {
            result = 100;
        }
        return result;
    }

    public void setDeviceAllTimeOfWork() {
        for (DeviceAnalytics item : deviceAnalytics) {
            item.setAllTimeOfWork(allTimeOfWork);
        }
    }

    public ArrayList<SourceAnalytics> getSourcesAnalytics() {
        return sourcesAnalytics;
    }

    public ArrayList<DeviceAnalytics> getDeviceAnalytics() {
        return deviceAnalytics;
    }

    public ArrayList<StepModel> getSteps() {
        return steps;
    }

    public Integer getStepCount() {
        return steps.size();
    }

    public void setAllTimeOfWork(Double allTimeOfWork) {
        this.allTimeOfWork = allTimeOfWork;
    }
}


