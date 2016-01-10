package com.ilegra.teste.modelo;

import java.util.ArrayList;
import java.util.List;

public class Sales {

	private int id;
	private Salesman salesman;

	private List<Item> itens;
	

	public Sales(int id, Salesman salesmanName, List<Item> itens) {
		this.id = id;
		this.salesman = salesmanName;
		this.itens = itens;
	}

	public Sales() {
		itens = new ArrayList<Item>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Salesman getSalesman() {
		return salesman;
	}

	public void setSalesman(Salesman salesmanName) {
		this.salesman = salesmanName;
	}

	public List<Item> getItens() {
		return itens;
	}

	public void setItens(List<Item> itens) {
		this.itens = itens;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Sales other = (Sales) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Sales [id=" + id + ", salesmanName=" + salesman.getName() + ", itens=" + itens + "]";
	}

}
