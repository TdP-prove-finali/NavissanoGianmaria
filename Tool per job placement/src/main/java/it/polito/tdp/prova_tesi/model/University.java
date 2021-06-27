package it.polito.tdp.prova_tesi.model;

public class University implements Comparable<University>{
	
	private int ranking_2019;
	private String institution_Name;
	private String country;
	private double overall_Score;
	private int arts_And_Humanitites_Rank;
	private int engineering_And_Technology_Rank;
	private int life_Sciences_And_Technology_Rank;
	private int natural_Sciences_Rank;
	private int social_Sciences_And_Management_Rank;
	private double tempScore;
	
	
	

	public University(int ranking_2019, String institution_Name, String country, double overall_Score) {
		super();
		this.ranking_2019 = ranking_2019;
		this.institution_Name = institution_Name;
		this.country = country;
		this.overall_Score = overall_Score;
		this.arts_And_Humanitites_Rank = 0;
		this.engineering_And_Technology_Rank = 0;
		this.life_Sciences_And_Technology_Rank = 0;
		this.natural_Sciences_Rank = 0;
		this.social_Sciences_And_Management_Rank = 0;
		this.tempScore = 0;
	}


	


	public int getRanking_2019() {
		return ranking_2019;
	}





	public void setRanking_2019(int ranking_2019) {
		this.ranking_2019 = ranking_2019;
	}





	public String getInstitution_Name() {
		return institution_Name;
	}





	public void setInstitution_Name(String institution_Name) {
		this.institution_Name = institution_Name;
	}





	public String getCountry() {
		return country;
	}





	public void setCountry(String country) {
		this.country = country;
	}





	public double getOverall_Score() {
		return overall_Score;
	}





	public void setOverall_Score(double overall_Score) {
		this.overall_Score = overall_Score;
	}





	public int getArts_And_Humanitites_Rank() {
		return arts_And_Humanitites_Rank;
	}





	public void setArts_And_Humanitites_Rank(int arts_And_Humanitites_Rank) {
		this.arts_And_Humanitites_Rank = arts_And_Humanitites_Rank;
	}





	public int getEngineering_And_Technology_Rank() {
		return engineering_And_Technology_Rank;
	}





	public void setEngineering_And_Technology_Rank(int engineering_And_Technology_Rank) {
		this.engineering_And_Technology_Rank = engineering_And_Technology_Rank;
	}





	public int getLife_Sciences_And_Technology_Rank() {
		return life_Sciences_And_Technology_Rank;
	}





	public void setLife_Sciences_And_Technology_Rank(int life_Sciences_And_Technology_Rank) {
		this.life_Sciences_And_Technology_Rank = life_Sciences_And_Technology_Rank;
	}





	public int getNatural_Sciences_Rank() {
		return natural_Sciences_Rank;
	}





	public void setNatural_Sciences_Rank(int natural_Sciences_Rank) {
		this.natural_Sciences_Rank = natural_Sciences_Rank;
	}





	public int getSocial_Sciences_And_Management_Rank() {
		return social_Sciences_And_Management_Rank;
	}





	public void setSocial_Sciences_And_Management_Rank(int social_Sciences_And_Management_Rank) {
		this.social_Sciences_And_Management_Rank = social_Sciences_And_Management_Rank;
	}


	public void setTempScore(double tempScore){
		this.tempScore = tempScore;
	}



	public double getTempScore() {
		return tempScore;
	}





	@Override
	public String toString() {
		return institution_Name + ", " + country+". score = "+this.tempScore;
	}





	@Override
	public int compareTo(University other) {
		if(this.tempScore>other.getTempScore())
			return -1;
		if(this.tempScore<other.getTempScore())
			return +1;
		return 0;
	}
	
	

}
