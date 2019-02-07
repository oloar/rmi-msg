import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.*;

public class ChatServer {

	public static void  main(String [] args) {
		try {
			// Create a Hello remote object
			Registry registry= LocateRegistry.getRegistry();

			ServImplem s = new ServImplem();
			Serv s_stub = (Serv) UnicastRemoteObject.exportObject(s, 0);

			// Register the remote object in RMI registry with a given identifier
			registry.rebind("ChatService", s_stub);

			System.out.println ("Server ready");

		} catch (Exception e) {
			System.err.println("Error on server :" + e) ;
			e.printStackTrace();
		}
	}
}
