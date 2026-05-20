package com.radl.sa.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertiesLoader {

	public static Properties loadProps() throws IOException {
		// get wd path
		Path wd = Paths.get(System.getProperty("user.dir"));
		// iteratively resolve folder names against path
		Path propPath = wd.resolve("src", "main", "resources", "app.properties");
		// load props
		Properties props = new Properties();
		props.load(Files.newInputStream(propPath));
		
		return props;
	}
}
