import java.io.Serializable;

public class Message implements Serializable {
	private String sender;
	private int senderId;
	private int roomId;
	private String text;

	public Message(String sender, int senderId, int roomId, String text) {
		this.sender = sender;
		this.senderId = senderId;
		this.text = text;
		this.roomId = roomId;
	}

	public Message(String sender, int senderId, String text) {
		this.sender = sender;
		this.senderId = senderId;
		this.text = text;
		this.roomId = 0;
	}

	public String sender() {
		return this.sender;
	}

	public String text() {
		return this.text;
	}

	public int senderId(){
		return this.senderId;
	}

	public int roomId() {
		return this.roomId;
	}

	public String toCSV() {
		return this.sender + "," + this.senderId + "," + this.roomId + "," + this.text + "\n";
	}

	public static Message fromCSV(String s) {
		String[] splitted = s.split(",");
		return new Message(splitted[0], Integer.parseInt(splitted[1]), Integer.parseInt(splitted[2]), splitted[3]);
	}

	public String toString(){
		return this.sender+"("+this.senderId+"): "+this.text;
	}

}
