import java.util.ArrayList;

public class Room{

  int id;
  ArrayList<Client> clients;

  public Room(int id){
    this.clients = new ArrayList<Client>();
    this.id = id;
  }

  public boolean isInTheRoom(Client c){
    return this.clients.contains(c);
  }

  public void joinRoom(Client c){
    this.clients.add(c);
  }

  public void leaveRoom(Client c){
    this.clients.remove(c);
  }

  public ArrayList<Client> getClients(){
    return this.clients;
  }

  public int getId(){
    return this.id;
  }

  public int getNumberClients(){
    return this.clients.size();
  }

}
