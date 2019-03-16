package com.abinc.smarthtmlanalyzer;

import java.io.File;

/**
 * Created by Alex on 16.03.2019.
 */
public class Main {
    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Please specify path to original file and path to file for compare");
            System.exit(0);
        }

        String resourcePathSource = args[0];
        String resourcePathComparable = args[1];

        String targetElementId = "make-everything-ok-button";
        if (args.length > 2)
            targetElementId = args[2];

        checkFile(resourcePathSource);
        checkFile(resourcePathComparable);

        ElementAnalyzer elementAnalyzer = new ElementAnalyzer(resourcePathSource, targetElementId);
        SearchResult searchResult = elementAnalyzer.searchElement(resourcePathComparable);

        System.out.println("Target element id: " + targetElementId);

        if (searchResult.getElement() != null) {
            System.out.println("Element was found with next query: " + searchResult.getSearchQuery());
            System.out.println("Element was found with path comparison: " + searchResult.getPathCompared());
            System.out.println("Element path: " + HtmlParserHelper.getElementPath(searchResult.getElement()));
        } else
            System.out.println("Element was not found");
    }

    private static void checkFile(String path) {
        File f = new File(path);
        if (!f.exists() || f.isDirectory()) {
            System.out.println("File not found.");
            System.exit(0);
        }
    }
}
