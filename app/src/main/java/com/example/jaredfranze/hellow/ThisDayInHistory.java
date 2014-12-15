package com.example.jaredfranze.hellow;

/**
 * Created by Jared.Franze on 11/17/2014.
 */

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.StringTokenizer;
import android.os.StrictMode;

import java.net.URL;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import java.io.IOException;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ThisDayInHistory {
    String rssResult = "";
    String title, link, description;
    boolean item = false;

    public ThisDayInHistory() {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL rssUrl = new URL("http://www.history.com/this-day-in-history/rss");
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            RSSHandler rssHandler = new RSSHandler();
            xmlReader.setContentHandler(rssHandler);
            InputSource inputSource = new InputSource(rssUrl.openStream());
            xmlReader.parse(inputSource);

        } catch (IOException e) {
            rssResult = (e.getMessage());
        } catch (SAXException e) {
            rssResult = (e.getMessage());
        } catch (ParserConfigurationException e) {
            rssResult = (e.getMessage());
        }

        parseText(rssResult);
    }

    public void parseText(String text)
    {
        StringBuilder builder = new StringBuilder();
        String[] split = text.split("link:");
        String[] split2 = split[1].split("description:");

        String[] split3 = split2[1].split("\\.");
        String temp = split3[1];
        builder.append(split3[0]);
        /*
        while(temp.substring(0,1) != " ")
        {
            split3 = temp.split("i");
            temp = split3[1];
            builder.append(split3[0]);
        }
        */
        String[] titleSplit = split[0].split(": ",2);
        //split[0] holds title
        //split2[0] holds link
        //split3[0] holds description
        title = titleSplit[1];
        link = split2[0];

        String finalString = builder.toString();
        //description = split3[0].substring(7);
        description = finalString.substring(7);
        //System.out.println(split3[0].substring(0, 7));
    }

    public String getDescription()
    {
        return description;
    }
    public String getTitle()
    {
        return title;
    }

    class RSSHandler extends DefaultHandler {

        public void startElement(String uri, String localName, String qName,
                                 Attributes attrs) throws SAXException {
            if (localName.equals("item"))
                item = true;

            if (!localName.equals("item") && item == true)
                rssResult = rssResult + localName + ": ";
        }

        public void endElement(String namespaceURI, String localName,
                               String qName) throws SAXException {

        }

        public void characters(char[] ch, int start, int length)
                throws SAXException {
            String cdata = new String(ch, start, length);
            if (item == true)
                rssResult = rssResult + (cdata.trim()).replaceAll("\\s+", " ") + "\t";

        }

    }
}
