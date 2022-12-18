package com.kindredgroup.unibetlivetest.service;

import com.kindredgroup.unibetlivetest.entity.Event;
import com.kindredgroup.unibetlivetest.entity.Selection;
import com.kindredgroup.unibetlivetest.exception.CustomException;
import com.kindredgroup.unibetlivetest.repository.EventRepository;
import com.kindredgroup.unibetlivetest.types.ExceptionType;
import com.kindredgroup.unibetlivetest.types.SelectionState;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final SelectionService selectionService;

    public List<Event> getEventsByState(Boolean isLive) {
        List<Event> events;
        if (Boolean.TRUE.equals(isLive)) {
            events = eventRepository.findLiveEvents();
        } else {
            events = eventRepository.findAll();
        }
        if (events.isEmpty()) {
            throw CustomException.of(
                    ExceptionType.NO_CONTENT,
                    "No content"
            );
        }
        return events;
    }

    public Optional<Event> findEventById(Long eventId) {
        return eventRepository.findById(eventId);
    }

    public List<Selection> getEventSelections(Long eventId, SelectionState selectionState) {
        Optional<Event> optionalEvent = this.findEventById(eventId);
        // If the event found is null we return a 404 HTTP error
        Event event = optionalEvent.orElseThrow(
                () -> CustomException.of(
                        ExceptionType.EVENT_NOT_FOUND,
                        "The specified event was not found"
                )
        );
        List<Selection> selections = selectionService.getEventSelections(event.getId(), selectionState);
        // In case of empty array result we want the webservice
        // To return a 204 HTTP response
        if (selections.isEmpty()) {
            throw CustomException.of(
                    ExceptionType.NO_CONTENT,
                    "No content"
            );
        }
        return selections;
    }
}
