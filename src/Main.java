import javax.swing.*;
import java.net.FileNameMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) throws Exceptions.InvalidTaskDataException {
        Map<Integer,Item>items=new HashMap<>();
        WareHouse wareHouse = new WareHouse();
        FactoryManger manger = new FactoryManger();
        SwingUtilities.invokeLater(() -> {
            new AdminFrame(manger,wareHouse).setVisible(true);

        });


        Item iron = null;
        ProductLine line1 = new ProductLine(2, "electron");
        manger.addLines(line1);
        try {
            iron = new Item(1, "Iron", "Raw", 10, 6, 3);
            wareHouse.setMaxCapacity(iron, 100);
            Product product = new Product("ironProduct");
            product.addIngredient(iron, 2);
            Task h = new Task(1, product, 3);
            Task m = new Task(2, product, 5);
            Task s = new Task(3, product, 2);
            line1.getTaskqueue().add(h);
            line1.getTaskqueue().add(m);
            line1.getTaskqueue().add(h);

        } catch (Exceptions.InvalidTaskDataException j) {
            j.getMessage();

        }

    }
}
