import java.io.*;
import java.rmi.*;
import java.rmi.registry.*;
import java.util.ArrayList;

public class ServImplem implements Serv {
	private int id;
	private ArrayList<Room> rooms;

	public ServImplem() {
		id = 0;
		rooms = new ArrayList<Room>();
		rooms.add(new Room(0));
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
		rooms.get(0).joinRoom(c);
		sendMsgToServ(new Message("Server", 0, 0, c.getPseudo() + " has joined."));
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

	public int getRoomOfClient(Client c) throws RemoteException{
		for(Room r : this.rooms){
			if(r.isInTheRoom(c)){
				return r.getId();
			}
		}
		return -1;
	}

	/**
	 * Send message to server
	 * @param m : message to send
	 * @throws RemoteException
	 */
	public void sendMsgToServ(Message m) throws RemoteException {
		System.out.println(m.sender() + " says " + m.text() + " in " + m.roomId());

		sendMsgToRoom(m, this.rooms.get(m.roomId()));
		writeToHistory(m);
	}

	public void sendMsgToRoom(Message m, Room room){
		for(Client c : room.getClients()){
			try{
				if (c.getId() != m.senderId()) {
					c.recvMsg(m);
				}
			}catch(RemoteException e){
				System.err.println("[E]: Could not send to client.");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Send message to all connected clients
	 * @param m : message to send
	 */
	private void broadcast(Message m) {
		for (Room r : rooms){
			for (Client c : r.getClients())
				try {
					if (c.getId() != m.senderId()) {
						c.recvMsg(m);
					}
				} catch (RemoteException e) {
					System.err.println("[E]: Could not send to client");
					e.printStackTrace();
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
		if (m.roomId() != 0) { // Only log clients message
			try {
				BufferedWriter br = new BufferedWriter(new FileWriter("history.txt", true));
				br.write(m.toCSV());
				br.close();
			} catch (IOException e) {
				System.err.println("Err: Could not write history to file.");
				System.err.println(e.getMessage());
			}
		}
	}

	public void clientLeave(Client c) throws RemoteException {
		System.out.println("Removing clients.");
		for(Room r : getRooms())
			if(r.isInTheRoom(c))
				r.leaveRoom(c);
	}
}
