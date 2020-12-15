package gameClient;


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

public class FinalGui extends JFrame implements  ActionListener {

    private JPanel login;

    FinalGui(){
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new FlowLayout(FlowLayout.LEFT,2,2));
        login = new LoginPanel();
//        login.setVisible(true);
//
////
//
//        JTextField textField1 = new JTextField();
//        textField1.setPreferredSize(new Dimension(250,40));
//
//        JTextField textField2 = new JTextField();
//        textField1.setPreferredSize(new Dimension(250,40));
//        this.add(textField1,BorderLayout.PAGE_START);
//        this.add(textField2,BorderLayout.PAGE_END);
        this.pack();
        this.add(login);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}

class LoginPanel extends JPanel
{
    private JLabel idL, senL;
    private JTextField idTF, senTF;
    private JButton ok, cancel;

    LoginPanel()
    {
        JPanel loginP = new JPanel();
        loginP.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 2));
        loginP.setPreferredSize(new Dimension(250, 85));

        JTextField idTF = new JTextField(22);
        JTextField senTF = new JTextField(22);

        JLabel idL = new JLabel("ID:");
        JLabel senL = new JLabel("Senario:");

        ok = new JButton("OK");
        cancel = new JButton("CANCEL");

        loginP.add(idL);
        loginP.add(idTF);
        loginP.add(senL);
        loginP.add(senTF);
        loginP.add(ok);
        loginP.add(cancel);
    }
}
