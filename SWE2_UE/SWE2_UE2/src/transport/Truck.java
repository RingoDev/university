package transport;

public abstract class Truck extends Transporter{
    public Truck(String name, double costPerKM, int maxLoad, Location location) {
        super(name, costPerKM, maxLoad, location);
    }

    @Override
    public double goTo(Location location) throws UnreachableLocationException {
        if (this.location.reachableOverland(location)) {
            double x = Math.round(super.costPerKM * super.location.getDistance(location) * 100) / 100.0;
            super.location = location;
            return x;
        } else {
            throw new UnreachableLocationException("Trucks can't swim");
        }
    }
}
