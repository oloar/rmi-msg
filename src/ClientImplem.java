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
        System.out.print(", registering to server...");
        int choice = -1;
        try{
          this.ref = (Client) UnicastRemoteObject.exportObject(this,0);
          this.setId(this.s.clientRegister(this.ref));
          this.connected = true;
          // System.out.println("Done.");
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
            this.register();
            System.out.println("All done.");
            this.printChatHelp();
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
                    case "/create":
                        try {
                            int id = this.s.newRoom();
                            System.out.println("Room created with id : " + id);
                        } catch (RemoteException e) {
                            System.err.println("Error creating the room.");
                        }
                        break;
                    case "/join":
                        try{
                            int id;
                            System.out.println("Which room?");
                            System.out.print(this.s.printRooms());
                            Scanner sc = new Scanner(System.in);
                            id = Integer.parseInt(sc.nextLine()); // TODO : input validation
                            if (this.s.existingId(id)) {
                                this.s.changeRoom(this, id);
                            } else {
                                System.out.println("This room does not exist.");
                            }
                        }catch(RemoteException e){
                            System.err.println("Error changing room.");
                        }
                        break;
                      case "/printrooms":
                        try{
                          System.out.println(this.s.printRooms());
                        }catch(RemoteException e){
                          System.out.println("Error printing room list");
                        }
                        break;
                      case "/help": case "/list":
                        this.printChatHelp();
                        break;
                    default:
                        System.out.println("Unknown command");
                        break;
                }
            }
    }

    private void printChatHelp(){
      System.out.println("Commandes du chat :");
      System.out.println("  /history print room history");
      System.out.println("  /exit or /leave or /quit Leave chat");
      System.out.println("  /create Create a new room");
      System.out.println("  /printrooms Print the list of existing rooms");
      System.out.println("  /join Join a room (Enter id after)");
      System.out.println("  /help Print this message");
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
