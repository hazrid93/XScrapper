package com.jrp.pma.services;

import com.jrp.pma.dao.VideoRepository;
import com.jrp.pma.entities.Video;
import com.jrp.pma.utils.AsyncConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class VideoService {

    @Autowired
    VideoRepository videoRepository;

    @Async(AsyncConfiguration.TASK_EXECUTOR_SERVICE)
    public CompletableFuture<Page<Video>> findAll(final Pageable pageable) {
        return videoRepository.findAllBy(pageable);
    }
    @Async(AsyncConfiguration.TASK_EXECUTOR_SERVICE)
    public CompletableFuture<Optional<Video>> findOneById(final String id) {
        return videoRepository
                .findOneById(id)
                .thenApply(Optional::ofNullable);
    }




}
