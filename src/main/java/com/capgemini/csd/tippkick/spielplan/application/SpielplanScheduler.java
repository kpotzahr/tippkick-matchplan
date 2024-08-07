package com.capgemini.csd.tippkick.spielplan.application;


import com.capgemini.csd.tippkick.spielplan.domain.Match;
import com.capgemini.csd.tippkick.spielplan.domain.MatchRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ScheduledFuture;

@Component
@Slf4j
class SpielplanScheduler {
    @Autowired
    private TaskScheduler taskScheduler;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private MatchService matchService;
    private Set<ScheduledFuture<?>> futureTasks = new HashSet<>();

    @PostConstruct
    public void init() {
        this.refreshScheduler();
    }

    /**
     * Refresh scheduler for all matches that have not happened yet. Refresh has to be called
     * after application start and after match import as the scheduled tasks are ephemeral.
     */
    void refreshScheduler() {
        log.info("Refreshing Spielplan scheduler");
        futureTasks.forEach(f -> f.cancel(false));

        matchRepository
                .findByStarttimeGreaterThan(Instant.now())
                .forEach(m -> futureTasks.add(scheduleApplicationEventRunnable(m)));
    }

    private ScheduledFuture<?> scheduleApplicationEventRunnable(Match m) {
        return taskScheduler.schedule(
                () -> matchService.onMatchStarted(m), m.getStarttime()
        );
    }
}
