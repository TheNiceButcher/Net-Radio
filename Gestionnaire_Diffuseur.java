import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.*;
/**
Gére la communication entre un gestionnaire et les diffuseurs
**/
public class Gestionnaire_Diffuseur implements Runnable{
	private Socket sock;
	private Un_Gestionnaire gest;
	private String id;
	public Gestionnaire_Diffuseur(Socket sock,Un_Gestionnaire gest, Un_Diffuseur diff)
	{
		this.sock = sock;
		this.gest = gest;
		this.id = diff.getIdentifiant();
	}
	public void run()
	{
		boolean is_connected = true;
		try (
		BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		PrintWriter  pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));){
			sock.setSoTimeout(20000); // On laisse 20 secondes au diffuseur pour répondre
			while(is_connected)
			{
				//On demande toutes les 11 secondes au diffuseur s'il est encore "vivant"
				Thread.sleep(11000);
				pw.print("RUOK\r\n");
				pw.flush();
				String s = br.readLine();
				// null -> déconnexion
				if(s == null)
				{
					System.out.println("Connexion perdu avec " + id);
					gest.retrait_diff(id);
					is_connected = false;
				}
				else if(s.equals("IMOK"))
				{
					System.out.println(id + "Bien connecté");
				}
				else
				{
					System.out.println("Message inconnu");
				}
			}
			//Le diffuseur n'a pas répondu en 11 secondes -> on le retire de la liste
		} catch(SocketTimeoutException e) {
			System.out.println("Connexion perdu avec " + id);
			gest.retrait_diff(id);
			try {
				sock.close();
			} catch(Exception e1) {
				e1.printStackTrace();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
