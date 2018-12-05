package Data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
        String serializedObject = "";
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(this);
            so.flush();
            serializedObject = bo.toString();

        } catch (Exception e) {
            System.out.println(e);
        }
        return serializedObject;
    }

    public static ServiceProviderData fromString(String serializedServiceProviderData){
        ServiceProviderData s = null;
        try {
            byte b[] = serializedServiceProviderData.getBytes();
            ByteArrayInputStream bi = new ByteArrayInputStream(b);
            ObjectInputStream si = new ObjectInputStream(bi);
            s =  (ServiceProviderData) si.readObject();
        } catch (Exception e) {
            System.out.println(e);
        }
        return s;
    }

}