package gameClient;

import javax.swing.*;
import java.awt.*;

public class DetailsPanel extends JPanel {

   Arena _ar;
   int _gameID;
   JLabel levelID;
   JLabel agentLabel;

   DetailsPanel(Arena arena, int gameID) {
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
      agentLabel.setHorizontalAlignment(JLabel.CENTER);
      agentLabel.setVerticalAlignment(JLabel.CENTER);
      this.add(agentLabel);
   }

   private String updateAgent() {
      StringBuilder ag = new StringBuilder();
      for (Agent agent : _ar.getAgents()) {
         ag.append(String.format("agent number %d score is: %f /n", agent.getID(), agent.getValue()));
      }
      return ag.toString();
   }

   public void paint(Graphics g) {
      levelID.show();
      agentLabel.setText(updateAgent());
      agentLabel.show();
   }
}