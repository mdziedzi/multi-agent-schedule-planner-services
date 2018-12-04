package main.java.Data;

import java.util.Date;

public class ServiceProviderData {
    public final Date openingHour;
    public final Date closingHour;
    public final int maximumNumberOfPlaces;
    public final String name;
    public final String type;
    public final String address;

    public ServiceProviderData() {
        openingHour = null;
        closingHour = null;
        maximumNumberOfPlaces = 0;
        name = null;
        type = null;
        address = null;
    }
}
