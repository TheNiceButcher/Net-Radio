import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.*;
/**
 Gère la partie communication entre les clients et le diffuseur diff, via socket
**/
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
			//String mess = br.readLine();
			char[] readd = new char[157];
			br.read(readd,0,157);
			String mess = new String(readd);
			System.out.println(mess);
			System.out.println(mess.length());
			String type_mess = mess.substring(0,4);
			System.out.println(type_mess);
			if (type_mess.equals("MESS"))
			{
				String id = mess.substring(5,13);
				String message = mess.substring(14,154);
				System.out.println(id + " " + message);
				if(mess.charAt(4) != ' ' || mess.charAt(13) != ' ')
				{
					System.out.println("Mauvaise format pour MESS");
				}
				else if (!mess.substring(154,156).equals("\r\n"))
				{
					System.out.println("Mauvais format");
				}
				else
				{
					diff.ajout_message(message,id);
					pw.print("ACKM\r\n");
					pw.flush();
				}
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
					String to_send = "OLDM " + Entite.ajout_zero(msg.get(0),4);
					to_send += " " + Entite.ajout_diese(msg.get(1),8) + " ";
					to_send += Entite.ajout_diese(msg.get(2),140) + "\r\n";
					System.out.println(to_send);
					pw.print(to_send);
					pw.flush();
				}
				pw.print("ENDM\r\n");
				pw.flush();
			}
			else
			{
				System.out.println("Message " + mess + "non reconnu");
			}
			socket.close();
		} catch(Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
}
