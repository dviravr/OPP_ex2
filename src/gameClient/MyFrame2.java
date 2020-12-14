package gameClient;

import Server.Game_Server_Ex2;
import api.*;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

public class MyFrame2 extends JPanel implements ActionListener {
    private Arena _ar;
    private gameClient.util.Range2Range _w2f;
    private  int _senario;
   private game_service _game;
    private Timer timer;



    MyFrame2(Arena arena,int gameID,game_service game_service ) {

        _ar = arena;
        _senario = gameID;
        _game = game_service;
    }
//
//    MyFrame2(Arena ar) {
//        this.setName("Cache them");
//        _ar =ar ;
//        timer = new Timer(1000,this);
//
//    }



//    MyFrame2(String a) {
//        super(a);
//        //int _ind = 0;
//    }

    public void update (Arena ar) {
        this._ar = ar;
       // updateFrame(ar);
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
        drawAgants(g);
        drawInfo(g);
        paintAgentValue(g);
        paintSenario(g);
        paintTimeToEnd(g);
    }

    	public void paint(Graphics g) {
		Image buffer_image;
		Graphics buffer_graphics;
		int w = this.getWidth();
		int h = this.getHeight();
		// Create a new "canvas"
		buffer_image = createImage(w, h);
		buffer_graphics= buffer_image.getGraphics();

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
        Iterator<node_data> iter = gg.getV().iterator();
        while (iter.hasNext()) {
            node_data n = iter.next();
            g.setColor(Color.blue);
            drawNode(n, 5, g);
            Iterator<edge_data> itr = gg.getE(n.getKey()).iterator();
            while (itr.hasNext()) {
                edge_data e = itr.next();
                g.setColor(Color.gray);
                drawEdge(e, g);
            }
        }
    }

    private void drawPokemons(Graphics g) {
        List<CL_Pokemon> fs = _ar.getPokemons();
        if (fs != null) {
            Iterator<CL_Pokemon> itr = fs.iterator();

            while (itr.hasNext()) {

                CL_Pokemon f = itr.next();
                Point3D c = f.getLocation();
                int r = 10;
                g.setColor(Color.green);
                if (f.getType() < 0) {
                    g.setColor(Color.orange);
                }
                if (c != null) {

                    geo_location fp = this._w2f.world2frame(c);
                    g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
                    	//g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);

                }
            }
        }
    }

    private void drawAgants(Graphics g) {
        List<CL_Agent> rs = _ar.getAgents();
        //	Iterator<OOP_Point3D> itr = rs.iterator();
        g.setColor(Color.red);
        int i = 0;
        while (rs != null && i < rs.size()) {
            CL_Agent agent = rs.get(i);
            geo_location c = rs.get(i).getLocation();
            int r = 8;
            i++;
            if (c != null) {

                geo_location fp = this._w2f.world2frame(c);
                g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
                String str = String.format("agent %d \n score: %.1f",agent.getID(),agent.getValue());
                g.drawString(str, (int)fp.x()-15 ,(int)fp.y()-r*5);
            }
        }
    }

    private void drawNode(node_data n, int r, Graphics g) {
        geo_location pos = n.getLocation();
        geo_location fp = this._w2f.world2frame(pos);
        g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
        g.drawString("" + n.getKey(), (int) fp.x(), (int) fp.y() - 4 * r);
    }

    private void drawEdge(edge_data e, Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();
        geo_location s = gg.getNode(e.getSrc()).getLocation();
        geo_location d = gg.getNode(e.getDest()).getLocation();
        geo_location s0 = this._w2f.world2frame(s);
        geo_location d0 = this._w2f.world2frame(d);
        g.drawLine((int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y());
        //	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
    private void paintAgentValue(Graphics g){
        g.setColor(Color.gray.darker());
        g.setFont(new Font(null,Font.BOLD,22));
        g.drawString("Agent Value:",this.getWidth()-175,30);
        g.setColor(Color.green.darker());
        g.setFont(new Font(null,Font.BOLD,22));
        for(CL_Agent agent :_ar.getAgents()){
            String str = String.format("agent %d:  score: %.1f",agent.getID(),agent.getValue());
            g.drawString(str,this.getWidth()-200,50 +40*agent.getID());
        }
    }


    private void paintSenario(Graphics g) {
        g.setColor(Color.black);
        g.setFont(new Font(null, Font.BOLD, 25));
        g.drawString("senario "+ _senario, 40, 35);
    }

    private void paintTimeToEnd(Graphics g) {
        g.setColor(Color.GREEN.darker().darker());
        g.setFont(new Font(null, Font.BOLD, 22));
        long timeToEnd= _game.timeToEnd();
        g.drawString("Time to end: " + (float)timeToEnd / 100, 40,55);

    }
}