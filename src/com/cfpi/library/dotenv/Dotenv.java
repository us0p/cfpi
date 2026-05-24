package com.cfpi.library.dotenv;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Dotenv {
    private String filename = ".env";
    private String dir = ".";
    private Map<String, String> vars = new HashMap<>();

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
	for (String line : fileEntries) {
	    line = line.strip();
	    if (line.isEmpty() || line.startsWith("#")) continue;

	    int eq = line.indexOf("=");
	    if (eq < 0) continue;

	    String key = line.substring(0, eq).strip();
	    String value = line.substring(eq + 1).strip();
	    vars.put(key, value);
	}
    }

    public String get(String key) {
	return vars.get(key);
    }
}
