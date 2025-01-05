package com.example.coincheck.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class RequestedCheck {
	@NotNull(message = "amount is required")
	@DecimalMax(value = "10000.00", message="amount can not be more than 10k")
	@DecimalMin(value = "0.00", message="amount can not be less than 0")
	private Double targetamount;
	@NotNull(message = "choose at least 1")
	private Double[] coindenominations;
	
	public Double getTargetamount() {
		return targetamount;
	}
	public void setTargetamount(Double targetamount) {
		this.targetamount = targetamount;
	}
	public Double[] getCoindenominations() {
		return coindenominations;
	}
	public void setCoindenominations(Double[] coindenominations) {
		this.coindenominations = coindenominations;
	}
}
