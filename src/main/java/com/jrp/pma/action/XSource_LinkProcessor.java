package com.jrp.pma.action;

import com.jrp.pma.interfaces.LinkHandler;
import com.jrp.pma.model.XSource_Content;
import com.jrp.pma.utils.XSource_Constants;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;


public class XSource_LinkProcessor extends RecursiveTask<Collection<XSource_Content>> {

    private static final Logger log = LoggerFactory.getLogger(XSource_LinkProcessor.class);

   // @Autowired
  //  private XSource_LinkContentProcessor linkContentProcessor;

    private String url;
    private LinkHandler link;
    private int page;

    public XSource_LinkProcessor() {
    }

    public XSource_LinkProcessor(String url, LinkHandler link, int page) {
        this.url = url;
        this.link = link;
        this.page = page;
    }

   // http://tutorials.jenkov.com/java-util-concurrent/java-fork-and-join-forkjoinpool.html
    @Override
    protected Collection<XSource_Content> compute() {
        String currentUrl = url;
        if(page > 1) {
            currentUrl = url + "&page=" + page;
        }

        Collection<XSource_Content> XSourceContent = new CopyOnWriteArrayList<XSource_Content>();

        try {
            log.debug("current page: " + currentUrl + ", thread: " + Thread.currentThread().getName() );
            log.debug("thread: " + Thread.currentThread().getName() + ", page: " + page);

            Connection.Response connectionResponse = Jsoup.connect(currentUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36")
                    .referrer("http://www.google.com")
                    .timeout(5000)
                    // .cookie("RNKEY", "1044737*1255609:2620068738:1115668686:1")
                    .method(Connection.Method.GET)
                    .execute();

            Document htmlBody = connectionResponse.parse();
            Element htmlULForResult = htmlBody.select(XSource_Constants.XSource_UL_CSS_SELECT).first();
            Elements contentLIBlock = htmlULForResult.select(XSource_Constants.XSource_LI_CSS_SELECT);

            List<XSource_LinkContentProcessor> listAction = new ArrayList<XSource_LinkContentProcessor>();
            for (Element contentBlocks : contentLIBlock) {
                // Call XSource_Content builder static class, to extract the data
                listAction.add(new XSource_LinkContentProcessor(contentBlocks, page));
            }

            for(XSource_LinkContentProcessor XSource_linkContentProcessor: listAction){
                XSource_linkContentProcessor.fork();
            }

            for(XSource_LinkContentProcessor XSource_linkContentProcessor: listAction){
                XSourceContent.add(XSource_linkContentProcessor.join());
            }

            return XSourceContent;

        } catch (Exception e) {
            // do nothing.
            log.error(e.getMessage());
        }

        return XSourceContent;
    }
}
