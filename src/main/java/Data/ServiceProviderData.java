package Data;

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

    public ServiceProviderData(
            Date openingHour,
            Date closingHour,
            int maximumNumberOfPlaces,
            String name,
            String type,
            String address) {
        this.openingHour = openingHour;
        this.closingHour = closingHour;
        this.maximumNumberOfPlaces = maximumNumberOfPlaces;
        this.name = name;
        this.type = type;
        this.address = address;
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