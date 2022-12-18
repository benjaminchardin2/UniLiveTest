package com.kindredgroup.unibetlivetest.repository;

import com.kindredgroup.unibetlivetest.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = """
    SELECT * 
    FROM Event e
    WHERE e.id in (
        SELECT distinct
            eBis.id
        FROM Event eBis
    
        LEFT JOIN Market m
        ON m.event_id = eBis.id

        LEFT JOIN Selection s
        ON s.market_id = m.id
    
        WHERE s.state = 'OPENED'
            and eBis.start_date < CURRENT_TIMESTAMP()
    )        
    ORDER BY e.start_date ASC
    """,
            nativeQuery = true)
    // An event is consider live if it has started
    // and at least one selection of one of its market
    // is opened
    List<Event> findLiveEvents();
}
