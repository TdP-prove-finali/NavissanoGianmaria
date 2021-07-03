package it.polito.tdp.prova_tesi;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import model.Job;
import model.Model;
import model.University;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<University> boxBachelorUni;

    @FXML
    private ComboBox<University> boxMasterUni;

    @FXML
    private ComboBox<String> boxSkills;

    @FXML
    private ComboBox<String> boxFunctionalArea;

    @FXML
    private ComboBox<String> boxIndustry;

    @FXML
    private TextArea txtResult;
    
    @FXML
    private Slider artsAndHumanitiesSlider;

    @FXML
    private Slider engineeringAndTechnologySlider;

    @FXML
    private Slider lifeSciencesAndMedicineSlider;

    @FXML
    private Slider naturalSciencesSlider;

    @FXML
    private Slider socialSciencesAndManagementSlider;

    @FXML
    private TextArea resultUniversitiesTxt;
    
    @FXML
    private ComboBox<String> languageBox;

    @FXML
    private ComboBox<String> mainSubjectBox;
    
    @FXML
    private ComboBox<Job> boxJobOnOffer;

    @FXML
    void doFindJobs(ActionEvent event) {
    	this.txtResult.clear();
    	
    	String functional_Area = this.boxFunctionalArea.getValue();
    	String industry = this.boxIndustry.getValue();
    	String skill = this.boxSkills.getValue();
    	University uBachelor = this.boxBachelorUni.getValue();
    	University uMaster = this.boxMasterUni.getValue();
    	
    	List<Job> bestJobs = new LinkedList<>();
    	
    	if(uBachelor==null && uMaster==null) {
    		
    		if(functional_Area==null) {
        		this.txtResult.appendText("Select a functional area\n");
        		return;
        	}
        	if(industry == null) {
        		this.txtResult.appendText("Select an industry of interest\n");
        		return;
        	}
        	if(skill==null) {
        		this.txtResult.appendText("Select your best skill\n");
        		return;
        	}
        	bestJobs = this.model.getBestJobs(industry, functional_Area, skill);
    	}else {
    		if(skill==null) {
        		this.txtResult.appendText("Select your best skill\n");
        		return;
        	}
    		bestJobs = this.model.getBestJobs(uBachelor, uMaster, skill);
    	}
    	
    	if(bestJobs==null) {
    		this.txtResult.appendText("There are no job offers corresponding to the selected input values\n");
    		return;
    	}
    	
    	for(Job j : bestJobs) {
    		this.txtResult.appendText("Here are the most suitable job offers according to your own interests\n");
    		this.txtResult.appendText(j+"\n\n");
    	}
    }

    @FXML
    void doFindUniversities(ActionEvent event) {
    	this.resultUniversitiesTxt.clear();
    	String language = this.languageBox.getValue();
    	if(language == null) {
    		this.resultUniversitiesTxt.appendText("You didn't select any language\n");
    		return;
    	}
    	//this.resultUniversitiesTxt.appendText("There are "+this.model.getValidUniversitiesNumber(language)+" universities that respect the language criteria\n");
    	Job jobOnOffer = this.boxJobOnOffer.getValue();
    	
    	List<University> bestUniversities = new LinkedList<>();
    	if(jobOnOffer==null) {
    		String subject = this.mainSubjectBox.getValue();
        	if(subject==null) {
        		this.resultUniversitiesTxt.appendText("You didn't select a main subject\n");
        		return;
        	}
    		bestUniversities = this.model.getBestUniversities(language, subject, (int)artsAndHumanitiesSlider.getValue(), (int)engineeringAndTechnologySlider.getValue(), 
					(int)lifeSciencesAndMedicineSlider.getValue(), (int)naturalSciencesSlider.getValue(), (int)socialSciencesAndManagementSlider.getValue());
    		
    	}else {
    		bestUniversities = this.model.getBestUniversities(jobOnOffer, language);
    	}
    	
    	if(bestUniversities==null) {
    		this.resultUniversitiesTxt.appendText("Oh no! Something went wrong\n");
    	}else {
    		this.resultUniversitiesTxt.appendText("Here are the best 5 universities that will provide suitable candidates for the position you are offering:\n");
    		for(University u : bestUniversities) {
        		this.resultUniversitiesTxt.appendText(u.toStringRes()+"\n");
        	}
    		
    	}
    	
    	
    	
    	
    	
    	
    }
    
    @FXML
    void stopCV(ActionEvent event) {
    	this.boxBachelorUni.setValue(null);
    	this.boxMasterUni.setValue(null);
    }
    
    @FXML
    void stopJobCharacteristics(ActionEvent event) {
    	this.boxFunctionalArea.setValue(null);
    	this.boxIndustry.setValue(null);
    }
    
    @FXML
    void noRecruiterInput(ActionEvent event) {
    	this.artsAndHumanitiesSlider.setValue(0);
    	this.engineeringAndTechnologySlider.setValue(0);
    	this.lifeSciencesAndMedicineSlider.setValue(0);
    	this.naturalSciencesSlider.setValue(0);
    	this.socialSciencesAndManagementSlider.setValue(0);
    	this.mainSubjectBox.setValue(null);
    }

    @FXML
    void initialize() {
    	assert boxBachelorUni != null : "fx:id=\"boxBachelorUni\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxMasterUni != null : "fx:id=\"boxMasterUni\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxSkills != null : "fx:id=\"boxSkills\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxFunctionalArea != null : "fx:id=\"boxFunctionalArea\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxIndustry != null : "fx:id=\"boxIndustry\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert artsAndHumanitiesSlider != null : "fx:id=\"artsAndHumanitiesSlider\" was not injected: check your FXML file 'Scene.fxml'.";
        assert engineeringAndTechnologySlider != null : "fx:id=\"engineeringAndTechnologySlider\" was not injected: check your FXML file 'Scene.fxml'.";
        assert lifeSciencesAndMedicineSlider != null : "fx:id=\"lifeSciencesAndMedicineSlider\" was not injected: check your FXML file 'Scene.fxml'.";
        assert naturalSciencesSlider != null : "fx:id=\"naturalSciencesSlider\" was not injected: check your FXML file 'Scene.fxml'.";
        assert socialSciencesAndManagementSlider != null : "fx:id=\"socialSciencesAndManagementSlider\" was not injected: check your FXML file 'Scene.fxml'.";
        assert resultUniversitiesTxt != null : "fx:id=\"resultUniversitiesTxt\" was not injected: check your FXML file 'Scene.fxml'.";
        assert languageBox != null : "fx:id=\"languageBox\" was not injected: check your FXML file 'Scene.fxml'.";
        assert mainSubjectBox != null : "fx:id=\"mainSubjectBox\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxJobOnOffer != null : "fx:id=\"boxJobOnOffer\" was not injected: check your FXML file 'Scene.fxml'.";

    }


    
    public void setModel(Model model) {
    	this.model = model;
    	
    	this.txtResult.setStyle("-fx-font-family: monospace");
    	this.resultUniversitiesTxt.setStyle("-fx-font-family: monospace");
    	
    	this.boxSkills.getItems().addAll(this.model.getAllSkills());
    	this.languageBox.getItems().addAll(this.model.getLanguages());
    	this.mainSubjectBox.getItems().addAll(this.model.getAllSubjects());
    	this.boxBachelorUni.getItems().addAll(this.model.getAllUni());
    	this.boxMasterUni.getItems().addAll(this.model.getAllUni());
    	this.boxFunctionalArea.getItems().addAll(this.model.getAllFunctionalAreas());
    	this.boxIndustry.getItems().addAll(this.model.getAllIndustries());
    	this.boxJobOnOffer.getItems().addAll(this.model.getAllJobs());
    }
}
