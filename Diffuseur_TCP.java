import java.util.*;
import java.io.*;
import java.net.*;
/**
 Gère les communication TCP du diffuseur diff 
**/
public final class Diffuseur_TCP implements Runnable {
	private Un_Diffuseur diff;
	public Diffuseur_TCP(Un_Diffuseur diff)
	{
		this.diff = diff;
	}
	public void run()
	{
		try {
			int port = diff.getPortTCP();
			ServerSocket server = new ServerSocket(port);
			while(true)
			{
				Socket sock = server.accept();
				Diffuseur_Client dc = new Diffuseur_Client(sock,diff);
				new Thread(dc).start();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
