package main.java.Behaviours;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public abstract class ServiceProviderCommonBehaviour extends Behaviour {
//    private final static Logger LOGGER = LoggerFactory.getLogger(ServiceProviderCommonBehaviour.class);




    public ACLMessage createNotUnderstoodMessage(ACLMessage message){
//        LOGGER.info("Creating not understood message: "+message.getContent());
        ACLMessage reply = message.createReply();
        reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
        reply.setContent("Not understood message");
        return reply;
    }

}
