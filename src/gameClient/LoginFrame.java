package gameClient;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {

    private final JButton _startGame;
    private int _scenario = 0;
    private long _id = 316095660;
    JTextField _idTF = new JTextField(7);
    JTextField _scenarioTF = new JTextField(3);

    LoginFrame() {
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);
        this.setTitle("POKEMON - DAVID AND DVIR");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel idP = new JPanel();
        JPanel scenP = new JPanel();
        JPanel startP = new JPanel();

        JLabel idL = new JLabel("ID: ");
        JLabel scenarioL = new JLabel("Scenario: ");

        Font f = new Font("TimesRoman", Font.BOLD, 24);
        _idTF.setFont(f);
        idL.setFont(f);
        _scenarioTF.setFont(f);
        scenarioL.setFont(f);

        _startGame = new JButton("START GAME");

        idP.add(idL);
        idP.add(_idTF);
        scenP.add(scenarioL);
        scenP.add(_scenarioTF);
        startP.add(_startGame);

        ImageIcon image = new ImageIcon("data/catch.jpg");
        JLabel header = new JLabel("", image, JLabel.CENTER);

        Box theBox = Box.createVerticalBox();
        theBox.add(header);
        theBox.add(idP);
        theBox.add(scenP);
        theBox.add(startP);

        this.add(theBox);

        _startGame.addActionListener(new ListenForButton());
        this.setVisible(true);
    }

    private class ListenForButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == _startGame) {
                try {
                    _id = Long.parseLong(_idTF.getText());
                    _scenario = Integer.parseInt(_scenarioTF.getText());
                    if (_scenario < 0) {
                        throw new NumberFormatException();
                    }
                    startGame();
                } catch (NumberFormatException err) {
                    JOptionPane.showMessageDialog(null, "Invalid input!");
                }
            }
        }
    }

    private void startGame() {
        Thread client = new Thread(new Ex2());
        client.start();
        this.dispose();
    }

    public int getScenario() {
        return _scenario;
    }

    public long getId() {
        return _id;
    }

}
