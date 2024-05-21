package org.example.payments.processor;


import org.example.payments.enums.PaymentState;
import org.example.payments.events.Event;
import org.example.payments.events.PaymentCancelled;
import org.example.payments.events.PaymentCreated;
import org.example.payments.events.TransferReceived;

import java.util.List;

public class PaymentProcessor {
    public static PaymentState determinePaymentState(List<Event> events) {
        int expectedAmount = 0;
        int receivedAmount = 0;
        boolean paymentCreated = false;
        boolean paymentCancelled = false;

        for (Event event : events) {
            if (event instanceof PaymentCreated) {
                PaymentCreated paymentCreatedEvent = (PaymentCreated) event;
                expectedAmount = paymentCreatedEvent.getExpectedAmount();
                paymentCreated = true;
            } else if (event instanceof TransferReceived) {
                TransferReceived transferReceivedEvent = (TransferReceived) event;
                receivedAmount += transferReceivedEvent.getAmount();
            } else if (event instanceof PaymentCancelled) {
                paymentCancelled = true;
            }
        }

        if (paymentCancelled) {
            return PaymentState.CANCELLED;
        } else if (paymentCreated) {
            if (receivedAmount == 0) {
                return PaymentState.NEW;
            } else if (receivedAmount >= expectedAmount) {
                return PaymentState.PAID;
            } else {
                return PaymentState.PARTIALLY_PAID;
            }
        }

        throw new IllegalStateException("Invalid sequence of events");
    }

    public static void main(String[] args) {
        // Test examples
        List<Event> events1 = List.of(new PaymentCreated(3), new TransferReceived(3));
        System.out.println(determinePaymentState(events1)); //PAID

        List<Event> events2 = List.of(new PaymentCreated(3), new TransferReceived(1), new TransferReceived(2));
        System.out.println(determinePaymentState(events2)); //PAID

        List<Event> events3 = List.of(new PaymentCreated(3), new TransferReceived(1), new TransferReceived(1));
        System.out.println(determinePaymentState(events3)); //PARTIALLY_PAID

        List<Event> events4 = List.of(new PaymentCreated(3), new TransferReceived(1), new TransferReceived(1), new PaymentCancelled());
        System.out.println(determinePaymentState(events4)); //CANCELLED
    }
}