package org.example.payments.events;

public class TransferReceived extends Event{
    private final int amount;

    public TransferReceived(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
