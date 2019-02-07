import java.rmi.*;

// ICI que les méthodes partagées
public interface Client extends Remote{
    public void recvMsg(Message message) throws RemoteException;
    public String getPseudo() throws RemoteException;
    public int getId() throws RemoteException;
}
