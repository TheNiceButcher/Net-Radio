import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
/**
 Permet la création d'un diffuseur
**/
public class Diffuseur {
	public static void main(String[] args) {
		List<String> f = new ArrayList<>();
		for (int i = 0; i < 5;i++)
		{
			f.add("Salut #mon gars");
			f.add("Pk0");
		}
		try
		{
			Un_Diffuseur diff = new Un_Diffuseur(args[0],f);
			diff.lancer();
		}
		catch(BadConfigFileException e)
		{
			String error_msg = e.getMessage();
			System.out.println(e);
		}
		catch(Exception e1)
		{
			e1.printStackTrace();
		}
	}
}
