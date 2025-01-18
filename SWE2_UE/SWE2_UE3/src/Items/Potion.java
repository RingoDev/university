package Items;

public class Potion extends Item {
    private final Item[] items;

    public Potion(String name, Item ...items){
        super(name);
        this.items= items;
    }

    @Override
    public float getPrice(){
        float sum = 0;
        for (Item item : this.items) {
            sum += item.getPrice();
        }
        return (float) (sum*1.2);
    }

    @Override
    public int getPower(){
        int sum = 0;
        for(Item item : this.items){
            sum+= item.getPower();
        }
        return sum*2;
    }

    @Override
    public int getCooldown(){
        int max = 0;
        for(Item item: this.items){
            if(item.getCooldown() > max){
                max = item.getCooldown();
            }
        }
        return max*2;
    }

    public String toString() {

        StringBuilder str = new StringBuilder();

        for (Item item : this.items) {
            str.append(item.toString()).append(", ");
        }
        //deletes last comma
        str.deleteCharAt(str.length() - 1);
        str.deleteCharAt(str.length() - 1);
        return "Potion: " + super.toString() + "[ " + str + " ]";
    }
}
