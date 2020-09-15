package com.jrp.pma.action;

import com.jrp.pma.entities.Video;
import com.jrp.pma.model.XSource_Content;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

/*
    This class might seem redundant but keeping it for future use in case i'm doing more heavy lifting
 */
public class XSource_LinkContentProcessor extends RecursiveTask<XSource_Content> {

    private static final Logger log = LoggerFactory.getLogger(XSource_LinkContentProcessor.class);
    private Element contentBlocks;
    private int page;

    public XSource_LinkContentProcessor() {
    }

    public XSource_LinkContentProcessor(Element contentBlocks, int page) {
        this.contentBlocks = contentBlocks;
        this.page = page;
    }

    @Override
    protected XSource_Content compute() {
        XSource_Content XSource_content = null;
        try {
            XSource_content = new XSource_Content.XSource_Content_Builder(contentBlocks).build();
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return XSource_content;
    }
}
