package Behaviours;

import Constants.Constants;
import Data.ServiceProvider.ReservationData;
import Data.Common.ReservationResponse;
import Data.ServiceProvider.ServiceProviderData;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.util.*;

public class ServiceProviderScheduler extends CommonTask {

    private static final int slotDuration = 15;

    private Date openingHour;
    private Date closingHour;
    private int maximumNumberOfPlaces;
    private Map<Integer, ArrayList<ReservationData>> newReservations;
    // private ArrayList<ArrayList<ReservationData>> reservations;

    public ServiceProviderScheduler() {
        newReservations = new TreeMap<>();
        openingHour = null;
        closingHour = null;
        maximumNumberOfPlaces = 0;
    }

    private static int convertDateToMinutes(Date data) {
        return data.getHours() * 60 + data.getMinutes();
    }

    @Override
    public ACLMessage ProcessMessage(ACLMessage msg) {
        if (msg != null) {
            String conversationId = msg.getConversationId();
            switch (conversationId) {
                case Constants.ServiceProviderSchedulerMessages.NOTIFY_CHANGES:
                    return onNotifyChanges(msg);
                case Constants.ServiceProviderSchedulerMessages.RECEIVE_RESERVATION_TO_PROCESS:
                    return onReceiveReservationToProcess(msg);
                case Constants.ServiceProviderSchedulerMessages.RECEIVE_SERVICE_DATA:
                    return onReceiveServiceData(msg);
                case Constants.ServiceProviderSchedulerMessages.SEND_RESERVATION_STATUS:
                    return onSendReservationStatus(msg);
                default:
                    return createNotUnderstoodMessage(msg);
            }
        }
        return new ACLMessage();
    }

    private ACLMessage onNotifyChanges(ACLMessage msg) {
        //TODO
        return null;
    }

    private ACLMessage onReceiveReservationToProcess(ACLMessage msg) {
        ReservationData reservationData = ReservationData.deserialize(msg.getContent());
        int openingTime = openingHour.getHours() * 60 + openingHour.getMinutes();
        int closingTime = closingHour.getHours() * 60 + closingHour.getMinutes();
        int requestedOpeningTime = reservationData.beginHour.getHours() * 60 + reservationData.beginHour.getMinutes();
        int requestedClosingTime = reservationData.endHour.getHours() * 60 + reservationData.endHour.getMinutes();

        ACLMessage internalMsg = new ACLMessage();
        internalMsg.setConversationId(Constants.ServiceProviderSecretaryMessages.RECEIVE_RESERVATION_STATUS);

        if (requestedOpeningTime < openingTime) {
            String explanation = "This service opens at " + openingHour.getHours() + ":" + openingHour.getMinutes();
            internalMsg.setContent(ReservationResponse.serialize(new ReservationResponse(-1, explanation)));
            SendMessageToOtherTask(internalMsg);
        } else if (requestedClosingTime > closingTime) {
            String explanation = "This service closes at" + closingHour.getHours() + ":" + closingHour.getMinutes();
            internalMsg.setContent(ReservationResponse.serialize(new ReservationResponse(-1, explanation)));
            SendMessageToOtherTask(internalMsg);
        } else {
            int firstSlot = (requestedOpeningTime - openingTime) / slotDuration;
            int lastSlot = (closingTime - requestedClosingTime) / slotDuration;
            for (int x = firstSlot - 1; x < lastSlot; x++) {
//                if (reservations.get(x).size() == maximumNumberOfPlaces) {
//                    int hours = Math.round((openingTime + x * slotDuration) / 60);
//                    int minutes = ((openingTime + x * slotDuration) / 60) % 60;
//                    String explanation = "The slot at " + hours + ":" + minutes + " is full";
//                    internalMsg.setContent(ReservationResponse.serialize(new ReservationResponse(-1, explanation)));
//                    SendMessageToOtherTask(internalMsg);
//                    return new ACLMessage();
//                }
            }
        }

        return new ACLMessage();
    }

