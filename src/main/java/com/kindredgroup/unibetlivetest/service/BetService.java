package com.kindredgroup.unibetlivetest.service;

import com.kindredgroup.unibetlivetest.dto.BetDto;
import com.kindredgroup.unibetlivetest.entity.Bet;
import com.kindredgroup.unibetlivetest.entity.Customer;
import com.kindredgroup.unibetlivetest.entity.Selection;
import com.kindredgroup.unibetlivetest.exception.CustomException;
import com.kindredgroup.unibetlivetest.exception.CustomNonStandardException;
import com.kindredgroup.unibetlivetest.repository.BetRepository;
import com.kindredgroup.unibetlivetest.types.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class BetService {
    private final BetRepository betRepository;
    private final SelectionService selectionService;
    private final CustomerService customerService;

    // Throw an exception if customer already has a bet ongoing
    private void verifyCustomerHasNoOngoingBet(Long customerId) {
        if (betRepository.existsByCustomerIdAndBetStateNull(customerId)) {
            throw CustomException.of(
                    ExceptionType.ON_GOING_BET,
                    "Customer already placed a Bet"
            );
        }
    }

    // Throw an exception if selection is closed
    // Or if the user placed the bet with a different odd
    // than the current one
    private void verifySelectionInformationsAreValid(BetDto betDto, Selection selection) {
        if (selection.getState().equals(SelectionState.CLOSED)) {
            throw CustomNonStandardException.of(
                    NonStandardExceptionType.SELECTION_CLOSED,
                    "Selection is closed"
            );
        }
        if (!selection.getCurrentOdd().equals(betDto.getOdd())) {
            throw CustomNonStandardException.of(
                    NonStandardExceptionType.ODDS_CHANGED,
                    "Odds have changed"
            );
        }
    }

    // Throws an exception if customer has insufficient
    // funds to place the bet
    private static void verifyCustomerHasSufficientFunds(
            BigDecimal customerBalance,
            BigDecimal betAmount
            ) {
        if(customerBalance.compareTo(betAmount) < 0) {
            throw CustomNonStandardException.of(
                    NonStandardExceptionType.INSUFICIENT_FUNDS,
                    "Insufficient funds"
            );
        }
    }

    private Bet createBetFromBetDto(BetDto betDto, Selection selection, Customer customer) {
       Bet bet = new Bet();
       return bet.setBetState(null)
               .setDate(Date.from(Instant.now()))
               .setOdd(betDto.getOdd())
               .setAmount(betDto.getAmount())
               .setSelection(selection)
               .setCustomer(customer);
    }

    @Transactional
    public void placeBet(BetDto betDto) {
        Customer customer = customerService.findCustomerByPseudo("unibest");
        verifyCustomerHasNoOngoingBet(customer.getId());
        verifyCustomerHasSufficientFunds(customer.getBalance(), betDto.getAmount());

        Selection selection = selectionService.getSelectionById(betDto.getSelectionId());
        verifySelectionInformationsAreValid(betDto, selection);

        Bet bet = createBetFromBetDto(betDto, selection, customer);
        betRepository.save(bet);
        customerService.adjustBalanceAfterPlacedBet(betDto.getAmount(), customer);
    }

    public List<Bet> fetchBetsToPay(Long selectionId) {
        return betRepository.findBySelectionIdAndBetStateNull(selectionId);
    }

    @Transactional
    public void payBetIfNeeded(Bet bet, Selection selection) {
        if (selection.getResult().equals(SelectionResult.LOST)) {
            bet.setBetState(BetState.LOST);
        } else {
            bet.setBetState(BetState.WON);
            customerService.adjustBalanceAfterWonBet(
                    bet.getAmount(),
                    bet.getOdd(),
                    bet.getCustomer()
            );
        }
        betRepository.save(bet);
    }
}
