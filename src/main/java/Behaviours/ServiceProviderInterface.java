package main.java.Behaviours;

import main.java.Constants.Constants;
import main.java.Data.ServiceProviderData;
import main.java.Interfaces.ServiceProviderInterfaceInterface;
import main.java.Exceptions.*;

import jade.lang.acl.ACLMessage;

import java.util.Date;


public class ServiceProviderInterface extends ServiceProviderCommonBehaviour implements ServiceProviderInterfaceInterface {

    private Date openingHour = null;
    private Date closingHour = null;
    private int maximumNumberOfPlaces = 0;
    private String name = null;
    private String type = null;
    private String address = null;

    @Override
    public void setServiceProviderData(ServiceProviderData data) throws negativeValueException {
        if(data.openingHour != null && openingHour != data.openingHour){
            openingHour = data.openingHour;
        }
        if(data.closingHour != null && closingHour != data.closingHour){
            closingHour = data.closingHour;
        }
        if(data.maximumNumberOfPlaces != 0 && maximumNumberOfPlaces != data.maximumNumberOfPlaces){
            if(data.maximumNumberOfPlaces < 0){
                throw new negativeValueException("maximumNumberOfPlaces");
            }
            maximumNumberOfPlaces = data.maximumNumberOfPlaces;
        }
        

    }

    @Override
    public void action() {

        ACLMessage msg = myAgent.blockingReceive();
        if (msg != null) {
            System.out.println("Message: " + msg.toString());
            String conversationId = msg.getConversationId();
            switch (conversationId) {
                case Constants.ServiceProviderInterfaceMessages.VERIFY_RESERVATION:
                    System.out.println("ads");

            }
        }
    }

    public boolean done() {
        return false;
    }
}