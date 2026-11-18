import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;

public class ProductLineFrame extends JFrame {
    private FactoryManger manager;
    private WareHouse wareHouse;
    private JTextField textField;
    private JTextField quantity;
    private JTable tabletask;
    private DefaultTableModel model;
    private JComboBox<ProductLine> line;

    public ProductLineFrame(FactoryManger manager, WareHouse wareHouse) {
        this.manager = manager;
        this.wareHouse = wareHouse;
        ProductLineFileManager.loadProductLines(manager,"ProductLine.txt");
        setTitle("خطوط الانتاج");
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        JLabel titlelabel = new JLabel("ادارة خطوط الانتاج والمهام");
        titlelabel.setFont(new Font("Arial", Font.BOLD, 18));
        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        add(titlelabel, BorderLayout.NORTH);
        JPanel inputpanel = new JPanel(new GridLayout(0, 2, 8, 12));
        inputpanel.setBorder(BorderFactory.createTitledBorder("اضافة"));
        String[] colums = {"رقم الخط", "اسم الخط", "الحالة", "المنتج", "الكمية", "نسبة الانجاز", "حالة المهمة", "المهام"};
        model = new DefaultTableModel(colums, 0);
        tabletask = new JTable(model);
        inputpanel.add(new JLabel(" خط الانتاج:"));
        line = new JComboBox<>();
        inputpanel.add(line);
        loads();
        inputpanel.add(new JLabel("اسم المنتج"));
        textField = new JTextField(10);
        inputpanel.add(new JLabel("الكمية"));
        quantity = new JTextField(10);
        inputpanel.add(textField);
        inputpanel.add(quantity);
        JPanel buttonpanel = new JPanel();
        JButton addtask = new JButton("اضافة مهمة");
        JButton delettask = new JButton("الغاء مهمة");
        JButton viewtaskforline = new JButton("عرض المهام التابعة لخط انتاج");
        JButton viewtaskforproduct = new JButton("عرض مهام منتج معين");
        JButton viewlinesforproduct = new JButton("عرض خطوط الانتاج التي قامت بمهمات محددة لمنتج معين");
        JButton viewProductsForLine = new JButton("عرض المنتجات المصنعة التابعة لخط انتاج معين");
        JButton viewallproducts=new JButton("عرض المنتجات المصنعة المصنعة من جميع خطوط الانتاج");
        JButton loadbutton=new JButton("تحميل المخزون");
        JButton savebutton=new JButton("حفظ البيانات");
        JButton statustask = new JButton("تصفية المهام");
        JButton cancelbutton = new JButton("الغاء");
        buttonpanel.add(addtask);
        buttonpanel.add(delettask);
        buttonpanel.add(viewtaskforline);
        buttonpanel.add(viewtaskforproduct);
        buttonpanel.add(viewlinesforproduct);
        buttonpanel.add(viewProductsForLine);
        buttonpanel.add(viewallproducts);
        buttonpanel.add(loadbutton);
        buttonpanel.add(savebutton);
        buttonpanel.add(statustask);
        buttonpanel.add(cancelbutton);
        tabletask = new JTable(model);
        tabletask.getColumnModel().getColumn(7).setMinWidth(0);
        tabletask.getColumnModel().getColumn(7).setMaxWidth(0);
        tabletask.getColumnModel().getColumn(7).setWidth(0);


        JScrollPane scrollPane = new JScrollPane(tabletask);
        add(new JScrollPane(tabletask), BorderLayout.WEST);
        add(inputpanel, BorderLayout.NORTH);
        add(buttonpanel, BorderLayout.CENTER);
        update();
        addtask.addActionListener(e -> {
            ProductLine line1 = (ProductLine) line.getSelectedItem();
            if (line1 == null) {
                JOptionPane.showMessageDialog(this, "اختر خط انتاج اولا");
                return;
            }
            String productName = textField.getText().trim();
            if (productName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ادخل اسم المنتج");
                return;
            }
            int q;
            try {
                q = Integer.parseInt(quantity.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "الكمية غير صحيحة");
                return;
            }
            Product product = new Product(productName);
            Task task = null;
            try {
                task = new Task(line1.getNumberLine(), product, q);
                line1.getTaskqueue().add(task);
                if (line1.getTaskqueue().size()==1) {
                    boolean started = line1.startTask(task);
                    if (started) {
                        JOptionPane.showMessageDialog(this, "تم اضافة المهمة");
                    } else {
                        JOptionPane.showMessageDialog(this, "المواد غير كافية لبدء المهمة");
                    }
                }
                else{
                        JOptionPane.showMessageDialog(this, "تم اضافة المهمة ولكن لم تبدأ بعد");
                    }

                refreshTable();
            } catch (Exceptions.InvalidTaskDataException ex) {
                JOptionPane.showMessageDialog(this, "حدث خطأ اثناء بدء المهمة"+ex.getMessage());
                return;
            }
        });
        delettask.addActionListener(e -> {
            try {
                String productName = JOptionPane.showInputDialog(this, "ادخل اسم المنتج الذي تريد الغاء مهمته");
                if (productName == null || productName.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "الرجاء إدخال اسم المنتج");
                    return;
                }

                int row = tabletask.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(this, "لا يوجد مهمة بهذا الاسم","erorr",JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int linenumber = (int) model.getValueAt(row, 0);
                ProductLine line1 = manager.getLines().stream()
                        .filter(l -> l.getNumberLine() == linenumber)
                        .findFirst().orElse(null);
                if (line1 == null || line1.getTaskqueue() == null) {
                    JOptionPane.showMessageDialog(this, "لا توجد مهمة مرتبطة بهذا الخط");
                    return;
                }
                Task task = line1.getTaskqueue().stream()
                        .filter(t -> t.getProduct().getName().equalsIgnoreCase(productName))
                        .findFirst().orElse(null);
                if (task != null) {
                    task.cancel();
                    JOptionPane.showMessageDialog(this, "تم الغاء المهمة بنجاح");
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(this, "لم يتم العثور على مهمة بهذا المنتج");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "حدث خطأ: " + ex.getMessage(), "خطأ", JOptionPane.ERROR_MESSAGE);
            }
        });
        cancelbutton.addActionListener(e -> {
            this.dispose();
            new SupervisorFrame(manager, wareHouse).setVisible(true);
            refreshTable();

        });
        viewtaskforline.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(this, "ادخل رقم خط الانتاج");
            if (input == null || input.isEmpty()) {
                return;
            }
            int linenumber;
            try {
                linenumber = Integer.parseInt(input);
            } catch (NumberFormatException ey) {
                JOptionPane.showMessageDialog(this, "الرقم غير صحيح");
                return;

            }
            ProductLine line1 = manager.getLines().stream().filter(w -> w.getNumberLine() == linenumber).findFirst().orElse(null);
            if (line1 == null) {
                JOptionPane.showMessageDialog(this, "اختر خط انتاج اولا");
                return;
            }
            Queue<Task> tasks = line1.getTaskqueue();
            if (tasks == null || tasks.isEmpty()) {
                JOptionPane.showMessageDialog(this, "لا توجد مهام لهذا الخط");
                return;
            }
            model.setRowCount(0);

            synchronized (tasks) {
                for (Task task : tasks) {
                    model.addRow(new Object[]{
                            line1.getNumberLine(),
                            line1.getNameLine(),
                            line1.getStatus(),
                            task.getProduct().getName(),
                            task.getQuantity(),
                            task.getProgressPercent() + "%",
                            task.isFinished()?"مكتملة":( task.isCancelled() ? "ملغاة" : "قيد التنفيذ"),
                            task


                    });
                }
            }
        });

        viewtaskforproduct.addActionListener(x -> {
            String nameproduct = JOptionPane.showInputDialog(this, "ادخل اسم المنتج");
            if (nameproduct == null && nameproduct.trim().isEmpty()) {
                return;
            }
            model.setRowCount(0);
            boolean found = false;
            for (ProductLine line : manager.getLines()) {
                Queue<Task> tasksqueue = line.getTaskqueue();
                if (tasksqueue != null || !tasksqueue.isEmpty()) {
                    synchronized (tasksqueue) {
                        for (Task task1 : tasksqueue) {
                            if (task1.getProduct().getName().equalsIgnoreCase(nameproduct)) {
                                model.addRow(new Object[]{
                                        line.getNumberLine(),
                                        line.getNameLine(),
                                        line.getStatus(),
                                        task1.getProduct().getName(),
                                        task1.getQuantity(),
                                        task1.getProgressPercent() + "%",
                                        task1.isCancelled() ? "ملغاة" : (task1.isFinished() ? "مكتملة" : "قيد التنفيذ"),
                                        task1
                                });
                                found = true;
                            }
                        }
                    }
                }
            }
            if (!found) {
                JOptionPane.showMessageDialog(this, "لا توجد مهام بهذا المنتج");

            }
        });


        statustask.addActionListener(e -> {
            String[] options = {"قيد التنفيذ", "مكتملة", "ملغاة", "عرض الكل"};
            String choice = (String) JOptionPane.showInputDialog(this, "اختر حالة المهمة", "تصفية المهام", JOptionPane.QUESTION_MESSAGE
                    , null, options, options[0]
            );
            if (choice == null) return;
            model.setRowCount(0);
            int count = 0;
            boolean found = false;
            for (ProductLine line1 : manager.getLines()) {
                Queue<Task> tasks = line1.getTaskqueue();
                if (tasks == null && tasks.isEmpty()) continue;
                    synchronized (tasks) {
                        for (Task task : tasks) {
                            boolean matches = false;
                            if ("قيد التنفيذ".equals(choice) && !task.isCancelled() && !task.isFinished()) {
                                matches = true;
                            } else if ("ملغاة".equals(choice) && task.isCancelled()) {
                                matches = true;
                            } else if ("مكتملة".equals(choice) && task.isFinished()) {
                                matches = true;
                            }
                            else if ("عرض الكل".equals(choice)){
                                matches=true;
                            }

                            if (matches) {
                                model.addRow(new Object[]{
                                        line1.getNumberLine(),
                                        line1.getNameLine(),
                                        line1.getStatus(),
                                        task.getProduct().getName(),
                                        task.getQuantity(),
                                        task.getProgressPercent() + "%",
                                       task.isCancelled()?"ملغية": task.isFinished() ? "مكتملة" : "قيد التنفيذ",
                                        task
                                });
                                count++;
                                found = true;
                            }
                        }
                    }
                }


            if (count == 0) {
                JOptionPane.showMessageDialog(this, "لا توجد مهام تطابق الفلتر", "معلومة", JOptionPane.INFORMATION_MESSAGE);

            }


        });
        viewlinesforproduct.addActionListener(e -> {
            String nameProduct = JOptionPane.showInputDialog(this, "ادخل اسم المنتج");
            if (nameProduct == null || nameProduct.trim().isEmpty()) {
                return;
            }
            model.setRowCount(0);
            boolean found = true;
            for (ProductLine line1 : manager.getLines()) {
                Queue<Task> taskQueue = line1.getTaskqueue();
                if (taskQueue != null && taskQueue.isEmpty()) {
                    synchronized (taskQueue) {
                        for (Task task : taskQueue) {
                            if (task.getProduct().getName().equalsIgnoreCase(nameProduct)) {
                                model.addRow(new Object[]{
                                        line1.getNumberLine(),
                                        line1.getNameLine(),
                                        line1.getStatus(),
                                        task.getProduct().getName(),
                                        task.getProgressPercent() + "%",
                                        task.isFinished()?"مكتملة":task.isCancelled() ? "ملغاة" : "قيد التنفيذ",
                                        task
                                });
                                found = true;
                                break;
                            }
                        }
                    }
                }
            }
            if (!found) {
                JOptionPane.showMessageDialog(this, "لا توجد خطوط انتاج قامت بمهمات لهذا المنتج", "معلومة", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        viewProductsForLine.addActionListener(e -> {
            String lineNumberStr = JOptionPane.showInputDialog(this, "ادخل رقم خط الإنتاج");
            if (lineNumberStr == null || lineNumberStr.trim().isEmpty()) {
                return;
            }

            int lineNumber;
            try {
                lineNumber = Integer.parseInt(lineNumberStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "رقم خط الإنتاج غير صحيح", "خطأ", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ProductLine selectedLine = manager.getLines().stream()
                    .filter(line -> line.getNumberLine() == lineNumber)
                    .findFirst()
                    .orElse(null);

            if (selectedLine == null) {
                JOptionPane.showMessageDialog(this, "خط الإنتاج غير موجود", "خطأ", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Queue<Task> taskQueue = selectedLine.getTaskqueue();
            if (taskQueue == null || taskQueue.isEmpty()) {
                JOptionPane.showMessageDialog(this, "لا توجد مهام لهذا الخط", "معلومة", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            model.setRowCount(0);
            boolean found = false;

            synchronized (taskQueue) {
                for (Task task : taskQueue) {
                    model.addRow(new Object[]{
                            selectedLine.getNumberLine(),
                            selectedLine.getNameLine(),
                            selectedLine.getStatus(),
                            task.getProduct().getName(),
                            task.getQuantity(),
                            task.getProgressPercent() + "%",
                            task.isCancelled()?"ملغية":task.isFinished() ? "مكتملة" : "قيد التنفيذ",
                            task
                    });
                    found = true;
                    break;
                }
            }

            if (!found) {
                JOptionPane.showMessageDialog(this, "لا توجد مهام لهذا الخط", "معلومة", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        viewallproducts.addActionListener(e -> {
            model.setRowCount(0);
            boolean found = false;

            for (ProductLine line : manager.getLines()) {
                Queue<Task> taskQueue = line.getTaskqueue();

                if (taskQueue != null && !taskQueue.isEmpty()) {
                    synchronized (taskQueue) {
                        for (Task task : taskQueue) {
                            model.addRow(new Object[]{
                                    line.getNumberLine(),
                                    line.getNameLine(),
                                    line.getStatus(),
                                    task.getProduct().getName(),
                                    task.getQuantity(),
                                    task.getProgressPercent() + "%",
                                    task.isCancelled() ? "ملغاة" : (task.isFinished() ? "مكتملة" : "قيد التنفيذ")
                            });
                            found = true;
                            break;
                        }
                    }
                }
            }

            if (!found) {
                JOptionPane.showMessageDialog(this, "لا توجد مهام لأي خط إنتاج", "معلومة", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        loadbutton.addActionListener(e->{
            InventoryFileManager.loadStock(wareHouse,"stock.txt");
            JOptionPane.showMessageDialog(this,"تم تحميل المخزون بنجاح");
            refreshTable();
        });
        savebutton.addActionListener(e->{
            InventoryFileManager.saveStock(wareHouse,"stock.txt");
            JOptionPane.showMessageDialog(this,"تم حفظ المخزون بنجاح");
        });



    }



    private void update() {
        refreshTable();
        loads();
    }

    private void loads() {
        line.removeAllItems();
        if (!manager.getLines().isEmpty()) {
            for (ProductLine l : manager.getLines()) {
                line.addItem(l);
            }
            line.setSelectedIndex(0);
        }

    }


    private void refreshTable() {
        SwingUtilities.invokeLater(()->{;
            model.setRowCount(0);
            for (ProductLine line : manager.getLines()) {
                Queue<Task> tasks = line.getTaskqueue();
                if (tasks == null || tasks.isEmpty()) {
                    model.addRow(new Object[]{
                            line.getNumberLine(),
                            line.getNameLine(),
                            line.getStatus(),
                            "-",
                            0,
                            "-",
                            "-"
                    });
                } else {
                    synchronized (tasks) {
                        for (Task task : tasks) {
                            model.addRow(new Object[]{
                                    line.getNumberLine(),
                                    line.getNameLine(),
                                    line.getStatus(),
                                    task.getProduct().getName(),
                                    task.getQuantity(),
                                    task.getProgressPercent()+"%",
                                  task.isFinished()?"مكتملة":  task.isCancelled()?"ملغاة":"قيد التنفيذ",
                                    task

                            });

                        }


                    }
                }
            }


        });
    }
}

