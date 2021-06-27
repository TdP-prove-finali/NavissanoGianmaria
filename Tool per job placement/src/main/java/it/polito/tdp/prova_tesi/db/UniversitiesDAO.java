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
			
			return result;
			
		} catch(SQLException e) {
			throw new RuntimeException("Error during connection to database", e);
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
							
							
							//Ranking numerico standard
							try {ranking = Integer.parseInt(rankingString);
							} catch(NumberFormatException e) {
								throw new RuntimeException("Error in database reading: uncorrect ranking for "+rs.getString("institution_name"), e);
							}
						}
						
					}
					
					
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
						score = Double.parseDouble(scoreString);
					}
					
					
					if(score!=0 && ranking!=0) {
						University uni = new University(ranking, rs.getString("institution_name"), rs.getString("country"), score);
						idMap.put(uni.getInstitution_Name(), uni);
					}
					
					
				}
					
			}
			
			
		} catch(SQLException e) {
			throw new RuntimeException("Error during connection to database", e);
		}
		
		
		
	}
	
	
	public void getMacroSubjectsRankings(Map<String, University> idMap) {
		String sql ="select q.institution_name, a.rank, e.rank, l.rank, n.rank, s.rank "
				+ "from qsuniversityrankings q, arts_and_humanities a, engineering_and_technology e, life_sciences_and_technology l, natural_sciences n, social_sciences_and_management s "
				+ "where q.Institution_Name = a.University_Name and q.Institution_Name = e.University_Name "
				+ "and q.Institution_Name = l.University_Name and q.Institution_Name = n.University_Name "
				+ "and q.Institution_Name = s.University_Name "
				+ "and institution_name is not null and ranking_2019 <>'N/A'";
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				if(idMap.containsKey(rs.getString("q.institution_name"))) {
					
					University u = idMap.get(rs.getString("q.institution_name"));
					u.setArts_And_Humanitites_Rank(rs.getInt("a.rank"));
					u.setEngineering_And_Technology_Rank(rs.getInt("e.rank"));
					u.setLife_Sciences_And_Technology_Rank(rs.getInt("l.rank"));
					u.setNatural_Sciences_Rank(rs.getInt("n.rank"));
					u.setSocial_Sciences_And_Management_Rank(rs.getInt("s.rank"));
					
				}
			}
			

			
		} catch(SQLException e) {
			throw new RuntimeException("Error during connection to database", e);
		}
	}

}
