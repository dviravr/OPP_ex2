package gameClient;

import api.directed_weighted_graph;
import api.game_service;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.Thread.sleep;

public class BigFrame extends JFrame implements  ActionListener,Runnable {
    MyFrame2 panel;
    DetailsPanel detailsPanel;
    Arena _ar;
    int _gameID;
    game_service _game;
    Timer timer;

    BigFrame(Arena arena,int gameID,game_service game_service ){

        _ar =arena;
        _gameID = gameID;
        _game=game_service;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setLocationRelativeTo(null);
        this.setSize(1000, 700);
        this.setTitle("POCIMON - DAVID AND DVIR");
        detailsPanel = new DetailsPanel(arena,gameID);
        detailsPanel.setBounds(0,0,this.getWidth(),100);
        this.add(detailsPanel);
        panel = new MyFrame2(arena);
        panel.setBounds(0,100,this.getWidth(),getHeight()-100);
        this.add(panel);


//        run();
        this.setVisible(true);

        //   run();
    }
//
//    public void paint(Graphics g){
//      JLabel levelID = new JLabel();
//      levelID.setText("Game ID:"+ _gameID );
//      this.add(levelID);
//
//    }
public void update(Arena ar) {
    this._ar = ar;
    panel.update(ar);
//    detailsPanel.
}
    @Override
    public void run() {
       while (_game.isRunning()){
           directed_weighted_graph graph = _game.getJava_Graph_Not_to_be_used();
           _ar.setAgents(Arena.getAgents(_game.getAgents(),graph));
           _ar.setPokemons(Arena.json2Pokemons(_game.getPokemons(), _ar.getPokemons(), graph));
           panel.update(_ar);
           panel.repaint();
           detailsPanel.repaint();

           //detailsPanel.update(_ar);
           try {
               sleep(1000);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }

       }

        timer = new Timer(10,this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        update(_ar);
        panel.repaint();
        detailsPanel.repaint();

    }

    public void start() {
        run();
    }

    public void updateGame(game_service game) {
_game=game;

    }
}
