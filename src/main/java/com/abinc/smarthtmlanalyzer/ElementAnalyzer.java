package com.abinc.smarthtmlanalyzer;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.*;

/**
 * Created by Alex on 16.03.2019.
 */
public class ElementAnalyzer {

    private File sourceFile;
    private Map<String, String> elementAttributes;
    private Element sourceElement;

    public ElementAnalyzer(String resourcePath, String elementId) {
        sourceFile = new File(resourcePath);
        analyzeSourceElement(elementId);
    }

    private void analyzeSourceElement(String id) {
        Optional<Element> element = HtmlParserHelper.findElementById(sourceFile, id);
        sourceElement = element.get();
        elementAttributes = new HashMap<>();
        element.get().attributes().forEach(a -> elementAttributes.put(a.getKey(), a.getValue()));
        //String text = element.get().text();   // have an issue with getting text
        //elementAttributes.put("text", text);
    }

    public SearchResult searchElement(String resourcePath) {
        File file = new File(resourcePath);

        List<String> searchCriterias = new ArrayList<>();
        elementAttributes.entrySet().forEach(a -> searchCriterias.add(getSearchCriteria(a.getKey(), a.getValue())));

        LinkedList<List<String>> searchCriteriaCombinations = new LinkedList<>();

        for (int i = 1; i <= elementAttributes.size(); i++)
            searchCriteriaCombinations.addAll(combination(searchCriterias, i));

        while (!searchCriteriaCombinations.isEmpty()) {
            String query = getQuery(sourceElement.tagName(), searchCriteriaCombinations.pop());
            Optional<Elements> elements = HtmlParserHelper.findElementsByQuery(file, query);

            if (elements.get().size() == 1) {
                SearchResult searchResult = new SearchResult(elements.get().get(0), query);
                return searchResult;
            }

            if (elements.get().size() > 1) {
                SearchResult searchResult = new SearchResult(getElementByPath(elements.get()), query);
                searchResult.setPathCompared(true);
                return searchResult;
            }
        }

        return new SearchResult();
    }

    private Element getElementByPath(Elements elements) {

        int elementNumber = 0;
        int minDifference = 0;
        for (int i = 0; i < elements.size(); i++) {
            int difference = getPathSimilarity(elements.get(i));
            if (difference <= minDifference) {
                minDifference = difference;
                elementNumber = i;
            }
        }

        return elements.get(elementNumber);
    }

    private int getPathSimilarity(Element element) {
        Elements parents = element.parents();
        Collections.reverse(parents);

        Elements parentsSource = sourceElement.parents();
        Collections.reverse(parentsSource);

        Element parent = element.parents().last();
        Element parentSource = sourceElement.parents().last();

        int difference = 0;
        int i;
        for (i = 0; i < element.parents().size(); i++) {
            if (!parent.nodeName().equals(parentSource.nodeName())
                    || !parent.attributes().equals(parentSource.attributes()))
                break;

            parent = parents.get(i);
            parentSource = parentsSource.get(i);

            if (parents.size() > parentsSource.size() && i == parentsSource.size() - 1) {
                difference = parents.size() - parentsSource.size();
                break;
            }
            difference = element.parents().size() - i;
        }

        return difference;
    }

    private String getSearchCriteria(String name, String value) {
        return String.format("[%s=\"%s\"]", name, value);
    }

    private String getQuery(String tag, List<String> searchCriterias) {
        StringBuilder sb = new StringBuilder();
        sb.append(tag);
        searchCriterias.forEach(c -> sb.append(c));
        return sb.toString();
    }

    // this is not original method
    private List<List<String>> combination(List<String> values, int size) {

        if (0 == size) {
            return Collections.singletonList(Collections.emptyList());
        }

        if (values.isEmpty()) {
            return Collections.emptyList();
        }

        List<List<String>> combination = new LinkedList<List<String>>();

        String actual = values.get(0);

        List<String> subSet = new LinkedList<String>(values);
        subSet.remove(actual);

        List<List<String>> subSetCombination = combination(subSet, size - 1);

        for (List<String> set : subSetCombination) {
            List<String> newSet = new LinkedList<String>(set);
            newSet.add(0, actual);
            combination.add(newSet);
        }

        combination.addAll(combination(subSet, size));

        return combination;
    }
}
