import java.util.*;
import java.io.*;
import java.net.*;

public class Diffuseur_Multi implements Runnable{
	private List<List<String>> messages;
	private int compteur;
	private int port_multi;
	private String multi_dif;
	private List<Map<String,String>> messages_diffuses;
	public Diffuseur_Multi(int port,String multi_dif,String identifiant)
	{
		this.port_multi = port;
		this.multi_dif = multi_dif;
		this.compteur = 0;
		this.messages = new ArrayList<>();
		for (int i = 0; i < 5;i++)
		{
			messages.add(Arrays.asList(identifiant,"Salut mon gars"));
			messages.add(Arrays.asList(identifiant,"Pk0"));
		}
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
			Thread.sleep(10000);
			DatagramSocket env = new DatagramSocket();
            byte[]data=new byte[1024];
            DatagramPacket paquet=new DatagramPacket(data,data.length);
			Iterator<List<String>> it = messages.iterator();
            while(it.hasNext()){
				Thread.sleep(4000);
				List<String> msg = it.next();
				String identifiant = ajout_diese(msg.get(0),8);
				String message_a_diff = ajout_diese(msg.get(1),140);
				String compt_to_send = String.valueOf(compteur);
				for (int i = compt_to_send.length(); i < 4; i++)
				{
					compt_to_send = "0" + compt_to_send;
				}
				String mess = "DIFF " + compt_to_send + " " + identifiant + " " + message_a_diff + "\r\n";
				data = mess.getBytes();
				DatagramPacket diffuse = new DatagramPacket(data,data.length,
										InetAddress.getByName(multi_dif),port_multi);
				env.send(diffuse);
				compteur = ((compteur + 1) % 10000);

            }

        } catch(Exception e){
            e.printStackTrace();
       }

	}
}
