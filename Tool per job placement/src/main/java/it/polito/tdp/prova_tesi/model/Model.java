package it.polito.tdp.prova_tesi.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.polito.tdp.prova_tesi.db.JobsDAO;
import it.polito.tdp.prova_tesi.db.UniversitiesDAO;

public class Model {
	
	private List<String> skills;
	private JobsDAO jobsDao;
	private UniversitiesDAO uniDao;
	private Map<String, Language> langMap;
	private Map<String, University> idMap;
	private List<University> bestUniversities;
	private double bestScore;
	private List<University> validUniversities;
	
	public Model() {
		jobsDao = new JobsDAO();
		uniDao = new UniversitiesDAO();
		this.langMap = this.setLanguages(this.uniDao.getAllCountries());
		idMap = new HashMap<>();
		this.uniDao.getAllUniversities(idMap);
		this.uniDao.getMacroSubjectsRankings(idMap);
		
	}
	
	public List<String> getAllSkills(){
		
		skills = this.jobsDao.getAllSkills();
		System.out.println("Ci sono "+skills.size()+" skills");
		
		List<String> singleSkills = new LinkedList<>();
		
		for(String s : skills) {
			String[] sTemp = s.split("[|]+");
			for(String st : sTemp) {
				st = st.trim();
				if(!singleSkills.contains(st)) //la contains è onerosa, si potrebbe usare una mappa.
					singleSkills.add(st);
			}
		}
		
		
		Collections.sort(singleSkills);
		return singleSkills;
	}
	
	public String getJobsBySkill(String skill){
		List<String> jobs = this.jobsDao.getJobBySkill(skill);
		String result = "";
		for(String s : jobs) {
			result += s+"\n";
		}
		return result;
	}
	
	public List<String> getLanguages(){
		List<String> result = new LinkedList<>(this.langMap.keySet());
		Collections.sort(result);
		return result;
	}
	
	public Language getCountriesByLanguage(String lang){
		return this.langMap.get(lang);
	}
	
	public List<University> getBestUniversities(String language, int artsAndHumanitiesValue, int engineeringAndTechnologyValue, int lifeSciencesAndMedicineValue, int naturalSciencesValue, int socialSciencesAndManagementValue){
		this.bestUniversities = null;
		this.bestScore = Double.MIN_VALUE;
		List<University> result = new LinkedList<>();
		Language l = this.langMap.get(language);
		for(University u : this.idMap.values()) {
			for(String country : l.getCountries()) {
				if(u.getCountry().equals(country) && !result.contains(u))
					result.add(u);
			}
		}
		
		this.validUniversities = result;
		/*System.out.println("Corrispondono alla lingua selezionata le seguenti università:\n");
		for(University u : result)
			System.out.println(u+"\n");*/
		//this.cerca(new LinkedList<University>(), artsAndHumanitiesValue, engineeringAndTechnologyValue, lifeSciencesAndMedicineValue, naturalSciencesValue, socialSciencesAndManagementValue);
		
		
		
		//Prova per vedere tutti i punteggi
		this.calcolaScore(validUniversities, artsAndHumanitiesValue, engineeringAndTechnologyValue, lifeSciencesAndMedicineValue, naturalSciencesValue, socialSciencesAndManagementValue);
		this.bestUniversities = this.validUniversities;
		
		Collections.sort(this.bestUniversities);
		return this.bestUniversities;

	}
	
