package fact.it.apt_parkridesedgeservice.model;

public class Themepark {
    private String id;
    private String name;
    private String address;
    private int capacity;
    private String themeparkCode;

    public Themepark(){}

    public Themepark(String name, String address, int capacity, String themeparkCode) {
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.themeparkCode = themeparkCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getThemeparkCode() {
        return themeparkCode;
    }

    public void setThemeparkCode(String themeparkCode) {
        this.themeparkCode = themeparkCode;
    }
}
