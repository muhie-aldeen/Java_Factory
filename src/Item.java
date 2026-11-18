
public class Item {
    private int id;
    private String name;
    private String division;
    private double price;
    private int quantity;
    private int minRequired;
    private String category;

    public Item() {
    }

    public Item(int id, String name, String division, double price, int quantity, int minRequired) throws Exceptions.InvalidTaskDataException {
        if (id<0||id==0){
            throw new Exceptions.InvalidTaskDataException(" the ID musn't be a negative value ");
        }
        if (name==null||name.trim().isEmpty()){
            throw new Exceptions.InvalidTaskDataException(" the name should be inserted");
        }
        if (division==null||division.trim().isEmpty()){
            throw new Exceptions.InvalidTaskDataException(" pls insert the the division");
        }
        if (price<0){
            throw new Exceptions.InvalidTaskDataException(" the price musn't be neagtaiv or zero");
        }

        if (quantity<0){
            throw new Exceptions.InvalidTaskDataException("  the quantity musn't be neagtaiv or zero");
        }

        if (minRequired<0){
            throw new Exceptions.InvalidTaskDataException("the minRequired musn't be neagtaiv or zero ");
        }


        this.id = id;
        this.name = name;
        this.division = division;
        this.price = price;
        this.quantity =quantity;
        this.minRequired = minRequired;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) throws Exceptions.InvalidTaskDataException {
        if (id<0||id==0){
            throw new Exceptions.InvalidTaskDataException("  the ID musn't be a negative value");
        }
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name)throws Exceptions.InvalidTaskDataException {
        if (name==null||name.trim().isEmpty()){
            throw new Exceptions.InvalidTaskDataException("the name should be inserted");
        }
        this.name = name;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division)throws Exceptions.InvalidTaskDataException {
        if (division==null||division.trim().isEmpty()){
            throw new Exceptions.InvalidTaskDataException(" pls insert the the division");
        }
        this.division = division;
    }

    public int getQuantity() {

        return quantity;
    }

    public void setQuantity(int quantity) throws Exceptions.InvalidTaskDataException {

        if(quantity<0){
            throw new Exceptions.InvalidTaskDataException("the quantity can't be a negativ value!!!");
        }
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) throws Exceptions.InvalidTaskDataException {

        if(price<0){
            throw new Exceptions.InvalidTaskDataException(" the price can't be negative!!!!");
        }
        this.price = price;
    }

    public int getMinRequired() {
        return minRequired;
    }

    public void setMinRequired(int minRequired)throws Exceptions.InvalidTaskDataException {

        if(minRequired<0){
            throw new Exceptions.InvalidTaskDataException("the minmum quantity can't be a negative value!!");
        }
        this.minRequired = minRequired;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o==null||getClass()!=o.getClass())return false;
        Item item = (Item) o;
        return name!=null&&name.equalsIgnoreCase(item.name);

    }
    @Override
    public int hashCode(){
         return name!=null?name.toLowerCase().hashCode():0;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}