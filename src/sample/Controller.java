package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Controller {

    private List<Verse> verses = new ArrayList<>();
    private List<Integer> order = new ArrayList<>();

    @FXML
    private ListView<Integer> listView;

    @FXML
    private TextArea inputTextArea;

    @FXML
    private TextArea outputTextArea;

    @FXML
    private Label label;

    @FXML
    private RadioButton radio2;

    @FXML
    private ToggleGroup oddRadioGroup;

    @FXML
    private RadioButton radio3;

    @FXML
    private Button button;

    public void processText(){
        verses = new ArrayList<>();
        String text = inputTextArea.getText();
        String[] lines = text.split("\n");
        Verse v = new Verse(new ArrayList<>());
        for(String line : lines){
            if(line.length() == 0)
                continue;
            if(line.matches("^\\s*$"))
                continue;
            if(line.matches("^---.*---$")){
                if(v.getLines().size() > 0)
                    verses.add(v);
                v = new Verse(new ArrayList<>());
                String newLine = line.replaceAll("[^0-9]", "");
                Scanner s = new Scanner(newLine);
                int id = -1;
                if(s.hasNextInt())
                    id = s.nextInt();
                v.setId(id);
            }
            else{
                v.addLine(line);
            }
        }
        if(v.getLines().size() > 0)
            verses.add(v);

    }

    public void populateList(){
        ObservableList<Integer> l = FXCollections.observableArrayList();
        for(Verse v : verses){
            if(v.getId() > 0){
                l.add(v.getId());
            }
        }
        listView.setItems(l);
    }

    public void updateOutput(){
        outputTextArea.setText("");
        int nr = 0;
        for(Integer id : order){
            for(Verse v : verses){
                if(id == v.getId()){
                    List<String> lines = v.getLines();
                    for(int i = 0 ; i < lines.size() ; i++){
                        String text = "---[Verse:" + (++nr) + "]---\n";
                        text += lines.get(i) + "\n";
                        if(i + 1 < lines.size()){
                            text += lines.get(i + 1) + "\n";
                            i++;
                            if(radio3.isSelected() && i + 1 == lines.size() - 1){
                                text += lines.get(i + 1) + "\n";
                                i++;
                            }
                        }
                        outputTextArea.appendText(text);
                    }
                }
            }
        }
    }

    public void initialize(){
        inputTextArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                processText();
                populateList();
                updateOutput();
            }
        });

        listView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ObservableList<Integer> l = listView.getSelectionModel().getSelectedIndices();
                if(l.size() == 1){
                    int value = listView.getItems().get(l.get(0));
                    System.out.println("Item selected! -> " + value);
                    order.add(value);
                    listView.getSelectionModel().clearSelection();
                }
                label.setText("Order:");
                for(Integer i : order){
                    label.setText(label.getText() + "  " + i);
                }
                updateOutput();
            }
        });
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                order.clear();
                updateOutput();
                label.setText("Order:");
            }
        });
        radio2.setOnMouseClicked(p->{updateOutput();});
        radio3.setOnMouseClicked(p->{updateOutput();});
    }


}
