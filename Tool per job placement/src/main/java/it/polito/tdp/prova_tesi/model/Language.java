package it.polito.tdp.prova_tesi.model;

import java.util.LinkedList;
import java.util.List;

public class Language {
	
	private String language;
	private List<String> countries;
	
	public Language(String language, String country) {
		super();
		this.language = language;
		countries = new LinkedList<String>();
		countries.add(country);
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public List<String> getCountries() {
		return countries;
	}

	public void setCountries(List<String> countries) {
		this.countries = countries;
	}
	
	public void addCountry(String country) {
		if(!this.countries.contains(country))
			this.countries.add(country);
	}

}
