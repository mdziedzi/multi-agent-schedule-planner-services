package main.java.Exceptions;

public class negativeValueException extends Exception {
    public negativeValueException(String msg) {
        super("Negative value in: " + msg);
    }
}
