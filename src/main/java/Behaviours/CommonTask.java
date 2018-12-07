package Behaviours;

import jade.lang.acl.ACLMessage;

public abstract class CommonTask {
    public void SetBasicBehaviour(BasicBehaviour bb){
        basicBehaviour = bb;
    }

    protected BasicBehaviour basicBehaviour;

    public boolean SendMessageToOtherTask(ACLMessage msg){
        return basicBehaviour.SendMessageToTask(msg);
    }

    public abstract boolean isMessageRelevant(ACLMessage msg);

    public abstract ACLMessage ProcessMessage(ACLMessage message);

    public ACLMessage createNotUnderstoodMessage(ACLMessage message) {
        ACLMessage reply = message.createReply();
        reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
        reply.setContent("Not understood message");
        return reply;
    }

}
