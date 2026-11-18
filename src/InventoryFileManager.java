
import java.io.*;
import java.util.Map;

public class InventoryFileManager {

    // ====== LOAD (Input) ======
    public static void loadStock(WareHouse wareHouse, String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

            String line;
            while ((line = br.readLine()) != null) {
                try {
                    String[] data = line.split(",");

                    int id = Integer.parseInt(data[0]);
                    String name = data[1];
                    String division = data[2];
                    double price = Double.parseDouble(data[3]);
                    int quantity = Integer.parseInt(data[4]);
                    int minReq = Integer.parseInt(data[5]);

                    Item item = new Item(id, name, division, price, quantity, minReq);

                    wareHouse.setMaxCapacity(item, 1000);
                    wareHouse.addItem(item, quantity);

                } catch (Exception e) {
                    // خطأ بسطر واحد
                   ErrorLogger.log("Invalid line in stock file: " + line, e);
                }
            }

        } catch (IOException e) {
            // خطأ ملف
            ErrorLogger.log("Error reading stock file", e);
        }
    }

    // ====== SAVE (Output) ======
    public static void saveStock(WareHouse wareHouse, String fileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {

            for (Map.Entry<Item, Integer> entry : wareHouse.getStock().entrySet()) {
                Item item = entry.getKey();
                int qty = entry.getValue();

                bw.write(
                        item.getId() + "," +
                                item.getName() + "," +
                                item.getDivision() + "," +
                                item.getPrice() + "," +
                                qty + "," +
                                item.getMinRequired()
                );
                bw.newLine();
            }

        } catch (IOException e) {
            ErrorLogger.log("Error writing stock file", e);
        }
    }
}