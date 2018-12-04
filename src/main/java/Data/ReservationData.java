package Data;

import jade.core.AID;

import java.util.Date;

public class ReservationData {
    public final int id;
    public final AID agentId;
    public final Date beginHour;
    public final Date endHour;

    public ReservationData(){
        id = 0;
        agentId = null;
        beginHour = null;
        endHour = null;
    }
}
