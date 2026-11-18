import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

public class InventoryFrame extends JFrame {
    private FactoryManger manager;
    private WareHouse wareHouse;
    private JTextField namefield;
    private JTextField quantityfield;
    private JTextField searchField;
    private JTextField priceField;
    private JComboBox typeBox;
    private JTable table;
    private DefaultTableModel model;

    public InventoryFrame( FactoryManger manager,WareHouse wareHouse ) {
       this.manager=manager;
       this.wareHouse=wareHouse;
       InventoryFileManager.loadStock(wareHouse,"stock.txt");
        setTitle("إدارة المخزون");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        JPanel input = new JPanel();
        input.setLayout(new GridLayout(5, 2, 10, 10));
        input.setBorder(BorderFactory.createTitledBorder("ادخال بيانات العنصر"));
        input.add(new JLabel("اسم العنصر:"));
        namefield = new JTextField();
        namefield.setPreferredSize(new Dimension(150, 30));
        input.add(namefield);
        input.add(new JLabel("الكمية:"));
        quantityfield = new JTextField();
        quantityfield.setPreferredSize(new Dimension(150, 30));
        input.add(quantityfield);
        input.add(new JLabel("الفئة:"));
        JComboBox<String>categoryField=new JComboBox<>(new String[]{"اكل ومشروبات","مواد غذائية","منظفات","البسة","الكترونيات","ادوات كهربائية"});
        categoryField.setPreferredSize(new Dimension(150, 30));
        input.add(categoryField);
        input.add(new JLabel("السعر"));
        priceField = new JTextField();
        priceField.setPreferredSize(new Dimension(150, 30));
        input.add(priceField);
        typeBox = new JComboBox<>(new String[]{"خام", "منتج"});
        input.add(new JLabel("النوع:"));
        input.add(typeBox);
        JPanel panelButton = new JPanel();
        panelButton.setLayout(new GridLayout(3, 1, 10, 10));
        JButton addbutton = new JButton("إضافة عنصر");
        JButton deletbutton = new JButton("حذف عنصر");
        JButton updatebutton = new JButton("تعديل عنصر");
        JButton loadbutton=new JButton("تحميل المخزون");
        JButton savebutton=new JButton("حفظ  حالة المخزون");
        JButton cancelbutton = new JButton("الغاء");
        addbutton.setPreferredSize(new Dimension(100, 30));
        deletbutton.setPreferredSize(new Dimension(100, 30));
        updatebutton.setPreferredSize(new Dimension(100, 30));
        panelButton.add(addbutton);
        panelButton.add(deletbutton);
        panelButton.add(updatebutton);
        panelButton.add(cancelbutton);
        panelButton.add(loadbutton);
        panelButton.add(savebutton);
        model = new DefaultTableModel(new String[]{"الاسم", "الكمية", "النوع", "الفئة", "السعر"}, 0); // إضافة عمود السعر
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);


        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("بحث حسب الاسم والفئة:"));
        searchField = new JTextField(15);
        searchPanel.add(searchField);
        JButton searchButton = new JButton("بحث");
        searchPanel.add(searchButton);
        add(input, BorderLayout.NORTH);
        add(panelButton, BorderLayout.EAST);
        add(scrollPane, BorderLayout.CENTER);
        add(searchPanel, BorderLayout.SOUTH);

        addbutton.addActionListener(e -> {
            String name = namefield.getText();
            String type = (String) typeBox.getSelectedItem();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "اسم العنصر غير صالح", "خطأ", JOptionPane.ERROR_MESSAGE);
                return;
            }
            int quantity;
            try {
                quantity = Integer.parseInt(quantityfield.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "الكمية غير صحيحة", "خطأ", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double price;
            try {
                price = Double.parseDouble(priceField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "السعر غير صحيح", "خطأ", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String category= (String) categoryField.getSelectedItem();
            Item item = new Item();
            try {
                item.setId(wareHouse.getStock().size() + 1);
            } catch (Exceptions.InvalidTaskDataException ex) {
                throw new RuntimeException(ex);
            }
            try {
                item.setName(name);
            } catch (Exceptions.InvalidTaskDataException ex) {
                throw new RuntimeException(ex);
            }
            try {
                item.setDivision(type);
            } catch (Exceptions.InvalidTaskDataException ex) {
                throw new RuntimeException(ex);
            }
            try {
                item.setQuantity(quantity);
            } catch (Exceptions.InvalidTaskDataException ex) {
                throw new RuntimeException(ex);
            }
            item.setCategory(category);
            try {
                item.setPrice(price);
            } catch (Exceptions.InvalidTaskDataException ex) {
                throw new RuntimeException(ex);
            }
            try {
    if (!wareHouse.getMaxCapacity().containsKey(item)) {
        wareHouse.setMaxCapacity(item, 1000);
    }
}
catch (Exception ec){
    ec.printStackTrace();

}

            try {
                wareHouse.addItem(item, quantity);
                JOptionPane.showMessageDialog(this,"تمت الاضافة");
                loadTable();
            } catch (InterruptedException | Exceptions.InvalidTaskDataException ex) {
                ex.printStackTrace();
            }

        });


        deletbutton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "من فضلك اختر عنصر من الجدول");
                return;
            }
            String name = (String) model.getValueAt(row, 0);
            for (Item item : wareHouse.getStock().keySet()) {
                if (item.getName().equals(name)) {
                    try {
                        wareHouse.removeItem(item,wareHouse.getStock().get(item));
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    } catch (Exceptions.InvalidTaskDataException ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
                }
            }
            model.removeRow(row);
            loadTable();

        });



        updatebutton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "من فضلك اختر عنصر من الجدول");
                return;
            }
            String name = namefield.getText();
            String type = (String) typeBox.getSelectedItem();
            String category= (String) categoryField.getSelectedItem();
            int quantity;
            try {
                quantity = Integer.parseInt(quantityfield.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "الكمية غير صحيحة", "خطأ", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double price;
            try {
                price = Double.parseDouble(priceField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "السعر غير صحيح", "خطأ", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String catygory= (String) categoryField.getSelectedItem();
            if (catygory==null||catygory.isEmpty()){
                JOptionPane.showMessageDialog(this,"الفئة غير صالحة","erorr",JOptionPane.ERROR_MESSAGE);
            }
            String oldName = (String) model.getValueAt(row, 0);
            Item itemToUpdate = null;
            for (Item item : wareHouse.getStock().keySet()) {
                if (item.getName().equals(oldName)) {
                    itemToUpdate = item;
                    break;
                }
            }
            if (itemToUpdate != null) {
                boolean isUpdated = false;
                if (!itemToUpdate.getName().equals(name)) {
                    try {
                        itemToUpdate.setName(name);
                    } catch (Exceptions.InvalidTaskDataException ex) {
                        throw new RuntimeException(ex);
                    }
                    isUpdated = true;
                }
                if (!itemToUpdate.getDivision().equals(type)) {
                    try {
                        itemToUpdate.setDivision(type);
                    } catch (Exceptions.InvalidTaskDataException ex) {
                        throw new RuntimeException(ex);
                    }
                    isUpdated = true;
                }
                if (itemToUpdate.getQuantity() != quantity) {
                    try {
                        itemToUpdate.setQuantity(quantity);
                    } catch (Exceptions.InvalidTaskDataException ex) {
                        throw new RuntimeException(ex);
                    }
                    isUpdated = true;
                }
                if (itemToUpdate.getPrice() != price) {
                    try {
                        itemToUpdate.setPrice(price);
                    } catch (Exceptions.InvalidTaskDataException ex) {
                        throw new RuntimeException(ex);
                    }
                    isUpdated=true;
                }
                if (!itemToUpdate.getCategory().equals(catygory)) {
                        itemToUpdate.setCategory(catygory);
                        isUpdated=true;
                }
                if (isUpdated) {
                    model.setValueAt(name, row, 0);
                    model.setValueAt(quantity, row, 1);
                    model.setValueAt(type, row, 2);
                    model.setValueAt(itemToUpdate.getCategory(),row,3);
                    model.setValueAt(itemToUpdate.getPrice(), row, 4);
                    wareHouse.updateItem(itemToUpdate);
                    JOptionPane.showMessageDialog(this, "تم تعديل العنصر بنجاح");
                    loadTable();
                } else {
                    JOptionPane.showMessageDialog(this, "لم يتم تعديل العنصر(لايوجد تغييرات)", "تنبيه", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "العنصر غير موجود في المخزون", "خطأ", JOptionPane.ERROR_MESSAGE);
            }
        });


        searchButton.addActionListener(e -> {
            String searchText = searchField.getText();
            model.setRowCount(0);
            for (Item item : wareHouse.getStock().keySet()) {
                boolean matchesSearch = item.getName().contains(searchText) || item.getDivision().contains(searchText);
                boolean isAvailable = wareHouse.getStock().get(item) < wareHouse.getMaxCapacity().get(item);
                if (matchesSearch && isAvailable) {
                    int quntity = wareHouse.getStock().get(item);
                    if (quntity >= 0) {
                        model.addRow(new Object[]{item.getName(), item.getQuantity(), item.getDivision(),item.getCategory(), item.getPrice()});
                    } else {
                        JOptionPane.showMessageDialog(this, "الكمية غير صالحة للعنصر", "خطأ", JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
        });
        cancelbutton.addActionListener(e -> {
            this.setVisible(false);
            new SupervisorFrame(manager,wareHouse).setVisible(true);
        });
        loadbutton.addActionListener(e->{
            InventoryFileManager.loadStock(wareHouse,"stock.txt");
            JOptionPane.showMessageDialog(this,"تم تحميل المخزون بنجاح");
loadTable();
        });
        savebutton.addActionListener(e->{
            InventoryFileManager.saveStock(wareHouse,"stock.txt");
            JOptionPane.showMessageDialog(this,"تم حفظ المخزون بنجاح");
        });

        setVisible(true);

    }
    private void loadTable() {
        model.setRowCount(0);
        if (wareHouse==null||wareHouse.getStock()==null)return;
        for (Map.Entry<Item, Integer> entry : wareHouse.getStock().entrySet()) {
Item item=entry.getKey();
int q=entry.getValue();
model.addRow(new Object[]{
        item.getName(),
        q,
        item.getDivision(),
        item.getCategory(),
        item.getPrice(),
        item.getMinRequired()
});
        }
    }
        }
