package org.expasy.cellosaurus;

import org.expasy.cellosaurus.format.xml.Parser;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.stream.Collectors;

public class Listener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.print("Loading XML... ");

        Parser parser = new Parser(getClass().getClassLoader().getResource("cellosaurus.xml").getFile());
        Manager.database = parser.getDatabase();
        Manager.cellLines = parser.getCellLines().stream()
                .filter(x -> x.getSpecies().equals("Homo sapiens"))
                .collect(Collectors.toList());

        System.out.println("Done");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("Closing...");
    }
}
