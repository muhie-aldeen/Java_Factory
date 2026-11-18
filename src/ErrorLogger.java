
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;



public class ErrorLogger {

    private static final String ERROR_FILE = "error.txt";

    public static void log(String message, Exception e) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ERROR_FILE, true))) {
            bw.write(LocalDateTime.now() + " - " + message);
            bw.newLine();
            bw.write("Exception: " + e.getMessage());
            bw.newLine();
            bw.write("-----------------------------------");
            bw.newLine();
        } catch (IOException ioException) {
            System.out.println("Failed to write error log.");
        }
    }
}