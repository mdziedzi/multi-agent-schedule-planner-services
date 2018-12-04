package main.java.Agents;

import javax.swing.*;
import java.awt.*;

public class ServiceAgentGUI extends JFrame {

    private final ServiceAgent serviceAgent;
    private final JPanel jPanel;
    private final JLabel helloLabel;

    public ServiceAgentGUI(ServiceAgent serviceAgent) {
        this.serviceAgent = serviceAgent;

        setSize(200, 200);

        jPanel = new JPanel();

        helloLabel = new JLabel("Hello");


        jPanel.add(helloLabel);


        jPanel.setBackground(new Color(58, 121, 123));

        this.add(jPanel);

        setTitle(serviceAgent.getLocalName());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setVisible(true);
    }
}
