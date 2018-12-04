package main.java.Data;

import java.util.Date;

public class ServiceProviderData {
    public Date openingHour;
    public Date closingHour;
    public int maximumNumberOfPlaces;
    public String name;
    public String type;
    public String address;

    public ServiceProviderData() {
        openingHour = null;
        closingHour = null;
        maximumNumberOfPlaces = 0;
        name = null;
        type = null;
        address = null;
    }

    @Override
    public String toString() {
        return "ServiceProviderData{" +
                "openingHour=" + openingHour +
                ", closingHour=" + closingHour +
                ", maximumNumberOfPlaces=" + maximumNumberOfPlaces +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", address='" + address + '\'' +
                '}';
    }


}
