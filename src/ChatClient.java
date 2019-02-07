
public class ChatClient{

	public static void main(String [] args) {

		try {
			if (args.length < 1) {
				System.out.println("Usage: java HelloClient <rmiregistry host>");
				return;}

			String host = args[0];

            ClientImplem client = new ClientImplem("Tester");
            client.connect(host);


		} catch (Exception e)  {
			System.err.println("Error on client: " + e);
		}
	}
}
