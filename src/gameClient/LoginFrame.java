package gameClient;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {

    private JButton start;
    private int scenario = 0;
    private long id = 316095660;
    JTextField idTF = new JTextField(7);
    JTextField scenTF = new JTextField(3);

    LoginFrame() {
        this.setSize(500, 500);
        this.setLocationRelativeTo(null);
        this.setTitle("POKEMON - DAVID AND DVIR");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel idP = new JPanel();
        JPanel scenP = new JPanel();
        JPanel startP = new JPanel();

        JLabel idL = new JLabel("ID: ");
        JLabel scenL = new JLabel("Scenario: ");

        Font f = new Font("TimesRoman", Font.BOLD, 24);
        idTF.setFont(f);
        idL.setFont(f);
        scenTF.setFont(f);
        scenL.setFont(f);

        start = new JButton("START GAME");

        idP.add(idL);
        idP.add(idTF);
        scenP.add(scenL);
        scenP.add(scenTF);
        startP.add(start);

        ImageIcon image = new ImageIcon("data/catch.jpg");
        JLabel header = new JLabel("", image, JLabel.CENTER);

        Box theBox = Box.createVerticalBox();
        theBox.add(header);
        theBox.add(idP);
        theBox.add(scenP);
        theBox.add(startP);

        this.add(theBox);

        start.addActionListener(new ListenForButton());
        this.setVisible(true);
    }

    private class ListenForButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == start) {
                try {
                    id = Long.parseLong(idTF.getText());
                    scenario = Integer.parseInt(scenTF.getText());
                    if (scenario < 0) {
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
        return scenario;
    }

    public long getId() {
        return id;
    }

}
