package com.kindredgroup.unibetlivetest.api;

import com.kindredgroup.unibetlivetest.entity.Event;
import com.kindredgroup.unibetlivetest.entity.Selection;
import com.kindredgroup.unibetlivetest.exception.CustomException;
import com.kindredgroup.unibetlivetest.service.EventService;
import com.kindredgroup.unibetlivetest.types.ExceptionType;
import com.kindredgroup.unibetlivetest.types.SelectionState;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Log4j2
@RequestMapping(Urls.BASE_PATH)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EventApi {

    private final EventService eventService;

    @Operation(summary = "Allow user to retrieve events by state")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "204", description = "No Live events")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "500", description = "Server Error")
    @GetMapping(Urls.EVENTS)
    public List<Event> fetchEvents(
            @Parameter(description = "événements live", example = "true")
            @RequestParam(required = false) final Boolean isLive
    ) {
        return eventService.getEventsByState(isLive);
    }

    @Operation(summary = "Allow user to retrieve selections for event")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "204", description = "No Content")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "404", description = "Event not found")
    @ApiResponse(responseCode = "500", description = "Server Error")
    @GetMapping(Urls.SELECTIONS)
    public List<Selection> fetchEventSelections(
            @PathVariable() final Long id,
            @Parameter(description = "état de la selection", example = "OPENED")
            @RequestParam(required = false) final SelectionState state
            ) {
        return eventService.getEventSelections(id, state);
    }

}
