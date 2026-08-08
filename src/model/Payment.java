package model;

import java.sql.Date;

public class Payment {

    private int paymentId;
    private int bookingId;
    private String paymentMethod;
    private Date paymentDate;
    private double amount;
    private String paymentStatus;

    public Payment(int paymentId,
                   int bookingId,
                   String paymentMethod,
                   Date paymentDate,
                   double amount,
                   String paymentStatus) {

        this.paymentId = paymentId;
        this.bookingId = bookingId;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public double getAmount() {
        return amount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    @Override
    public String toString() {
        return paymentId + " | " +
                bookingId + " | " +
                paymentMethod + " | " +
                paymentDate + " | " +
                amount + " | " +
                paymentStatus;
    }
}