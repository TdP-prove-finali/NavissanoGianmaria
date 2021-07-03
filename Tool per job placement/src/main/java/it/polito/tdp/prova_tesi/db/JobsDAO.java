package it.polito.tdp.prova_tesi.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.prova_tesi.model.Job;

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
			conn.close();
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
			conn.close();
			return result;
			
		} catch(SQLException e) {
			throw new RuntimeException("Error during connection to database", e);
		}
	}
	
	public List<Job> getAllJobs(){
		String sql = "SELECT distinct Job_Title, Industry, Functional_Area, Job_Salary, Key_Skills, Role "
				+ "FROM jobs "
				+ "WHERE Job_Experience_Required LIKE '0 -%'";
		List<Job> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				result.add(new Job(rs.getString("Job_Title"), rs.getString("Industry"), rs.getString("Functional_Area"), rs.getString("Job_Salary"), rs.getString("Key_Skills"), rs.getString("Role")));
			}
			conn.close();
			return result;
			
		} catch(SQLException e) {
			throw new RuntimeException("Error during connection to database", e);
		}
		
	}
	
	public List<String> getAllIndustries(){
		String sql = "SELECT distinct industry "
				+ "FROM jobs "
				+ "WHERE Job_Experience_Required LIKE '0 -%'";
		
		List<String> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				result.add(rs.getString("industry"));
			}
			conn.close();
			return result;
			
		} catch(SQLException e) {
			throw new RuntimeException("Error during connection to database", e);
		}
	}
	
	public List<String> getAllFunctionalAreas(){
		String sql = "SELECT distinct functional_area "
				+ "FROM jobs "
				+ "WHERE Job_Experience_Required LIKE '0 -%'";
		List<String> result = new LinkedList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				result.add(rs.getString("functional_area"));
			}
			conn.close();
			return result;
			
		} catch(SQLException e) {
			throw new RuntimeException("Error during connection to database", e);
		}
		
	}
	
}
