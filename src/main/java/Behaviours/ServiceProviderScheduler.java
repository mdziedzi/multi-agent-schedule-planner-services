package Behaviours;

import Constants.Constants;
import Data.ServiceProvider.ReservationData;
import Data.Common.ReservationResponse;
import Data.ServiceProvider.ServiceProviderData;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.Date;

public class ServiceProviderScheduler extends CommonTask {

    private static final int slotDuration = 15;

    private Date openingHour;
    private Date closingHour;
    private int maximumNumberOfPlaces;
    private ArrayList<ArrayList<ReservationData>> reservations;

    public ServiceProviderScheduler() {
        reservations = new ArrayList<>(1);
        reservations.add(new ArrayList<>(1));
        openingHour = new Date(0,0,0,0,0);
        closingHour = new Date(0,0,0,0,0);
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
                if (reservations.get(x).size() == maximumNumberOfPlaces) {
                    int hours = Math.round((openingTime + x * slotDuration) / 60);
                    int minutes = ((openingTime + x * slotDuration) / 60) % 60;
                    String explanation = "The slot at " + hours + ":" + minutes + " is full";
                    internalMsg.setContent(ReservationResponse.serialize(new ReservationResponse(-1, explanation)));
                    SendMessageToOtherTask(internalMsg);
                    return new ACLMessage();
                }
            }
        }

        return new ACLMessage();
    }

    private ACLMessage onReceiveServiceData(ACLMessage msg) {
        ServiceProviderData serviceProviderData;
        serviceProviderData = ServiceProviderData.deserialize(msg.getContent());

        int openingMinutes = openingHour.getHours() * 60 + openingHour.getMinutes();
        int closingMinutes = closingHour.getHours() * 60 + openingHour.getMinutes();
        int newOpeningMinutes = serviceProviderData.openingHour.getHours() * 60 + serviceProviderData.openingHour.getMinutes();
        int newClosingMinutes = serviceProviderData.closingHour.getHours() * 60 + serviceProviderData.closingHour.getMinutes();
        if (openingMinutes > newOpeningMinutes) {
            int slotsToDelete = (newOpeningMinutes - openingMinutes) / slotDuration;
            for (int y = 0; y < slotsToDelete; y++) {
                for (int x = maximumNumberOfPlaces; x >= 0; x--) {
                    if (reservations.get(y).get(x) != null) {
                        ACLMessage internalMsg = new ACLMessage();
                        internalMsg.setConversationId(Constants.ServiceProviderSecretaryMessages.CANCEL_RESERVATION);
                        internalMsg.setContent(ReservationData.serialize(reservations.get(y).get(x)));
                        SendMessageToOtherTask(internalMsg);
                    }
                }
                reservations.remove(y);
            }
        }
        if (openingMinutes < newOpeningMinutes) {
            int slotsToInsert = (openingMinutes - newOpeningMinutes) / slotDuration;
            for (int y = 0; y < slotsToInsert; y++) {
                reservations.add(new ArrayList<>(maximumNumberOfPlaces));
            }
        }
        if (closingMinutes > newClosingMinutes) {
            int slotsToDelete = (closingMinutes - newClosingMinutes) / slotDuration;
            for (int y = 0; y < slotsToDelete; y++) {
                for (int x = maximumNumberOfPlaces; x >= 0; x--) {
                    if (reservations.get(y).get(x) != null) {
                        ACLMessage internalMsg = new ACLMessage();
                        internalMsg.setConversationId(Constants.ServiceProviderSecretaryMessages.CANCEL_RESERVATION);
                        internalMsg.setContent(ReservationData.serialize(reservations.get(y).get(x)));
                        SendMessageToOtherTask(internalMsg);
                    }
                }
                reservations.remove(y);
            }
        }
        if (closingMinutes < newClosingMinutes) {
            int slotsToInsert = (newClosingMinutes - closingMinutes) / slotDuration;
            for (int y = 0; y < slotsToInsert; y++) {
                reservations.add(0, new ArrayList<>(maximumNumberOfPlaces));
            }
        }

        if (maximumNumberOfPlaces > serviceProviderData.maximumNumberOfPlaces) {
            for (int y = 0; y < reservations.size(); y++) {
                for (int x = maximumNumberOfPlaces; x >= serviceProviderData.maximumNumberOfPlaces; x--) {
                    if (reservations.get(y).get(x) != null) {
                        ACLMessage internalMsg = new ACLMessage();
                        internalMsg.setConversationId(Constants.ServiceProviderSecretaryMessages.CANCEL_RESERVATION);
                        internalMsg.setContent(ReservationData.serialize(reservations.get(y).get(x)));
                        SendMessageToOtherTask(internalMsg);
                        reservations.get(y).remove(x);
                    }
                }

            }
        }
        if (maximumNumberOfPlaces < serviceProviderData.maximumNumberOfPlaces) {
            for (int y = 0; y < reservations.size(); y++) {
                for (int x = maximumNumberOfPlaces; x < serviceProviderData.maximumNumberOfPlaces; x++) {
                    reservations.get(y).add(null);
                }
            }
        }
        maximumNumberOfPlaces = serviceProviderData.maximumNumberOfPlaces;
        openingHour = serviceProviderData.openingHour;
        closingHour = serviceProviderData.closingHour;

        return new ACLMessage();
    }

    private ACLMessage onSendReservationStatus(ACLMessage msg) {
        //TODO
        return null;
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