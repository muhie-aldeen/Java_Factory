import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SupervisorFrame extends JFrame {
    private WareHouse wareHouse;
    private FactoryManger manager;
    public SupervisorFrame(FactoryManger manager,WareHouse wareHouse){
    this.manager=manager;
    this.wareHouse=wareHouse;
    InventoryFileManager.loadStock(wareHouse,"stock.txt");
    ProductLineFileManager.loadProductLines(manager,"ProductLines.txt");
         setTitle("واجهة المشرف");
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel=new JPanel(new GridLayout(2,1));
        JLabel label=new JLabel("Welcom in supervisor frame......",JLabel.LEFT);
        label.setFont(new Font("Arial",Font.BOLD,20));
        JLabel text=new JLabel("لوحة تحكم المشرف:",JLabel.RIGHT);
      text.setFont(new Font("ARIAL", Font.PLAIN,18));
      panel.add(label);
      panel.add(text);
      add(panel,BorderLayout.NORTH);
      JButton button1=new JButton("ادارة المخزون");
      JButton button2=new JButton("ادارة خطوط الانتاج والمهام");
      JButton button3=new JButton("تسجيل خروج");
      JPanel panel1=new JPanel(new GridLayout(3,1,15,15));
      panel1.add(button1);
      panel1.add(button2);
      panel1.add(button3);
      add(panel1,BorderLayout.CENTER);
      button1.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
               new InventoryFrame(manager,wareHouse).setVisible(true);
               dispose();
          }
      });
      button2.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
              new ProductLineFrame(manager,wareHouse).setVisible(true);
              dispose();

          }
      });
      button3.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
              new LoginFrame(manager,wareHouse).setVisible(true);
              dispose();
          }
      });
      setVisible(true);
    }


}
