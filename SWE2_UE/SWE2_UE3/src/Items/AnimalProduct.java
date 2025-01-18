package Items;

public class AnimalProduct extends Resource{

    public AnimalProduct(String name) {
        super(name);
    }
    @Override
    public int getPower(){
        return name.length()*2;
    }


    @Override
    public int getCooldown(){
        return 10;
    }

    public String toString(){
        return "AnimalProduct: " + super.toString();
    }

}
