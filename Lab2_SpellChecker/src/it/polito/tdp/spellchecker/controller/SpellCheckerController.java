/**
 * Sample Skeleton for 'SpellChecker.fxml' Controller Class
 */

package it.polito.tdp.spellchecker.controller;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.polito.tdp.spellchecker.model.Dictionary;
import it.polito.tdp.spellchecker.model.EnglishDictionary;
import it.polito.tdp.spellchecker.model.ItalianDictionary;
import it.polito.tdp.spellchecker.model.RichWord;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class SpellCheckerController {
	
	private Dictionary model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxLang"
    private ComboBox<String> boxLang; // Value injected by FXMLLoader

    @FXML // fx:id="txtWords"
    private TextArea txtWords; // Value injected by FXMLLoader

    @FXML // fx:id="txtCheck"
    private TextFlow txtCheck; // Value injected by FXMLLoader

    @FXML // fx:id="lblError"
    private Label lblError; // Value injected by FXMLLoader

    @FXML // fx:id="txtSeconds"
    private Label txtSeconds; // Value injected by FXMLLoader

    @FXML
    void doClearText(ActionEvent event) {
    	txtWords.clear();
    	txtCheck.getChildren().clear();
    	lblError.setVisible(false);
    }

    @FXML
    void doSpellCheck(ActionEvent event) {
    	
    	List<RichWord> check = null;
    	boolean error = false;
    	
		if(boxLang.getValue()==null) {
			txtSeconds.setText("Seleziona un valore");
			return ;
		}
    	
    	if( boxLang.getValue()== "Italian")
    		model = new ItalianDictionary();
    	else
    		model = new EnglishDictionary();
    	
    	long t0 = System.nanoTime();
		check = model.spellCheckText(splitText(txtWords.getText().toLowerCase()));
		long t1 = System.nanoTime();
		
    	for(RichWord w : check) {
    		Text word = new Text();
			word.setText(w.getWord()+" ");
    		
    		if(!w.isCheck()) {
    			word.setFill(Color.RED);
    			txtCheck.getChildren().add(word);
    			error = true;
    		} else {
    			word.setFill(Color.BLACK);
    			txtCheck.getChildren().add(word);
    		}	
    	}
		
    	if (error) 
    		lblError.setVisible(true);
    	
		txtSeconds.setText("Spell check completed in "+(t1-t0)/(1E9)+" seconds");
		
    }
    
    public List<String> splitText(String str) {
    	
        List<String> output = new LinkedList<String>();
        Matcher match = Pattern.compile("[a-z]+|[A-Z]+").matcher(str);
        
        while (match.find()) {
            output.add(match.group());
        }
        return output;
    	
    }
    
    public void setModel(Dictionary model) {
    	this.model = model;
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxLang != null : "fx:id=\"boxLang\" was not injected: check your FXML file 'SpellChecker.fxml'.";
        assert txtWords != null : "fx:id=\"txtWords\" was not injected: check your FXML file 'SpellChecker.fxml'.";
        assert txtCheck != null : "fx:id=\"txtCheck\" was not injected: check your FXML file 'SpellChecker.fxml'.";
        assert lblError != null : "fx:id=\"lblError\" was not injected: check your FXML file 'SpellChecker.fxml'.";
        assert txtSeconds != null : "fx:id=\"txtSeconds\" was not injected: check your FXML file 'SpellChecker.fxml'.";

        boxLang.getItems().addAll("English","Italian");

    }
}