package com.kindredgroup.unibetlivetest.service;

import com.kindredgroup.unibetlivetest.entity.Customer;
import com.kindredgroup.unibetlivetest.exception.CustomException;
import com.kindredgroup.unibetlivetest.repository.CustomerRepository;
import com.kindredgroup.unibetlivetest.types.ExceptionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.lang.String.format;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Customer findCustomerByPseudo(String pseudo) {
        return customerRepository.getCustomerByPseudo(pseudo).orElseThrow(() -> new CustomException(format("customer %s not found", pseudo), ExceptionType.CUSTOMER_NOT_FOUND));
    }

    // Remove Bet amount from customer balance and save changes to database
    public void adjustBalanceAfterPlacedBet(BigDecimal amount, Customer customer) {
        customer.setBalance(customer.getBalance().subtract(amount));
        customerRepository.save(customer);
    }

    public void adjustBalanceAfterWonBet(
            BigDecimal amount,
            BigDecimal odd,
            Customer customer
    ) {
        // We multiply the amount placed in the bet by the odd to find how
        // Much we need to add to the user balance
        // Scale was chose arbitrary
        // Todo Discuss scale
        BigDecimal amountToPay = amount.multiply(odd).setScale(2, RoundingMode.HALF_UP);
        customer.setBalance(customer.getBalance().add(amountToPay));
        customerRepository.save(customer);
    }
}
