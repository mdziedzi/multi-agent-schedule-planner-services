package Data.ServiceProvider;

import jade.core.AID;

import java.io.*;
import java.util.Base64;
import java.util.Date;

public class ReservationData implements Serializable {
    public  int id;
    public AID agentId;
    public Date beginHour;
    public Date endHour;

    public ReservationData() {
        id = 0;
        //agentId = null;
        beginHour = null;
        endHour = null;
    }


    public static String serialize(ReservationData data) {
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

    public static ReservationData deserialize(String serializedReservationRequestData){
        ReservationData s = null;
        try {
            final byte[] bytes = Base64.getDecoder().decode(serializedReservationRequestData);
            byte b[] = serializedReservationRequestData.getBytes();
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream si = new ObjectInputStream(bi);
            s =  (ReservationData) si.readObject();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return s;
    }
}
