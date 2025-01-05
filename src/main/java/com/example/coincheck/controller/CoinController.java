package com.example.coincheck.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.coincheck.model.RequestedCheck;

import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/coin")
public class CoinController {
	// list of all possible coin denominations
	private double[] possibledenominations = {0.01, 0.05, 0.1, 0.2, 0.5, 1, 2, 5, 10, 50, 100, 1000};
	
	// method to accept the api request
	@PostMapping("/check")
	public ResponseEntity<?> check(@Valid @RequestBody RequestedCheck input, BindingResult bindingResult){
		// check if there is any validation error in requestbody
		if(bindingResult.hasErrors()) {
			Map<String,String> errors = new HashMap<>();
			bindingResult.getFieldErrors().forEach(error ->
			errors.put(error.getField(), error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(errors);
		}
		// check if the coin denomination is empty
		if(input.getCoindenominations() == null || input.getCoindenominations().length == 0) {
			return ResponseEntity.badRequest().body("Coin denominations cannot be null or empty.");
		}
		// return the response message
		return ResponseEntity.ok(getcoinlist(input.getTargetamount(), input.getCoindenominations()));
	}
	
	// method to make coinlist
	public String getcoinlist(double targetamount, Double[] coindenominations){
		List<Double> coinlist = new ArrayList<Double>();
		// sorting the array descending to start checking from biggest coin
		Arrays.sort(coindenominations, Collections.reverseOrder());
		double remainingamount = targetamount;
		for (double coinvalue: coindenominations) {
			// make most of the target amount with the biggest coins as much as possible
			while (remainingamount >= coinvalue) { 
				remainingamount = Math.round((remainingamount - coinvalue)*100.0)/100.0;
				coinlist.add(coinvalue);
			}
			// check if the remaining amount is less than the smallest coin value
			if (remainingamount < coindenominations[coindenominations.length-1]) {
				break;
			}
		}
		// if all target amount is assigned to coins return the coin list
		if (remainingamount == 0) {
			Collections.sort(coinlist);
			return String.format("The list of minimum number of coins is %s", coinlist.toString());
		} else { // if not, need to return the required coin denominations
			coinlist = getsuggestedcoins(remainingamount);
			Collections.sort(coinlist);
			return String.format("The target amount is not achievable using the selected coin denominations. Suggested to add the following coins. %s", coinlist.toString());
		}
	}
	
	// method to make suggested coins
	public List<Double> getsuggestedcoins (double remainingamount) {
		List<Double> coinlist = new ArrayList<Double>();
		double newremainingamount = remainingamount;
		// check in reverse for all possible denomination to start checking from biggest possible coin
		for (int i = possibledenominations.length - 1; i >= 0; i--)
		{
			// make most of the remaining amount with the biggest coins as much as possible
			while (newremainingamount >= possibledenominations[i]) {
				newremainingamount = Math.round((newremainingamount - possibledenominations[i])*100.0)/100.0;
				coinlist.add(possibledenominations[i]);
			}
		}
		// extract the unique coin values to send back the suggestion
		List<Double> uniquecoins = coinlist.stream().distinct().collect(Collectors.toList());
		return uniquecoins;
	}
}
