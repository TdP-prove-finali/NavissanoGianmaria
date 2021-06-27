package it.polito.tdp.prova_tesi.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class JobsDAO {
	
	public List<String> getAllSkills(){
		String sql = "select distinct key_skills "
				+ "from jobs "
				+ "where job_experience_required like '0 -%'";
		List<String> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				if(rs.getString("key_skills")!=null)
					result.add(rs.getString("key_skills"));
			}
			
			return result;
			
		} catch(SQLException e) {
			throw new RuntimeException("Error during connection to database", e);
		}
		
	}
	
	public List<String> getJobBySkill(String skill){
		String sql = "select distinct job_title "
				+ "from jobs "
				+ "where job_experience_required like '0 -%' and key_skills like ?";
		List<String> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			skill = "%"+skill+"%";
			st.setString(1, skill);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				result.add(rs.getString("job_title"));
			}
			
			return result;
			
		} catch(SQLException e) {
			throw new RuntimeException("Error during connection to database", e);
		}
	}
	
}
