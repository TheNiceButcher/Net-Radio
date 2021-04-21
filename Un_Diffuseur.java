import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
public class Un_Diffuseur {
	private final int port_multi;
	private final int port_tcp;
	private final String addr_multi;
	private final String identifiant;
	private List<List<String>> mess_a_diff;
	private List<List<String>> mess_diffuse;
	private int compteur;
	public Un_Diffuseur(String config_file, List<List<String>> mess)
	{
		List<String> infos = recup_info(config_file);
		if(infos == null)
		{
			System.out.println("Fichier de configuration inexistant");
			//return null;
		}
		if (infos.size() != 4)
		{
			System.out.println("Fichier de configuration au mauvais format");
			//return null;
		}
		this.identifiant = infos.get(0);
		this.addr_multi = infos.get(1);
		this.port_multi = Integer.parseInt(infos.get(2));
		this.port_tcp = Integer.parseInt(infos.get(3));
		this.mess_a_diff = new ArrayList<>(mess);
		this.mess_diffuse = new ArrayList<>();
		this.compteur = 0;
	}
	/**
		Récupere les informations de configuration pour le diffuseur
	**/
	public static List<String> recup_info(String filename)
	{
		File f = new File(filename);
		if (f.exists())
		{
			List<String> list_args = new ArrayList<>();
			try {
				BufferedReader d = new BufferedReader(new FileReader(f));
				String current_line = "";
				while((current_line = d.readLine())!=null)
				{
					list_args.add(current_line);
				}
				return list_args;
			}catch (Exception e) {
				e.printStackTrace();
			}
			return list_args;
		}
		else
		{
			return null;
		}
	}
	public synchronized int getCompteur()
	{
		return this.compteur;
	}
	public synchronized void incrCompteur()
	{
		compteur = (compteur + 1) % 10000;
	}
	public String getIdentifiant()
	{
		return this.identifiant;
	}
	public String getAdresseMulti()
	{
		return this.addr_multi;
	}
	public int getPortMulti()
	{
		return this.port_multi;
	}
	public int getPortTCP()
	{
		return this.port_tcp;
	}
	public List<List<String>> getMessageADiffuser()
	{
		return this.mess_a_diff;
	}
	public synchronized void ajout_message(String message, String identifiant)
	{
		System.out.println(message);
		this.mess_a_diff.add(0,Arrays.asList(identifiant,message));
	}
	public synchronized void diffusion_message(List<String> mess)
	{
		List<String> mess1 = new ArrayList<>(this.mess_a_diff.remove(0));
		mess1.add(0,String.valueOf(getCompteur()));
		this.mess_diffuse.add(mess1);
	}
	public synchronized List<List<String>> getMessageDiffuse()
	{
		return this.mess_diffuse;
	}
	public void lancer()
	{
		new Thread(new Diffuseur_Multi(this)).start();
		new Thread(new Diffuseur_TCP(this)).start();
	}
}
