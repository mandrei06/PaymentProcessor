package org.example.payments.events;

public class PaymentCreated extends Event {
    private final int expectedAmount;

    public PaymentCreated(int expectedAmount) {
        this.expectedAmount = expectedAmount;
    }

    public int getExpectedAmount() {
        return expectedAmount;
    }
}
