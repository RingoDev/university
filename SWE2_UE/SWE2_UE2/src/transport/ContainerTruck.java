package transport;

public class ContainerTruck extends Truck {
    public ContainerTruck(String name, int costPerKM, int maxLoad, Location location) {
        super(name, costPerKM, maxLoad, location);
    }

    @Override
    public void load(Cargo cargo) throws InvalidCargoException, OverloadedException {
        if (cargo.type == CargoType.LIQUID) {
            throw new InvalidCargoException("ContainerTruck can't transport liquid cargo");
        } else {
            super.load(cargo);
        }
    }
}

