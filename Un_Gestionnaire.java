import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.*;
public final class Un_Gestionnaire
{
	private final int port;
	private final int max_diff;
	private List<List<String>> diffuseurs;
	public Un_Gestionnaire(int port,int max_diff)
	{
		this.port = port;
		this.max_diff = max_diff;
		this.diffuseurs = new ArrayList<>();
	}
	public int getPort()
	{
		return this.port;
	}
	public synchronized List<List<String>> getDiffuseurs()
	{
		return new ArrayList<>(diffuseurs);
	}
	public synchronized boolean ajout_diff(Un_Diffuseur diff,String ip)
	{
		if(diffuseurs.size() == this.max_diff)
		{
			return false;
		}
		String id = diff.getIdentifiant();
		String port_tcp = String.valueOf(diff.getPortTCP());
		String port_multi = String.valueOf(diff.getPortMulti());
		String addr_mult = diff.getAdresseMulti();
		diffuseurs.add(Arrays.asList(id,port_multi,addr_mult,port_tcp,ip));
		return true;
	}
	public synchronized boolean retrait_diff(String id)
	{
		Iterator<List<String>> it = this.diffuseurs.iterator();
		while(it.hasNext())
		{
			List<String> l = it.next();
			if(l.contains(id))
			{
				it.remove();
				return true;
			}
		}
		return false;
	}
	public void lancer()
	{
		try
		{
			ServerSocket server = new ServerSocket(port);
			while(true)
			{
				Socket sock = server.accept();
				Gestionnaire_Client gc = new Gestionnaire_Client(this,sock);
				new Thread(gc).start();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
