package com.jrp.pma.dao;

import com.jrp.pma.entities.Star;
import com.jrp.pma.utils.AsyncConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public interface StarRepository extends PagingAndSortingRepository<Star, Long> {
    // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
    //https://www.baeldung.com/spring-data-jpa-pagination-sorting

    @Async(AsyncConfiguration.TASK_EXECUTOR_REPOSITORY)
    CompletableFuture<Page<Star>> findAllBy(final Pageable pageable);

    @Async(AsyncConfiguration.TASK_EXECUTOR_REPOSITORY)
    CompletableFuture<Star> findOneById(final String id);
}
