import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.*;
/**
Gére la communication entre un gestionnaire et un client
**/
public class Gestionnaire_Client implements Runnable
{
	private Un_Gestionnaire gestion;
	private Socket socket;
	public Gestionnaire_Client(Un_Gestionnaire gestion, Socket socket)
	{
		this.gestion = gestion;
		this.socket = socket;
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
			if (type_mess.equals("LIST"))
			{
				List<List<String>> list_diff = gestion.getDiffuseurs();
				int numdiff = list_diff.size();
				pw.print("LINB " + String.valueOf(numdiff) + "\r\n");
				pw.flush();
				for(List<String> diff : list_diff)
				{
					String id = diff.get(0);
					String port1 = diff.get(1);
					String ip1 = diff.get(2);
					String port2 = diff.get(3);
					String ip2 = diff.get(4);
					String msg = "ITEM " + id + " " + ip1 + " " + port1 + " " + ip2 + " " + port2;
					pw.print(msg + "\r\n");
					pw.flush();
					System.out.println(msg);
				}
				socket.close();
			}
			if(type_mess.equals("REGI"))
			{
				String id = mess.substring(5,13);
				String ip1 = mess.substring(14,29);
				String port1 = mess.substring(30,34);
				String ip2 = mess.substring(35,50);
				String port2 = mess.substring(51,55);
				Un_Diffuseur diff = new Un_Diffuseur(id,ip1,Integer.parseInt(port1),Integer.parseInt(port2));
				boolean ok = gestion.ajout_diff(diff,ip2);
				if(ok)
				{
					pw.print("REOK\r\n");
					pw.flush();
					Gestionnaire_Diffuseur g = new Gestionnaire_Diffuseur(socket,gestion,diff);
					Thread t =  new Thread(g);
					t.start();
					t.join();
				}
				else
				{
					pw.print("RENO\r\n");
					pw.flush();
					socket.close();
				}
			}
		} catch(Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
}
