package it.polito.tdp.prova_tesi.model;

import java.util.Comparator;

public class JobsComparator implements Comparator<Job>{
	
	private String skill;
	
	public JobsComparator(String skill) {
		this.skill = skill;
	}

	@Override
	public int compare(Job j1, Job j2) {
		if(j1.getKey_Skills()==null || j2.getKey_Skills()==null)
			return 0;
		if(j1.getKey_Skills().contains(this.skill) && !j2.getKey_Skills().contains(this.skill))
				return -1;
		if(j2.getKey_Skills().contains(this.skill) && !j1.getKey_Skills().contains(this.skill))
			return +1;
		return 0;
	}

}
