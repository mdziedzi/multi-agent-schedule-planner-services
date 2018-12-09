package Data.Common;

import Exceptions.negativeValueException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.Date;

public class ReservationRequest {
    public Date beginHour;
    public Date endHour;
    public int slots;


    public ReservationRequest(){
        beginHour = null;
        endHour = null;
        slots = 0;
    }

    public ReservationRequest(Date bHour, Date eHour, int slotsNumber) throws negativeValueException{
        if(slots <= 0) throw new negativeValueException("Slots should be greater than zero: "+ slots);
        slots = slotsNumber;
        beginHour = bHour;
        endHour =eHour;
    }

    public static String serialize(ReservationRequest data) {
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

    public static ReservationRequest deserialize(String serializedReservationRequest){
        ReservationRequest s = null;
        try {
            final byte[] bytes = Base64.getDecoder().decode(serializedReservationRequest);
            byte b[] = serializedReservationRequest.getBytes();
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream si = new ObjectInputStream(bi);
            s =  (ReservationRequest) si.readObject();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        return s;
    }

    @Override
    public String toString() {
        return "ReservationRequest{" +
                "beginHour=" + beginHour +
                ", endHour=" + endHour +
                ", slots=" + slots +
                '}';
    }
}
