package com.kindredgroup.unibetlivetest.repository;

import com.kindredgroup.unibetlivetest.entity.Customer;
import com.kindredgroup.unibetlivetest.entity.Selection;
import com.kindredgroup.unibetlivetest.types.SelectionState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SelectionRepository extends JpaRepository<Selection, Integer> {

    List<Selection> getSelectionByStateEquals(SelectionState state);

    @Query(value = """
    SELECT distinct
        *
    FROM Selection s

    LEFT JOIN Market m
    ON s.market_id = m.id

    LEFT JOIN Event e
    ON m.event_id = e.id

    WHERE s.state = :selectionState
        and e.id = :eventId
    """,
            nativeQuery = true)
    List<Selection> getEventSelectionsByState(Long eventId, String selectionState);

    @Query(value = """
    SELECT distinct
        *
    FROM Selection s

    LEFT JOIN Market m
    ON s.market_id = m.id

    LEFT JOIN Event e
    ON m.event_id = e.id

    WHERE e.id = :eventId
    """,
            nativeQuery = true)
    List<Selection> getEventSelections(Long eventId);

    Optional<Selection> getSelectionById(Long selectionId);

    @Query(value = """
    SELECT * 
    FROM Selection s
    WHERE s.id in (
        SELECT distinct
            sBis.id
        FROM Selection sBis
    
        JOIN Bet b
        ON b.selection_id = sBis.id
    
        WHERE sBis.state = 'CLOSED' 
        AND b.state IS NULL
    )
    """,
            nativeQuery = true)
    List<Selection> fetchClosedSelectionsWithUnpaidBets();
}
