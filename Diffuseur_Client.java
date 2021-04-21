import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.*;
public class Diffuseur_Client implements Runnable {
	private Un_Diffuseur diff;
	private Socket socket;
	public Diffuseur_Client(Socket socket,Un_Diffuseur diff)
	{
		this.socket = socket;
		this.diff = diff;
	}
	public void run()
	{
		try (
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter  pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		) {
			while (true)
			{
				String mess = br.readLine();
				if(mess == null)
					continue;
				String type_mess = mess.substring(0,4);
				System.out.println(type_mess);
				if (type_mess.equals("MESS"))
				{
					String id = mess.substring(5,12);
					String message = mess.substring(14);
					System.out.println(id + "Yeah" + message);
					diff.ajout_message(message,id);
					pw.print("ACKM\r\n");
					pw.flush();
				}
			}
		} catch(Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
}
