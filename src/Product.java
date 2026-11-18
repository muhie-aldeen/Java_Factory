import java.util.HashMap;
import java.util.Map;

public class Product {
    private int id;
    private String name;
    private Map<Item,Integer> recipe=new HashMap<>();


    public Product(int id, String name) throws Exceptions.InvalidTaskDataException {
        if (id<=0){
            throw new Exceptions.InvalidTaskDataException(" the ID of the product musn't be negativ");
        }
        if (name==null||name.trim().isEmpty()){
            throw new Exceptions.InvalidTaskDataException("product name shouldn't be empty");
        }
        this.id = id;
        this.name = name;
        recipe=new HashMap<>();
    }

    public Product(String name) {
        this.name=name;
    }

    public int getId() {
        return id;
    }
    public void setId(int id)  throws Exceptions.InvalidTaskDataException{
        if (id<0){
            throw new Exceptions.InvalidTaskDataException("the id musn't be a negative value");
        }
        this.id = id;
    }


    public Map<Item, Integer> getRecipe() {
        return recipe;
    }
    public String getName() {
        return name;
    }

    public void setName(String name)throws Exceptions.InvalidTaskDataException {
        if (name==null||name.trim().isEmpty()){
            throw new Exceptions.InvalidTaskDataException("the name musn't be empty");
        }
        this.name = name;
    }

    public void addIngredient(Item element,int amount) throws Exceptions.InvalidTaskDataException{// throws InterruptedException
        if(element ==null){
            throw new Exceptions.InvalidTaskDataException("the element musn't not be null!! ");
        }
        if(amount<=0){
            throw new Exceptions.InvalidTaskDataException("the quantity can't be negative!!");
        }
        recipe.put(element,amount);

    }



    public double calculateCost(){
        double cost=0;
        for(Map.Entry<Item,Integer>entry:recipe.entrySet()){
            Item item = entry.getKey();
            int quantity=entry.getValue();
            cost= cost+(item.getPrice()*quantity);
        }
        return cost;
    }

    @Override
    public String toString() {
        return "Product{" + "idp=" + id + ", namep=" + name+ ", recipe=" + recipe + '}';
    }

}
