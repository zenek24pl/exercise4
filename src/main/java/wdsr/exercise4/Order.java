package wdsr.exercise4;

import java.io.Serializable;
import java.math.BigDecimal;

public class Order implements Serializable {
	private static final long serialVersionUID = -6950895777195964088L;
	
	private int id;
	private String product;
	private BigDecimal price;
	
	public Order(int id, String product, BigDecimal price) {
		this.id = id;
		this.product = product;
		this.price = price;
	}

	public int getId() {
		return id;
	}

	public String getProduct() {
		return product;
	}

	public BigDecimal getPrice() {
		return price;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
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
		Order other = (Order) obj;
		if (id != other.id)
			return false;
		if (price == null) {
			if (other.price != null)
				return false;
		} else if (!price.equals(other.price))
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		return true;
	}
}
