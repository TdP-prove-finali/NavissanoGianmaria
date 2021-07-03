package it.polito.tdp.prova_tesi.model;

import java.util.LinkedList;
import java.util.List;

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
	
	private int Accounting_and_Finance;
	private int Agriculture_and_Forestry;
	private int Anatomy_and_Physiology;
	private int Anthropology;
	private int Archaeology;
	private int Architecture_and_Built_Environment;
	private int Art_and_Design;
	private int Biological_Sciences;
	private int Business_and_Management_Studies;
	private int Chemistry;
	private int Classics_and_Ancient_History;
	private int Communication_and_Media_Studies;
	private int Computer_Science_and_Information;
	private int Dentistry;
	private int Developement_studies;
	private int Economics_and_Econometrics;
	private int Education_and_Training;
	private int Electrical_and_Electronic_Engineering;
	private int Engineering_Chemical;
	private int English_Language_and_Literature;
	private int Environmental_Sciences;
	private int Geography;
	private int History;
	private int Hospitality_and_Leisure_Management;
	private int Law_and_Legal_Studies;
	private int Library_and_Information_Management;
	private int Linguistics;
	private int Materials_Sciences;
	private int Mathematics;
	private int Mechanical_Aeronautical_and_Manufacturing_Engineering;
	private int Medicine;
	private int Mineral_and_Mining_Engineering;
	private int Modern_Languages;
	private int Nursing;
	private int Performing_Arts;
	private int Pharmacy_and_Pharmacology;
	private int Philosophy;
	private int Physics_and_Astronomy;
	private int Politics;
	private int Psychology;
	private int Social_Policy_and_Administration;
	private int Sociology;
	private int Sports_related_Subjects;
	private int Statistics_and_Operational_Research;
	private int Theology_Divinity_and_Religious_Studies;
	private int Veterinary_Science;

	
	
	
	

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
	
	
	
	public int getSubject(String subject) {
		
		if(subject.equals("Accounting and Finance"))
			return this.Accounting_and_Finance;
		if(subject.equals("Agriculture and Forestry"))
			return this.Agriculture_and_Forestry;
		if(subject.equals("Anatomy and Physiology"))
			return this.Anatomy_and_Physiology;
		if(subject.equals("Anthropology"))
			return this.Anthropology;
		if(subject.equals("Archaeology"))
			return this.Archaeology;
		if(subject.equals("Architecture and Built Environment"))
			return this.Architecture_and_Built_Environment;
		if(subject.equals("Art and Design"))
			return this.Art_and_Design;
		if(subject.equals("Biological Sciences"))
			return this.Biological_Sciences;
		if(subject.equals("Business and Management Studies"))
			return this.Business_and_Management_Studies;
		if(subject.equals("Chemistry"))
			return this.Chemistry;
		if(subject.equals("Classics and Ancient History"))
			return this.Classics_and_Ancient_History;
		if(subject.equals("Communication and Media Studies"))
			return this.Communication_and_Media_Studies;
		if(subject.equals("Computer Science and Information"))
			return this.Computer_Science_and_Information;
		if(subject.equals("Dentistry"))
			return this.Dentistry;
		if(subject.equals("Developement Studies"))
			return this.Developement_studies;
		if(subject.equals("Economics and Econometrics"))
			return this.Economics_and_Econometrics;
		if(subject.equals("Education and Training"))
			return this.Education_and_Training;
		if(subject.equals("Electrical and Electronic Engineering"))
			return this.Electrical_and_Electronic_Engineering;
		if(subject.equals("Engineering Chemical"))
			return this.Engineering_Chemical;
		if(subject.equals("English Language and Literature"))
			return this.English_Language_and_Literature;
		if(subject.equals("Environmental Sciences"))
			return this.Environmental_Sciences;
		if(subject.equals("Geography"))
			return this.Geography;
		if(subject.equals("History"))
			return this.History;
		if(subject.equals("Hospitality and Leisure Management"))
			return this.Hospitality_and_Leisure_Management;
		if(subject.equals("Law and Legal Studies"))
			return this.Law_and_Legal_Studies;
		if(subject.equals("Library and Information Management"))
			return this.Library_and_Information_Management;
		if(subject.equals("Linguistics"))
			return this.Linguistics;
		if(subject.equals("Materials Sciences"))
			return this.Materials_Sciences;
		if(subject.equals("Mathematics"))
			return this.Mathematics;
		if(subject.equals("Mechanical Aeronautical and Manufacturing Engineering"))
			return this.Mechanical_Aeronautical_and_Manufacturing_Engineering;
		if(subject.equals("Medicine"))
			return this.Medicine;
		if(subject.equals("Mineral and Mining Engineering"))
			return this.Mineral_and_Mining_Engineering;
		if(subject.equals("Modern Languages"))
			return this.Modern_Languages;
		if(subject.equals("Nursing"))
			return this.Nursing;
		if(subject.equals("Performing Arts"))
			return this.Performing_Arts;
		if(subject.equals("Pharmacy and Pharmacology"))
			return this.Pharmacy_and_Pharmacology;
		if(subject.equals("Philosophy"))
			return this.Philosophy;
		if(subject.equals("Physics and Astronomy"))
			return this.Physics_and_Astronomy;
		if(subject.equals("Politics"))
			return this.Politics;
		if(subject.equals("Psychology"))
			return this.Psychology;
		if(subject.equals("Social Policy and Administration"))
			return this.Social_Policy_and_Administration;
		if(subject.equals("Sociology"))
			return this.Sociology;
		if(subject.equals("Sports related Subjects"))
			return this.Sports_related_Subjects;
		if(subject.equals("Statistics and Operational Research"))
			return this.Statistics_and_Operational_Research;
		if(subject.equals("Theology, Divinity and Religious Studies"))
			return this.Theology_Divinity_and_Religious_Studies;
		if(subject.equals("Veterinary Science"))
			return this.Veterinary_Science; 
		return 0;
		
		
	}
	
	public List<String> getBestSubject(List<String> subjects){
		List<String> result = new LinkedList<>();
		int bestRank = Integer.MAX_VALUE;
		for(String s : subjects) {
			int rank = this.getSubject(s);
			if(rank<bestRank) {
				bestRank = rank;
				result.clear();
				result.add(s);
			}else {
				if(rank == bestRank)
					result.add(s);
			}
		}
		return result;
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



	public String toStringRes() {
		String scoreString = ""+this.tempScore;
		if(scoreString.contains(".") && (scoreString.length()-scoreString.indexOf('.'))>=3)
			return institution_Name + ", " + country+" SCORE = "+scoreString.substring(0, scoreString.indexOf('.')+3);
		return institution_Name + ", " + country+" SCORE = "+scoreString;
	}
	
	


	@Override
	public String toString() {

		return this.institution_Name;
	}



	@Override
	public int compareTo(University other) {
		if(this.tempScore>other.getTempScore())
			return -1;
		if(this.tempScore<other.getTempScore())
			return +1;
		return 0;
	}





	public int getAccounting_and_Finance() {
		return Accounting_and_Finance;
	}





	public void setAccounting_and_Finance(int accounting_and_Finance) {
		Accounting_and_Finance = accounting_and_Finance;
	}





	public int getAgriculture_and_Forestry() {
		return Agriculture_and_Forestry;
	}





	public void setAgriculture_and_Forestry(int agriculture_and_Forestry) {
		Agriculture_and_Forestry = agriculture_and_Forestry;
	}





	public int getAnatomy_and_Physiology() {
		return Anatomy_and_Physiology;
	}





	public void setAnatomy_and_Physiology(int anatomy_and_Physiology) {
		Anatomy_and_Physiology = anatomy_and_Physiology;
	}





	public int getAnthropology() {
		return Anthropology;
	}





	public void setAnthropology(int anthropology) {
		Anthropology = anthropology;
	}





	public int getArchaeology() {
		return Archaeology;
	}





	public void setArchaeology(int archaeology) {
		Archaeology = archaeology;
	}





	public int getArchitecture_and_Built_Environment() {
		return Architecture_and_Built_Environment;
	}





	public void setArchitecture_and_Built_Environment(int architecture_and_Built_Environment) {
		Architecture_and_Built_Environment = architecture_and_Built_Environment;
	}





	public int getArt_and_Design() {
		return Art_and_Design;
	}





	public void setArt_and_Design(int art_and_Design) {
		Art_and_Design = art_and_Design;
	}





	public int getBiological_Sciences() {
		return Biological_Sciences;
	}





	public void setBiological_Sciences(int biological_Sciences) {
		Biological_Sciences = biological_Sciences;
	}





	public int getBusiness_and_Management_Studies() {
		return Business_and_Management_Studies;
	}





	public void setBusiness_and_Management_Studies(int business_and_Management_Studies) {
		Business_and_Management_Studies = business_and_Management_Studies;
	}





	public int getChemistry() {
		return Chemistry;
	}





	public void setChemistry(int chemistry) {
		Chemistry = chemistry;
	}





	public int getClassics_and_Ancient_History() {
		return Classics_and_Ancient_History;
	}





	public void setClassics_and_Ancient_History(int classics_and_Ancient_History) {
		Classics_and_Ancient_History = classics_and_Ancient_History;
	}





	public int getCommunication_and_Media_Studies() {
		return Communication_and_Media_Studies;
	}





	public void setCommunication_and_Media_Studies(int communication_and_Media_Studies) {
		Communication_and_Media_Studies = communication_and_Media_Studies;
	}





	public int getComputer_Science_and_Information() {
		return Computer_Science_and_Information;
	}





	public void setComputer_Science_and_Information(int computer_Science_and_Information) {
		Computer_Science_and_Information = computer_Science_and_Information;
	}





	public int getDentistry() {
		return Dentistry;
	}





	public void setDentistry(int dentistry) {
		Dentistry = dentistry;
	}





	public int getDevelopement_studies() {
		return Developement_studies;
	}





	public void setDevelopement_studies(int developement_studies) {
		Developement_studies = developement_studies;
	}





	public int getEconomics_and_Econometrics() {
		return Economics_and_Econometrics;
	}





	public void setEconomics_and_Econometrics(int economics_and_Econometrics) {
		Economics_and_Econometrics = economics_and_Econometrics;
	}





	public int getEducation_and_Training() {
		return Education_and_Training;
	}





	public void setEducation_and_Training(int education_and_Training) {
		Education_and_Training = education_and_Training;
	}





	public int getElectrical_and_Electronic_Engineering() {
		return Electrical_and_Electronic_Engineering;
	}





	public void setElectrical_and_Electronic_Engineering(int electrical_and_Electronic_Engineering) {
		Electrical_and_Electronic_Engineering = electrical_and_Electronic_Engineering;
	}





	public int getEngineering_Chemical() {
		return Engineering_Chemical;
	}





	public void setEngineering_Chemical(int engineering_Chemical) {
		Engineering_Chemical = engineering_Chemical;
	}





	public int getEnglish_Language_and_Literature() {
		return English_Language_and_Literature;
	}





	public void setEnglish_Language_and_Literature(int english_Language_and_Literature) {
		English_Language_and_Literature = english_Language_and_Literature;
	}





	public int getEnvironmental_Sciences() {
		return Environmental_Sciences;
	}





	public void setEnvironmental_Sciences(int environmental_Sciences) {
		Environmental_Sciences = environmental_Sciences;
	}





	public int getGeography() {
		return Geography;
	}





	public void setGeography(int geography) {
		Geography = geography;
	}





	public int getHistory() {
		return History;
	}





	public void setHistory(int history) {
		History = history;
	}





	public int getHospitality_and_Leisure_Management() {
		return Hospitality_and_Leisure_Management;
	}





	public void setHospitality_and_Leisure_Management(int hospitality_and_Leisure_Management) {
		Hospitality_and_Leisure_Management = hospitality_and_Leisure_Management;
	}





	public int getLaw_and_Legal_Studies() {
		return Law_and_Legal_Studies;
	}





	public void setLaw_and_Legal_Studies(int law_and_Legal_Studies) {
		Law_and_Legal_Studies = law_and_Legal_Studies;
	}





	public int getLibrary_and_Information_Management() {
		return Library_and_Information_Management;
	}





	public void setLibrary_and_Information_Management(int library_and_Information_Management) {
		Library_and_Information_Management = library_and_Information_Management;
	}





	public int getLinguistics() {
		return Linguistics;
	}





	public void setLinguistics(int linguistics) {
		Linguistics = linguistics;
	}





	public int getMaterials_Sciences() {
		return Materials_Sciences;
	}





	public void setMaterials_Sciences(int materials_Sciences) {
		Materials_Sciences = materials_Sciences;
	}





	public int getMathematics() {
		return Mathematics;
	}





	public void setMathematics(int mathematics) {
		Mathematics = mathematics;
	}





	public int getMechanical_Aeronautical_and_Manufacturing_Engineering() {
		return Mechanical_Aeronautical_and_Manufacturing_Engineering;
	}





	public void setMechanical_Aeronautical_and_Manufacturing_Engineering(
			int mechanical_Aeronautical_and_Manufacturing_Engineering) {
		Mechanical_Aeronautical_and_Manufacturing_Engineering = mechanical_Aeronautical_and_Manufacturing_Engineering;
	}





	public int getMedicine() {
		return Medicine;
	}





	public void setMedicine(int medicine) {
		Medicine = medicine;
	}





	public int getMineral_and_Mining_Engineering() {
		return Mineral_and_Mining_Engineering;
	}





	public void setMineral_and_Mining_Engineering(int mineral_and_Mining_Engineering) {
		Mineral_and_Mining_Engineering = mineral_and_Mining_Engineering;
	}





	public int getModern_Languages() {
		return Modern_Languages;
	}





	public void setModern_Languages(int modern_Languages) {
		Modern_Languages = modern_Languages;
	}





	public int getNursing() {
		return Nursing;
	}





	public void setNursing(int nursing) {
		Nursing = nursing;
	}





	public int getPerforming_Arts() {
		return Performing_Arts;
	}





	public void setPerforming_Arts(int performing_Arts) {
		Performing_Arts = performing_Arts;
	}





	public int getPharmacy_and_Pharmacology() {
		return Pharmacy_and_Pharmacology;
	}





	public void setPharmacy_and_Pharmacology(int pharmacy_and_Pharmacology) {
		Pharmacy_and_Pharmacology = pharmacy_and_Pharmacology;
	}





	public int getPhilosophy() {
		return Philosophy;
	}





	public void setPhilosophy(int philosophy) {
		Philosophy = philosophy;
	}





	public int getPhysics_and_Astronomy() {
		return Physics_and_Astronomy;
	}





	public void setPhysics_and_Astronomy(int physics_and_Astronomy) {
		Physics_and_Astronomy = physics_and_Astronomy;
	}





	public int getPolitics() {
		return Politics;
	}





	public void setPolitics(int politics) {
		Politics = politics;
	}





	public int getPsychology() {
		return Psychology;
	}





	public void setPsychology(int psychology) {
		Psychology = psychology;
	}





	public int getSocial_Policy_and_Administration() {
		return Social_Policy_and_Administration;
	}





	public void setSocial_Policy_and_Administration(int social_Policy_and_Administration) {
		Social_Policy_and_Administration = social_Policy_and_Administration;
	}





	public int getSociology() {
		return Sociology;
	}





	public void setSociology(int sociology) {
		Sociology = sociology;
	}





	public int getSports_related_Subjects() {
		return Sports_related_Subjects;
	}





	public void setSports_related_Subjects(int sports_related_Subjects) {
		Sports_related_Subjects = sports_related_Subjects;
	}





	public int getStatistics_and_Operational_Research() {
		return Statistics_and_Operational_Research;
	}





	public void setStatistics_and_Operational_Research(int statistics_and_Operational_Research) {
		Statistics_and_Operational_Research = statistics_and_Operational_Research;
	}





	public int getTheology_Divinity_and_Religious_Studies() {
		return Theology_Divinity_and_Religious_Studies;
	}





	public void setTheology_Divinity_and_Religious_Studies(int theology_Divinity_and_Religious_Studies) {
		Theology_Divinity_and_Religious_Studies = theology_Divinity_and_Religious_Studies;
	}





	public int getVeterinary_Science() {
		return Veterinary_Science;
	}





	public void setVeterinary_Science(int veterinary_Science) {
		Veterinary_Science = veterinary_Science;
	}
	
	
	
	

}
