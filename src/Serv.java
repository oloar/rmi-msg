import java.util.ArrayList;
import java.rmi.*;

public interface Serv extends Remote {
	public int clientRegister(Client c) throws RemoteException;
	public void clientLeave(Client c) throws RemoteException;

	public void sendMsgToServ(Message m) throws RemoteException;

	public ArrayList<Message> getHistory(Client c) throws RemoteException;

	public String printRooms() throws RemoteException;
	public int newRoom() throws RemoteException;
	public void changeRoom(Client c, int newRoomId) throws RemoteException;

	public int getRoomOfClient(Client c) throws RemoteException;
	public ArrayList<Room> getRooms() throws RemoteException;

	public boolean existingId(int id) throws RemoteException;
}
