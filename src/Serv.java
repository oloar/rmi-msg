import java.util.ArrayList;
import java.rmi.*;

public interface Serv extends Remote {
	public int clientRegister(Client c, int roomId) throws RemoteException;

	public void sendMsgToServ(Message m) throws RemoteException;

	public Room getRoomOfClient(Client c) throws RemoteException;
	public ArrayList<Room> getRooms() throws RemoteException;
	public boolean existingId(int id) throws RemoteException;

	public ArrayList<Message> getHistory() throws RemoteException;

	public void clientLeave(Client c) throws RemoteException;
}
