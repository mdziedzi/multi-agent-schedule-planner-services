package Agents;

import Data.ServiceProviderData;
import jade.gui.GuiEvent;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServiceAgentGUI extends JFrame {

    private final ServiceAgent serviceAgent;
    private JPanel panel;
    private JTextField[] fields;
    JButton btn;
    private String[] labels = {
            "Opening hour",
            "Closing hour",
            "Max nr of places",
            "Name",
            "Type",
            "Address"};

    public ServiceAgentGUI(ServiceAgent serviceAgent) {
        this.serviceAgent = serviceAgent;

        createGUI();

        setSize(400, 200);


    }

    private void createGUI() {
        panel = new JPanel();

        panel.setBackground(new Color(58, 121, 123));
        panel.setLayout(new BorderLayout());

        JPanel labelPanel = new JPanel(new GridLayout(labels.length, 1));
        JPanel fieldPanel = new JPanel(new GridLayout(labels.length, 1));
        panel.add(labelPanel, BorderLayout.WEST);
        panel.add(fieldPanel, BorderLayout.CENTER);
        fields = new JTextField[labels.length];

        for (int i = 0; i < labels.length; i += 1) {
            fields[i] = new JTextField();
            fields[i].setColumns(20);

            fields[i].setToolTipText("dd-mm-rrrr");

            JLabel lab = new JLabel(labels[i], JLabel.RIGHT);
            lab.setLabelFor(fields[i]);


            labelPanel.add(lab);
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
            p.add(fields[i]);
            fieldPanel.add(p);
        }


        btn = new JButton("Send");
        btn.addActionListener(e -> {
            try {
                GuiEvent ge = new GuiEvent(ServiceAgentGUI.this, 1);
                ge.addParameter(new ServiceProviderData(
                        parseDate(fields[0].getText()),
                        parseDate(fields[1].getText()),
                        Integer.valueOf(fields[2].getText()),
                        fields[3].getText(),
                        fields[4].getText(),
                        fields[5].getText()));
                serviceAgent.postGuiEvent(ge);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        });

        panel.add(btn, BorderLayout.SOUTH);

        this.add(panel);

        setTitle(serviceAgent.getLocalName());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setVisible(true);
    }

    private Date parseDate(String date) throws ParseException {
        return new SimpleDateFormat("dd-MM-yyyy").parse(date);
    }


}
