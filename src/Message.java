public class Message {
	private String sender;
	private int senderId;
	private String text;

	public Message(String sender, int senderId, String text) {
		this.sender = sender;
		this.senderId = senderId;
		this.text = text;
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

	public String toString(){
		return this.sender+": "+this.text;
	}

}
