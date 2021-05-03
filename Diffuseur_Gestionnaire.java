import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.*;
/**
Gére la communication entre un diffuseur et un gestionnaire
**/
public class Diffuseur_Gestionnaire implements Runnable,Entite{
	private String addr_diff;
	private Un_Diffuseur diff;
	public Diffuseur_Gestionnaire(Un_Diffuseur addr_diff,String ip)
	{
		this.addr_diff = ip;
		this.diff = addr_diff;
	}
	public void run()
	{
		try (Scanner s = new Scanner(System.in);){
			while(true)
			{
				System.out.println("Tapez 1 pour être enregistrer dans un gestionnaire");
				int c = s.nextInt();
				if (c == 1)
				{
					break;
				}
			}
			System.out.println("Adresse du gestionnaire ?");
			//String addr = "127.0.1.1";
			String addr = s.next();
			System.out.println("Port ?");
			//int port = 9456;
			int port = s.nextInt();
			Socket sock = new Socket(addr,port);
			BufferedReader br=new BufferedReader(new InputStreamReader(sock.getInputStream()));
			PrintWriter pw=new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
			String mess = "REGI " + diff.getIdentifiant() + " " + Entite.convert_ip(diff.getAdresseMulti());
			mess += " " + Entite.ajout_zero(String.valueOf(diff.getPortMulti()),4) + " " + Entite.convert_ip(addr_diff);
			mess += " " + Entite.ajout_zero(String.valueOf(diff.getPortTCP()),4);
			pw.print(mess + "\r\n");
			pw.flush();
			String msg_retour = br.readLine();
			if(msg_retour.equals("REOK"))
			{
				while(true)
				{
					String d = br.readLine();
					if(d != null && d.equals("RUOK"))
					{
						pw.print("IMOK\r\n");
						pw.flush();
					}
					else
						break;
				}
			}
			else if(msg_retour.equals("RENO"))
			{
				System.out.println(msg_retour + " Impossible d'enregistrer");
			}
			sock.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
