package com.kindredgroup.unibetlivetest.batchs;


import com.kindredgroup.unibetlivetest.entity.Bet;
import com.kindredgroup.unibetlivetest.entity.Selection;
import com.kindredgroup.unibetlivetest.service.BetService;
import com.kindredgroup.unibetlivetest.service.SelectionService;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Log4j2
@RequiredArgsConstructor
public class MarketBatch {

    private final SelectionService selectionService;
    private final BetService betService;

    @Scheduled(fixedRateString = "${market.batch.rate}")
    @Synchronized
    public void payerLesParis() {
        Long startTimeInMillis = Instant.now().toEpochMilli();
        log.info("starting payerLesParis job execution");
        List<Selection> selectionsToPay = selectionService.fetchClosedSelectionsWithUnpaidBets();
        AtomicInteger linesTreated = new AtomicInteger();
        log.info("{} selections(s) to pay.", selectionsToPay.size());
        selectionsToPay.forEach(
                selection -> {
                    List<Bet> betsToPay = betService.fetchBetsToPay(selection.getId());
                    log.info("{} bet(s) to pay for selection {}.", betsToPay.size(), selection.getName());
                    betsToPay.forEach(
                            bet -> {
                                try {
                                    betService.payBetIfNeeded(bet, selection);
                                    linesTreated.getAndIncrement();
                                    log.info("bet {} was successfully paid", bet.getId());
                                } catch (Exception e) {
                                    log.error("error while paying bet {}", bet.getId(), e);
                                }
                            }
                    );
                }
        );
        log.info("{} bet(s) were successfully paid", linesTreated);
        Long endTimeInMillis = Instant.now().toEpochMilli();
        log.info("ending payerLesParis job execution after : {} ms", endTimeInMillis - startTimeInMillis);
    }

}
