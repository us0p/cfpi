package com.cfpi;

import com.cfpi.library.dotenv.Dotenv;
import java.io.IOException;

public class CFPI {
    public static void main(String[] args) {
	Dotenv dotenv = new Dotenv();

	try {
	    dotenv.load();
	} catch (IOException e) {
	    System.out.printf("Failed to load env file: %s%n.", e);
	}
    }
}
