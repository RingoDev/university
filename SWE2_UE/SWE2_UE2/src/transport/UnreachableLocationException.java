package transport;

public class UnreachableLocationException extends TransportException {
    public UnreachableLocationException(String msg){
        super(msg);
    }
}
