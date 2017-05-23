package wdsr.exercise4;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.faces.convert.BigDecimalConverter;

public class PriceAlert implements Serializable {
	private static final long serialVersionUID = 2634775509893849192L;
	
	private long timestamp;
	private String stock;
	private BigDecimal currentPrice;
	
	public PriceAlert(long timestamp, String stock, BigDecimal currentPrice) {
		this.timestamp = timestamp;
		this.stock = stock;
		this.currentPrice = currentPrice;
	}
	public PriceAlert(String message ) {
		Map<String, String> messageString = new HashMap<String, String>();
		
		String[] pairs = message.split("\n");
		for (int i=0;i<pairs.length;i++) {
		    String pair = pairs[i];
		    String[] keyValue = pair.split("=");
		    messageString.put(keyValue[0], keyValue[1]);
		}
		this.timestamp = Long.parseLong(messageString.get("Timestamp").trim());
		this.stock = messageString.get("Stock").trim();
		this.currentPrice = new BigDecimal(messageString.get("Price").trim());
	}

	public long getTimestamp() {
		return timestamp;
	}

	public String getStock() {
		return stock;
	}

	public BigDecimal getCurrentPrice() {
		return currentPrice;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currentPrice == null) ? 0 : currentPrice.hashCode());
		result = prime * result + ((stock == null) ? 0 : stock.hashCode());
		result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PriceAlert other = (PriceAlert) obj;
		if (currentPrice == null) {
			if (other.currentPrice != null)
				return false;
		} else if (!currentPrice.equals(other.currentPrice))
			return false;
		if (stock == null) {
			if (other.stock != null)
				return false;
		} else if (!stock.equals(other.stock))
			return false;
		if (timestamp != other.timestamp)
			return false;
		return true;
	}
}
