package server;

import shared.OrderServer;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RunServer {

    public static void main(final String[] args) throws RemoteException, AlreadyBoundException {
        final OrderServer server = new ServerImpl();
        final Registry registry = LocateRegistry.createRegistry(1099);
        registry.bind("OrderServer", server);
        System.out.println("Server started");
    }
}
