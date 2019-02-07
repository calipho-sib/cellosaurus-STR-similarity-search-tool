package org.expasy.cellosaurus;

import org.expasy.cellosaurus.format.xml.Parser;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.net.URL;
import java.util.stream.Collectors;

public class Listener implements ServletContextListener {
    private final static String URL = "ftp://ftp.expasy.org/databases/cellosaurus/cellosaurus.xml";

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            System.out.print("Loading XML... ");

            Parser parser = new Parser(new URL(Listener.URL));
            Manager.database = parser.getDatabase();
            Manager.cellLines = parser.getCellLines().stream()
                    .filter(x -> x.getSpecies().equals("Homo sapiens"))
                    .collect(Collectors.toList());

            System.out.println("Done");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("Closing...");
    }
}
