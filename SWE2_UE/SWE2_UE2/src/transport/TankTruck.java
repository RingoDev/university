package transport;

public class TankTruck extends Truck {

    public TankTruck(String name, int costPerKM, int maxLoad, Location location) {
        super(name, costPerKM, maxLoad, location);
    }

    @Override
    public void load(Cargo cargo) throws InvalidCargoException, OverloadedException {
        if (cargo.type == CargoType.SOLID) {
            throw new InvalidCargoException("Tanktruck can't transport solid cargo");
        } else super.load(cargo);
    }
}

