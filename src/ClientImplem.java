import java.util.ArrayList;
import java.rmi.*;
import java.rmi.registry.*;
import java.rmi.server.*;

public class ClientImplem implements Client{

    private int id;
    private Client c;
    private String pseudo;
    private Serv s;
    private boolean connected;

    public ClientImplem(String pseudo){
        this.pseudo = pseudo;
        this.connected = false;
    }

    private void register(){
        System.out.print("Register to server...");
        try{
            this.c = (Client) UnicastRemoteObject.exportObject(this,0);
            this.setId(this.s.clientRegister(c));
        }catch(Exception e){
            System.err.println("Error while registering client");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        this.connected = true;
        System.out.println("Done.");
    }

    public void connect(String host){

        // Get remote object reference
        System.out.print("Connection to server...");
        try{
            Registry registry = LocateRegistry.getRegistry(host);
            this.s = (Serv) registry.lookup("ChatService");
        }catch(Exception e){
            System.err.println("Error while connecting client");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        System.out.println("Done.");
        this.register();

    }

    public void sendMessageToServer(String message){
        if(message.charAt(0) != '/'){
            Message m = new Message(this.pseudo, this.id, message);
            try{
                this.s.sendMsgToServ(m);
            }catch(Exception e){
                System.err.println("Error while sending message to server");
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
        }else{
            // TODO : gérer l'historique
            switch(message){
                case "/history":
                    try {
                        ArrayList<Message> h = this.s.getHistory();
                        for (Message m : h) {
                            System.out.println(m);
                        }
                    } catch(RemoteException e) {
                        System.err.println("Error while fetching history.");
                    }
                    break;
                case "/exit": case "/quit": case "/leave":
                    this.leaveChat();
                    break;
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
            this.s.clientLeave(this.c);
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
