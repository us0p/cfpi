package com.cfpi;

import com.cfpi.library.dotenv.Dotenv;
import java.io.IOException;

public class CFPI {
    public static void main(String[] args) {
	Dotenv dotenv = new Dotenv();

	try {
	    System.out.println("STARTING APPLICATION...");
	    dotenv.load();
	    System.out.println("FINISHED APPLICATION");
	} catch (IOException e) {
	    System.out.printf("ERROR: %s%n.", e);
	}
    }
}
