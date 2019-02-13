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
		// TODO : writeToHistory(m);
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


}

