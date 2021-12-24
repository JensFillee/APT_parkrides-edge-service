package fact.it.apt_parkridesedgeservice.model;

import java.util.ArrayList;
import java.util.List;

public class FilledThemepark {

    private String themeparkName;
    private String themeparkAddress;
    private int themeparkCapacity;
    private String themeparkCode;
    private List<RideDetail> rideDetails;

    public FilledThemepark(Themepark themepark,List<Attraction> attractions){
        setThemeparkName(themepark.getName());
        setThemeparkAddress(themepark.getAddress());
        setThemeparkCapacity(themepark.getCapacity());
        setThemeparkCode(themepark.getThemeparkCode());
        rideDetails = new ArrayList<>();
        attractions.forEach(attraction ->
                rideDetails.add(new RideDetail(attraction.getAttractionCode(),
                        attraction.getName(),
                        attraction.getTypeId(),
                        attraction.getMinHeight(),
                        attraction.getDescription()))
                );
        setRideDetails(rideDetails);
    }

    public FilledThemepark(Themepark themepark,Attraction attraction){
        setThemeparkName(themepark.getName());
        setThemeparkAddress(themepark.getAddress());
        setThemeparkCapacity(themepark.getCapacity());
        setThemeparkCode(themepark.getThemeparkCode());
        rideDetails = new ArrayList<>();
        rideDetails.add(new RideDetail(attraction.getAttractionCode(),
                attraction.getName(),
                attraction.getTypeId(),
                attraction.getMinHeight(),
                attraction.getDescription())
        );
        setRideDetails(rideDetails);
    }

    public String getThemeparkName() {
        return themeparkName;
    }

    public void setThemeparkName(String themeparkName) {
        this.themeparkName = themeparkName;
    }

    public String getThemeparkAddress() {
        return themeparkAddress;
    }

    public void setThemeparkAddress(String themeparkAddress) {
        this.themeparkAddress = themeparkAddress;
    }

    public int getThemeparkCapacity() {
        return themeparkCapacity;
    }

    public void setThemeparkCapacity(int themeparkCapacity) {
        this.themeparkCapacity = themeparkCapacity;
    }

    public String getThemeparkCode() {
        return themeparkCode;
    }

    public void setThemeparkCode(String themeparkCode) {
        this.themeparkCode = themeparkCode;
    }

    public List<RideDetail> getRideDetails() {
        return rideDetails;
    }

    public void setRideDetails(List<RideDetail> rideDetails) {
        this.rideDetails = rideDetails;
    }
}
