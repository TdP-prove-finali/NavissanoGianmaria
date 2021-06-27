package it.polito.tdp.prova_tesi;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.prova_tesi.model.Language;
import it.polito.tdp.prova_tesi.model.Model;
import it.polito.tdp.prova_tesi.model.University;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<String> boxSkills;

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
    private ChoiceBox<String> languageBox;

    @FXML
    private ChoiceBox<String> mainSubjectBox;

    @FXML
    private TextArea resultUniversitiesTxt;

    @FXML
    void doFindJobs(ActionEvent event) {
    	
    }

    @FXML
    void doFindUniversities(ActionEvent event) {
    	this.resultUniversitiesTxt.clear();
    	String language = this.languageBox.getValue();
    	if(language == null) {
    		this.resultUniversitiesTxt.appendText("You didn't select any language\n");
    		return;
    	}
    	List<University> bestUniversities = this.model.getBestUniversities(language, (int)artsAndHumanitiesSlider.getValue(), (int)engineeringAndTechnologySlider.getValue(), (int)lifeSciencesAndMedicineSlider.getValue(), (int)naturalSciencesSlider.getValue(), (int)socialSciencesAndManagementSlider.getValue());
    	this.resultUniversitiesTxt.appendText("There are "+this.model.getValidUniversitiesNumber()+" universities that respect the language criteria\n");
    	for(University u : bestUniversities) {
    		this.resultUniversitiesTxt.appendText(u+"\n");
    	}
    	
    }

    @FXML
    void initialize() {
        assert boxSkills != null : "fx:id=\"boxSkills\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert artsAndHumanitiesSlider != null : "fx:id=\"artsAndHumanititesSlider\" was not injected: check your FXML file 'Scene.fxml'.";
        assert engineeringAndTechnologySlider != null : "fx:id=\"engineeringAndTechnologySlider\" was not injected: check your FXML file 'Scene.fxml'.";
        assert lifeSciencesAndMedicineSlider != null : "fx:id=\"lifeSciencesAndMedicineSlider\" was not injected: check your FXML file 'Scene.fxml'.";
        assert naturalSciencesSlider != null : "fx:id=\"naturalSciencesSlider\" was not injected: check your FXML file 'Scene.fxml'.";
        assert socialSciencesAndManagementSlider != null : "fx:id=\"socialSciencesAndManagementSlider\" was not injected: check your FXML file 'Scene.fxml'.";
        assert languageBox != null : "fx:id=\"languageBox\" was not injected: check your FXML file 'Scene.fxml'.";
        assert mainSubjectBox != null : "fx:id=\"mainSubjectBox\" was not injected: check your FXML file 'Scene.fxml'.";
        assert resultUniversitiesTxt != null : "fx:id=\"resultUniversitiesTxt\" was not injected: check your FXML file 'Scene.fxml'.";

    }


    
    public void setModel(Model model) {
    	this.model = model;
    	
    	this.boxSkills.getItems().addAll(this.model.getAllSkills());
    	this.languageBox.getItems().addAll(this.model.getLanguages());
    }
}
