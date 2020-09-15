package com.jrp.pma.controllers;

import com.jrp.pma.entities.Video;
import com.jrp.pma.services.VideoService;
import com.jrp.pma.utils.AsyncConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/")
public class RestController {
    // more about CompletableFuture https://medium.com/@ronikbasak/completablefuture-a-new-era-of-asynchronous-programming-86c2fe23e246
    // more about CompletableFuture https://www.javacodegeeks.com/2013/05/java-8-definitive-guide-to-completablefuture.html
    // more about CompletableFuture https://www.oracle.com/technical-resources/articles/java/architect-streams-pt2.html
    // explanation about flatmap https://www.java67.com/2016/03/how-to-use-flatmap-in-java-8-stream.html

    private static final Logger log = LoggerFactory.getLogger(RestController.class);
    private static final String REQUEST_PATH_API_VIDEOS_INDIVIDUAL_VIDEO = "/{videoId}";

    private final VideoService videoService;

    @Autowired
    public RestController(VideoService videoService) {
        this.videoService = videoService;
    }

    @Async(AsyncConfiguration.TASK_EXECUTOR_CONTROLLER)
    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CompletableFuture<ResponseEntity> getVideos(final Pageable paging) {
        return videoService
                .findAll(paging)
                .<ResponseEntity>thenApply(ResponseEntity::ok)
                .exceptionally(handleGetVideosFailure);
    }

    @Async(AsyncConfiguration.TASK_EXECUTOR_CONTROLLER)
    @GetMapping(value = REQUEST_PATH_API_VIDEOS_INDIVIDUAL_VIDEO,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CompletableFuture<ResponseEntity> getVideo(@PathVariable final String videoId) {

        return videoService
                .findOneById(videoId)
                .thenApply(mapMaybeVideoToResponse)
                .exceptionally(handleGetVideoFailure.apply(videoId));
    }

    private static Function<Throwable, ResponseEntity> handleGetVideosFailure = throwable -> {
        log.error("Unable to retrieve videos", throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    };

    private static Function<Optional<Video>, ResponseEntity> mapMaybeVideoToResponse = maybeVideo -> maybeVideo
            .<ResponseEntity>map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());

    private static Function<String, Function<Throwable, ResponseEntity>> handleGetVideoFailure = videoId -> throwable -> {
        log.error(String.format("Unable to retrieve video for id: %s", videoId), throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    };
}
