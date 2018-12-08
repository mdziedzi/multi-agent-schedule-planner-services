package Behaviours;

import Constants.Constants;
import Data.ServiceProviderData;
import Exceptions.negativeValueException;
import jade.lang.acl.ACLMessage;

import java.util.Objects;


public class ServiceProviderInterface extends CommonTask {


    private ServiceProviderData serviceProviderData;


    public void setServiceProviderData(ServiceProviderData data) throws negativeValueException {
        if (data.openingHour != null && serviceProviderData.openingHour != data.openingHour) {
            serviceProviderData.openingHour = data.openingHour;
        }
        if (data.closingHour != null && serviceProviderData.closingHour != data.closingHour) {
            serviceProviderData.closingHour = data.closingHour;
        }
        if (data.maximumNumberOfPlaces != 0 && serviceProviderData.maximumNumberOfPlaces != data.maximumNumberOfPlaces) {
            if (data.maximumNumberOfPlaces < 0) {
                throw new negativeValueException("maximumNumberOfPlaces");
            }
            serviceProviderData.maximumNumberOfPlaces = data.maximumNumberOfPlaces;
        }
        if (data.name != null && !Objects.equals(serviceProviderData.name, data.name)) {
            serviceProviderData.name = data.name;
        }
        if (data.type != null && !Objects.equals(serviceProviderData.type, data.type)) {
            serviceProviderData.type = data.type;
        }
        if (data.address != null && !Objects.equals(serviceProviderData.address, data.address)) {
            serviceProviderData.address = data.address;
        }
    }

    @Override
    public ACLMessage ProcessMessage(ACLMessage msg) {
        if (msg != null) {
            String conversationId = msg.getConversationId();
            switch (conversationId) {
                case Constants.ServiceProviderInterfaceMessages.VERIFY_RESERVATION:
                    return onVerifyReservation(msg);
                case Constants.ServiceProviderInterfaceMessages.SEND_SERVICE_DATA:
                    return onSendServiceData(msg);
                case Constants.ServiceProviderInterfaceMessages.SET_SERVICE_DATA:
                    return onSetServiceData(msg);
                default:
                    return createNotUnderstoodMessage(msg);
            }
        }
        return new ACLMessage();
    }
    private ACLMessage onSendServiceData(ACLMessage msg){
        ACLMessage reply = msg.createReply();
        reply.setConversationId(Constants.ServiceProviderSecretaryMessages.RECEIVE_SERVICE_DATA);
        reply.setContent(ServiceProviderData.serialize(serviceProviderData));
        SendMessageToOtherTask(reply);

        return new ACLMessage();
    }
    private ACLMessage onVerifyReservation(ACLMessage msg){
        //TODO
        return null;
    }
    private ACLMessage onSetServiceData(ACLMessage msg){
        try{
            setServiceProviderData(ServiceProviderData.deserialize(msg.getContent()));
            System.out.println(this.toString());
        }
        catch(negativeValueException e)
        {
            e.printStackTrace();
        }

        return new ACLMessage();
    }

    @Override
    public boolean isMessageRelevant(ACLMessage msg) {
        if (msg != null) {
            switch (msg.getConversationId()) {
                case Constants.ServiceProviderInterfaceMessages.SEND_SERVICE_DATA:
                case Constants.ServiceProviderInterfaceMessages.VERIFY_RESERVATION:
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }
}