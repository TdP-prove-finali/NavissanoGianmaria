package it.polito.tdp.prova_tesi.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.polito.tdp.prova_tesi.db.JobsDAO;
import it.polito.tdp.prova_tesi.db.UniversitiesDAO;

public class Model {
	
	private JobsDAO jobsDao;
	private UniversitiesDAO uniDao;
	private Map<String, Language> langMap;
	private Map<String, University> idMapUni;
	private List<University> bestUniversities;
	private double bestScore;
	private List<University> validUniversities;
	private List<Job> jobs;
	
	
	public Model() {
		jobsDao = new JobsDAO();
		uniDao = new UniversitiesDAO();
		idMapUni = new HashMap<>();
		
		//inizializzo l'elenco di paesi-lingue
		this.langMap = this.setLanguages(this.uniDao.getAllCountries());
		
		//inizializzo l'elenco di università
		this.uniDao.getAllUniversities(idMapUni);
		this.uniDao.getMacroSubjectsRankings(idMapUni);
		this.uniDao.setAllSubjects(idMapUni);
		
		//inizializzo i jobs
		this.jobs = this.jobsDao.getAllJobs();
		
	}
	
	public List<String> getAllSkills(){
		
		List<String> skills = this.jobsDao.getAllSkills();
		
		Set<String> singleSkills = new HashSet<>();
		
		for(String s : skills) {
			String[] sTemp = s.split("[|]+");
			for(String st : sTemp) {
				st = st.trim();
				if(!singleSkills.contains(st.toLowerCase()) && !st.equals(""))
					singleSkills.add(st.toLowerCase());
			}
		}
		List<String> result = new LinkedList<>(singleSkills);
		Collections.sort(result);
		return result;
	}
	
	public List<String> getAllIndustries(){
		List<String> result = this.jobsDao.getAllIndustries();
		Collections.sort(result);
		return result;
	}
	
	public List<String> getAllFunctionalAreas(){
		List<String> result = this.jobsDao.getAllFunctionalAreas();
		Collections.sort(result);
		return result;
	}
	
	public List<University> getAllUni(){
		List<University> uni = new LinkedList<>(this.idMapUni.values());
		Collections.sort(uni, new UniversityByName());
		return uni;
	}
	
