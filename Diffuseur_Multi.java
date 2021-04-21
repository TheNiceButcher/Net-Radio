import java.util.*;
import java.io.*;
import java.net.*;
/**
 Gére la partie multi-diffusion du diffuseur diff
**/
public final class Diffuseur_Multi implements Runnable{
	private Un_Diffuseur diff;
	public Diffuseur_Multi(Un_Diffuseur diff)
	{
		this.diff = diff;
	}
	/**
	Renvoie mot à la bonne longueur (lg_voulu), en le complémentant avec des '#'
	Renvoie null si mot a plus de lg_voulu caracères
	**/
	public static String ajout_diese(String mot,int lg_voulu)
	{
		int lg_mot = mot.length();
		if (lg_mot > lg_voulu)
		{
			return null;
		}
		String mot_complet = new String(mot);
		for (int i = lg_mot; i < lg_voulu; i++)
		{
			mot_complet += "#";
		}
		return mot_complet;
	}
	/**
	Renvoie mot à la bonne longueur, en ajoutant des "0" au début de celui-ci
	**/
	public static String ajout_zero(String mot,int lg_voulu)
	{
		int lg_mot = mot.length();
		if (lg_mot > lg_voulu)
		{
			return null;
		}
		String mot_complet = new String(mot);
		for (int i = lg_mot; i < lg_voulu; i++)
		{
			mot_complet = "0" +  mot_complet;
		}
		return mot_complet;
	}
   public void run()
   {
	   try{
		   Thread.sleep(3000);
		   DatagramSocket env = new DatagramSocket();
		   byte[]data=new byte[1024];
		   int port_multi = diff.getPortMulti();
		   String addr_multi = diff.getAdresseMulti();
		   while(true)
		   {
			   System.out.println(diff.getMessageADiffuser().size());
			   Thread.sleep(2000);
			   synchronized(diff)
			   {
				   if (diff.getMessageADiffuser().size() == 0)
				   {
					   continue;
				   }
				   List<String> msg = diff.getMessageADiffuser().get(0);

				   String identifiant = ajout_diese(msg.get(0),8);
				   String message_a_diff = ajout_diese(msg.get(1),140);
				   String compt_to_send = String.valueOf(diff.getCompteur());
				   compt_to_send = ajout_zero(compt_to_send,4);
				   String mess = "DIFF " + compt_to_send + " " + identifiant + " " + message_a_diff + "\r\n";
				   data = mess.getBytes();
				   DatagramPacket diffuse = new DatagramPacket(data,data.length,
											   InetAddress.getByName(addr_multi),port_multi);
					env.send(diffuse);
					diff.diffusion_message(msg);
				}
			}

		} catch(Exception e){
			e.printStackTrace();
		}

	}
}
