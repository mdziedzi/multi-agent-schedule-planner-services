package Behaviours;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;


public abstract class CommonTask {
    public abstract boolean isMessageRelevant(ACLMessage msg);

    public abstract ACLMessage ProcessMessage(ACLMessage message);

    public ACLMessage createNotUnderstoodMessage(ACLMessage message) {
        ACLMessage reply = message.createReply();
        reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
        reply.setContent("Not understood message");
        return reply;
    }

}
