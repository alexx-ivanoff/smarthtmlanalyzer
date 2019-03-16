package com.abinc.smarthtmlanalyzer;

import org.jsoup.nodes.Element;

/**
 * Created by Alex on 16.03.2019.
 */
public class SearchResult {
    private Element element;
    private String searchQuery;
    private boolean pathCompared;

    public SearchResult(Element element, String searchQuery)    {
        this.element = element;
        this.searchQuery = searchQuery;
    }

    public SearchResult()    {
        this.element = null;
        this.searchQuery = "";
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public Element getElement() {
        return element;
    }

    public boolean getPathCompared() {
        return pathCompared;
    }

    public void setPathCompared(boolean pathCompared) {
        this.pathCompared = pathCompared;
    }
}
