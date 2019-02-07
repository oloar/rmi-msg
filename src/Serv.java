import java.rmi.*;

public interface Serv extends Remote {
	public int clientRegister(Client c) throws RemoteException;

	public void sendMsgToServ(Message m) throws RemoteException;
}

