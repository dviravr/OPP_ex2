package gameClient;

import api.game_service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameFrame extends JFrame implements ActionListener, Runnable {
   private final GamePanel _panel;
   private Arena _ar;
   private final game_service _game;

   GameFrame(Arena arena, int gameID, game_service game_service) {
      _ar = arena;
      _game = game_service;
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      this.setSize(1500, 800);
      this.setBackground(Color.WHITE);
      this.setTitle("POKEMON - DAVID AND DVIR");
      this.setLocation(20, 20);

      _panel = new GamePanel(arena, gameID, _game);
      _panel.setBounds(0, 150, this.getWidth(), getHeight() - 150);
      this.add(_panel);
      this.setVisible(true);
   }

   public void update(Arena ar) {
      this._ar = ar;
      _panel.update(ar);
   }

   @Override
   public void run() {
      while (_game.isRunning()) {
         repaint();
         _panel.update(_ar);
         _panel.repaint();
      }
   }

   @Override
   public void actionPerformed(ActionEvent e) {
      update(_ar);
      _panel.repaint();
   }
}
