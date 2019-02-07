import java.rmi.*;
import java.rmi.registry.*;

public class ClientImplem implements Client{

    private int id;
    private String pseudo;
    private Serv s;

    public ClientImplem(String pseudo){
        this.pseudo = pseudo;
    }

    private void register(){
        System.out.print("Register to server...");
        this.setId(this.s.clientRegister());
        System.out.println("Done.");
    }

    public void connect(String host){

        // Get remote object reference
        System.out.print("Connection to server...");
        Registry registry = LocateRegistry.getRegistry(host);
        this.s = (Serv) registry.lookup("ChatService");
        System.out.println("Done.");
        this.register();

    }

    public void sendMessageToServer(String message){
        if(message.charAt(0) != '/'){
            Message m = new Message(this.pseudo, this.id, message);
            this.s.sendMessageToServer(m);
        }else{
            // TODO : gérer l'historique
            switch(message){
                case "/history":
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

    @Override
    public void recvMsg(Message message) throws RemoteException{
        System.out.println(message.toString());
    }

    public void leaveChat(){
        // TODO complete ?
        this.s.clientLeave();
    }

    public int getId(){
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

}
