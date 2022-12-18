package com.kindredgroup.unibetlivetest.service;

import com.kindredgroup.unibetlivetest.entity.Selection;
import com.kindredgroup.unibetlivetest.exception.CustomException;
import com.kindredgroup.unibetlivetest.repository.SelectionRepository;
import com.kindredgroup.unibetlivetest.types.ExceptionType;
import com.kindredgroup.unibetlivetest.types.SelectionState;
import com.kindredgroup.unibetlivetest.utils.Helpers;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
@Log4j2
public class SelectionService {

    private final SelectionRepository selectionRepository;

    /**
     * 1. Récupère toute les selections ouvertes
     * 2. Mis à jour la cote aléatoirement
     */

    public Long updateOddsRandomly() {
        final List<Selection> selectionsOpened = selectionRepository.getSelectionByStateEquals(SelectionState.OPENED);
        List<Selection> selections = selectionsOpened.isEmpty() ? List.of() : selectionsOpened
                .stream()
                .map(selection -> {
                    selection.setCurrentOdd(Helpers.updateOddRandomly(selection.getCurrentOdd()));
                    return selectionRepository.save(selection);
                })
                .toList();
        return (long) selections.size();
    }

    /**
     * 1. Récupère toute les selections ouvertes
     * 2. Ferme 5 sélections aléatoirement.
     */

    public Long closeOddsRandomly() {
        final List<Selection> selectionsOpened = selectionRepository.getSelectionByStateEquals(SelectionState.OPENED);
        List<Selection> selections = selectionsOpened.isEmpty() ? List.of() : IntStream
                .range(0, 5)
                .mapToObj(i -> selectionRepository.save(
                        selectionsOpened.get(Helpers.getRandomIndex(0, selectionsOpened.size()))
                                .setState(SelectionState.CLOSED)
                                .setResult(Helpers.setResultRandomly())))
                .toList();
        return (long) selections.size();
    }

    public List<Selection> getEventSelections(Long eventId, SelectionState selectionState) {
        if (selectionState != null) {
            return selectionRepository.getEventSelectionsByState(eventId, selectionState.name());
        } else {
            return selectionRepository.getEventSelections(eventId);
        }
    }

    public Selection getSelectionById(Long selectionId) {
        return selectionRepository
                .getSelectionById(selectionId)
                .orElseThrow(() -> CustomException.of(
                        ExceptionType.SELECTION_NOT_FOUND,
                        "Selection not found"
                ));
    }

    public List<Selection> fetchClosedSelectionsWithUnpaidBets() {
        return selectionRepository
                .fetchClosedSelectionsWithUnpaidBets();
    }
}
