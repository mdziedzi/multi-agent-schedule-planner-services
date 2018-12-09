package Behaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;

public class BasicBehaviour extends CyclicBehaviour {

    private ArrayList<CommonTask> TasksList;

    public BasicBehaviour(ArrayList<CommonTask> tasks) {
        TasksList = new ArrayList<>(tasks);
    }

    @Override
    public void action() {
        ACLMessage msg = myAgent.receive();
        boolean isSPFound = false;
        if (msg != null) {
            for (CommonTask sp : TasksList) {
                if (sp.isMessageRelevant(msg)) {
                    isSPFound = true;
                    myAgent.send(sp.ProcessMessage(msg));
                }
            }
            if (!isSPFound) {
                myAgent.send(createNotUnderstoodMessage(msg));
            }
        } else {
            block();
        }
    }

    public void SendMessageToAgent(ACLMessage msg){
        myAgent.send(msg);
    }

    public String SendMessageToTask(ACLMessage msg){
        for(CommonTask ct : TasksList){
            if(ct.isMessageRelevant(msg)){
                return ct.ProcessMessage(msg).getContent();
            }
        }
        return "";
    }

    public ACLMessage createNotUnderstoodMessage(ACLMessage message) {
        ACLMessage reply = message.createReply();
        reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
        reply.setContent("Not understood message");
        return reply;
    }
}
