import java.util.Scanner;

public class ChatClient{

	public static void main(String [] args) {
		String entry;
		String pseudo;

		try {
			if (args.length < 1) {
				System.out.println("Usage: java HelloClient <rmiregistry host>");
				return;}

			String host = args[0];
			Scanner sc = new Scanner(System.in);


			System.out.println("Type in your name :");
			pseudo = sc.nextLine();
			// TODO : input validation
			ClientImplem client = new ClientImplem(pseudo);
			client.connect(host);


			while(client.isConnected()){
                entry = sc.nextLine();
                client.parseInput(entry);
            }


		} catch (Exception e)  {
			System.err.println("Error on client: " + e);
		}
	}
}
