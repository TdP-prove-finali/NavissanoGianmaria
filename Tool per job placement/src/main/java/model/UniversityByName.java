package model;

import java.util.Comparator;

public class UniversityByName implements Comparator<University>{

	@Override
	public int compare(University u1, University u2) {
		return u1.getInstitution_Name().compareTo(u2.getInstitution_Name());
	}

}
