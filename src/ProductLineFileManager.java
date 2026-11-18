import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProductLineFileManager {

    private static final String FILENAME = "product_lines.csv";

    // ====== LOAD (Input) ======
    public static List<ProductLine> loadProductLines(FactoryManger manger,String FILENAME) {
        List<ProductLine> productLines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    String[] data = line.split(",");

                    // تحويل البيانات إلى كائن ProductLine
                    int numberLine = Integer.parseInt(data[0]);
                    String nameLine = data[1];
                    String status = data[2];

                    ProductLine productLine = new ProductLine(numberLine, nameLine);
                    productLines.add(productLine);

                } catch (Exception e) {
                    ErrorLogger.log("Invalid line in product lines file: " + line, e);
                }
            }
        } catch (IOException e) {
            ErrorLogger.log("Error reading product lines file", e);
        }

        return productLines;
    }

    // ====== SAVE (Output) ======
    public static void saveProductLines(List<ProductLine> productLines, String s) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILENAME))) {
            for (ProductLine line : productLines) {
                bw.write(line.getNumberLine() + "," +
                        line.getNameLine() + "," +
                        line.getStatus());
                bw.newLine();
            }
        } catch (IOException e) {
            ErrorLogger.log("Error writing product lines file", e);
        }
    }
}