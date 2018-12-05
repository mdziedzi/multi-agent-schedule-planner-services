package Behaviours;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.ArrayList;

public class ServiceProviderBasicBehaviour extends CyclicBehaviour {
    private ArrayList<ServiceProviderCommonBehaviour> myBehaviours;

    public ServiceProviderBasicBehaviour()
    {
        myBehaviours  = new ArrayList<>();

        myBehaviours.add(new ServiceProviderInterface());
        myBehaviours.add(new ServiceProviderSecretary());
        myBehaviours.add(new ServiceProviderScheduler());
    }
    @Override
    public void action()
    {
        ACLMessage msg = myAgent.receive();
        boolean isSPFound = false;
        if(msg != null)
        {
            for (ServiceProviderCommonBehaviour sp : myBehaviours)
            {
                if(sp.isMessageRelevant(msg)){
                    isSPFound = true;
                    sp.SetACLMessage(msg);
                    sp.action();
                }
            }
            if(!isSPFound){
                myAgent.send(createNotUnderstoodMessage(msg));
            }
        }
    }
    public ACLMessage createNotUnderstoodMessage(ACLMessage message){
        ACLMessage reply = message.createReply();
        reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
        reply.setContent("Not understood message");
        return reply;
    }
}
