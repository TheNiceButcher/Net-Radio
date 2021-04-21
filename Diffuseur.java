import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
public class Diffuseur {
	public static void main(String[] args) {
		List<List<String>> f = new ArrayList<>();
		for (int i = 0; i < 5;i++)
		{
			f.add(Arrays.asList("diffpro","Salut #mon gars"));
			f.add(Arrays.asList("diffpro","Pk0"));
		}
		try
		{
			Un_Diffuseur diff = new Un_Diffuseur(args[0],f);
			diff.lancer();
		}
		catch(IllegalArgumentException e)
		{
			String error_msg = e.getMessage();
			if(error_msg.substring(0,24).equals("Fichier de configuration"))
				System.out.println(error_msg);
			else
				e.printStackTrace();
		}
	}
}
