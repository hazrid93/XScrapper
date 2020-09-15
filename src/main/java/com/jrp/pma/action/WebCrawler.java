package com.jrp.pma.action;

import com.jrp.pma.dao.VideoRepository;
import com.jrp.pma.entities.Video;
import com.jrp.pma.interfaces.LinkHandler;
import com.jrp.pma.model.XSource_Content;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ForkJoinPool;


/**
 * This will be the starting class to start the scrapper processes.
 */

public class WebCrawler implements LinkHandler {
    // https://stackoverflow.com/questions/31757216/spring-cannot-propagate-transaction-to-forkjoins-recursiveaction
    private static final Logger log = LoggerFactory.getLogger(WebCrawler.class);

    //private final Collection<String> visitedLinks = Collections.synchronizedSet(new HashSet<String>());
    private final Collection<String> visitedLinks = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
    private String url;
    private ForkJoinPool mainPool;
    private int page;

    public WebCrawler() {
    }

    public WebCrawler(String startURL, int threadCount, int page) {
        this.url = startURL;
        this.page = page;
        mainPool = new ForkJoinPool(threadCount);
    }

    public Collection<XSource_Content> startScrapping(){
        Collection<XSource_Content> object = null;
        for(int i=1; i<=page; i++){
            object =  mainPool.invoke(new XSource_LinkProcessor(this.url,this, i));
        }
        return object;
    }

    public int size() {
        return visitedLinks.size();
    }

    public boolean visited(String link) {
        return visitedLinks.contains(link);
    }

    public void addAsVisited(String link) {
        visitedLinks.add(link);
    }
}

