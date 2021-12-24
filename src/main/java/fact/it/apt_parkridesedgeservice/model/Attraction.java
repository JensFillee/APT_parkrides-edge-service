package fact.it.apt_parkridesedgeservice.model;

public class Attraction {
    private int id;
    private String name;
    private int minHeight;
    private int typeId;
    private String description;
    private String themeparkCode;
    private String attractionCode;


    public Attraction(){}

    public Attraction(String name, int minHeight, int typeId, String description, String themeparkCode, String attractionCode) {
        this.name = name;
        this.minHeight = minHeight;
        this.typeId = typeId;
        this.description = description;
        this.themeparkCode = themeparkCode;
        this.attractionCode = attractionCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getThemeparkCode() {
        return themeparkCode;
    }

    public void setThemeparkCode(String themeparkCode) {
        this.themeparkCode = themeparkCode;
    }

    public String getAttractionCode() {
        return attractionCode;
    }

    public void setAttractionCode(String attractionCode) {
        this.attractionCode = attractionCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
