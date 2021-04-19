import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
public class Diffuseur {
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
			finally{
				return list_args;
			}
		}
		else
		{
			return null;
		}
	}
	public static void main(String[] args) {
		List<String> infos = recup_info(args[0]);
		new Thread(new Diffuseur_Multi(Integer.parseInt(infos.get(2)),infos.get(1),infos.get(0))).start();
	}
}
