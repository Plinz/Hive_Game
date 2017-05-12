package main.java.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class Save {

	public static void makeSave(String name, Core core) {
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(Core.class);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty("jaxb.encoding", "UTF-8");
			marshaller.setProperty("jaxb.formatted.output", true);
			if (!Files.isDirectory(Paths.get("Hive_save")))
				Files.createDirectories(Paths.get("Hive_save"));				
			marshaller.marshal(core, new File("Hive_save/"+name+".xml"));
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
