package org.expasy.cellosaurus;

import org.expasy.cellosaurus.formats.Parser;
import org.expasy.cellosaurus.formats.xml.XmlParser;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.net.URL;

import org.expasy.cellosaurus.genomics.str.*;


/**
 * Class being executed once when the webapp is deployed. Its purpose is to parse the Cellosaurus cell lines with STR
 * profiles and store them in memory for subsequent searches.
 */
public class Listener implements ServletContextListener {
    private final static String URL = "ftp://ftp.expasy.org/databases/cellosaurus/cellosaurus.xml";
    /**
     * Read the XML version of the Cellosaurus database from the FTP and store the human cell lines with STR profiles
     * and the database latest release information into the {@code Manager} as static variables.
     *
     * @param servletContextEvent the servlet context event
     */
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            System.out.print("Loading XML... ");

            Parser parser = new XmlParser();
            //parser.parse(new URL(Listener.URL));
            parser.parse("cellosaurus.xml");

            System.out.println("Done");

            showData();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showData() {
        System.out.println("Data loaded from ftp after parsing");
        System.out.println("HUMAN CellLines: " + Species.HUMAN.getCellLines().size());
        System.out.println("Mouse CellLines: " + Species.MOUSE.getCellLines().size());
        System.out.println("Dog   CellLines: " + Species.DOG.getCellLines().size());
    }
}
