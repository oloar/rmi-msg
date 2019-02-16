import java.util.ArrayList;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;
import java.util.Scanner;

public class ClientImplem implements Client{

    private int id;
    private Client ref;
    private String pseudo;
    private Serv s;
    private boolean connected;

    public ClientImplem(String pseudo){
        this.pseudo = pseudo;
        this.connected = false;
    }

    private void register(){
        Scanner sc = new Scanner(System.in);
        System.out.println("Register to server...");
        int choice = -1;
        try{
          this.ref = (Client) UnicastRemoteObject.exportObject(this,0);
          this.setId(this.s.clientRegister(this.ref));
          this.connected = true;
          System.out.println("Done.");
        }catch(Exception e){
            System.err.println("Error while registering client");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void connect(String host){

        // Get remote object reference
        System.out.print("Connection to server...");
        try{
            Registry registry = LocateRegistry.getRegistry(host);
            this.s = (Serv) registry.lookup("ChatService");
            System.out.println("Done.");
            this.register();
        }catch(Exception e){
            System.err.println("Error while connecting client");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

    }

    private void sendMessageToServer(String message) {
        try {
            this.s.sendMsgToServ(new Message(this.pseudo, this.id, this.s.getRoomOfClient(ref), message));
        } catch (RemoteException e) {
            System.err.println("Error sending message.");
        }
    }

    private void fetchHistory() {
        try {
            ArrayList<Message> h = this.s.getHistory(this.ref);
            for (Message m : h) {
                System.out.println(m);
            }
        } catch (RemoteException e) {
            System.err.println("Error fetching history.");
        }
    }

    public void parseInput(String message){
        if (message.length() != 0)
            if(message.charAt(0) != '/'){
                sendMessageToServer(message);
            } else {
                // Commands
                switch(message){
                    case "/history":
                        fetchHistory();
                        break;
                    case "/exit": case "/quit": case "/leave":
                        this.leaveChat();
                        break;
                    case "/changeroom":
                      try{
                        System.out.println("Which room?");
                        this.s.printRooms();
                        Scanner sc = new Scanner(System.in);
                        int id = sc.nextInt();
                        if(this.s.existingId(id)){
                          this.s.changeRoom(this, id);
                        }
                      }catch(RemoteException e){
                        System.err.println("Error changing room.");
                      }
                    default:
                        System.out.println("Unknown command");
                        break;
                }
            }
    }

    public void recvMsg(Message message) throws RemoteException{
        System.out.println(message.toString());
    }

    public void leaveChat(){
        System.out.println("Leaving chat...");
        this.connected = false;
        try {
            this.s.clientLeave(this.ref);
        } catch (RemoteException e) {
            System.err.println("Could not disconnect from server.");
        }
        System.exit(0);
    }

    public int getId() throws RemoteException {
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getPseudo() throws RemoteException{
        return pseudo;
    }

    public void setPseudo(String pseudo){
        this.pseudo = pseudo;
    }

    public boolean isConnected(){
        return this.connected;
    }

}
