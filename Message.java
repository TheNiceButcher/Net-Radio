import java.util.*;
public final class Message {
	private final String id;
	private final Optional<Integer> num_msg;
	private final String message;
	public Message(int num, String identifiant, String contenu)
	{
		this.num_msg = Optional.of(num);
		this.id = identifiant;
		this.message = contenu;
	}
	public Message(String identifiant, String contenu)
	{
		this.num_msg = Optional.empty();
		this.id = identifiant;
		this.message = contenu;
	}
	public int getNumMessage()
	{
		return this.num_msg.get();
	}
	public String getIdentifiant()
	{
		return this.id;
	}
	public String getMessage()
	{
		return this.message;
	}
}
