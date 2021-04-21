import java.util.*;
import java.io.*;
import java.net.*;

public final class Diffuseur_Multi implements Runnable{
	private Un_Diffuseur diff;
	public Diffuseur_Multi(Un_Diffuseur diff)
	{
		this.diff = diff;
	}
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
   public void run()
   {
	   try{
		   Thread.sleep(3000);
		   DatagramSocket env = new DatagramSocket();
		   byte[]data=new byte[1024];
		   Iterator<List<String>> it = diff.getMessageADiffuser().iterator();
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
					   for (int i = compt_to_send.length(); i < 4; i++)
					   {
						   compt_to_send = "0" + compt_to_send;
					   }
					   String mess = "DIFF " + compt_to_send + " " + identifiant + " " + message_a_diff + "\r\n";
					   data = mess.getBytes();
					   DatagramPacket diffuse = new DatagramPacket(data,data.length,
											   InetAddress.getByName(addr_multi),port_multi);
					   env.send(diffuse);
					   diff.incrCompteur();
					   diff.diffusion_message(msg);
				   }
		}

	   } catch(Exception e){
		   e.printStackTrace();
	  }

  }
}
