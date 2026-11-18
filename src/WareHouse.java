import java.util.HashMap;
import java.util.Map;

public class WareHouse {
    protected int minRequired;

    private Item item;
    private Map<Item,Integer> stock=new HashMap<>();
    private Map<Item,Integer>reserved=new HashMap<>();
    private Map<Item,Integer>maxCapacity=new HashMap<>();
    public WareHouse(){
        stock=new HashMap<>();
    };
    //--------------------------------------------------------------
    public WareHouse(Item item){
        this.item=item;
    }
    public synchronized void setMaxCapacity(Item item,int capacity) throws Exceptions.InvalidTaskDataException{//to set hte max capacity for all items
        if (capacity<0||item==null){//***********
            throw new Exceptions.InvalidTaskDataException("wrong data");
        }
        //  throw NullPointerException;
        maxCapacity.put(item , capacity);
        stock.putIfAbsent(item,0);
        reserved.putIfAbsent(item,0);//اذا العنصر غير موجود اضفه بقيمة صفر اذا موجود لا تفعل شيء
    }
    //--------------------------------------------------------------
    public synchronized void reserve(Item item,int quantity)throws Exceptions.InvalidTaskDataException{
        if (item==null||quantity<=0){
            throw new Exceptions.InvalidTaskDataException("wrong data insetring!");
        }
        if(!canReserve(item,quantity)){
            throw new Exceptions.InvalidTaskDataException("not enough stock to reserve");
        }
        reserved.put(item,reserved.getOrDefault(item,0)+quantity);
    }
    //--------------------------------------------------------------
    public synchronized boolean canReserve(Item item,int quantity){//يفحص هل المواد بكفو لحجز المواد ولا لا بدنا نستعمله بتابع run
        int available=stock.getOrDefault(item,0)-reserved.getOrDefault(item,0);//we used getOrDefault/// مشان اذا العنصر مش موجود ما يرجع //null// وينهار البرنامج
        return available>=quantity;
    }
    //--------------------------------------------------------------
    public synchronized void realiseReservation(Item item,int quantity)throws Exceptions.InvalidTaskDataException{//تحقيق الحجز للمواد يعني نحن اول شي فحصنا بعدين حجزنا المواد شلناهن من المستودع
        if(item==null||quantity<=0){
            throw new Exceptions.InvalidTaskDataException("wrong data inserting please chang the quantity and don't let any celles empty");
        }
        if (reserved.getOrDefault(item,0) < quantity){
            throw new Exceptions.InvalidTaskDataException("Not enough reserved items");
        }
        if (stock.getOrDefault(item,0) < quantity)
            throw new Exceptions.InvalidTaskDataException("Not enough stock to consume");
        reserved.put(item,reserved.get(item)-quantity);
    }
    //---------------------------------------------------------------
    public synchronized void consumeReserved(Item item, int quantity)throws Exceptions.InvalidTaskDataException{//تابع يدل عل ان المواد الان تم تصنيعها اي لا يمكن ارجاعها اصبحت منتجات
        if (item==null||quantity<0){
            throw new Exceptions.InvalidTaskDataException("wrong data inserting please chang the quantity and don't let any celles empty");
        }
        stock.put(item,stock.get(item)-quantity);

        reserved.put(item,reserved.get(item)-quantity);

        checkMinimum(item);

        notifyAll();
    }
    //------------------------------------------------------------------
    public synchronized void checkMinimum(Item item){//تحقق من الحد الادنى
        int current=stock.getOrDefault(item,0);
        if (current<item.getMinRequired()){
            System.out.println("Low in stock !!!"+item.getName()+"remaining"+current);
        }
    }
    //-----------------------------------------------------------------

    public synchronized void addItem(Item item,int quantity)throws InterruptedException,Exceptions.InvalidTaskDataException{//اضافة عالمستودع
        if(item==null||quantity<=0){
            throw new Exceptions.InvalidTaskDataException("wrong data inserting please chang the quantity and don't let any celles empty");
        }
        if(!maxCapacity.containsKey(item)){
            throw new Exceptions.InvalidTaskDataException("the item capacity is not excicted");
        }
        int current =stock.getOrDefault(item, 0);//تحسينات بسيطة مشان اخطاء التشغيل
        int capacity=maxCapacity.get(item);
        while (current+quantity >capacity){//اذا الاضافة رح تتعدى السعة استنى
            wait();
            current =stock.getOrDefault(item, 0);//تحديث بعد الانتظار
        }
        stock.put(item,current+quantity);//add the quantity
        System.out.println("<"+stock.get(item)+">");
        notifyAll();
    }
    //------------------------------------------------------------------

    public synchronized int removeItem(Item item,int quantity) throws InterruptedException,Exceptions.InvalidTaskDataException{//حذف من االمستودع الكمية يلي بدي ياه
        if (item==null||quantity<=0){
            throw new Exceptions.InvalidTaskDataException("wrong data inserting please chang the quantity and don't let any celles empty");
        }
        while (stock.getOrDefault(item,0)<quantity){//تعديل الاسم
            System.out.println("waiting.....");
            wait();
        }
        stock.put(item,stock.get(item)-quantity);
        System.out.println(+stock.get(item));
        checkMinimum(item);
        notifyAll();
        return quantity;
    }
    //-----------------------------------------------------------------
    public synchronized boolean hasEnough(Item item,int quantity){
        return stock.getOrDefault(item,0)>=quantity;

    }

    public Map<Item, Integer> getMaxCapacity() {
        return maxCapacity;
    }


    public Map<Item, Integer> getStock() {
        return stock;
    }

    public void updateItem(Item itemToUpdate) {

    }
}
