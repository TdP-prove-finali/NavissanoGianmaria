package it.polito.tdp.prova_tesi.model;

public class Job implements Comparable<Job>{
	
	private String Job_Title;
	private String Industry;
	private String Functional_Area;
	private String Job_Salary;
	private String Key_Skills;
	private String Role;
	
	public Job(String job_Title, String industry, String functional_Area, String job_Salary, String key_Skills, String role) {
		super();
		Job_Title = job_Title;
		Industry = industry;
		Functional_Area = functional_Area;
		Job_Salary = job_Salary;
		Key_Skills = key_Skills;
		Role = role;
	}

	public String getJob_Title() {
		return Job_Title;
	}

	public void setJob_Title(String job_Title) {
		Job_Title = job_Title;
	}

	public String getIndustry() {
		return Industry;
	}

	public void setIndustry(String industry) {
		Industry = industry;
	}

	public String getFunctional_Area() {
		return Functional_Area;
	}

	public void setFunctional_Area(String functional_Area) {
		Functional_Area = functional_Area;
	}

	public String getJob_Salary() {
		return Job_Salary;
	}

	public void setJob_Salary(String job_Salary) {
		Job_Salary = job_Salary;
	}

	public String getKey_Skills() {
		return Key_Skills;
	}

	public void setKey_Skills(String key_Skills) {
		Key_Skills = key_Skills;
	}

	public String getRole() {
		return Role;
	}

	public void setRole(String role) {
		Role = role;
	}

	@Override
	public String toString() {
		return "JOB TITLE: " + Job_Title + " ROLE: " + Role;
	}

	@Override
	public int compareTo(Job other) {
		return this.Job_Title.compareTo(other.getJob_Title());
	}
	
	
	
	

}
