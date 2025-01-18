package transport;

public class CargoPlane extends Transporter {

    private final int constCost;

    public CargoPlane(String name, int costPerKM, int maxLoad, Location location) {
        super(name, costPerKM, maxLoad, location);
        this.constCost = 10000;
    }

    @Override
    public double goTo(Location location) {
        double x = Math.round(constCost + super.costPerKM * super.location.getDistance(location) * 100) / 100.0;
        super.location = location;
        return x;
    }

    @Override
    public void load(Cargo cargo) throws InvalidCargoException, OverloadedException {
        if (cargo.type == CargoType.LIQUID) {
            throw new InvalidCargoException("Plane can't transport liquid cargo");
        } else {
            super.load(cargo);
        }
    }

    @Override
    public String toString() {
        return super.toString() + " --- the constant cost for flying is: " + this.constCost + "€";
    }
}
