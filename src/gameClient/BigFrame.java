package gameClient;

import api.game_service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BigFrame extends JFrame implements ActionListener, Runnable {
   private MyFrame2 panel;
   private Arena _ar;
   private int _gameID;
   private game_service _game;
   private Timer timer;

   BigFrame(Arena arena, int gameID, game_service game_service) {
      _ar = arena;
      _gameID = gameID;
      _game = game_service;
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//      this.setLocationRelativeTo(null);
      this.setSize(1500, 800);
      this.setBackground(Color.WHITE);
      this.setTitle("POKEMON - DAVID AND DVIR");
      this.setLocation(20, 20);


      panel = new MyFrame2(arena, _gameID, _game);
      panel.setBounds(0, 150, this.getWidth(), getHeight() - 150);
      this.add(panel);
      this.setVisible(true);
   }

   public void update(Arena ar) {
      this._ar = ar;
      panel.update(ar);
   }

   @Override
   public void run() {
      while (_game.isRunning()) {
         repaint();
         panel.update(_ar);
         panel.repaint();
      }
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      update(_ar);
      panel.repaint();
      //detailsPanel.repaint();
   }
}
