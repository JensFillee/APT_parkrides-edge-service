package fact.it.apt_parkridesedgeservice.model;

public class RideDetail {
    private String attractionCode;
    private String name;
    private Integer typeId;
    private Integer minHeight;
    private String description;

    public RideDetail(String attractionCode, String name, Integer typeId, Integer minHeight, String description) {
        this.attractionCode = attractionCode;
        this.name = name;
        this.typeId = typeId;
        this.minHeight = minHeight;
        this.description = description;
    }

    public String getAttractionCode() {
        return attractionCode;
    }

    public void setAttractionCode(String attractionCode) {
        this.attractionCode = attractionCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(Integer minHeight) {
        this.minHeight = minHeight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
