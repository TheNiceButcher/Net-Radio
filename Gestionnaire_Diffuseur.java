import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.*;
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
			sock.setSoTimeout(20000);
			while(is_connected)
			{
				Thread.sleep(30);
				pw.print("RUOK\r\n");
				pw.flush();
				String s = br.readLine();
				if(s.equals("IMOK\r\n"))
				{

				}
			}
		} catch(SocketTimeoutException e) {
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
