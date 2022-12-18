package com.kindredgroup.unibetlivetest.api;

import com.kindredgroup.unibetlivetest.dto.BetDto;
import com.kindredgroup.unibetlivetest.entity.Customer;
import com.kindredgroup.unibetlivetest.service.BetService;
import com.kindredgroup.unibetlivetest.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Urls.BASE_PATH)
@Log4j2
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BetApi {

    private final BetService betService;

    @Operation(summary = "Allow user to place a bet")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @ApiResponse(responseCode = "404", description = "Selection not found")
    @ApiResponse(responseCode = "409", description = "User already has a bet going on")
    @ApiResponse(responseCode = "500", description = "Server Error")
    @ApiResponse(responseCode = "600", description = "Insufficient funds")
    @ApiResponse(responseCode = "601", description = "Odds have changed")
    @ApiResponse(responseCode = "602", description = "Selection closed")
    @PostMapping(Urls.ADD_BET)
    public void placeBet(
            @RequestBody BetDto betDto
            ) {
        betService.placeBet(betDto);
    }

}
