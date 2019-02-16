import java.io.*;
import java.rmi.*;
import java.rmi.registry.*;
import java.util.ArrayList;

public class ServImplem implements Serv {
	private int id;
	private ArrayList<Client> clients; // HashMap ?
	private ArrayList<Room> rooms;

	public ServImplem() {
		id = 0;
		clients = new ArrayList<>();
		rooms = new ArrayList<Room>();
	}

	/**
	 * Register a client to the server
	 * @param c : client reference
	 * @return : client id strictly greater than 0
	 */
	public int clientRegister(Client c, int roomId) throws RemoteException {
		int cId;

		id++; // min id = 1
		cId = id;
		clients.add(c);
		for(Room r : this.getRooms()){
			if(r.getId() == roomId)
				r.joinRoom(c);
				break;
		}
		return cId;
	}

	public ArrayList<Room> getRooms() throws RemoteException{
		return this.rooms;
	}

	public boolean existingId(int id) throws RemoteException{
		for(Room r : getRooms())
			if(r.getId() == id)
				return true;
		return false;
	}

	public Room getRoomOfClient(Client c) throws RemoteException{
		for(Room r : this.rooms){
			if(r.isInTheRoom(c)){
				return r;
			}
		}
		return null;
	}

	/**
	 * Send message to server
	 * @param m : message to send
	 * @throws RemoteException
	 */
	public void sendMsgToServ(Message m) throws RemoteException {
		System.out.println(m.sender() + " says " + m.text());
		sendToClients(m);
		writeToHistory(m);
	}

	public void sendMsgToRoom(Message m, Room room){
		for(Client c : room.getClients()){
			try{
				if (c.getId() != m.senderId()) {
					c.recvMsg(m);
				}
			}catch(RemoteException e){
				System.out.println("[E]: Could not send to client");
			}
		}
	}

	/**
	 * Send message to all connected clients
	 * @param m : message to send
	 */
	private void sendToClients(Message m) {
		for (Client c : clients) {
			try {
			if (c.getId() != m.senderId()) {
				c.recvMsg(m);
			}
			} catch (RemoteException e) {
				System.err.println("[E]: Could not send to client");
			}
		}

	}

	/**
	 * Construct an arraylist of messages from a file
	 * @return The history in the form a an arraylist of message
	 * @throws RemoteException
	 */
	public ArrayList<Message> getHistory() throws RemoteException {
		ArrayList<Message> history = new ArrayList<>();
		BufferedReader br;
		String line;
		try {
			br = new BufferedReader(new FileReader("history.txt"));
			line = br.readLine();
			while(line != null) {
				history.add(Message.fromCSV(line));
				line = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			System.err.println("Err: History file not found.");
		} catch (IOException e) {
			System.err.println("Err: Could not load history.");
			System.err.println(e.getMessage());
		}
		return history;
	}

	private void writeToHistory(Message m) {
		try {
			BufferedWriter br = new BufferedWriter(new FileWriter("history.txt", true));
			br.write(m.toCSV());
			br.close();
		} catch (IOException e) {
			System.err.println("Err: Could not write history to file.");
		}
	}

	public void clientLeave(Client c) throws RemoteException {
		System.out.println("Removing clients."); // TODO : Send disconnection msg to clients
		clients.remove(c);
		for(Room r : getRooms())
			if(r.isInTheRoom(c))
				r.leaveRoom(c);
	}
}
