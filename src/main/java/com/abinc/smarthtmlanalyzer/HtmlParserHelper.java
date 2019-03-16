package com.abinc.smarthtmlanalyzer;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

/**
 * Created by Alex on 16.03.2019.
 */
public class HtmlParserHelper {

    private static String CHARSET_NAME = "utf8";

    public static Optional<Element> findElementById(File htmlFile, String targetElementId) {
        try {
            Document doc = Jsoup.parse(
                    htmlFile,
                    CHARSET_NAME,
                    htmlFile.getAbsolutePath());

            return Optional.of(doc.getElementById(targetElementId));

        } catch (IOException e) {
            System.out.println(String.format("Error reading [%s] file", htmlFile.getAbsolutePath(), e));
            return Optional.empty();
        }
    }

    public static Optional<Elements> findElementsByQuery(File htmlFile, String cssQuery) {
        try {
            Document doc = Jsoup.parse(
                    htmlFile,
                    CHARSET_NAME,
                    htmlFile.getAbsolutePath());

            return Optional.of(doc.select(cssQuery));

        } catch (IOException e) {
            System.out.println(String.format("Error reading [%s] file", htmlFile.getAbsolutePath(), e));
            return Optional.empty();
        }
    }

    public static String getElementPath(Element element)    {
        String path = "";

        if (element != null) {
            String elementNode = element.nodeName();
            while (element.parentNode() != null) {
                path = element.parentNode().nodeName() + "/" + path;
                element = element.parent();
            }
            path += elementNode;
        }

        return path;
    }
}
