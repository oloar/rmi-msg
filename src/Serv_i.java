import java.rmi.*;
import java.rmi.registry.*;
import java.util.ArrayList;

public class Serv_i implements Serv {
	private int id;
	private Registry reg;
	private ArrayList<Integer> clients;

	public Serv_i(Registry reg) {
		id = 0;
		this.reg = reg;
		clients = new ArrayList<>();
	}

	public int clientRegister() throws RemoteException {
		int cId;

		id++; // min id = 1
		cId = id;
		clients.add(cId);
		return cId;
	}

	public void sendMsgToServ(Message m) throws RemoteException {
		sendToClients(m);
		// TODO : writeToHistory(m);
	}

	private Client getClientService(int cId) {
		Client c;
		try {
			c = reg.lookup(cId + "Service");
		} catch (NotBoundException e) {
			// TODO : Logger
			System.err.println("[E]: Service not bound in current registry for client " + cId +".");
			return null;
		}
		return c;
	}

	private void sendToClients(Message m) {
		Client c;
		for (int cId : clients) {
			if (cId != m.senderId()) {
				c = getClientService(cId);
				c.receiveMsg(m);
			}
		}

	}


}

