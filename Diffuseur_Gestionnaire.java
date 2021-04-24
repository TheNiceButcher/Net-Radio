import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.*;
public class Diffuseur_Gestionnaire implements Runnable{
	private String addr_diff;
	private Un_Diffuseur diff;
	public Diffuseur_Gestionnaire(Un_Diffuseur addr_diff,String ip)
	{
		this.addr_diff = ip;
		this.diff = addr_diff;
	}
	public static String convert_ip(String ip)
	{
		String[] m = ip.split("\\.");
		System.out.println(m.length);
		String new_ip = new String();
		for (int i = 0; i < m.length;i++)
		{
			new_ip += Diffuseur_Multi.ajout_zero(m[i],3);
			if(i != 3)
			{
				new_ip += ".";
			}
			System.out.println(m[i]);
		}
		System.out.println(new_ip);
		return new_ip;
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
			//System.out.println("Adresse du gestionnaire ?");
			String addr = "127.0.1.1";
			//System.out.println("Port ?");
			int port = 9456;
			Socket sock = new Socket(addr,port);
			BufferedReader br=new BufferedReader(new InputStreamReader(sock.getInputStream()));
			PrintWriter pw=new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
			String mess = "REGI " + diff.getIdentifiant() + " " + convert_ip(diff.getAdresseMulti());
			mess += " " + Diffuseur_Multi.ajout_zero(String.valueOf(diff.getPortMulti()),4) + " " + convert_ip(addr_diff);
			mess += " " + Diffuseur_Multi.ajout_zero(String.valueOf(diff.getPortTCP()),4);
			convert_ip(diff.getAdresseMulti());
			pw.print(mess + "\r\n");
			pw.flush();
			String msg_retour = br.readLine();
			if(msg_retour.equals("REOK"))
			{
				while(true)
				{
					String d = br.readLine();
					if(d.equals("RUOK"))
					{
						pw.print("IMOK\r\n");
						pw.flush();
					}
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
