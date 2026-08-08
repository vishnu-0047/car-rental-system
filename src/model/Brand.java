package model;

public class Brand {

    private int brandId;
    private String brandName;

    public Brand(int brandId, String brandName) {
        this.brandId = brandId;
        this.brandName = brandName;
    }

    public int getBrandId() {
        return brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    @Override
    public String toString() {
        return brandId + " | " + brandName;
    }
}