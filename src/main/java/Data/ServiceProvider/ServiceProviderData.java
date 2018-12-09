package Data.ServiceProvider;

import java.io.*;
import java.util.Base64;
import java.util.Date;

public class ServiceProviderData implements Serializable {
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

    public static String serialize(ServiceProviderData data) {
        String serializedObject = "";
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream so = new ObjectOutputStream(bo);
            so.writeObject(data);
            so.flush();
            final byte[] byteArray = bo.toByteArray();
            serializedObject = Base64.getEncoder().encodeToString(byteArray);

        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return serializedObject;
    }

    public static ServiceProviderData deserialize(String serializedServiceProviderData){
        ServiceProviderData s = null;
        try {
            final byte[] bytes = Base64.getDecoder().decode(serializedServiceProviderData);
            byte b[] = serializedServiceProviderData.getBytes();
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream si = new ObjectInputStream(bi);
            s =  (ServiceProviderData) si.readObject();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return s;
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