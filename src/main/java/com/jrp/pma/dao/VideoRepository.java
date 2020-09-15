package com.jrp.pma.dao;

import com.jrp.pma.controllers.RestController;
import com.jrp.pma.entities.Video;
import com.jrp.pma.utils.AsyncConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.concurrent.CompletableFuture;

// spring transaction management with async https://declara.com/content/eaL8p2aO
//https://dzone.com/articles/concurrency-and-locking-with-jpa-everything-you-ne
// https://stackoverflow.com/questions/10394857/how-to-use-transactional-with-spring-data
@Repository
public interface VideoRepository extends PagingAndSortingRepository<Video, Long> {
    // paging: http://www.programmersought.com/article/1633114063/
    // https://www.baeldung.com/spring-transactional-propagation-isolation

    static final Logger log = LoggerFactory.getLogger(VideoRepository.class);
    // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
    //https://www.baeldung.com/spring-data-jpa-pagination-sorting
    // https://www.baeldung.com/spring-data-derived-queries

    @Async(AsyncConfiguration.TASK_EXECUTOR_REPOSITORY)
    CompletableFuture<Page<Video>> findAllBy(final Pageable pageable);

    @Async(AsyncConfiguration.TASK_EXECUTOR_REPOSITORY)
    CompletableFuture<Video> findOneById(final String id);

    /*
    @Transactional
    public <S extends Video> S save(S entity); */

    default <S extends Video> S saveCustom(S entity) {
        log.info("saveCustom is called");
        save(entity);
        return entity;
    }

    //https://stackoverflow.com/questions/34973306/why-is-spring-data-jpa-async-not-working
    // https://stackoverflow.com/questions/11881479/how-do-i-update-an-entity-using-spring-data-jpa

}
