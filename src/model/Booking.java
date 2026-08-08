package model;

import java.sql.Date;

public class Booking {

    private int bookingId;
    private int customerId;
    private int carId;
    private Date startDate;
    private Date endDate;
    private double totalAmount;
    private String status;

    public Booking(int bookingId,
                   int customerId,
                   int carId,
                   Date startDate,
                   Date endDate,
                   double totalAmount,
                   String status) {

        this.bookingId = bookingId;
        this.customerId = customerId;
        this.carId = carId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public int getBookingId() {
        return bookingId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getCarId() {
        return carId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return bookingId + " | " +
                customerId + " | " +
                carId + " | " +
                startDate + " | " +
                endDate + " | " +
                totalAmount + " | " +
                status;
    }
}