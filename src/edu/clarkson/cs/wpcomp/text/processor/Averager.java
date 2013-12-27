package edu.clarkson.cs.wpcomp.text.processor;

import java.math.BigDecimal;

public class Averager {

	private BigDecimal sum;

	private int count;

	public Averager(BigDecimal start) {
		this.sum = start;
		this.count = 1;
	}

	public void inc(BigDecimal inc) {
		this.sum = sum.add(inc);
		this.count += 1;
	}

	public BigDecimal getAverage() {
		return sum.divide(BigDecimal.valueOf(count), 2,
				BigDecimal.ROUND_HALF_UP);
	}

}
