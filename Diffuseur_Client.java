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
			String mess = br.readLine();
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
				else if (type_mess.equals("LAST")) {
					int nb_mess = Integer.parseInt(mess.substring(5));
					List<List<String>> message = new ArrayList<>(diff.getMessageDiffuse());
					int total_msg = message.size();
					if (total_msg < nb_mess)
					{
						nb_mess = total_msg;
					}
					message = message.subList(total_msg - nb_mess,total_msg);
					for (List<String> msg : message)
					{
						String to_send = "OLDM " + Diffuseur_Multi.ajout_zero(msg.get(0),3);
						to_send += " " + msg.get(1) + " " + msg.get(2) + "\r\n";
						pw.print(to_send);
						pw.flush();
					}
					pw.print("ENDM\r\n");
				}
				socket.close();
		} catch(Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
}
