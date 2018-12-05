package Behaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;

public class BasicBehaviour extends CyclicBehaviour {

    private ArrayList<CommonBehaviour> myBehaviours;

    public BasicBehaviour(ArrayList<CommonBehaviour> behaviours) {
        myBehaviours = new ArrayList<>(behaviours);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();
        boolean isSPFound = false;
        if (msg != null) {
            for (CommonBehaviour sp : myBehaviours) {
                if (sp.isMessageRelevant(msg)) {
                    isSPFound = true;
                    sp.SetACLMessage(msg);
                    sp.action();
                }
            }
            if (!isSPFound) {
                myAgent.send(createNotUnderstoodMessage(msg));
            }
        }
    }

    public ACLMessage createNotUnderstoodMessage(ACLMessage message) {
        ACLMessage reply = message.createReply();
        reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
        reply.setContent("Not understood message");
        return reply;
    }
}
