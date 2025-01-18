package Items;

public abstract class Resource extends Item {
    public Resource(String name) {
        super(name);
    }
    @Override
    public float getPrice(){
        return this.name.length();
    }
}