	public List<Job> getAllJobs(){
		List<Job> result = new LinkedList<>(this.jobs);
		Collections.sort(result);
		return result;
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
	
	public void setValidUniversities(String language){
		
		//Le validUniversities sono le università che rispettano il requisito linguistico
		
		this.validUniversities = new LinkedList<>();
		Language l = this.langMap.get(language);
		
		
		for(University u : this.idMapUni.values()) {
			for(String country : l.getCountries()) {
				if(u.getCountry().equals(country) && !this.validUniversities.contains(u))
					this.validUniversities.add(u);
			}
		}
		
	}
	
	public List<University> getBestUniversities(String language, String subject, int artsAndHumanitiesValue, int engineeringAndTechnologyValue, 
			int lifeSciencesAndMedicineValue, int naturalSciencesValue, int socialSciencesAndManagementValue){
		this.bestUniversities = null;
		this.bestScore = Double.MIN_VALUE;
		List<University> result = new LinkedList<>();
				
		
		//Le valid universities sono quelle che rispettano il vincolo della lingua
		this.setValidUniversities(language);

		
		
		
		//List<University> parziale = new LinkedList<University>();
		//this.cerca(parziale, subject, artsAndHumanitiesValue, engineeringAndTechnologyValue, lifeSciencesAndMedicineValue, naturalSciencesValue, socialSciencesAndManagementValue, 0.0);
		
		//se le università che rispettano il requisito lingustico sono meno di
		//quelle che voglio estrarre allora ritorno direttamente l'elenco completo
		if(this.validUniversities.size()<5) {
			for(University u : this.validUniversities)
				this.calcolaScore(u, artsAndHumanitiesValue, engineeringAndTechnologyValue, lifeSciencesAndMedicineValue, naturalSciencesValue, socialSciencesAndManagementValue, subject);
			Collections.sort(validUniversities);
			return validUniversities;
		}
		
		Set<University> parziale = new HashSet<>();
		this.cercaUni(parziale, subject, artsAndHumanitiesValue, engineeringAndTechnologyValue, 
				lifeSciencesAndMedicineValue, naturalSciencesValue, socialSciencesAndManagementValue, 0.0, 0);
		Collections.sort(bestUniversities);
		return this.bestUniversities;
	}
	
	public List<University> getBestUniversities(Job jobOnOffer, String language){
		
		//individuo le subject relative alla industry ed alla functional_area del job
		List<String> subjects = this.getSubjectByIndustryAndFunctionalArea(jobOnOffer.getIndustry(), jobOnOffer.getFunctional_Area());
		if(subjects.isEmpty()) {
			return null;
		}
		
		//Le valid universities sono quelle che rispettano il vincolo della lingua
		this.setValidUniversities(language);
		
		//Tramite un metodo ricorsivo calcolo la lista di università migliori
		//in base all'overall_score ed ai ranking nelle subject individuate.
		//Dovrei riuscire ad utilizzare lo stesso metodo ricorsivo con un calcolaScore diverso!
		
		Set<University> parziale = new HashSet<>();
		this.bestScore = 0.0;
		this.bestUniversities = null;
		this.cercaUni(parziale, subjects, 0.0, 0);
		Collections.sort(bestUniversities);
		return this.bestUniversities;
	}
	
	public void cercaUni(Set<University> parziale, String subject, int artsAndHumanitiesValue, int engineeringAndTechnologyValue, 
			int lifeSciencesAndMedicineValue, int naturalSciencesValue, int socialSciencesAndManagementValue, double score, int L) {
		
		//metodo ricorsivo che esplora l'alternativa AGGIUNGO vs NON AGGIUNGO.
		
		//+++++++++++CASI TERMINALI++++++++++
		
		if(parziale.size()==5) {
			
			if(score>bestScore) {
				this.bestUniversities = new LinkedList<>(parziale);
				bestScore = score;
			}
			return;
			
		}
		//Quando finisco le uni
		if(L==this.validUniversities.size())
			return;
		
		
			
		//++++++++++++++++GENERO SOTTOPROBLEMI+++++++++++++++
		
		//Devo aggiungere o no l'elemento in posizione L della lista delle
		//università valide?
		
		University u = this.validUniversities.get(L);
		
		//Aggiungo
		parziale.add(u);
		
		
		double delta = this.calcolaScore(u, artsAndHumanitiesValue, engineeringAndTechnologyValue, 
				lifeSciencesAndMedicineValue, naturalSciencesValue, socialSciencesAndManagementValue, subject);
		
		this.cercaUni(parziale, subject, artsAndHumanitiesValue, engineeringAndTechnologyValue, 
				lifeSciencesAndMedicineValue, naturalSciencesValue, socialSciencesAndManagementValue, 
				score+delta, L+1);
		
		parziale.remove(u);
		
		
		//Non aggiungo
		this.cercaUni(parziale, subject, artsAndHumanitiesValue, engineeringAndTechnologyValue, 
				lifeSciencesAndMedicineValue, naturalSciencesValue, socialSciencesAndManagementValue, 
				score, L+1);
		
	}
	
	public double calcolaScore(University u, int artsAndHumanitiesValue, int engineeringAndTechnologyValue, int lifeSciencesAndMedicineValue, 
			int naturalSciencesValue, int socialSciencesAndManagementValue, String subject) {
		
		
		double score = 0.0;
			
		//il punteggio è dato dalla somma del ranking generico, dato dall'overall score
		score += u.getOverall_Score();
		
		//a cui va sommato il punteggio dato dalla Main Subject
		score += (double)u.getSubject(subject);
		
		//e dalla somma dei rankings per Macro-subject
		int multi = 1000; //moltiplicatore
		
		if(u.getArts_And_Humanitites_Rank()>0) {
			score += (double)multi*(double)artsAndHumanitiesValue*(1.0/(double)u.getArts_And_Humanitites_Rank());
		}
		if(u.getEngineering_And_Technology_Rank()>0) {
			score += (double)multi*(double)engineeringAndTechnologyValue*(1.0/(double)u.getEngineering_And_Technology_Rank());
		}
		if(u.getLife_Sciences_And_Technology_Rank()>0) {
			score += (double)multi*(double)lifeSciencesAndMedicineValue*(1.0/(double)u.getLife_Sciences_And_Technology_Rank());
		}
		if(u.getNatural_Sciences_Rank()>0) {
			score += (double)multi*(double)naturalSciencesValue*(1.0/(double)u.getNatural_Sciences_Rank());
		}
		if(u.getSocial_Sciences_And_Management_Rank()>0) {
			score += (double)multi*(double)socialSciencesAndManagementValue*(1.0/(double)u.getSocial_Sciences_And_Management_Rank());
		}
		
		u.setTempScore(score);
		
		return score;
	}
	
	public void cercaUni(Set<University> parziale, List<String> subjects, double score, int L) {
		
		//metodo ricorsivo che esplora l'alternativa AGGIUNGO vs NON AGGIUNGO.
		
				//+++++++++++CASI TERMINALI++++++++++
				
				if(parziale.size()==5) {
					
					if(score>bestScore) {
						this.bestUniversities = new LinkedList<>(parziale);
						bestScore = score;
					}
					return;
					
				}
				//Quando finisco le uni
				if(L==this.validUniversities.size())
					return;
				
				
					
				//++++++++++++++++GENERO SOTTOPROBLEMI+++++++++++++++
				
				//Devo aggiungere o no l'elemento in posizione L della lista delle
				//università valide?
				
				University u = this.validUniversities.get(L);
				
				//Aggiungo
				parziale.add(u);
				
				
				double delta = this.calcolaScore(u, subjects);
				
				this.cercaUni(parziale, subjects, score+delta, L+1);
				
				parziale.remove(u);
				
				
				//Non aggiungo
				this.cercaUni(parziale, subjects, score, L+1);
				
	}
	
	public double calcolaScore(University u, List<String> subjects) {
		
		double score = 0.0;
		int multi = 1000;
		
		//il punteggio è dato dalla somma del ranking generico, dato dall'overall score
		score += u.getOverall_Score();
		
		//a cui devo sommare il contributo dato dal ranking nelle subjects passate come parametro
		for(String s : subjects) {
			int sRank = u.getSubject(s);
			if(sRank>0)
				score += (double)multi*(1.0/(double)sRank);
		}
		
		u.setTempScore(score);
		
		return score;
	}
	
	public List<Job> getBestJobs(String industry, String functional_Area, String skill){
		
		//individuo i jobs tramite functional_area e industry
		List<Job> result = new LinkedList<>();
		
		for(Job j : this.jobs) {
			if(j.getFunctional_Area().equals(functional_Area) && j.getIndustry().equals(industry))
				result.add(j);
		}
		
		if(result.isEmpty())
			return null;
		
		//ordiniamo i lavori presenti in result in base alla presenza della best skill
		Collections.sort(result, new JobsComparator(skill));
		
		return result;
		
	}
	
	public List<Job> getBestJobs(University uBachelor, University uMaster, String skill){
		
		//individuo le subject principali delle università in input
		List<String> subjectUBachelor = new LinkedList<>();
		List<String> subjectUMaster = new LinkedList<>();
		
		if(uBachelor!=null)
			subjectUBachelor = uBachelor.getBestSubject(this.getAllSubjects());
		if(uMaster!=null)
			subjectUMaster = uMaster.getBestSubject(this.getAllSubjects());
		
		
		//individuo le industry legate alle subject trovate
		List<String> industries = new LinkedList<>();
		for(String i : this.getAllIndustries()) {
			for(String subjectByIndustry : this.getSubjectByIndustryAndFunctionalArea(i, "")) {
				for(String subjectBachelor : subjectUBachelor) {
					if(subjectBachelor.equals(subjectByIndustry) && !industries.contains(i))
						industries.add(i);
				}
				for(String subjectMaster : subjectUMaster) {
					if(subjectMaster.equals(subjectByIndustry) && !industries.contains(i))
						industries.add(i);
				}
			}
		}
		
		//individuo le functional_Area legate alle subject trovate
		List<String> functionalAreas = new LinkedList<>();
		for(String fa : this.getAllFunctionalAreas()) {
			for(String subjectByFArea : this.getSubjectByIndustryAndFunctionalArea("", fa)) {
				for(String subjectBachelor : subjectUBachelor) {
					if(subjectBachelor.equals(subjectByFArea) && !functionalAreas.contains(fa))
						functionalAreas.add(fa);
				}
				for(String subjectMaster : subjectUMaster) {
					if(subjectMaster.equals(subjectByFArea) && !functionalAreas.contains(fa))
						functionalAreas.add(fa);
				}
			}
		}
		
		if(industries.isEmpty() || functionalAreas.isEmpty())
			return null;
		
		//infine individuo i job legati a quelle industry e functionalArea
		List<Job> result = new LinkedList<>();
		
		for(Job j : this.jobs) {
			for(String i : industries) {
				for(String fa : functionalAreas) {
					if(j.getFunctional_Area().equals(fa) && j.getIndustry().equals(i))
						result.add(j);
				}
			}
			
		}
		
		if(result.isEmpty())
			return null;
		
		//ordiniamo i lavori presenti in result in base alla presenza della best skill
		Collections.sort(result, new JobsComparator(skill));
		
		return result;
		
	}
	
	
	public int getValidUniversitiesNumber(String language) {
		this.setValidUniversities(language);
		return this.validUniversities.size();
	}
	
	
	public List<String> getSubjectByIndustryAndFunctionalArea(String industry, String functional_Area){
	
		List<String> result = new LinkedList<>();
		
		switch(industry) {
		case "Accounting, Finance":
			result.add("Accounting_and_Finance");
			break;
		case "Advertising, PR, MR, Event Management":
			result.add("Communication_and_Media_Studies");
			break;
		case "Agriculture, Dairy":
			result.add("Agriculture_and_Forestry");
			break;
		case "Animation, Gaming":
			result.add("Computer_Science_and_Information");
			break;
		case "Architecture, Interior Design":
			result.add("Architecture_and_Built_Environment");
			result.add("Art_and_Design");
			break;
		case "Automobile, Auto Anciliary, Auto Components":
			result.add("Mechanical_Aeronautical_and_Manufacturing_Engineering");
			break;
		case "Aviation, Aerospace, Aeronautical":
			result.add("Mechanical_Aeronautical_and_Manufacturing_Engineering");
			break;
		case "Banking, Financial Services, Broking":
			result.add("Economics_and_Econometrics");
			break;
		case "BPO, Call Centre, ITeS":
			result.add("Communication_and_Media_Studies");
			break;
		case "Ceramics, Sanitary ware":
			result.add("Materials_Sciences");
			break;
		case "Chemicals, PetroChemical, Plastic, Rubber":
			result.add("Engineering_Chemical");
			result.add("Materials_Sciences");
			break;
		case "Construction, Engineering, Cement, Metals":
			result.add("Mechanical_Aeronautical_and_Manufacturing_Engineering");
			result.add("Materials_Sciences");
			break;
		case "Consumer Electronics, Appliances, Durables":
			result.add("Electrical_and_Electronic_Engineering");
			result.add("Computer_Science_and_Information");
			break;
		case "Courier, Transportation, Freight , Warehousing":
			result.add("Economics_and_Econometrics");
			break;
		case "Education, Teaching, Training":
			result.add("Education_and_Training");
			break;
		case "Electricals, Switchgears":
			result.add("Electrical_and_Electronic_Engineering");
			result.add("Computer_Science_and_Information");
			break;
		case "Export, Import":
			result.add("Economics_and_Econometrics");
			result.add("Business_and_Management_Studies");
			break;
		case "Facility Management":
			result.add("Education_and_Training");
			result.add("Business_and_Management_Studies");
			break;
		case "Fertilizers, Pesticides":
			result.add("Agriculture_and_Forestry");
			break;
		case "FMCG, Foods, Beverage":
			result.add("Hospitality_and_Leisure_Management");
			break;
		case "Food Processing":
			result.add("Hospitality_and_Leisure_Management");
			break;
		case "Gems, Jewellery":
			result.add("Mineral_and_Mining_Engineering");
			break;
		case "Government, Defence":
			result.add("Politics");
			break;
		case "Heat Ventilation, Air Conditioning":
			result.add("Mechanical_Aeronautical_and_Manufacturing_Engineering");
			break;
		case "Industrial Products, Heavy Machinery":
			result.add("Mechanical_Aeronautical_and_Manufacturing_Engineering");
			break;
		case "Insurance":
			result.add("Economics_and_Econometrics");
			break;
		case "Internet, Ecommerce":
			result.add("Computer_Science_and_Information");
			result.add("Electrical_and_Electronic_Engineering");
			break;
		case "Iron and Steel":
			result.add("Materials_Sciences");
			break;
		case "IT-Hardware & Networking":
			result.add("Computer_Science_and_Information");
			result.add("Electrical_and_Electronic_Engineering");
			break;
		case "IT-Software, Software Services":
			result.add("Computer_Science_and_Information");
			result.add("Electrical_and_Electronic_Engineering");
			break;
		case "KPO, Research, Analytics":
			result.add("Statistics_and_Operational_Research");
			break;
		case "Legal":
			result.add("Law_and_Legal_Studies");
			break;
		case "Media, Entertainment, Internet":
			result.add("Communication_and_Media_Studies");
			break;
		case "Medical, Healthcare, Hospitals":
			result.add("Medicine");
			result.add("Nursing");
			break;
		case "NGO, Social Services, Regulators, Industry Associations":
			result.add("Psychology");
			break;
		case "Oil and Gas, Energy, Power, Infrastructure":
			result.add("Mineral_and_Mining_Engineering");
			break;
		case "Pharma, Biotech, Clinical Research":
			result.add("Pharmacy_and_Pharmacology");
			break;
		case "Printing, Packaging":
			result.add("Communication_and_Media_Studies");
			break;
		case "Publishing":
			result.add("Communication_and_Media_Studies");
			break;
		case "Real Estate, Property":
			result.add("Economics_and_Econometrics");
			break;
		case "Recruitment, Staffing":
			result.add("Sociology");
			result.add("Psychology");
			break;
		case "Retail, Wholesale":
			result.add("Economics_and_Econometrics");
			break;
		case "Semiconductors, Electronics":
			result.add("Computer_Science_and_Information");
			result.add("Electrical_and_Electronic_Engineering");
			break;
		case "Strategy, Management Consulting Firms":
			result.add("Business_and_Management_Studies");
			break;
		case "Telcom, ISP":
			result.add("Computer_Science_and_Information");
			result.add("Electrical_and_Electronic_Engineering");
			break;
		case "Textiles, Garments, Accessories":
			result.add("Materials_Sciences");
			break;
		case "Travel , Hotels , Restaurants , Airlines , Railways":
			result.add("Hospitality_and_Leisure_Management");
			break;
		case "Water Treatment, Waste Management":
			result.add("Environmental_Sciences");
			break;
		case "Wellness , Fitness , Sports, Beauty":
			result.add("Sports_related_Subjects");
			break;
		}
		
		switch(functional_Area) {
		case "Accounts , Finance , Tax , Company Secretary , Audit":
			if(!result.contains("Accounting_and_Finance")) result.add("Accounting_and_Finance");
			break;
		case "Analytics & Business Intelligence":
			if(!result.contains("Mathematics")) result.add("Mathematics");
			if(!result.contains("Statistics_and_Operational_Research")) result.add("Statistics_and_Operational_Research");
			break;
		case "Architecture , Interior Design":
			if(!result.contains("Architecture_and_Built_Environment")) result.add("Architecture_and_Built_Environment");
			break;
		case "Beauty / Fitness / Spa Services":
			if(!result.contains("Hospitality_and_Leisure_Management")) result.add("Hospitality_and_Leisure_Management");
			break;
		case "Defence Forces , Security Services":
			if(!result.contains("Sports_related_Subjects")) result.add("Sports_related_Subjects");
			break;
		case "Design , Creative , User Experience":
			if(!result.contains("Art_and_Design")) result.add("Art_and_Design");
			break;
		case "Engineering Design , R&D":
			if(!result.contains("Mechanical_Aeronautical_and_Manufacturing_Engineering")) result.add("Mechanical_Aeronautical_and_Manufacturing_Engineering");
			break;
		case "Executive Assistant , Front Office , Data Entry":
			if(!result.contains("Social_Policy_and_Administration")) result.add("Social_Policy_and_Administration");
			break;
		case "Export , Import , Merchandising":
			if(!result.contains("Business_and_Management_Studies")) result.add("Business_and_Management_Studies");
			if(!result.contains("Economics_and_Econometrics")) result.add("Economics_and_Econometrics");
			break;
		case "Fashion Designing , Merchandising":
			if(!result.contains("Art_and_Design")) result.add("Art_and_Design");
			break;
		case "Financial Services , Banking , Investments , Insurance":
			if(!result.contains("Economics_and_Econometrics")) result.add("Economics_and_Econometrics");
			break;
		case "Hotels , Restaurants":
			if(!result.contains("Hospitality_and_Leisure_Management")) result.add("Hospitality_and_Leisure_Management");
			break;
		case "HR , Recruitment , Administration , IR":
			if(!result.contains("Psychology")) result.add("Psychology");
			if(!result.contains("Social_Policy_and_Administration")) result.add("Social_Policy_and_Administration");
			break;
		case "IT Hardware , Technical Support , Telecom Engineering":
			if(!result.contains("Electrical_and_Electronic_Engineering")) result.add("Electrical_and_Electronic_Engineering");
			if(!result.contains("Computer_Science_and_Information")) result.add("Computer_Science_and_Information");
			break;
		case "IT Software - Application Programming , Maintenance":
			if(!result.contains("Electrical_and_Electronic_Engineering")) result.add("Electrical_and_Electronic_Engineering");
			if(!result.contains("Computer_Science_and_Information")) result.add("Computer_Science_and_Information");
			break;
		case "IT Software - Client/Server Programming":
			if(!result.contains("Electrical_and_Electronic_Engineering")) result.add("Electrical_and_Electronic_Engineering");
			if(!result.contains("Computer_Science_and_Information")) result.add("Computer_Science_and_Information");
			break;
		case "IT Software - DBA , Datawarehousing":
			if(!result.contains("Electrical_and_Electronic_Engineering")) result.add("Electrical_and_Electronic_Engineering");
			if(!result.contains("Computer_Science_and_Information")) result.add("Computer_Science_and_Information");
			break;
		case "IT Software - eCommerce , Internet Technologies":
			if(!result.contains("Electrical_and_Electronic_Engineering")) result.add("Electrical_and_Electronic_Engineering");
			if(!result.contains("Computer_Science_and_Information")) result.add("Computer_Science_and_Information");
			break;
		case "IT Software - Embedded , EDA , VLSI , ASIC , Chip Design":
			if(!result.contains("Electrical_and_Electronic_Engineering")) result.add("Electrical_and_Electronic_Engineering");
			if(!result.contains("Computer_Science_and_Information")) result.add("Computer_Science_and_Information");
			break;
		case "IT Software - ERP , CRM":
			if(!result.contains("Electrical_and_Electronic_Engineering")) result.add("Electrical_and_Electronic_Engineering");
			if(!result.contains("Computer_Science_and_Information")) result.add("Computer_Science_and_Information");
			break;
		case "IT Software - Mobile":
			if(!result.contains("Electrical_and_Electronic_Engineering")) result.add("Electrical_and_Electronic_Engineering");
			if(!result.contains("Computer_Science_and_Information")) result.add("Computer_Science_and_Information");
			break;
		case "IT Software - Network Administration , Security":
			if(!result.contains("Electrical_and_Electronic_Engineering")) result.add("Electrical_and_Electronic_Engineering");
			if(!result.contains("Computer_Science_and_Information")) result.add("Computer_Science_and_Information");
			break;
		case "IT Software - Other":
			if(!result.contains("Electrical_and_Electronic_Engineering")) result.add("Electrical_and_Electronic_Engineering");
			if(!result.contains("Computer_Science_and_Information")) result.add("Computer_Science_and_Information");
			break;
		case "IT Software - QA & Testing":
			if(!result.contains("Electrical_and_Electronic_Engineering")) result.add("Electrical_and_Electronic_Engineering");
			if(!result.contains("Computer_Science_and_Information")) result.add("Computer_Science_and_Information");
			break;
		case "IT Software - System Programming":
			if(!result.contains("Electrical_and_Electronic_Engineering")) result.add("Electrical_and_Electronic_Engineering");
			if(!result.contains("Computer_Science_and_Information")) result.add("Computer_Science_and_Information");
			break;
		case "IT Software - Systems , EDP , MIS":
			if(!result.contains("Electrical_and_Electronic_Engineering")) result.add("Electrical_and_Electronic_Engineering");
			if(!result.contains("Computer_Science_and_Information")) result.add("Computer_Science_and_Information");
			break;
		case "IT Software - Telecom Software":
			if(!result.contains("Electrical_and_Electronic_Engineering")) result.add("Electrical_and_Electronic_Engineering");
			if(!result.contains("Computer_Science_and_Information")) result.add("Computer_Science_and_Information");
			break;
		case "ITES , BPO , KPO , LPO , Customer Service , Operations":
			if(!result.contains("Statistics_and_Operational_Research")) result.add("Statistics_and_Operational_Research");
			break;
		case "Journalism , Editing , Content":
			if(!result.contains("Communication_and_Media_Studies")) result.add("Communication_and_Media_Studies");
			break;
		case "Legal , Regulatory , Intellectual Property":
			if(!result.contains("Law_and_Legal_Studies")) result.add("Law_and_Legal_Studies");
			break;
		case "Marketing , Advertising , MR , PR , Media Planning":
			if(!result.contains("Economics_and_Econometrics")) result.add("Economics_and_Econometrics");
			if(!result.contains("Communication_and_Media_Studies")) result.add("Communication_and_Media_Studies");
			break;
		case "Medical , Healthcare , R&D , Pharmaceuticals , Biotechnology":
			if(!result.contains("Medicine")) result.add("Medicine");
			if(!result.contains("Nursing")) result.add("Nursing");
			if(!result.contains("Pharmacy_and_Pharmacology")) result.add("Pharmacy_and_Pharmacology");
			break;
		case "Production , Manufacturing , Maintenance":
			if(!result.contains("Mechanical_Aeronautical_and_Manufacturing_Engineering")) result.add("Mechanical_Aeronautical_and_Manufacturing_Engineering");
			break;
		case "Purchase / Logistics / Supply Chain":
			if(!result.contains("Business_and_Management_Studies")) result.add("Business_and_Management_Studies");
			break;
		case "Sales , Retail , Business Development":
			if(!result.contains("Business_and_Management_Studies")) result.add("Business_and_Management_Studies");
			break;
		case "Site Engineering , Project Management":
			if(!result.contains("Business_and_Management_Studies")) result.add("Business_and_Management_Studies");
			break;
		case "Strategy , Management Consulting , Corporate Planning":
			if(!result.contains("Business_and_Management_Studies")) result.add("Business_and_Management_Studies");
			break;
		case "Supply Chain , Logistics , Purchase , Materials":
			if(!result.contains("Business_and_Management_Studies")) result.add("Business_and_Management_Studies");
			break;
		case "Teaching , Education , Training , Counselling":
			if(!result.contains("Education_and_Training")) result.add("Education_and_Training");
			break;
		case "Travel , Tours , Ticketing , Airlines":
			if(!result.contains("Hospitality_and_Leisure_Management")) result.add("Hospitality_and_Leisure_Management");
			break;
		case "TV , Films , Production , Broadcasting":
			if(!result.contains("Communication_and_Media_Studies")) result.add("Communication_and_Media_Studies");
			break;
		}
		
		return result;
		
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
				/*if(!result.containsKey("Italian"))
					result.put("Italian", new Language("Italian", country));
				 else result.get("Italian").addCountry(country);*/
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
	
	public List<String> getAllSubjects(){
		List<String> subjects = new LinkedList<>();
		subjects.add("Accounting and Finance");
		subjects.add("Agriculture and Forestry");
		subjects.add("Anatomy and Physiology");
		subjects.add("Anthropology");
		subjects.add("Archaeology");
		subjects.add("Architecture and Built Environment");
		subjects.add("Art and Design");
		subjects.add("Biological Sciences");
		subjects.add("Business and Management Studies");
		subjects.add("Chemistry");
		subjects.add("Classics and Ancient History");
		subjects.add("Communication and Media Studies");
		subjects.add("Computer Science and Information");
		subjects.add("Dentistry");
		subjects.add("Developement Studies");
		subjects.add("Economics and Econometrics");
		subjects.add("Education and Training");
		subjects.add("Electrical and Electronic Engineering");
		subjects.add("Engineering Chemical");
		subjects.add("English Language and Literature");
		subjects.add("Environmental Sciences");
		subjects.add("Geography");
		subjects.add("History");
		subjects.add("Hospitality and Leisure Management");
		subjects.add("Law and Legal Studies");
		subjects.add("Library and Information Management");
		subjects.add("Linguistics");
		subjects.add("Materials Sciences");
		subjects.add("Mathematics");
		subjects.add("Mechanical Aeronautical and Manufacturing Engineering");
		subjects.add("Medicine");
		subjects.add("Mineral and Mining Engineering");
		subjects.add("Modern Languages");
		subjects.add("Nursing");
		subjects.add("Performing Arts");
		subjects.add("Pharmacy and Pharmacology");
		subjects.add("Philosophy");
		subjects.add("Physics and Astronomy");
		subjects.add("Politics");
		subjects.add("Psychology");
		subjects.add("Social Policy and Administration");
		subjects.add("Sociology");
		subjects.add("Sports related Subjects");
		subjects.add("Statistics and Operational Research");
		subjects.add("Theology, Divinity and Religious Studies");
		subjects.add("Veterinary Science");
		
		return subjects;
	}
	

}
