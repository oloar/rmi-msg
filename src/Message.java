import java.io.Serializable;

public class Message implements Serializable {
	private String sender;
	private int senderId;
	private String text;
	private Room room;

	public Message(String sender, int senderId, String text, Room room) {
		this.sender = sender;
		this.senderId = senderId;
		this.text = text;
		this.room = room;
	}

	public Message(String sender, int senderId, String text) {
		this.sender = sender;
		this.senderId = senderId;
		this.text = text;
		this.room = room;
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

	public String toCSV() {
		return this.sender + "," + this.senderId + "," + this.text;
	}

	public static Message fromCSV(String s) {
		String[] splitted = s.split(",");
		return new Message(splitted[0], Integer.parseInt(splitted[1]), splitted[2]);
	}
	public String toString(){
		return this.sender+": "+this.text;
	}

}
