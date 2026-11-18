
public class Producer implements Runnable {
    private WareHouse wareHouse;
    int amount;
    int interval;
    private Item item;
    Producer(WareHouse wareHouse,Item item ,int amount,int interval)throws Exceptions.InvalidTaskDataException{//interval يعني المستخدم هو بيحدد الكمية مع الوقت المطلوب لتصنيعها
        if(interval==0||interval<0){
            throw new Exceptions.InvalidTaskDataException("the interval must be positive");
        }
        if(amount==0||amount<0){
            throw new Exceptions.InvalidTaskDataException("the amountl must be positive");
        }
        if (item ==null){
            throw new Exceptions.InvalidTaskDataException("item musn't be null");
        }
        this.amount=amount;
        this.wareHouse=wareHouse;
        this.interval=interval;
        this.item=item;
    }
    public void run() {
        while (true) {
            try {
                Thread.sleep(interval);
                wareHouse.addItem(item, amount);
                System.out.println(item.getName()+" produced "+amount);


            }catch (InterruptedException | Exceptions.InvalidTaskDataException e){
                System.out.println(e.getMessage());
                Thread.currentThread().interrupt();;
            }

        }
    }
}