package transport;

public abstract class Transporter {

    public int maxLoad;
    String name;
    double costPerKM;
    Location location;
    Cargo cargo;

    public Transporter(String name, double costPerKM, int maxLoad, Location location) {
        super();
        this.maxLoad = maxLoad;
        this.name = name;
        this.costPerKM = costPerKM;
        this.location = location;
    }

    public double goTo(Location location) throws UnreachableLocationException {
        double x = Math.round(this.costPerKM * this.location.getDistance(location) * 100) / 100.0;
        this.location = location;
        return x;
    }

    public void load(Cargo cargo) throws OverloadedException, InvalidCargoException {//throws OverloadedException if cargo is heavier than maxLoad
        if (this.cargo == null){
            if(cargo.weight <= this.maxLoad){
                this.cargo = cargo;
            }
            else throw new OverloadedException("Cargo is too heavy");
        }
        else throw new OverloadedException("Transporter is already loaded");
    }

    public Cargo unload() {
        Cargo x = this.cargo;
        this.cargo = null;
        return x;
    }


    @Override
    public String toString() {
        String str = name + " with capacity of " + maxLoad + " and cost of " + costPerKM + "€ per km is located at " + location;
        if (cargo == null) {
            return str + " --- cargo: none";
        } else {
            return str + " " + cargo;
        }
    }
}
