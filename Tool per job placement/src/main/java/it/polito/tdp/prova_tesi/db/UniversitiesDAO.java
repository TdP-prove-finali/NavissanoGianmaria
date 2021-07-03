package it.polito.tdp.prova_tesi.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.prova_tesi.model.University;

public class UniversitiesDAO {
	
	public List<String> getAllCountries() {
		String sql="select distinct country "
				+ "from qsuniversityrankings";
		List<String> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				if(rs.getString("country")!=null)
					result.add(rs.getString("country"));
			}
			conn.close();
			return result;
			
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void getAllUniversities(Map<String, University> idMap) {
		String sql="select ranking_2019, institution_name, country, overall_score\n"
				+ "from qsuniversityrankings\n"
				+ "where institution_name is not null and ranking_2019 <>'N/A'";
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				if(!idMap.containsKey(rs.getString("institution_name"))) {
					
					int ranking = 0;
					double score = 0.0;
					
					//Controllo i dati
					
					//La stringa del ranking può presentarsi in 3 versioni:
					//1. formato numerico semplice
					//2. numero seguito da '=' (28=) per indicare parità nel ranking
					//3. range di numeri (501-550) utilizzato nelle posizioni inferiori per indicare la parità di più università
					String rankingString = rs.getString("ranking_2019");
					
					if(rankingString.contains("=")) {
						
						
						//è un ranking di parità
						try { ranking = Integer.parseInt(rankingString.substring( 0, (rankingString.length()-1) ));
							}catch(NumberFormatException e) {
							throw new RuntimeException("Error in database reading: uncorrect ranking for "+rs.getString("institution_name"), e);
							}
						
						
					} else {
						
						
						if(rankingString.contains("-")) {
							
							
							//è un ranking tra un range (es 100-150)
							try { ranking = Integer.parseInt(rankingString.substring( 0, rankingString.indexOf('-')));
							}catch(NumberFormatException e) {
							throw new RuntimeException("Error in database reading: uncorrect ranking for "+rs.getString("institution_name"), e);
							}
							
							
						} else {
							
							
							//ranking numerico standard
							try {ranking = Integer.parseInt(rankingString);
							} catch(NumberFormatException e) {
								throw new RuntimeException("Error in database reading: uncorrect ranking for "+rs.getString("institution_name"), e);
							}
						}
						
					}
					
					
					//il valore dell'overall_score può essere in 2 versioni:
					//1.range di valori (24.1 - 24.6)
					//2. valore numerico standard
					String scoreString = rs.getString("overall_score");
					
					if(scoreString.contains("-")) {
						
						
						//lo score è un range (es. 24.1 - 24.6)
						try {
							double s1 = Double.parseDouble(scoreString.substring(0, scoreString.indexOf("-")-1));
							double s2 = Double.parseDouble(scoreString.substring(scoreString.indexOf("-")+1));
							score = (s1+s2)/(double)2;
						} catch(NumberFormatException e) {
							throw new RuntimeException("Error in database reading: uncorrect overall score for "+rs.getString("institution_name"), e);
						}
						
						
					} else {
						
						//score standard
						try {
							score = Double.parseDouble(scoreString);
						} catch(NumberFormatException e) {
							throw new RuntimeException("Error in database reading: uncorrect overall score for "+rs.getString("institution_name"), e);
						}
						
					}
					
					
					//Dopo il controllo sui dati posso aggiungere l'università alla lista
					if(score!=0 && ranking!=0) {
						University uni = new University(ranking, rs.getString("institution_name"), rs.getString("country"), score);
						idMap.put(uni.getInstitution_Name(), uni);
					}
					
					
				}
					
			}
			conn.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
			
		}
		
		
		
	}
	public void getMacroSubjectsRankings(Map<String, University> idMap) {
		this.getArtAndHumanitiesRank(idMap);
		this.getEngineeringAndTechnologyRank(idMap);
		this.getLifeSciencesAndTechnologyRank(idMap);
		this.getNaturalSciencesRank(idMap);
		this.getSocialSciencesAndManagementRank(idMap);
	}
	
	
	public void getArtAndHumanitiesRank(Map<String, University> idMap) {
		String sql = "SELECT q.institution_name, a.rank "
				+ "FROM qsuniversityrankings q, Arts_And_Humanities a "
				+ "WHERE q.institution_name IS NOT NULL AND q.ranking_2019 <>'N/A' "
				+ "AND q.institution_name = a.university_name";
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			

			while(rs.next()) {
				if(idMap.containsKey(rs.getString("q.institution_name"))) {
					
					
					University u = idMap.get(rs.getString("q.institution_name"));
					u.setArts_And_Humanitites_Rank(rs.getInt("a.rank"));
					
				}
			}
			
			conn.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void getEngineeringAndTechnologyRank(Map<String, University> idMap) {
		String sql = "SELECT q.institution_name, e.rank "
				+ "FROM qsuniversityrankings q, Engineering_And_Technology e "
				+ "WHERE q.institution_name IS NOT NULL AND q.ranking_2019 <>'N/A' "
				+ "AND q.institution_name = e.university_name";
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			

			while(rs.next()) {
				if(idMap.containsKey(rs.getString("q.institution_name"))) {
					
					
					University u = idMap.get(rs.getString("q.institution_name"));
					u.setEngineering_And_Technology_Rank(rs.getInt("e.rank"));
					
				}
			}
			
			conn.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void getLifeSciencesAndTechnologyRank(Map<String, University> idMap) {
		String sql = "SELECT q.institution_name, l.rank "
				+ "FROM qsuniversityrankings q, Life_Sciences_And_Technology l "
				+ "WHERE q.institution_name IS NOT NULL AND q.ranking_2019 <>'N/A' "
				+ "AND q.institution_name = l.university_name";
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			

			while(rs.next()) {
				if(idMap.containsKey(rs.getString("q.institution_name"))) {
					
					
					University u = idMap.get(rs.getString("q.institution_name"));
					u.setLife_Sciences_And_Technology_Rank(rs.getInt("l.rank"));
					
				}
			}
			
			conn.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void getNaturalSciencesRank(Map<String, University> idMap) {
		String sql = "SELECT q.institution_name, n.rank "
				+ "FROM qsuniversityrankings q, Natural_Sciences n "
				+ "WHERE q.institution_name IS NOT NULL AND q.ranking_2019 <>'N/A' "
				+ "AND q.institution_name = n.university_name";
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while(rs.next()) {
				if(idMap.containsKey(rs.getString("q.institution_name"))) {
					
					
					University u = idMap.get(rs.getString("q.institution_name"));
					u.setNatural_Sciences_Rank(rs.getInt("n.rank"));
					
				}
			}
			
			conn.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void getSocialSciencesAndManagementRank(Map<String, University> idMap) {
		String sql = "SELECT q.institution_name, s.rank "
				+ "FROM qsuniversityrankings q, Social_Sciences_And_Management s "
				+ "WHERE q.institution_name IS NOT NULL AND q.ranking_2019 <>'N/A' "
				+ "AND q.institution_name = s.university_name";
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				if(idMap.containsKey(rs.getString("q.institution_name"))) {
					
					
					University u = idMap.get(rs.getString("q.institution_name"));
					u.setSocial_Sciences_And_Management_Rank(rs.getInt("s.rank"));
					
					
				}
			}
			
			conn.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	/*public int getSubjectScore(String subject, University uni) {
		String sql = "SELECT u.Institution_Name, s.Accounting_and_Finance as 'Accounting and Finance', s.`Agriculture_&_Forestry` as 'Agriculture & Forestry', s.`Anatomy_&_Physiology` as 'Anatomy & Physiology', s.Anthropology as 'Anthropology', s.Archaeology as 'Archaeology', s.`Architecture_&_Built_Environment` as 'Architecture & Built Environment', s.`Art_&_Design` as 'Art & Design', s.Biological_Sciences as 'Biological Sciences', s.`Business_&_Management_Studies` as 'Business & Management Studies', s.Chemistry as 'Chemistry', s.`Classics_&_Ancient_History` as 'Classics & Ancient History', s.Communication_and_Media_Studies as 'Communication and Media Studies', s.Computer_Science_and_Information as 'Computer Science and Information', s.Dentistry as 'Dentistry', s.Developement_studies as 'Developement Studies', s.Economics_and_Econometrics as 'Economics and Econometrics', s.Education_and_Training as 'Education and Training', s.`Electrical_&_Electronic_Engineering` as 'Electrical & Electronic Engineering', s.Engineering_Chemical as 'Engineering Chemical', s.`English_Language_&_Literature` as 'English Language & Literature', s.Environmental_Sciences as 'Environmental Sciences', s.Geography as 'Geography', s.History as 'History', s.`Hospitality_&_Leisure_Management` as 'Hospitality & Leisure Management', s.`Law_&_Legal_Studies` as 'Law & Legal Studies', s.`Library_&_Information_Management` as 'Library & Information Management', s.Linguistics as 'Linguistics', s.Materials_Sciences as 'Materials Sciences', s.Mathematics as 'Mathematics', s.Mechanical_Aeronautical_and_Manufacturing_Engineering as 'Mechanical Aeronautical and Manufacturing Engineering', s.Medicine as 'Medicine', s.`Mineral_&_Mining_Engineering` as 'Mineral & Mining Engineering', s.Modern_Languages as 'Modern Languages', s.Nursing as 'Nursing', s.Performing_Arts as 'Performing Arts', s.`Pharmacy_&_Pharmacology` as 'Pharmacy & Pharmacology', s.Philosophy as 'Philosophy', s.`Physics_&_Astronomy` as 'Physics & Astronomy', s.Politics as 'Politics', s.Psychology as 'Psychology', s.`Social_Policy_&_Administration` as 'Social Policy & Administration', s.Sociology as 'Sociology', s.`Sports-related_Subjects` as 'Sports-related Subjects', s.`Statistics_&_Operational_Research` as 'Statistics & Operational Research', s.`Theology_Divinity_&_Religious_Studies` as 'Theology, Divinity & Religious Studies', s.Veterinary_Science as 'Veterinary Science' "
				+ "FROM University_Rankings_by_Subject s, QSUniversityRankings u "
				+ "WHERE u.Institution_Name = s.University_Name "
				+ "AND u.Institution_Name = ?";
		int res = 0;
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, uni.getInstitution_Name());
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				res = rs.getInt(subject);
			}
			
			conn.close();
			return res;
			
		} catch(SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}*/
	
	public void setAllSubjects(Map<String, University> idMap) {
		
		String sql = "SELECT u.Institution_Name, s.Accounting_and_Finance, s.Agriculture_and_Forestry, s.Anatomy_and_Physiology, s.Anthropology, s.Archaeology, s.Architecture_and_Built_Environment, s.Art_and_Design, s.Biological_Sciences, s.Business_and_Management_Studies, s.Chemistry, s.Classics_and_Ancient_History, s.Communication_and_Media_Studies, s.Computer_Science_and_Information, s.Dentistry, s.Developement_studies, s.Economics_and_Econometrics, s.Education_and_Training, s.Electrical_and_Electronic_Engineering, s.Engineering_Chemical, s.English_Language_and_Literature, s.Environmental_Sciences, s.Geography, s.History, s.Hospitality_and_Leisure_Management, s.Law_and_Legal_Studies, s.Library_and_Information_Management, s.Linguistics, s.Materials_Sciences, s.Mathematics, s.Mechanical_Aeronautical_and_Manufacturing_Engineering, s.Medicine, s.Mineral_and_Mining_Engineering, s.Modern_Languages, s.Nursing, s.Performing_Arts, s.Pharmacy_and_Pharmacology, s.Philosophy, s.Physics_and_Astronomy, s.Politics, s.Psychology, s.Social_Policy_and_Administration, s.Sociology, s.Sports_related_Subjects, s.Statistics_and_Operational_Research, s.Theology_Divinity_and_Religious_Studies, s.Veterinary_Science "
				+ "FROM University_Rankings_by_Subject s, QSUniversityRankings u "
				+ "WHERE u.Institution_Name = s.University_Name";
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				if(idMap.containsKey(rs.getString("u.institution_name"))) {
					
					
					University u = idMap.get(rs.getString("u.institution_name"));
					
					u.setAccounting_and_Finance(rs.getInt("s.Accounting_and_Finance"));
					u.setAgriculture_and_Forestry(rs.getInt("s.Agriculture_and_Forestry"));
					u.setAnatomy_and_Physiology(rs.getInt("s.Anatomy_and_Physiology"));
					u.setAnthropology(rs.getInt("s.Anthropology"));
					u.setArchaeology(rs.getInt("s.Archaeology"));
					u.setArchitecture_and_Built_Environment(rs.getInt("s.Architecture_and_Built_Environment"));
					u.setArt_and_Design(rs.getInt("s.Art_and_Design"));
					u.setBiological_Sciences(rs.getInt("s.Biological_Sciences"));
					u.setBusiness_and_Management_Studies(rs.getInt("s.Business_and_Management_Studies"));
					u.setChemistry(rs.getInt("s.Chemistry"));
					u.setClassics_and_Ancient_History(rs.getInt("s.Classics_and_Ancient_History"));
					u.setCommunication_and_Media_Studies(rs.getInt("s.Communication_and_Media_Studies"));
					u.setComputer_Science_and_Information(rs.getInt("s.Computer_Science_and_Information"));
					u.setDentistry(rs.getInt("s.Dentistry"));
					u.setDevelopement_studies(rs.getInt("s.Developement_studies"));
					u.setEconomics_and_Econometrics(rs.getInt("s.Economics_and_Econometrics"));
					u.setEducation_and_Training(rs.getInt("s.Education_and_Training"));
					u.setElectrical_and_Electronic_Engineering(rs.getInt("s.Electrical_and_Electronic_Engineering"));
					u.setEngineering_Chemical(rs.getInt("s.Engineering_Chemical"));
					u.setEnglish_Language_and_Literature(rs.getInt("s.English_Language_and_Literature"));
					u.setEnvironmental_Sciences(rs.getInt("s.Environmental_Sciences"));
					u.setGeography(rs.getInt("s.Geography"));
					u.setHistory(rs.getInt("s.History"));
					u.setHospitality_and_Leisure_Management(rs.getInt("s.Hospitality_and_Leisure_Management"));
					u.setLaw_and_Legal_Studies(rs.getInt("s.Law_and_Legal_Studies"));
					u.setLibrary_and_Information_Management(rs.getInt("s.Library_and_Information_Management"));
					u.setLinguistics(rs.getInt("s.Linguistics"));
					u.setMaterials_Sciences(rs.getInt("s.Materials_Sciences"));
					u.setMathematics(rs.getInt("s.Mathematics"));
					u.setMechanical_Aeronautical_and_Manufacturing_Engineering(rs.getInt("s.Mechanical_Aeronautical_and_Manufacturing_Engineering"));
					u.setMedicine(rs.getInt("s.Medicine"));
					u.setMineral_and_Mining_Engineering(rs.getInt("s.Mineral_and_Mining_Engineering"));
					u.setModern_Languages(rs.getInt("s.Modern_Languages"));
					u.setNursing(rs.getInt("s.Nursing"));
					u.setPerforming_Arts(rs.getInt("s.Performing_Arts"));
					u.setPharmacy_and_Pharmacology(rs.getInt("s.Pharmacy_and_Pharmacology"));
					u.setPhilosophy(rs.getInt("s.Philosophy"));
					u.setPhysics_and_Astronomy(rs.getInt("s.Physics_and_Astronomy"));
					u.setPolitics(rs.getInt("s.Politics"));
					u.setPsychology(rs.getInt("s.Psychology"));
					u.setSocial_Policy_and_Administration(rs.getInt("s.Social_Policy_and_Administration"));
					u.setSociology(rs.getInt("s.Sociology"));
					u.setSports_related_Subjects(rs.getInt("s.Sports_related_Subjects"));
					u.setStatistics_and_Operational_Research(rs.getInt("s.Statistics_and_Operational_Research"));
					u.setTheology_Divinity_and_Religious_Studies(rs.getInt("s.Theology_Divinity_and_Religious_Studies"));
					u.setVeterinary_Science(rs.getInt("s.Veterinary_Science"));
					
					
					
				}
			}
			
			conn.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	
	

}
