package main.java.model;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import main.java.utils.Consts;

public class Save {

	public static void makeSave(String name, Core core) {
		try {
			if (!Files.isDirectory(Paths.get("Hive_save")))
				Files.createDirectories(Paths.get("Hive_save"));
			Path path = Paths.get("Hive_save/" + name + ".xml");
			BufferedWriter writer = Files.newBufferedWriter(path);
			if (core.getMode() == Consts.PVP){
				writer.write(Consts.PVP + "\n"+ core.getStatus());
			} else {
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
