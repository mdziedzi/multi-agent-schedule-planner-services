package Data.Common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

public class ReservationResponse {

    public int id; // id = -1 - the reservation is not possible, read explanation to see why
    public String explanation;

    public ReservationResponse(int id, String explanation) {
        this.id = id;
        this.explanation = explanation;
    }

    public static String serialize(ReservationResponse data) {
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

    public static ReservationResponse deserialize(String serializedReservationRequestData){
        ReservationResponse s = null;
        try {
            final byte[] bytes = Base64.getDecoder().decode(serializedReservationRequestData);
            byte b[] = serializedReservationRequestData.getBytes();
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream si = new ObjectInputStream(bi);
            s =  (ReservationResponse) si.readObject();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return s;
    }
}