    private ACLMessage onReceiveServiceData(ACLMessage msg) {
        ServiceProviderData serviceProviderData;
        serviceProviderData = ServiceProviderData.deserialize(msg.getContent());

        int newOpeningMinutes = convertDateToMinutes(serviceProviderData.openingHour);
        int newClosingMinutes = convertDateToMinutes(serviceProviderData.closingHour);

        Map<Integer, ArrayList<ReservationData>> newReservations =
                generateNewReservations(newOpeningMinutes, newClosingMinutes, serviceProviderData.maximumNumberOfPlaces);

        if (openingHour == null || closingHour == null) {
            this.newReservations = newReservations;
            openingHour = serviceProviderData.openingHour;
            closingHour = serviceProviderData.closingHour;
            DEBUGforceFullReservations();
            printNewReservations();
            return new ACLMessage();
        }

        for (Map.Entry<Integer, ArrayList<ReservationData>> entry : this.newReservations.entrySet()) {
            if(!newReservations.containsKey(entry.getKey())){
                for(ReservationData r : entry.getValue()){
                    if(r != null){
                        System.out.println("1removing " + entry.getKey() + " " + r.id);
                        ACLMessage internalMsg = new ACLMessage();
                        internalMsg.setConversationId(Constants.ServiceProviderSecretaryMessages.CANCEL_RESERVATION);
                        internalMsg.setContent(ReservationData.serialize(r));
                        SendMessageToOtherTask(internalMsg);
                    }
                }
            } else{
                if(this.maximumNumberOfPlaces <= serviceProviderData.maximumNumberOfPlaces){
                    for(ReservationData r: entry.getValue()) {
                        System.out.println("1Adding");
                        for (int x = 0; x < maximumNumberOfPlaces; x++) {
                            newReservations.get(entry.getKey()).set(x, r);
                        }
                    }
                } else{
                    for(ReservationData r: entry.getValue()) {
                        for (int x = 0; x < serviceProviderData.maximumNumberOfPlaces; x++) {
                            newReservations.get(entry.getKey()).set(x, r);
                        }
                        for(int x = serviceProviderData.maximumNumberOfPlaces; x < maximumNumberOfPlaces; x++){
                            if(r != null) {
                                System.out.println("2removing " + entry.getKey() + " " + r.id);
                                ACLMessage internalMsg = new ACLMessage();
                                internalMsg.setConversationId(Constants.ServiceProviderSecretaryMessages.CANCEL_RESERVATION);
                                internalMsg.setContent(ReservationData.serialize(r));
                                SendMessageToOtherTask(internalMsg);
                            }
                        }
                    }
                }
            }
        }

        this.newReservations = newReservations;
        // DEBUGforceFullReservations();
        printNewReservations();
        return new ACLMessage();
    }

    private ACLMessage onSendReservationStatus(ACLMessage msg) {
        //TODO
        return null;
    }

    private static Map<Integer, ArrayList<ReservationData>> generateNewReservations(int openingMinutes, int closingMinutes, int maximumNumberOfPlaces) {
        int slotsNumber;
        if (openingMinutes > closingMinutes) {
            slotsNumber = (24 * 60 - (openingMinutes - closingMinutes)) / slotDuration;
        } else {
            slotsNumber = (closingMinutes - openingMinutes) / slotDuration;
        }
        Map<Integer, ArrayList<ReservationData>> newReservations = new TreeMap<>();
        for (int y = 0; y < slotsNumber; y++) {
            newReservations.put(openingMinutes + (y * slotDuration), new ArrayList<>(Collections.nCopies(maximumNumberOfPlaces, null)));
        }
        return newReservations;
    }

    private void printNewReservations() {
        for (Map.Entry<Integer, ArrayList<ReservationData>> entry : newReservations.entrySet()) {
            System.out.printf("%2d:%-2d | ", (int) Math.floor(entry.getKey() / 60.0), (entry.getKey() % 60));
            for (int x = 0; x < entry.getValue().size(); x++) {
                if (entry.getValue().get(x) == null) {
                    System.out.print("X ");
                } else {
                    System.out.print(entry.getValue().get(x).id + " ");
                }
            }
            System.out.println();
        }
    }

    private void DEBUGforceFullReservations(){
        for (Map.Entry<Integer, ArrayList<ReservationData>> entry : newReservations.entrySet()){
            for(int x = 0; x < entry.getValue().size(); x++){
                ReservationData r = new ReservationData();
                r.id = x;
                r.beginHour = new Date(0,0,0,10,0);
                r.endHour = new Date(0,0,0,10,0);
                r.agentId = new AID();
                entry.getValue().set(x, r);
            }
        }
    }

    @Override
    public boolean isMessageRelevant(ACLMessage msg) {
        if (msg != null) {
            switch (msg.getConversationId()) {
                case Constants.ServiceProviderSchedulerMessages.NOTIFY_CHANGES:
                case Constants.ServiceProviderSchedulerMessages.RECEIVE_RESERVATION_TO_PROCESS:
                case Constants.ServiceProviderSchedulerMessages.RECEIVE_SERVICE_DATA:
                case Constants.ServiceProviderSchedulerMessages.SEND_RESERVATION_STATUS:
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }
}