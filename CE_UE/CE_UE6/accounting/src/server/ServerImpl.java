package server;

import shared.Order;
import shared.OrderServer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ServerImpl implements OrderServer {

    private long orderId;
    private String handlebarType;
    private String handlebarMaterial;
    private String handlebarGearshift;
    private String handleMaterial;

    public ServerImpl() throws RemoteException {
        UnicastRemoteObject.exportObject(this, 0);
    }

    @Override
    public void sendOrder(Order o) throws RemoteException {

        orderId = o.getOrderId();
        handlebarType = o.getHandlebarType();
        handlebarMaterial = o.getHandlebarMaterial();
        handlebarGearshift = o.getHandlebarGearshift();
        handleMaterial = o.getHandleMaterial();

        String s = "Bestellung " + orderId + ": "
                    + handlebarType + ", "
                    + handlebarMaterial + ", "
                    + handlebarGearshift + ", "
                    + handleMaterial;

        try {
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("fibu/orders.txt", true)));
            writer.println(s);
            writer.flush();
            writer.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
