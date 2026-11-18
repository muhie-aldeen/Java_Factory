import java.util.*;

public class ProductLine implements Runnable {
    private String nameLine;
    private int numberLine;
    private int quntity;
private Product product;
private FactoryManger manger;
    private WareHouse wareHouse;
    private Map<Item, Integer> recipe;
    private Item output;
    private int time;
    private Queue<Task> taskqueue = new LinkedList<>();
    private Item item;
    private LineStatus status;


    ProductLine(int numberLine, String nameLine, Item item, WareHouse wareHouse, Task t, Map<Item, Integer> recipe, Item outItem, int time,int quntity) {
        this.numberLine = numberLine;
        this.nameLine = nameLine;
        this.quntity=quntity;
        this.item = this.item;
        this.wareHouse = wareHouse;
        this.taskqueue = new LinkedList<>();
        this.recipe = new HashMap<>();
        this.output = output;
        this.time = this.time;
        this.status = LineStatus.ACTIVE;
        new Thread(this).start();
    }

    public ProductLine(int numberLine, String nameLine) {
        this.nameLine=nameLine;
        this.numberLine=numberLine;
        this.time=1000;
        this.status=LineStatus.ACTIVE;
    }


    public boolean startTask(Task task) throws Exceptions.InvalidTaskDataException {
        Product product = task.getProduct();
        Map<Item, Integer> tempReserved = new HashMap<>();//ةشح للحجز المؤقت للموادmap

        synchronized (wareHouse) {
            for (Map.Entry<Item, Integer> entry : product.getRecipe().entrySet()) {
                Item item = entry.getKey();//معرفة هل المواد كافية لتحقيق لبطلبية او لا
                int neededForOne = entry.getValue();
//فائدة التابع هو اختبار هل المواد كافية للحجز ام لا و اذا كفية حجز المواد و تسجيل المواد داخل ماب مؤقتة
                int totalNeeded = neededForOne * task.getQuantity();
                //مازال يحتاج شرط انه هل هو كافي اذا ليس كافي
                if (!wareHouse.canReserve(item, totalNeeded)) {
                    for (Map.Entry<Item, Integer> r : tempReserved.entrySet()) {//هنا في حال كانت احد المواد غير كافية بعد ما تم حجز بعض المواد نريد ان نقول ان المواد غير كافية فنعيد باقي المكونات الى اماكنها
                        wareHouse.realiseReservation(r.getKey(), r.getValue());
                    }
                    return false;
                }
                wareHouse.reserve(item, totalNeeded);
                tempReserved.put(item, totalNeeded);
            }
            task.getTakenItems().putAll(tempReserved);//هاد السطر يجب عرضه في file
            taskqueue.add(task);
            notifyAll();
            return true;
        }
    }
    @Override
    public void run() {
        while (true) {
            Task currentTask = null;

            synchronized (this) {
                while (taskqueue.isEmpty()) {
                    try {
                        wait();  // انتظر لحد ما يجي مهمة جديدة
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
                currentTask = taskqueue.poll();  // خذ أول مهمة وأزلها من الطابور
            }

            if (currentTask == null) continue;

            // نفذ المهمة
            try {
                Product product = currentTask.getProduct();

                for (int i = 0; i < currentTask.getQuantity(); i++) {
                    if (currentTask.isCancelled()) {
                        break;
                    }

                    // استهلاك المواد لكل قطعة
                    for (Map.Entry<Item, Integer> entry : product.getRecipe().entrySet()) {
                        wareHouse.consumeReserved(entry.getKey(), entry.getValue());
                    }

                    Thread.sleep(time);
                    wareHouse.addItem(output, 1);
                    currentTask.increaseProduced();
                }

                if (!currentTask.isCancelled()) {
                    currentTask.finishTask();
                }

            } catch (InterruptedException | Exceptions.InvalidTaskDataException e) {
                e.printStackTrace();
            } finally {
                // إرجاع المواد غير المستخدمة
                synchronized (wareHouse) {
                    for (Map.Entry<Item, Integer> entry : currentTask.getTakenItems().entrySet()) {
                        Item mat = entry.getKey();
                        int reserved = entry.getValue();
                        int used =product.getRecipe().get(mat) * currentTask.getProducedCount();
                        int unused = reserved - used;
                        if (unused > 0) {
                            try {
                                wareHouse.realiseReservation(mat, unused);
                            } catch (Exceptions.InvalidTaskDataException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
                currentTask.getTakenItems().clear();
            }
        }
    }

    public void setRecipe(Map<Item, Integer> recipe) {
        this.recipe = recipe;
    }

    public void setOutput(Item output) {
        this.output = output;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setWareHouse(WareHouse wareHouse) {
        this.wareHouse = wareHouse;
    }

    public synchronized void setStatus(LineStatus status) {
        this.status = status;

    }


    public Queue<Task> getTaskqueue() {
        return taskqueue;
    }

    public void setTaskqueue(Queue<Task> taskqueue) {
        this.taskqueue = taskqueue;
    }

    @Override
    public String toString() {
        return "ProductLine{" +
                "numberLine=" + numberLine +
                ", nameLine='" + nameLine + '\'' +
                '}';
    }

    public synchronized LineStatus getStatus() {
        return status;
    }

    public int getNumberLine() {
        return numberLine;
    }

    public int getQuntity() {
        return quntity;
    }

    public String getNameLine() {
        return nameLine;
    }
}






