package app;

public class Settings {
    private Integer sourceCount;
    private Integer bufferSize;
    private Integer deviceCount;
    private Integer requestsAmount;

    private Double alpha;
    private Double beta;
    private Double lambda;

    public Settings() {
        sourceCount = 12;
        bufferSize = 6;
        deviceCount = 3;
        requestsAmount = 5000;
        alpha = 0.1;
        beta = 0.3;
        lambda = 1.2;
    }

    public Settings(Integer sourceCount, Integer bufferSize, Integer deviceCount, Integer requestsAmount, Double alpha, Double beta, Double lambda) {
        this.sourceCount = sourceCount;
        this.bufferSize = bufferSize;
        this.deviceCount = deviceCount;
        this.requestsAmount = requestsAmount;
        this.alpha = alpha;
        this.beta = beta;
        this.lambda = lambda;
    }

    public void setSourceCount(Integer sourceCount) {
        this.sourceCount = sourceCount;
    }

    public void setBufferSize(Integer bufferSize) {
        this.bufferSize = bufferSize;
    }

    public void setDeviceCount(Integer deviceCount) {
        this.deviceCount = deviceCount;
    }

    public void setRequestsAmount(Integer requestsAmount) {
        this.requestsAmount = requestsAmount;
    }

    public void setAlpha(Double alpha) {
        this.alpha = alpha;
    }

    public void setBeta(Double beta) {
        this.beta = beta;
    }

    public void setLambda(Double lambda) {
        this.lambda = lambda;
    }

    public Integer getSourceCount() {
        return sourceCount;
    }

    public Integer getBufferSize() {
        return bufferSize;
    }

    public Integer getDeviceCount() {
        return deviceCount;
    }

    public Integer getRequestsAmount() {
        return requestsAmount;
    }

    public Double getAlpha() {
        return alpha;
    }

    public Double getBeta() {
        return beta;
    }

    public Double getLambda() {
        return lambda;
    }
}
