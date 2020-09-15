package com.jrp.pma.services;

import com.jrp.pma.action.WebCrawler;
import com.jrp.pma.dao.VideoRepository;
import com.jrp.pma.entities.Video;
import com.jrp.pma.model.XSource_Content;
import com.jrp.pma.utils.AppProperties;
import com.jrp.pma.utils.AsyncConfiguration;
import com.jrp.pma.utils.XSource_Constants;
import net.bytebuddy.implementation.bytecode.Throw;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Component
public class XSource_CrawlerService {
    private static final Logger log = LoggerFactory.getLogger(XSource_CrawlerService.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private AppProperties applicationProperties;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    VideoRepository videoRepository;

    @Autowired
    @Qualifier(AsyncConfiguration.TASK_EXECUTOR_FORK)
    private Executor forkExecutor;

    @Scheduled(fixedDelay = XSource_Constants.XSource_MAX_SCRAP_DELAY )
    public void fetchVideoData() {
        log.info("The time is now {}", dateFormat.format(new Date()) + ", thread : " + Thread.currentThread().getName());
        startCrawler();
        // TODO: remove this later

        CompletableFuture<Void> result = CompletableFuture.runAsync(() -> {
           startCrawler();
        }, forkExecutor).exceptionally((ex) -> {
            log.error(ex.getMessage());
            return null;
        });

    }

    private void startCrawler(){
        int pageIdx = 0;
        Collection<XSource_Content> object = null;
        // parse <XSource_Constants.XSource_MAX_SCRAP_PAGE> pages (throttled so that no ip banned this application. seems like 15sec interval querying 5 pages is ok.
        while (pageIdx != XSource_Constants.XSource_MAX_SCRAP_PAGE) {
            try {
                pageIdx = pageIdx + 5; // 5 page at a time by forkjoin pool
                //TODO if change webcrawler to @component how to always get a new instance? need research
                object = new WebCrawler(XSource_Constants.XSource_WEBSITE_URI + XSource_Constants.XSource_SEARCH_URI, applicationProperties.getAsync().getForkThreadSize(), pageIdx).startScrapping();
                save(object);
            } catch (Exception e){
                log.error(e.getMessage());
            }
        }
    }

    private void save(Collection<XSource_Content> object){
        try {
            for (Object item : object) {
                if(item instanceof XSource_Content) {
                    XSource_Content XSource_content = (XSource_Content)item;
                    log.debug("Object contains: {}", item.toString());
                    // Video video = modelMapper.map(item, Video.class);
                    Video video = new Video();
                    video.setViewsCount(XSource_content.getViewsCount());
                    video.setDuration(XSource_content.getDuration());
                    video.setUserUrl(XSource_content.getUserUrl());
                    video.setVideoUrl(XSource_content.getVideoUrl());
                    video.setImgUrl(XSource_content.getImgUrl());
                    video.setTitle(XSource_content.getTitle());
                    videoRepository.saveCustom(video);
                }
            }
        } catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
