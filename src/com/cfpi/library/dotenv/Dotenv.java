package com.cfpi.library.dotenv;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

public class Dotenv {
    private String filename = ".env";
    private String dir = ".";

    private List<String> read() throws IOException {
	String location = this.dir + "/" + this.filename;
	Path path = Paths.get(location);

	if (Files.exists(path)) {
	    return Files.readAllLines(path);
	}

	return new ArrayList<>();
    }

    public void load() throws IOException {
	List<String> fileEntries = this.read();
	for (String env : fileEntries) {
	    
	    System.out.println(env);
	}
    }
}
