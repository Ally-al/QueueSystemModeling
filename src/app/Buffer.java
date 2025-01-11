package app;

import javafx.util.Pair;

import java.util.ArrayList;


public class Buffer {
    private final int bufferSize;
    private int putIndex = 0;
    private int getIndex = 0;
    private ArrayList<Request> requests;
    private int sourceCount;


    public Buffer() {
        bufferSize = 0;
        requests = new ArrayList<>();
    }

    public Buffer(int size, int sourceCount) {
        this.bufferSize = size;
        this.sourceCount = sourceCount;
        this.requests = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            requests.add(null);
        }
    }

    public boolean isEmptyBuffer() {
        for (int i = 0; i < bufferSize; i++) {
            if (requests.get(i) != null) {
                return false;
            }
        }
        return true;
    }

    public void setSourceCount(int sourceCount) {
        this.sourceCount = sourceCount;
    }

    //true - if was inserted
    //false - if was rejected from buffer
    public Pair<Boolean, Pair<Integer, Request>> put(Request newRequest) {
        for (int i = 0; i < bufferSize; i++) {
            if (requests.get(putIndex) == null) {
                requests.set(putIndex, new Request(newRequest));
                int insertedIndex = putIndex;
                putIndex++;
                putIndex = calculateCorrectIndex(putIndex);
                return new Pair<>(true, new Pair<>(insertedIndex, new Request(newRequest)));
            }
            putIndex++;
            putIndex = calculateCorrectIndex(putIndex);
        }

        int delIndex = findOldestRequestIndex(requests);
        Request deletedRequest = new Request(requests.get(delIndex));
        requests.set(delIndex, new Request(newRequest));
        putIndex++;
        putIndex = calculateCorrectIndex(putIndex);
        return new Pair<>(false, new Pair<>(delIndex, deletedRequest));
    }

    //return index of buffer and request
    public Pair<Integer, Request> get() {
        if (isEmptyBuffer()) {
            return null;
        }

        while (true) {
            for (int i = 0; i < bufferSize; i++) {
                Request tmpRequest = requests.get(getIndex);
                if (tmpRequest != null) {
                        requests.set(getIndex, null);
                        return new Pair<>(getIndex, new Request(tmpRequest));
                }
                getIndex++;
                getIndex = calculateCorrectIndex(getIndex);
            }
        }
    }


    private int findOldestRequestIndex(ArrayList<Request> requests) {
        double minTime = requests.get(0).getTime();
        int index = 0;
        for (int i = 1; i < bufferSize; i++) {
            Request tmpRequest = requests.get(i);
            if (tmpRequest.getTime() < minTime) {
                minTime = tmpRequest.getTime();
                index = i;
            }
        }
        return index;
    }

    private int calculateCorrectIndex(int index) {
        return (index >= bufferSize) || (index < 0) ? 0 : index;
    }
}
