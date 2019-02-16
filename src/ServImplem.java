import java.io.*;
import java.rmi.*;
import java.rmi.registry.*;
import java.util.ArrayList;
import java.util.Objects;

public class ServImplem implements Serv {
	private int id;
	private ArrayList<Room> rooms;

	public ServImplem() {
		id = 0;
		rooms = new ArrayList<Room>();
		rooms.add(new Room(0));
	}

	/////////////////////////////////////////////////////////////////////////////////////
	// CONNECTIONS
	/////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Register a client to the server
	 * @param c : client reference
	 * @return : client id strictly greater than 0
	 */
	public int clientRegister(Client c) throws RemoteException {
		int cId;

		id++; // min id = 1
		cId = id;
		Objects.requireNonNull(getRoomById(0)).joinRoom(c);
		sendMsgToServ(new Message("Server", 0, 0, c.getPseudo() + " has joined."));
		return cId;
	}

	/**
	 * Client leaves a room
	 * @param c : Client
	 */
	public void clientLeave(Client c) throws RemoteException {
		for(Room r : getRooms())
			if(r.isInTheRoom(c)) {
				r.leaveRoom(c);
				sendMsgToRoom(new Message("Server", 0, r.getId(), c.getPseudo() + " has left the channel."), r);
			}
	}

	/////////////////////////////////////////////////////////////////////////////////////
	// MESSAGING
	/////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Send message to server
	 * @param m : message to send
	 */
	public void sendMsgToServ(Message m) throws RemoteException {
		System.out.println(m.sender() + " says " + m.text() + " in " + m.roomId());

		sendMsgToRoom(m, Objects.requireNonNull(getRoomById(m.roomId())));
		writeToHistory(m);
	}


	/**
	 * Send message to everyone in the room
	 * @param m : message to send
	 * @param room : room to send it to
	 */
	private void sendMsgToRoom(Message m, Room room){
		for(Client c : room.getClients()){
			try{
				if (c.getId() != m.senderId()) {
					c.recvMsg(m);
				}
			}catch(RemoteException e) {
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

	/////////////////////////////////////////////////////////////////////////////////////
	// HISTORY
	/////////////////////////////////////////////////////////////////////////////////////
	private void writeToHistory(Message m) {
		if (m.senderId() != 0) { // Only log clients message
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

	/**
	 * Construct an arraylist of messages from a file
	 * @return The history in the form a an arraylist of message
	 */
	public ArrayList<Message> getHistory(Client c) throws RemoteException {
		ArrayList<Message> history = new ArrayList<>();
		BufferedReader br;
		String line;
		Message m;
		int roomId = getRoomOfClient(c);

		try {
			br = new BufferedReader(new FileReader("history.txt"));
			line = br.readLine();
			while(line != null) {
				m = Message.fromCSV(line);

				if (m.roomId() == roomId)
					history.add(m);
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

	/////////////////////////////////////////////////////////////////////////////////////
	// ROOMS
	/////////////////////////////////////////////////////////////////////////////////////

	/**
	 * Diplay rooms information
	 */
	public String printRooms() throws RemoteException{
		StringBuilder res = new StringBuilder();
		for(Room r : this.rooms){
			res.append("Room ").append(r.getId()).append(" has ").append(r.getNumberClients()).append(" clients connected.\n");
		}
		return res.toString();
	}

	/**
	 * Change a client from one room to another
	 * @param c : client
	 * @param newRoomId : the room to switch to
	 */
	public void changeRoom(Client c, int newRoomId) throws RemoteException{
		Room oldRoom, newRoom;
		if(existingId(newRoomId)){
			oldRoom = Objects.requireNonNull(getRoomById(getRoomOfClient(c)));
			oldRoom.leaveRoom(c);
			sendMsgToRoom(new Message("Server", 0, oldRoom.getId(), c.getPseudo() + " has left the channel."), oldRoom);
			newRoom = Objects.requireNonNull(getRoomById(newRoomId));
			newRoom.joinRoom(c);
			sendMsgToRoom(new Message("Server", 0, newRoom.getId(), c.getPseudo() + " has join the channel."), newRoom);
		}
	}

	/**
	 * Get a room by its id
	 * @param id : room's id
	 * @return the room
	 */
	private Room getRoomById(int id){
		for(Room r : this.rooms){
			if(r.getId() == id)
				return r;
		}
		return null;
	}

	/**
	 * Initialize a new room and add it to the list
	 */
	public int newRoom() throws RemoteException {
		int id = this.rooms.size();
		this.rooms.add(new Room(id));
		return id;
	}

	/**
	 * Get the rooms list
	 * @return ArrayList of Room
	 */
	public ArrayList<Room> getRooms() throws RemoteException{
		return this.rooms;
	}

	/**
	 * Check if an id exists
	 * @param id : id to check
	 * @return true if the id exist, false otherwise
	 */
	public boolean existingId(int id) throws RemoteException{
		for(Room r : getRooms())
			if(r.getId() == id)
				return true;
		return false;
	}

	/**
	 * Get client's room's id
	 * @param c : client
	 * @return id of the room the client is in.
	 */
	public int getRoomOfClient(Client c) throws RemoteException{
		for(Room r : this.rooms){
			if(r.isInTheRoom(c)){
				return r.getId();
			}
		}
		return -1;
	}
}
