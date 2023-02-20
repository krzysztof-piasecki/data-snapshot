package pl.krzysztof.piasecki.datasnapshot.utils;

public class Counter {
    private int validCounter;
    private int invalidCounter;

    public Counter() {
        this.validCounter = 0;
        this.invalidCounter = 0;
    }

    public void addValid(int number) {
        validCounter += number;
    }

    public void addInvalid(int number) {
        invalidCounter += number;
    }

    public int getValidCounter() {
        return validCounter;
    }

    public int getInvalidCounter() {
        return invalidCounter;
    }
}
