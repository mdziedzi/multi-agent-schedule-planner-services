package main.java.Behaviours;

import jade.lang.acl.ACLMessage;

public class ServiceProviderInterface extends ServiceProviderCommonBehaviour {

    @Override
    public void action() {
        ACLMessage msg = myAgent.blockingReceive();
        if(msg != null){
            System.out.println("Message: "+ msg.toString());
            myAgent.send(createNotUnderstoodMessage(msg));
        }
    }

    public boolean done(){
        return false;
    }
}