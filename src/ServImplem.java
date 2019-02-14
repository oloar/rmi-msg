import java.io.*;
import java.rmi.*;
import java.rmi.registry.*;
import java.util.ArrayList;

public class ServImplem implements Serv {
	private int id;
	private ArrayList<Client> clients; // HashMap ?

	public ServImplem() {
		id = 0;
		clients = new ArrayList<>();
	}

	/**
	 * Register a client to the server
	 * @param c : client reference
	 * @return : client id strictly greater than 0
	 */
	public int clientRegister(Client c) throws RemoteException {
		int cId;

		id++; // min id = 1
		cId = id;
		clients.add(c);
		return cId;
	}

	/**
	 * Send message to server
	 * @param m : message to send
	 * @throws RemoteException
	 */
	public void sendMsgToServ(Message m) throws RemoteException {
		sendToClients(m);
		writeToHistory(m);
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
		} catch (IOException e) {
			System.err.println("Err: Could not write history to file.");
		}
	}
}

