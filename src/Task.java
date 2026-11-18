import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Task {
    private int orderId;
    private Product product;
    private int quantity;
    private LocalDateTime orderDate;
    private LocalDateTime orderfinish;
    private Map<Item,Integer> takenItems;
    private volatile boolean finished;
    private volatile boolean cancelled;
    private int producedCount;//عدد القطع المنتجة
    private int quntity;


    public Task(int orderId,Product product,int quantity) throws Exceptions.InvalidTaskDataException{//كل ما نستدعي كونستركتر
        if (orderId<=0){
            throw new Exceptions.InvalidTaskDataException("the ID of the task musn't be negative value!!");
        }
        if(product ==null){
            throw new Exceptions.InvalidTaskDataException("the product musn't be null!! ");
        }
        if (quantity<=0){
            throw  new Exceptions.InvalidTaskDataException("the quantity can't be negative!!!");
        }
        this.orderId=orderId;
        this.product=product;
        this.quantity=quantity;
        this.orderDate=LocalDateTime.now();
        this.takenItems=new HashMap<>();
        this.finished=false;
        this.cancelled=false;
        this.producedCount=0;
    }

    public synchronized void increaseProduced(){
        if (!finished&&!cancelled){// ضفت شرط مشان ما يضل يزيد
            ++producedCount;
            printProgress();
        }
    }
    public void cancel(){
        cancelled=true;
    }
    public void finish(){
        finished=true;
    }
    public synchronized int getProgressPercent(){
        return (producedCount*100)/quantity;
    }
    public void printProgress(){
        System.out.println("progress : "+producedCount+"/"+quantity+getProgressPercent()+"%");
    }

    public int getOrderId() {
        return orderId;
    }

    public Product getProduct() {
        return product;
    }
    public int getProducedCount(){
        return producedCount;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public Map<Item, Integer> getTakenItems() {
        return takenItems;
    }
    public LocalDateTime getOrderfinish(){
        return orderfinish;
    }

    public boolean isFinished() {
        return finished;
    }
    public boolean isCancelled(){
        return cancelled;
    }
    public void finishTask(){
        finished=true;
        orderfinish=LocalDateTime.now();
        takenItems.clear();
    }

    public int getQuntity() {
        return quntity;
    }
}