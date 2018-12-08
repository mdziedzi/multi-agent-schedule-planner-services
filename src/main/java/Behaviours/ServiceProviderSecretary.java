package Behaviours;

import Constants.Constants;
import Data.ReservationData;
import Data.ReservationRequest;
import Data.ServiceProviderData;
import jade.lang.acl.ACLMessage;

public class ServiceProviderSecretary extends CommonTask {

    private ServiceProviderData serviceProviderData;

    public ServiceProviderSecretary() {
        serviceProviderData = new ServiceProviderData();
    }

    @Override
    public ACLMessage ProcessMessage(ACLMessage msg) {
        if (msg != null) {
            String conversationId = msg.getConversationId();
            switch (conversationId) {
                case Constants.ServiceProviderSecretaryMessages.RECEIVE_RESERVATION_STATUS:
                    //TODO
                    break;
                case Constants.ServiceProviderSecretaryMessages.RECEIVE_RESERVATION_REQUEST:
                    return onReceiveReservationRequest(msg);
                case Constants.ServiceProviderSecretaryMessages.SEND_RESERVATION_RESPONSE:
                    //TODO
                    break;
                case Constants.ServiceProviderSecretaryMessages.SEND_RESERVATION_TO_PROCESS:
                    //TODO
                    break;
                case Constants.ServiceProviderSecretaryMessages.SEND_SERVICE_DATA:
                    ACLMessage internalMsg = new ACLMessage();
                    internalMsg.setConversationId(Constants.ServiceProviderInterfaceMessages.SEND_SERVICE_DATA);
                   SendMessageToOtherTask(internalMsg);
                    ACLMessage reply = msg.createReply();
                    reply.setConversationId("TODO"); //TODO: set as Client constant
                    reply.setContent(ServiceProviderData.serialize(serviceProviderData));
                    return reply;
                case Constants.ServiceProviderSecretaryMessages.RECEIVE_SERVICE_DATA:
                    serviceProviderData = ServiceProviderData.deserialize(msg.getContent());
                    break;
                default:
                    return createNotUnderstoodMessage(msg);
            }
        }
        return new ACLMessage();
    }

    private ACLMessage onReceiveReservationStatus(ACLMessage msg){
        //TODO
        return null;
    }
    
    private ACLMessage onReceiveReservationRequest(ACLMessage msg){
        ReservationRequest reservationRequest = new ReservationRequest();
        reservationRequest = ReservationRequest.deserialize(msg.getContent());
        ReservationData reservationData = new ReservationData();
        reservationData.agentId = msg.getSender();
        reservationData.beginHour = reservationRequest.beginHour;
        reservationData.endHour = reservationRequest.endHour;

        ACLMessage internalMsg = new ACLMessage();
        internalMsg.setConversationId(Constants.ServiceProviderSchedulerMessages.RECEIVE_RESERVATION_TO_PROCESS);
        internalMsg.setContent(ReservationData.serialize(reservationData));
        SendMessageToOtherTask(internalMsg);

        return null;
    }

    @Override
    public boolean isMessageRelevant(ACLMessage msg) {
        if (msg != null) {
            switch (msg.getConversationId()) {
                case Constants.ServiceProviderSecretaryMessages.RECEIVE_RESERVATION_STATUS:
                case Constants.ServiceProviderSecretaryMessages.RECEIVE_RESERVATION_REQUEST:
                case Constants.ServiceProviderSecretaryMessages.SEND_RESERVATION_RESPONSE:
                case Constants.ServiceProviderSecretaryMessages.SEND_RESERVATION_TO_PROCESS:
                case Constants.ServiceProviderSecretaryMessages.SEND_SERVICE_DATA:
                case Constants.ServiceProviderSecretaryMessages.RECEIVE_SERVICE_DATA:
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }
}