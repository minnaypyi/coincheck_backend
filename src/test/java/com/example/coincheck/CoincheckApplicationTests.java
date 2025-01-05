package com.example.coincheck;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.coincheck.controller.CoinController;
import com.example.coincheck.model.RequestedCheck;

@SpringBootTest
class CoincheckApplicationTests {

	@Test
	public void testCoinCalculation() {
	    // When
	    RequestedCheck input = new RequestedCheck();
	    input.setTargetamount(5.0);
	    input.setCoindenominations(new Double[]{2.0, 0.5, 1.0});
	    CoinController coinController = new CoinController();
	    String result = coinController.getcoinlist(input.getTargetamount(), input.getCoindenominations());
	    
	    // Then
	    assertEquals("The list of minimum number of coins is [1.0, 2.0, 2.0]", result);
	    assertNotEquals("The list of minimum number of coins is [2.0, 2.0, 1.0]", result);
	}
	
	@Test
	public void testSuggestedCoins() {
	    // When
	    double remainingAmount = 7.0;
	    CoinController coinController = new CoinController();
	    
	    // Get suggested coins for the remaining amount
	    List<Double> result = coinController.getsuggestedcoins(remainingAmount);
	    
	    // Then
	    assertTrue(result.contains(5.0));
	    assertTrue(result.contains(2.0));
	    assertFalse(result.contains(0.1)); // Ensure smaller denominations are not included
	}
	
	@Test
	public void testZeroTargetAmount() {
	    // When
	    RequestedCheck input = new RequestedCheck();
	    input.setTargetamount(0.0);
	    input.setCoindenominations(new Double[]{1.0, 0.5, 2.0});
	    
	    // Then
	    CoinController coinController = new CoinController();
	    String result = coinController.getcoinlist(input.getTargetamount(), input.getCoindenominations());
	    assertEquals("The list of minimum number of coins is []", result);
	}

}
