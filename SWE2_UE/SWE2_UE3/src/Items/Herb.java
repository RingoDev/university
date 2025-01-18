package Items;

public class Herb extends Resource {
    private final int power;
    private final int cooldown;

    public Herb(String name, int power, int cooldown) {
        super(name);
        this.power = power;
        this.cooldown = cooldown;
    }
    @Override
    public int getPower(){
        return this.power;
    }


    @Override
    public int getCooldown(){
        return this.cooldown;
    }


    public String toString() {
        return "Herb: " + super.toString();
    }

}
