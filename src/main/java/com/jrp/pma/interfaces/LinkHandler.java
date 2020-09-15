package com.jrp.pma.interfaces;

public interface LinkHandler {

    /**
     * Return the size of visited links
     * @return The size of visited links
     */
    public int size();

    public boolean visited(String link);

    public void addAsVisited(String link);
}
