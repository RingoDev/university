package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface OrderServer extends Remote{
    public void sendOrder(Order o) throws RemoteException;
}
