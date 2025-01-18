package client;

import shared.Order;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class RunClient {

    public static void sendOrderToFibu(Order o,String host) throws RemoteException {
        final RMIClient client = new RMIClient();
        try {
            client.startClient(host);
        } catch (NotBoundException e) {
            e.printStackTrace();
        }


        try {
            client.getServer().sendOrder(o);
        } catch (final Exception e) {
            System.out.println("Error > " + e.getMessage());
        }
    }
}
