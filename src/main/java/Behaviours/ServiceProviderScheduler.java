package Behaviours;

import Constants.Constants;
import Data.ServiceProvider.ReservationData;
import Data.Common.ReservationResponse;
import Data.ServiceProvider.ServiceProviderData;
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
            printNewReservations();
            return new ACLMessage();
        }

        for (Map.Entry<Integer, ArrayList<ReservationData>> entry : this.newReservations.entrySet()) {
            if(!newReservations.containsKey(entry.getKey())){
                for(ReservationData r : entry.getValue()){
                    if(r != null){
                        ACLMessage internalMsg = new ACLMessage();
                        internalMsg.setConversationId(Constants.ServiceProviderSecretaryMessages.CANCEL_RESERVATION);
                        internalMsg.setContent(ReservationData.serialize(r));
                        SendMessageToOtherTask(internalMsg);
                    }
                }
            } else{
                if(this.maximumNumberOfPlaces <= serviceProviderData.maximumNumberOfPlaces){
                    for(ReservationData r: entry.getValue()) {
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
                            System.out.println("removing " + x);
                            if(r != null) {
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

        printNewReservations();
        return new ACLMessage();


//        if (openingMinutes > newOpeningMinutes) {
//            int slotsToDelete = (newOpeningMinutes - openingMinutes) / slotDuration;
//            for (int y = 0; y < slotsToDelete; y++) {
//                for (int x = maximumNumberOfPlaces; x >= 0; x--) {
//                    if (reservations.get(y).get(x) != null) {
//                        ACLMessage internalMsg = new ACLMessage();
//                        internalMsg.setConversationId(Constants.ServiceProviderSecretaryMessages.CANCEL_RESERVATION);
//                        internalMsg.setContent(ReservationData.serialize(reservations.get(y).get(x)));
//                        SendMessageToOtherTask(internalMsg);
//                    }
//                }
//                reservations.remove(y);
//            }
//        }
//        if (openingMinutes < newOpeningMinutes) {
//            int slotsToInsert = (openingMinutes - newOpeningMinutes) / slotDuration;
//            for (int y = 0; y < slotsToInsert; y++) {
//                reservations.add(new ArrayList<>(maximumNumberOfPlaces));
//            }
//        }
//        if (closingMinutes > newClosingMinutes) {
//            int slotsToDelete = (closingMinutes - newClosingMinutes) / slotDuration;
//            for (int y = 0; y < slotsToDelete; y++) {
//                for (int x = maximumNumberOfPlaces; x >= 0; x--) {
//                    if (reservations.get(y).get(x) != null) {
//                        ACLMessage internalMsg = new ACLMessage();
//                        internalMsg.setConversationId(Constants.ServiceProviderSecretaryMessages.CANCEL_RESERVATION);
//                        internalMsg.setContent(ReservationData.serialize(reservations.get(y).get(x)));
//                        SendMessageToOtherTask(internalMsg);
//                    }
//                }
//                reservations.remove(y);
//            }
//        }
//        if (closingMinutes < newClosingMinutes) {
//            int slotsToInsert = (newClosingMinutes - closingMinutes) / slotDuration;
//            for (int y = 0; y < slotsToInsert; y++) {
//                reservations.add(0, new ArrayList<>(maximumNumberOfPlaces));
//            }
//        }
//
//        if (maximumNumberOfPlaces > serviceProviderData.maximumNumberOfPlaces) {
//            for (int y = 0; y < reservations.size(); y++) {
//                for (int x = maximumNumberOfPlaces; x >= serviceProviderData.maximumNumberOfPlaces; x--) {
//                    if (reservations.get(y).get(x) != null) {
//                        ACLMessage internalMsg = new ACLMessage();
//                        internalMsg.setConversationId(Constants.ServiceProviderSecretaryMessages.CANCEL_RESERVATION);
//                        internalMsg.setContent(ReservationData.serialize(reservations.get(y).get(x)));
//                        SendMessageToOtherTask(internalMsg);
//                        reservations.get(y).remove(x);
//                    }
//                }
//
//            }
//        }
//        if (maximumNumberOfPlaces < serviceProviderData.maximumNumberOfPlaces) {
//            for (int y = 0; y < reservations.size(); y++) {
//                for (int x = maximumNumberOfPlaces; x < serviceProviderData.maximumNumberOfPlaces; x++) {
//                    reservations.get(y).add(null);
//                }
//            }
//        }
//        maximumNumberOfPlaces = serviceProviderData.maximumNumberOfPlaces;
//        openingHour = serviceProviderData.openingHour;
//        closingHour = serviceProviderData.closingHour;
//
//        printReservations();
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
                    System.out.print(x + " ");
                }
            }
            System.out.println();
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