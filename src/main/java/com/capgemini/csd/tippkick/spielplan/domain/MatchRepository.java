package com.capgemini.csd.tippkick.spielplan.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByStarttimeGreaterThan(Instant starttime);
}
