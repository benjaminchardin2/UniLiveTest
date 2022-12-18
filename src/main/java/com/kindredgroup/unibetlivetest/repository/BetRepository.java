package com.kindredgroup.unibetlivetest.repository;

import com.kindredgroup.unibetlivetest.entity.Bet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BetRepository extends JpaRepository<Bet, Long> {

    boolean existsByCustomerIdAndBetStateNull(Long customerId);

    List<Bet> findBySelectionIdAndBetStateNull(Long selectionId);
}
