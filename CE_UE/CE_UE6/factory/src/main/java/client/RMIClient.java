package client;
import shared.OrderServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {

        private OrderServer server;

        public void startClient(String host) throws RemoteException, NotBoundException {
            //connection to server
            final Registry registry = LocateRegistry.getRegistry(host, 1099);
            //search server
            server = (OrderServer) registry.lookup("OrderServer");
        }

        public OrderServer getServer() {
            return server;
        }
}
