package gameClient;

import api.*;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class MyFrame extends JPanel {
   private Arena _ar;
   private gameClient.util.Range2Range _w2f;
   private final int _scenario;
   private final game_service _game;


   MyFrame(Arena arena, int gameID, game_service game_service) {
      _ar = arena;
      _scenario = gameID;
      _game = game_service;
   }

   public void update(Arena ar) {
      this._ar = ar;
      repaint();
   }

   void updateFrame(Arena _ar) {
      Range rx = new Range(20, this.getWidth() - 20);
      Range ry = new Range(this.getHeight() - 10, 150);
      Range2D frame = new Range2D(rx, ry);
      directed_weighted_graph g = _ar.getGraph();
      _w2f = Arena.w2f(g, frame);
   }

   public void paintComponents(Graphics g) {
      super.paint(g);

      int w = this.getWidth();
      int h = this.getHeight();
      g.clearRect(0, 0, w, h);

      updateFrame(_ar);
      drawGraph(g);
      drawPokemons(g);
      drawAgents(g);
      drawInfo(g);
      paintAgentValue(g);
      paintPic(g);
      paintScenario(g);
      paintTimeToEnd(g);
   }

   public void paint(Graphics g) {
      Image buffer_image;
      Graphics buffer_graphics;
      int w = this.getWidth();
      int h = this.getHeight();
      // Create a new "canvas"
      buffer_image = createImage(w, h);
      buffer_graphics = buffer_image.getGraphics();

      // Draw on the new "canvas"
      paintComponents(buffer_graphics);

      // "Switch" the old "canvas" for the new one
      g.drawImage(buffer_image, 0, 0, this);
   }

   private void drawInfo(Graphics g) {
      List<String> str = _ar.get_info();
      String dt = "none";
      for (int i = 0; i < str.size(); i++) {
         g.drawString(str.get(i) + " dt: " + dt, 100, 60 + i * 20);
      }
   }

   private void drawGraph(Graphics g) {
      directed_weighted_graph gg = _ar.getGraph();
      for (node_data n : gg.getV()) {
         g.setColor(Color.blue);
         drawNode(n, 5, g);
         for (edge_data e : gg.getE(n.getKey())) {
            g.setColor(Color.gray);
            drawEdge(e, g);
         }
      }
   }

   private void drawPokemons(Graphics g) {
      List<Pokemon> pokemons = _ar.getPokemons();
      if (pokemons != null) {
         for (Pokemon pokemon : pokemons) {
            Point3D c = pokemon.getLocation();
            int r = 10;
            Image poc = new ImageIcon("data/poc1.png").getImage();
            if (pokemon.getType() < 0) {
               poc = new ImageIcon("data/poc2.png").getImage();
            }
            if (c != null) {
               geo_location fp = this._w2f.world2frame(c);
               g.drawImage(poc, (int) fp.x() - r, (int) fp.y() - r, 4 * r, 4 * r, null);
               // g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
            }
         }
      }
   }

   private void drawAgents(Graphics g) {
      List<Agent> agents = Arena.getAgents(_game.getAgents(), _ar.getAgents(), _ar.getGraph());
//      List<Agent> agents = _ar.getAgents();
      g.setColor(Color.red);
      if (agents != null) {
         for (Agent agent : agents) {
            geo_location c = agent.getLocation();
            int r = 8;
            if (c != null) {
               geo_location fp = this._w2f.world2frame(c);
               Image image = getAgentImage(agent.getID());
               g.drawImage(image, (int) fp.x() - 2 * r, (int) fp.y() - 2 * r, 4 * r, 4 * r, null);
               // g.fillOval((int) fp.x() - r, (int) fp.y() - r, 4 * r, 3 * r);
               String str = String.format("agent %d", agent.getID());
               g.drawString(str, (int) fp.x(), (int) fp.y() - r * 5);
            }
         }
      }
   }

   private void drawNode(node_data n, int r, Graphics g) {
      geo_location pos = n.getLocation();
      geo_location fp = this._w2f.world2frame(pos);
      g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
      g.drawString("" + n.getKey(), (int) fp.x(), (int) fp.y() - 4 * r);
   }

   private void drawEdge(edge_data edge, Graphics g) {
      directed_weighted_graph gg = _ar.getGraph();
      geo_location src = gg.getNode(edge.getSrc()).getLocation();
      geo_location dest = gg.getNode(edge.getDest()).getLocation();
      geo_location sFrame = _w2f.world2frame(src);
      geo_location dFrame = _w2f.world2frame(dest);
      g.drawLine((int) sFrame.x(), (int) sFrame.y(), (int) dFrame.x(), (int) dFrame.y());
   }

   private void paintAgentValue(Graphics g) {
      g.setColor(Color.gray.darker());
      g.setFont(new Font(null, Font.BOLD, 22));
      g.drawString("Agent Value:", this.getWidth() - 275, 30);
      g.setColor(Color.green.darker());
      g.setFont(new Font(null, Font.BOLD, 22));
      for (Agent agent : _ar.getAgents()) {
         Image image = getAgentImage(agent.getID());
         g.drawImage(image, this.getWidth() - 330, 40 + 30 * agent.getID(), 25, 25, null);
         String str = String.format("agent %d:  score: %.1f", agent.getID(), agent.getValue());
         g.drawString(str, this.getWidth() - 300, 58 + 30 * agent.getID());
      }
   }

   private Image getAgentImage(int id) {
      Image image = new ImageIcon("data/ash1.jpg").getImage();
      switch (id % 3) {
         case 0: {
            image = new ImageIcon("data/ash1.jpg").getImage();
            break;
         }
         case 1: {
            image = new ImageIcon("data/brokpoc.jpg").getImage();
            break;
         }
         case 2: {
            image = new ImageIcon("data/mistypoc.jpg").getImage();
            break;
         }
      }
      return image;
   }


   private void paintScenario(Graphics g) {
      g.setColor(Color.black);
      g.setFont(new Font(null, Font.BOLD, 25));
      g.drawString("scenario " + _scenario, 40, 35);
   }

   private void paintTimeToEnd(Graphics g) {
      g.setColor(Color.GREEN.darker().darker());
      g.setFont(new Font(null, Font.BOLD, 22));
      g.drawString("Time to end: " + _game.timeToEnd() / 1000, 40, 55);
   }

   private void paintPic(Graphics g) {
      Image image = new ImageIcon("data/catch.jpg").getImage();
      g.drawImage(image, (this.getWidth() / 2) - 145, 0, 290, 158, null);
   }
}