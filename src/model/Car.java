package model;

public class Car {

    private int carId;
    private int modelId;
    private String registrationNumber;
    private int manufacturingYear;
    private double pricePerDay;
    private String status;

    // Extra information obtained using JOIN
    private String brandName;
    private String modelName;
    private String categoryName;

    public Car(int carId,
               int modelId,
               String registrationNumber,
               int manufacturingYear,
               double pricePerDay,
               String status,
               String brandName,
               String modelName,
               String categoryName) {

        this.carId = carId;
        this.modelId = modelId;
        this.registrationNumber = registrationNumber;
        this.manufacturingYear = manufacturingYear;
        this.pricePerDay = pricePerDay;
        this.status = status;
        this.brandName = brandName;
        this.modelName = modelName;
        this.categoryName = categoryName;
    }

    public int getCarId() {
        return carId;
    }

    public int getModelId() {
        return modelId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public int getManufacturingYear() {
        return manufacturingYear;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public String getStatus() {
        return status;
    }

    public String getBrandName() {
        return brandName;
    }

    public String getModelName() {
        return modelName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    @Override
    public String toString() {

        return String.format(
                "%-4d %-15s %-15s %-15s %-6d ₹%-8.2f %-12s",
                carId,
                brandName,
                modelName,
                categoryName,
                manufacturingYear,
                pricePerDay,
                status
        );
    }
}