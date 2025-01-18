package transport;

public class Cargo {

    CargoType type;
    int weight;
    String description;

    public Cargo(CargoType type, String description, int weight) {
        this.type = type;
        this.weight = weight;
        this.description = description;
    }

    @Override
    public String toString() {
        String str;
        if (this.type == CargoType.SOLID) {
            str = "solid";
        } else {
            str = "liquid";
        }
        return "--- cargo: " + this.weight + " tons - Type:  " + str;
    }
}
