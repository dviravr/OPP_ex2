package gameClient;

import javax.swing.*;
import java.awt.*;

public class DetailsPanel extends JPanel {

    Arena _ar;
    int _gameID;
    JLabel levelID;
    JLabel agentLabel;

    DetailsPanel(Arena arena,int gameID){
        _ar = arena;
        _gameID = gameID;
        levelID = new JLabel();
        levelID.setText("Game ID:" + _gameID);
        levelID.setFont(new Font(null, Font.BOLD, 25));
        levelID.setHorizontalAlignment(JLabel.CENTER);
        levelID.setVerticalAlignment(JLabel.TOP);
        this.add(levelID);
        agentLabel = new JLabel();

        agentLabel.setText(updateAgent());
        agentLabel.setFont(new Font(null, Font.BOLD, 25));
//        agentLabel.setHorizontalAlignment(JLabel.CENTER);
//        agentLabel.setVerticalAlignment(JLabel.CENTER);
        this.add(agentLabel);

    }
private String updateAgent(){
    String ag = "";
    for (CL_Agent agent : _ar.getAgents()) {
        ag += String.format("agent number %d score is: %f /n",agent.getID(),agent.getValue());
    }
    return ag;
}
   public void paint(Graphics g) {
        levelID.show();
        agentLabel.setText(updateAgent());
        agentLabel.show();


   }
//        JLabel levelID = new JLabel();
//        levelID.setText("Game ID:" + _gameID);
//        levelID.setFont(new Font(null, Font.BOLD, 25));
//        levelID.setHorizontalAlignment(JLabel.CENTER);
//        levelID.setVerticalAlignment(JLabel.TOP);
//        this.add(levelID);
//        JLabel agentLabel = new JLabel();
//        String ag = "";
//        for (CL_Agent agent : _ar.getAgents()) {
//            ag += "agent number" + agent.getID() + " score is: " + agent.getValue() + "/n";
//        }
//        agentLabel.setText(ag);
//        agentLabel.setFont(new Font(null, Font.BOLD, 25));
////        agentLabel.setHorizontalAlignment(JLabel.CENTER);
////        agentLabel.setVerticalAlignment(JLabel.CENTER);
//        this.add(agentLabel);
//
//    }
}