	public void cerca(List<University> parziale, int artsAndHumanitiesValue, int engineeringAndTechnologyValue, int lifeSciencesAndMedicineValue, int naturalSciencesValue, int socialSciencesAndManagementValue) {
		
		//caso terminale
		if(parziale.size()==5) {
			double score = this.calcolaScore(parziale, artsAndHumanitiesValue, engineeringAndTechnologyValue, lifeSciencesAndMedicineValue, naturalSciencesValue, socialSciencesAndManagementValue);
			if(bestUniversities == null) {
				this.bestUniversities = new LinkedList<>(parziale);
				bestScore = score;
			} else {
				if(score>bestScore) {
					this.bestUniversities = new LinkedList<>(parziale);
					bestScore = score;
				}
			}
			return;
		}
		
		for(University u : this.validUniversities) {
			
			if(!parziale.contains(u)) {
				parziale.add(u);
				this.cerca(parziale, artsAndHumanitiesValue, engineeringAndTechnologyValue, lifeSciencesAndMedicineValue, naturalSciencesValue, socialSciencesAndManagementValue);
				parziale.remove(u);
			}
			
		}
		
		
	}
	
	public double calcolaScore(List<University> uni, int artsAndHumanitiesValue, int engineeringAndTechnologyValue, int lifeSciencesAndMedicineValue, int naturalSciencesValue, int socialSciencesAndManagementValue) {
		double score = 0.0;
		double tempScore;
		for(University u : uni) {
			tempScore = 0.0;
			//il punteggio è dato dalla somma del ranking generico, dato dall'overall score
			tempScore += u.getOverall_Score();
			
			//e dalla somma dei rankings per subject
			if(u.getArts_And_Humanitites_Rank()>0) tempScore += 1000*artsAndHumanitiesValue*(1/u.getArts_And_Humanitites_Rank());
			if(u.getEngineering_And_Technology_Rank()>0) tempScore += 1000*engineeringAndTechnologyValue*(1/u.getEngineering_And_Technology_Rank());
			if(u.getLife_Sciences_And_Technology_Rank()>0)	tempScore += 1000*lifeSciencesAndMedicineValue*(1/u.getLife_Sciences_And_Technology_Rank());
			if(u.getNatural_Sciences_Rank()>0) score += 1000*naturalSciencesValue*(1/u.getNatural_Sciences_Rank());
			if(u.getSocial_Sciences_And_Management_Rank()>0	) tempScore += 1000*socialSciencesAndManagementValue*(1/u.getSocial_Sciences_And_Management_Rank());
			
			u.setTempScore(tempScore);
			
			score += tempScore;
		}
		return score;
	}
	
	
	public int getValidUniversitiesNumber() {
		return this.validUniversities.size();
	}
	
	
	
