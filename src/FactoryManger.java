
import java.util.ArrayList;
import java.util.List;

public class FactoryManger {
    private WareHouse wareHouse;
    private List<ProductLine> lines = new ArrayList<>();

    public FactoryManger() {
    }

    public FactoryManger(WareHouse wareHouse) {
        this.wareHouse = wareHouse;
    }

    public void addLines(ProductLine line) throws Exceptions.InvalidTaskDataException {
        if (line == null) {
            throw new Exceptions.InvalidTaskDataException("the line cell musn't be empty");
        }
        lines.add(line);
        new Thread(line).start();
        System.out.println("Line " + line.getNumberLine() + " has been added and started.");
    }

    public void stopLineByNumber(int lineNumber) throws Exceptions.LineNotFoundException {
        for (ProductLine line : lines) {
            if (line.getNumberLine() == lineNumber) {
                line.setStatus(LineStatus.STOPPED);
                System.out.println("line" + lineNumber + "stopped");
                return;
            }
        }
        throw new Exceptions.LineNotFoundException("the line not Found");

    }//

    // حالة الصيانة نفسها بس string و فينا نساويها بالاسم
    public void maintenanceLine(int lineNumber) throws Exceptions.LineNotFoundException {
        for (ProductLine line : lines) {
            if (line.getNumberLine() == lineNumber) {
                line.setStatus(LineStatus.MAINTENANCE);
                return;
            }
        }
        throw new Exceptions.LineNotFoundException("line not fiund");
    }

    public void startLine(int LineNumber) throws Exceptions.LineNotFoundException {
        for (ProductLine line : lines) {
            if (line.getNumberLine() == LineNumber) {
                line.setStatus(LineStatus.ACTIVE);
                return;
            }
        }
        throw new Exceptions.LineNotFoundException("lin not found");
    }

    public List<ProductLine> getLines() {
        return lines;
    }

    public WareHouse getWareHouse() {
        return wareHouse;
    }

    public void setWareHouse(WareHouse wareHouse) {
        this.wareHouse = wareHouse;
    }
}