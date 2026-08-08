package model;

public class Model {

    private int modelId;
    private int brandId;
    private int categoryId;
    private String modelName;

    public Model(int modelId,
                 int brandId,
                 int categoryId,
                 String modelName) {

        this.modelId = modelId;
        this.brandId = brandId;
        this.categoryId = categoryId;
        this.modelName = modelName;
    }

    public int getModelId() {
        return modelId;
    }

    public int getBrandId() {
        return brandId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getModelName() {
        return modelName;
    }

    @Override
    public String toString() {
        return modelId + " | " +
                brandId + " | " +
                categoryId + " | " +
                modelName;
    }
}