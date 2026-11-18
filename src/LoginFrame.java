import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private FactoryManger manager;
    private WareHouse wareHouse;
    private JTextField username;
    private JPasswordField passwordField;
    private JButton login;
    public LoginFrame(FactoryManger manager,WareHouse wareHouse){
        this.manager=manager;
        this.wareHouse=wareHouse;
        JPanel panel=new JPanel(new GridLayout(10,10));
        ImageIcon imageIcon=new ImageIcon("n.png.png");
        JLabel label=new JLabel(imageIcon);
        panel.add(label,BorderLayout.SOUTH);
        label.setText("welcome in Invintory UPL");
        panel.add(label,BorderLayout.CENTER);
        setTitle("Login");
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        panel.add( new JLabel("          user name"));
        username= new JTextField();
        panel.add(username);
        panel.add(new JLabel("          password"));
        passwordField=new JPasswordField();
        panel.add(passwordField);
         login=new JButton("Login");
        panel.add(login);
        add(panel);
       login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user=username.getText();
                String password= new String( passwordField.getPassword());
                if (user.equals("admin")&&password.equals("123")) {
                    JOptionPane.showMessageDialog(null, "welcom admin");
                    new AdminFrame(manager,wareHouse).setVisible(true);
                    dispose();
                }
               else if (user.equals("supervisor")&&password.equals("126")) {
                    JOptionPane.showMessageDialog(null,"welcom supervisor");
                    new SupervisorFrame(manager,wareHouse).setVisible(true);
                    dispose();
                }
                else {
                    JOptionPane.showMessageDialog(null,"Invalid username or password!");

                }

            }
        });
        setVisible(true);
    }

}

