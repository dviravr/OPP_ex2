package gameClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EndGameFrame {

   private final JButton close;
   private final JFrame frame = new JFrame();

   EndGameFrame(GameFrame gameFrame, Arena ar, int scenario, long id) {
      gameFrame.dispose();
      frame.setSize(600, 500);
      frame.setLocationRelativeTo(null);
      frame.setTitle("POKEMON - DAVID AND DVIR");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      Font f = new Font("TimesRoman", Font.BOLD, 24);

      close = new JButton("close");
      close.setFont(f);
      JPanel closePanel = new JPanel();
      closePanel.add(close);

      double grade = 0;
      for (Agent agent : ar.getAgents()) {
         grade += agent.getValue();
      }

      JLabel idLabel = new JLabel("Well done id " + id);
      idLabel.setFont(f);
      JLabel gradeLable = new JLabel("your grade is " + grade + " in scenario " + scenario);
      gradeLable.setFont(f);

      JPanel idPanel = new JPanel();
      JPanel gradePanel = new JPanel();
      idPanel.add(idLabel);
      gradePanel.add(gradeLable);

      ImageIcon image = new ImageIcon("data/catch.jpg");
      JLabel header = new JLabel("", image, JLabel.CENTER);

      Box theBox = Box.createVerticalBox();
      theBox.add(header);
      theBox.add(idPanel);
      theBox.add(gradePanel);
      theBox.add(closePanel);

      frame.add(theBox);

      close.addActionListener(new EndGameFrame.ListenForButton());
      frame.setVisible(true);
   }

   public class ListenForButton implements ActionListener {
      public void actionPerformed(ActionEvent e) {
         if (e.getSource() == close) {
            frame.dispose();
         }
      }
   }
}