	public Map<String, Language> setLanguages(List<String> countries) {
		Map<String, Language> result = new HashMap<>();
		
		for(String country : countries) {
			switch(country) {
			case "Argentina": 
				if(!result.containsKey("Spanish"))
					result.put("Spanish", new Language("Spanish", country));
				 else result.get("Spanish").addCountry(country);
				break;
			case "Australia": 
				if(!result.containsKey("English"))
					result.put("English", new Language("English", country));
				 else result.get("English").addCountry(country);
				break;
			case "Austria":
				if(!result.containsKey("German"))
					result.put("German", new Language("German", country));
				 else result.get("German").addCountry(country);
				break;
			case "Azerbaijan": 
				if(!result.containsKey("Azerbaijani"))
					result.put("Azerbaijani", new Language("Azerbaijani", country));
				 else result.get("Azerbaijani").addCountry(country);
				break;
			case "Bahrain": 
				if(!result.containsKey("Arabic"))
					result.put("Arabic", new Language("Arabic", country));
				 else result.get("Arabic").addCountry(country);
				break;
			case "Bangladesh":
				if(!result.containsKey("Bengali"))
					result.put("Bengali", new Language("Bengali", country));
				 else result.get("Bengali").addCountry(country);
				break;
			case "Belarus":
				if(!result.containsKey("Belarusian"))
					result.put("Belarusian", new Language("Belarusian", country));
				 else result.get("Belarusian").addCountry(country);
				break;
			case "Belgium":
				if(!result.containsKey("French"))
					result.put("French", new Language("French", country));
				 else result.get("French").addCountry(country);
				if(!result.containsKey("Dutch"))
					result.put("Dutch", new Language("Dutch", country));
				 else result.get("Dutch").addCountry(country);
				if(!result.containsKey("German"))
					result.put("German", new Language("German", country));
				 else result.get("German").addCountry(country);
				break;
			case "Bosnia and Herzegovina":
				if(!result.containsKey("Bosnian"))
					result.put("Bosnian", new Language("Bosnian", country));
				 else result.get("Bosnian").addCountry(country);
				break;
			case "Brazil":
				if(!result.containsKey("Portuguese"))
					result.put("Portuguese", new Language("Portuguese", country));
				 else result.get("Portuguese").addCountry(country);
				break;
			case "Brunei":
				if(!result.containsKey("Malay"))
					result.put("Malay", new Language("Malay", country));
				 else result.get("Malay").addCountry(country);
				break;
			case "Bulgaria":
				if(!result.containsKey("Bulgarian"))
					result.put("Bulgarian", new Language("Bulgarian", country));
				 else result.get("Bulgarian").addCountry(country);
				break;
			case "Canada":
				if(!result.containsKey("English"))
					result.put("English", new Language("English", country));
				 else result.get("English").addCountry(country);
				if(!result.containsKey("French"))
					result.put("French", new Language("French", country));
				 else result.get("French").addCountry(country);
				break;
			case "Chile":
				if(!result.containsKey("Spanish"))
					result.put("Spanish", new Language("Spanish", country));
				 else result.get("Spanish").addCountry(country);
				break;
			case "China":
				if(!result.containsKey("Chinese"))
					result.put("Chinese", new Language("Chinese", country));
				 else result.get("Chinese").addCountry(country);
				break;
			case "Colombia":
				if(!result.containsKey("Spanish"))
					result.put("Spanish", new Language("Spanish", country));
				 else result.get("Spanish").addCountry(country);
				break;
			case "Costa Rica":
				if(!result.containsKey("Spanish"))
					result.put("Spanish", new Language("Spanish", country));
				 else result.get("Spanish").addCountry(country);
				break;
			case "Croatia":
				if(!result.containsKey("Croatian"))
					result.put("Croatian", new Language("Croatian", country));
				 else result.get("Croatian").addCountry(country);
				break;
			case "Cuba":
				if(!result.containsKey("Spanish"))
					result.put("Spanish", new Language("Spanish", country));
				 else result.get("Spanish").addCountry(country);
				break;
			case "Czech Republic":
				if(!result.containsKey("Czech"))
					result.put("Czech", new Language("Czech", country));
				 else result.get("Czech").addCountry(country);
				break;
			case "Denmark":
				if(!result.containsKey("Danish"))
					result.put("Danish", new Language("Danish", country));
				 else result.get("Danish").addCountry(country);
				break;
			case "Ecuador":
				if(!result.containsKey("Spanish"))
					result.put("Spanish", new Language("Spanish", country));
				 else result.get("Spanish").addCountry(country);
				break;
			case "Egypt":
				if(!result.containsKey("Egyptian"))
					result.put("Egyptian", new Language("Egyptian", country));
				 else result.get("Egyptian").addCountry(country);
				break;
			case "Estonia":
				if(!result.containsKey("Estonian"))
					result.put("Estonian", new Language("Estonian", country));
				 else result.get("Estonian").addCountry(country);
				break;
			case "Finland":
				if(!result.containsKey("Finnish"))
					result.put("Finnish", new Language("Finnish", country));
				 else result.get("Finnish").addCountry(country);
				if(!result.containsKey("Swedish"))
					result.put("Swedish", new Language("Swedish", country));
				 else result.get("Swedish").addCountry(country);
				break;
			case "France":
				if(!result.containsKey("French"))
					result.put("French", new Language("French", country));
				 else result.get("French").addCountry(country);
				break;
			case "Germany":
				if(!result.containsKey("German"))
					result.put("German", new Language("German", country));
				 else result.get("German").addCountry(country);
				break;
			case "Greece":
				if(!result.containsKey("Greek"))
					result.put("Greek", new Language("Greek", country));
				 else result.get("Greek").addCountry(country);
				break;
			case "Hong Kong":
				if(!result.containsKey("Cantonese"))
					result.put("Cantonese", new Language("Cantonese", country));
				 else result.get("Cantonese").addCountry(country);
				break;
			case "Hungary":
				if(!result.containsKey("Hungarian"))
					result.put("Hungarian", new Language("Hungarian", country));
				 else result.get("Hungarian").addCountry(country);
				break;
			case "India":
				if(!result.containsKey("Hindi"))
					result.put("Hindi", new Language("Hindi", country));
				 else result.get("Hindi").addCountry(country);
				break;
			case "Indonesia":
				if(!result.containsKey("Indonesian"))
					result.put("Indonesian", new Language("Indonesian", country));
				 else result.get("Indonesian").addCountry(country);
				break;
			case "Iran":
				if(!result.containsKey("Persian"))
					result.put("Persian", new Language("Persian", country));
				 else result.get("Persian").addCountry(country);
				break;
			case "Iraq":
				if(!result.containsKey("Arabic"))
					result.put("Arabic", new Language("Arabic", country));
				 else result.get("Arabic").addCountry(country);
				break;
			case "Ireland":
				if(!result.containsKey("English"))
					result.put("English", new Language("English", country));
				 else result.get("English").addCountry(country);
				break;
			case "Israel":
				if(!result.containsKey("Hebrew"))
					result.put("Hebrew", new Language("Hebrew", country));
				 else result.get("Hebrew").addCountry(country);
				if(!result.containsKey("Arabic"))
					result.put("Arabic", new Language("Arabic", country));
				 else result.get("Arabic").addCountry(country);
				break;
			case "Italy":
				if(!result.containsKey("Italian"))
					result.put("Italian", new Language("Italian", country));
				 else result.get("Italian").addCountry(country);
				break;
			case "Japan":
				if(!result.containsKey("Japanese"))
					result.put("Japanese", new Language("Japanese", country));
				 else result.get("Japanese").addCountry(country);
				break;
			case "Jordan":
				if(!result.containsKey("Arabic"))
					result.put("Arabic", new Language("Arabic", country));
				 else result.get("Arabic").addCountry(country);
				break;
			case "Kazakhstan":
				if(!result.containsKey("Kazakh"))
					result.put("Kazakh", new Language("Kazakh", country));
				 else result.get("Kazakh").addCountry(country);
				if(!result.containsKey("Russian"))
					result.put("Russian", new Language("Russian", country));
				 else result.get("Russian").addCountry(country);
				break;
			case "Kenya":
				if(!result.containsKey("Bantu Swahili"))
					result.put("Bantu Swahili", new Language("Bantu Swahili", country));
				 else result.get("Bantu Swahili").addCountry(country);
				if(!result.containsKey("English"))
					result.put("English", new Language("English", country));
				 else result.get("English").addCountry(country);
				break;
			case "Kuwait":
				if(!result.containsKey("Arabic"))
					result.put("Arabic", new Language("Arabic", country));
				 else result.get("Arabic").addCountry(country);
				break;
			case "Latvia":
				if(!result.containsKey("Latvian"))
					result.put("Latvian", new Language("Latvian", country));
				 else result.get("Latvian").addCountry(country);
				break;
			case "Lebanon":
				if(!result.containsKey("Arabic"))
					result.put("Arabic", new Language("Arabic", country));
				 else result.get("Arabic").addCountry(country);
				break;
			case "Lithuania":
				if(!result.containsKey("Lithuanian"))
					result.put("Lithuanian", new Language("Lithuanian", country));
				 else result.get("Lithuanian").addCountry(country);
				break;
			case "Macau":
				if(!result.containsKey("Cantonese"))
					result.put("Cantonese", new Language("Cantonese", country));
				 else result.get("Cantonese").addCountry(country);
				break;
			case "Malaysia":
				if(!result.containsKey("Malaysian"))
					result.put("Malaysian", new Language("Malaysian", country));
				 else result.get("Malaysian").addCountry(country);
				break;
			case "Mexico":
				if(!result.containsKey("Spanish"))
					result.put("Spanish", new Language("Spanish", country));
				 else result.get("Spanish").addCountry(country);
				break;
			case "Morocco":
				if(!result.containsKey("Arabic"))
					result.put("Arabic", new Language("Arabic", country));
				 else result.get("Arabic").addCountry(country);
				break;
			case "Netherlands":
				if(!result.containsKey("Dutch"))
					result.put("Dutch", new Language("Dutch", country));
				 else result.get("Dutch").addCountry(country);
				break;
			case "New Zealand":
				if(!result.containsKey("English"))
					result.put("English", new Language("English", country));
				 else result.get("English").addCountry(country);
				break;
			case "Norway":
				if(!result.containsKey("Norwegian"))
					result.put("Norwegian", new Language("Norwegian", country));
				 else result.get("Norwegian").addCountry(country);
				break;
			case "Oman":
				if(!result.containsKey("Arabic"))
					result.put("Arabic", new Language("Arabic", country));
				 else result.get("Arabic").addCountry(country);
				break;
			case "Pakistan":
				if(!result.containsKey("English"))
					result.put("English", new Language("English", country));
				 else result.get("English").addCountry(country);
				break;
			case "Palestinian Territory":
				if(!result.containsKey("Arabic"))
					result.put("Arabic", new Language("Arabic", country));
				 else result.get("Arabic").addCountry(country);
				break;
			case "Panama":
				if(!result.containsKey("Spanish"))
					result.put("Spanish", new Language("Spanish", country));
				 else result.get("Spanish").addCountry(country);
				break;
			case "Peru":
				if(!result.containsKey("Spanish"))
					result.put("Spanish", new Language("Spanish", country));
				 else result.get("Spanish").addCountry(country);
				break;
			case "Philippines":
				if(!result.containsKey("Filipino"))
					result.put("Filipino", new Language("Filipino", country));
				 else result.get("Filipino").addCountry(country);
				if(!result.containsKey("English"))
					result.put("English", new Language("English", country));
				 else result.get("English").addCountry(country);
				break;
			case "Poland":
				if(!result.containsKey("Polish"))
					result.put("Polish", new Language("Polish", country));
				 else result.get("Polish").addCountry(country);
				break;
			case "Portugal":
				if(!result.containsKey("Portuguese"))
					result.put("Portuguese", new Language("Portuguese", country));
				 else result.get("Portuguese").addCountry(country);
				break;
			case "Puerto Rico":
				if(!result.containsKey("Spanish"))
					result.put("Spanish", new Language("Spanish", country));
				 else result.get("Spanish").addCountry(country);
				break;
			case "Qatar":
				if(!result.containsKey("Arabic"))
					result.put("Arabic", new Language("Arabic", country));
				 else result.get("Arabic").addCountry(country);
				break;
			case "Romania":
				if(!result.containsKey("Romanian"))
					result.put("Romanian", new Language("Romanian", country));
				 else result.get("Romanian").addCountry(country);
				break;
			case "Russia":
				if(!result.containsKey("Russian"))
					result.put("Russian", new Language("Russian", country));
				 else result.get("Russian").addCountry(country);
				break;
			case "Saudi Arabia":
				if(!result.containsKey("Arabic"))
					result.put("Arabic", new Language("Arabic", country));
				 else result.get("Arabic").addCountry(country);
				break;
			case "Serbia":
				if(!result.containsKey("Serbian"))
					result.put("Serbian", new Language("Serbian", country));
				 else result.get("Serbian").addCountry(country);
				break;
			case "Singapore":
				if(!result.containsKey("Malay"))
					result.put("Malay", new Language("Malay", country));
				 else result.get("Malay").addCountry(country);
				if(!result.containsKey("English"))
					result.put("English", new Language("English", country));
				 else result.get("English").addCountry(country);
				break;
			case "Slovakia":
				if(!result.containsKey("Slovak"))
					result.put("Slovak", new Language("Slovak", country));
				 else result.get("Slovak").addCountry(country);
				break;
			case "Slovenia":
				if(!result.containsKey("Slovene"))
					result.put("Slovene", new Language("Slovene", country));
				 else result.get("Slovene").addCountry(country);
				break;
			case "South Africa":
				if(!result.containsKey("English"))
					result.put("English", new Language("English", country));
				 else result.get("English").addCountry(country);
				if(!result.containsKey("Afrikaans"))
					result.put("Afrikaans", new Language("Afrikaans", country));
				 else result.get("Afrikaans").addCountry(country);
				break;
			case "South Korea":
				if(!result.containsKey("Korean"))
					result.put("Korean", new Language("Korean", country));
				 else result.get("Korean").addCountry(country);
				break;
			case "Spain":
				if(!result.containsKey("Spanish"))
					result.put("Spanish", new Language("Spanish", country));
				 else result.get("Spanish").addCountry(country);
				break;
			case "Sri Lanka":
				if(!result.containsKey("Sinhala"))
					result.put("Sinhala", new Language("Sinhala", country));
				 else result.get("Sinhala").addCountry(country);
				break;
			case "Sweden":
				if(!result.containsKey("Swedish"))
					result.put("Swedish", new Language("Swedish", country));
				 else result.get("Swedish").addCountry(country);
				break;
			case "Switzerland":
				if(!result.containsKey("French"))
					result.put("French", new Language("French", country));
				 else result.get("French").addCountry(country);
				if(!result.containsKey("Italian"))
					result.put("Italian", new Language("Italian", country));
				 else result.get("Italian").addCountry(country);
				if(!result.containsKey("German"))
					result.put("German", new Language("German", country));
				 else result.get("German").addCountry(country);
				break;
			case "Taiwan":
				if(!result.containsKey("Mandarin"))
					result.put("Mandarin", new Language("Mandarin", country));
				 else result.get("Mandarin").addCountry(country);
				break;
			case "Thailand":
				if(!result.containsKey("Thai"))
					result.put("Thai", new Language("Thai", country));
				 else result.get("Thai").addCountry(country);
				break;
			case "Turkey":
				if(!result.containsKey("Turkish"))
					result.put("Turkish", new Language("Turkish", country));
				 else result.get("Turkish").addCountry(country);
				break;
			case "Ukraine":
				if(!result.containsKey("Ukrainian"))
					result.put("Ukrainian", new Language("Ukrainian", country));
				 else result.get("Ukrainian").addCountry(country);
				break;
			case "United Arab Emirates":
				if(!result.containsKey("Arabic"))
					result.put("Arabic", new Language("Arabic", country));
				 else result.get("Arabic").addCountry(country);
				break;
			case "United Kingdom":
				if(!result.containsKey("English"))
					result.put("English", new Language("English", country));
				 else result.get("English").addCountry(country);
				break;
			case "United States":
				if(!result.containsKey("English"))
					result.put("English", new Language("English", country));
				 else result.get("English").addCountry(country);
				break;
			case "Uruguay":
				if(!result.containsKey("Spanish"))
					result.put("Spanish", new Language("Spanish", country));
				 else result.get("Spanish").addCountry(country);
				break;
			case "Venezuela":
				if(!result.containsKey("Spanish"))
					result.put("Spanish", new Language("Spanish", country));
				 else result.get("Spanish").addCountry(country);
				break;
			case "Vietnam":
				if(!result.containsKey("Vietnamese"))
					result.put("Vietnamese", new Language("Vietnamese", country));
				 else result.get("Vietnamese").addCountry(country);
				break;
			}
			
		}
		
		
		
		return result;
	}
	

}
