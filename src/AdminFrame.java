import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

public class AdminFrame extends JFrame {
     private FactoryManger manager;
     private WareHouse wareHouse;
    private JTable table;
    private DefaultTableModel model;
    private JComboBox<String> status;

    public AdminFrame(FactoryManger manger,WareHouse wareHouse) {
        this.manager=manger;
        this.wareHouse=wareHouse;
        setTitle(" admin");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        JPanel panel = new JPanel(new GridLayout(2, 1));
        JLabel text = new JLabel("Welcome in admin frame....", JLabel.LEFT);
        text.setFont(new Font("Arial", Font.BOLD, 20));
        JLabel label = new JLabel("لوحة تحكم المدير", JLabel.RIGHT);
        label.setFont(new Font("ARIAL", Font.BOLD, 17));
        panel.add(text);
        panel.add(label);
        add(panel, BorderLayout.NORTH);
        JPanel panel1 = new JPanel(new GridLayout(5, 1, 10, 10));
        JButton addbutton = new JButton("اضافة خط انتاج");
        JButton statusbutton = new JButton("تغيير حالة الخط");
        JButton viewprogress = new JButton("عرض اداء الخطوط");
        JButton saveLinesButton=new JButton("حفظ خطوط الانتاج");
        JButton Lbutt = new JButton("تسجيل الخروج");
        panel1.add(addbutton);
        panel1.add(statusbutton);
        panel1.add(viewprogress);
        panel1.add(saveLinesButton);
        panel1.add(Lbutt);
        add(panel1, BorderLayout.CENTER);
        addbutton.addActionListener(e -> {
            String lineName = JOptionPane.showInputDialog(AdminFrame.this, "ادخل اسم الخط");
            if (lineName == null || lineName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "اسم الخط غير صالح");
                return;
            }
            int lineNumber = manager.getLines().size() + 1;
            Map<Item,Integer>recipe=new HashMap<>();
            Item item1=new Item();
            ProductLine newproductLine =new ProductLine(lineNumber,lineName);
            newproductLine.setWareHouse(manger.getWareHouse());
            newproductLine.setRecipe(recipe);
            newproductLine.setOutput(item1);
            newproductLine.setTime(1000);
            try {
                manager.addLines(newproductLine);
                JOptionPane.showMessageDialog(this,"تم اضافة الخط بنجاح");
            } catch (Exceptions.InvalidTaskDataException ex) {
               JOptionPane.showMessageDialog(this,"حدث خطأ ");
            }
            ProductLineFileManager.saveProductLines(manger.getLines(),"ProductLine.txt");
            refreshTable();


        });

        statusbutton.addActionListener(e -> {

            String lineId = JOptionPane.showInputDialog(
                    this,
                    "أدخل رقم خط الإنتاج:"
            );

            if (lineId == null) {
                JOptionPane.showMessageDialog(this,"لم يتم ادخال رقم","خطأ",JOptionPane.ERROR_MESSAGE);
                return;
            }
            int id=0;
            try {
               id=Integer.parseInt(lineId);
            }
            catch (NumberFormatException EX){
                JOptionPane.showMessageDialog(this,"الرجاء ادخل رقم صحيح", "خطأ",JOptionPane.ERROR_MESSAGE);
            return;
            }
            if (id<=0){
                JOptionPane.showMessageDialog(this,"رقم الخط يجب ان يكون اكبر من الصفر","خطأ",JOptionPane.ERROR_MESSAGE);
              return;
            }
            ProductLine selectedLine=null;
            for (ProductLine line:manager.getLines()){
                if (line.getNumberLine()==id){
                    selectedLine=line;
                    break;
                }
            }
            if (selectedLine==null){
                JOptionPane.showMessageDialog(this,"رقم الخط غير موجود","خطأ",JOptionPane.ERROR_MESSAGE);
                return;
            }

            String[] options = {"تشغيل", "إيقاف", "صيانة"};
            String choice = (String) JOptionPane.showInputDialog(
                    this,
                    "اختر العملية:",
                    "تغيير حالة الخط",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]
            );

            if (choice == null) return;
            switch (choice) {
                case "تشغيل":
                    try {
                        manager.startLine(id);
                    } catch (Exceptions.LineNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
                case "إيقاف":
                    try {
                        manager.stopLineByNumber(id);
                    } catch (Exceptions.LineNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
                case "صيانة":
                    try {
                        manager.maintenanceLine(id);
                    } catch (Exceptions.LineNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
            }

            JOptionPane.showMessageDialog(this, "تم تنفيذ العملية بنجاح");
        });
        viewprogress.addActionListener(e -> {
            if (this.manager.getLines().isEmpty()) {
                JOptionPane.showMessageDialog(this, "لا يوجد خطوط انتاج لعرض ادائها", "خطأ", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String[] columns = {"رقم الخط", "اسم الخط", "الحالة", "الكمية المنجزة", "نسبة الإنجاز"};
            model = new DefaultTableModel(columns, 0);
            JTable table=new JTable();
            table.setModel(model);
            boolean hasTask = false;
            for (ProductLine line : manager.getLines()) {
                Queue<Task> tasks = line.getTaskqueue();
                if (tasks != null && !tasks.isEmpty()) {
                    synchronized (tasks) {
                        for (Task task : tasks) {
                            if (!task.isCancelled() && !task.isFinished()) {
                                hasTask = true;
                                model.addRow(new Object[]{
                                        line.getNumberLine(),
                                        line.getNameLine(),
                                        line.getStatus(),
                                        task.getQuantity(),
                                        task.getProgressPercent() + "%"
                                });
                            }
                        }
                    }
                }
            }

            if (!hasTask) {
                JOptionPane.showMessageDialog(this, "لا توجد مهام قيد التنفيذ حاليا", "معلومة", JOptionPane.INFORMATION_MESSAGE);
            }

            JScrollPane scrollPane = new JScrollPane(table);
            JDialog dialog = new JDialog(this, "أداء خطوط الإنتاج", true);
            dialog.setSize(600, 500);
            dialog.setLocationRelativeTo(this);
            dialog.add(scrollPane);
            dialog.setVisible(true);
        });


        saveLinesButton.addActionListener(e->{
            ProductLineFileManager.saveProductLines( manger.getLines(),"ProductLines.csv");
            JOptionPane.showMessageDialog(this,"تم حفظ خطوط الانتاج");
        });


            Lbutt.addActionListener(e -> {
                new LoginFrame(manger,wareHouse).setVisible(true);
                this.dispose();

            });

        }
    private void refreshTable() {
        model.setRowCount(0);
        for (ProductLine line : manager.getLines()) {
            Queue<Task>tasks=line.getTaskqueue();
            if (tasks != null && !tasks.isEmpty()) {
                synchronized (tasks) {
                    for (Task task : tasks) {
                        model.addRow(new Object[]{
                                line.getNumberLine(),
                                line.getNameLine(),
                                line.getStatus(),
                                task.getProduct().getName(),
                                task.getQuantity(),
                                task.getProgressPercent() + "%",
                                task.isCancelled() ? "ملغاة" : (task.isFinished() ? "مكتملة" : "قيد التنفيذ")
                        });
                    }
                }
            } else {
                model.addRow(new Object[]{
                        line.getNumberLine(),
                        line.getNameLine(),
                        line.getStatus(),
                        "-",
                        0,
                        "-",
                        "-"
                });
            }
        }
    }
}