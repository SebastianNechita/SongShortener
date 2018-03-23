package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.*;

public class Controller {

    private Map<Integer, Verse> verses = new HashMap<>();
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

    @FXML
    private ChoiceBox<String> choiceBoxFormat;

    @FXML
    private TextField textFieldTitlu;

    @FXML
    private Label labelTitlu;


    private void processText() {
        verses = new HashMap<>();
        String text = inputTextArea.getText();
        String[] lines = text.split("\n");
        Verse v = new Verse(new ArrayList<>());
        for (String line : lines) {
            if (line.isEmpty() || line.matches("^\\s*$"))
                continue;
            if (isVerseHeader(line)) {
                v = createVerseBasedOnHeader(line);
            } else {
                v.addLine(line);
            }
            if (!v.getLines().isEmpty()) {
                verses.put(v.getId(), v);
            }
        }
    }

    private Verse createVerseBasedOnHeader(String verseHeader) {
        Verse v = new Verse(new ArrayList<>());
        if (!v.getLines().isEmpty())
            verses.put(v.getId(), v);
        v = new Verse(new ArrayList<>());
        String newLine = verseHeader.replaceAll("[^0-9]", "");
        try (Scanner s = new Scanner(newLine)) {
            int id = -1;
            if (s.hasNextInt())
                id = s.nextInt();
            v.setId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    private boolean isVerseHeader(String line) {
        return line.matches("^---.*---$");
    }

    private void populateList() {
        ObservableList<Integer> l = FXCollections.observableArrayList();
        verses.values()
                .stream()
                .filter(v -> v.getId() > 0)
                .forEach(v -> l.add(v.getId()));
        listView.setItems(l);
    }

    private void updateOutput() {

        if (this.choiceBoxFormat.getValue().equals("OpenLp")) {
            outputTextArea.setText("");
            int nr = 0;
            for (Integer id : order) {
                Verse verse = verses.get(id);
                List<String> lines = verse.getLines();
                int i = 0;
                while (i < lines.size()) {
                    StringBuilder text = new StringBuilder();
                    text.append("---[Verse:")
                            .append(++nr).append("]---\n")
                            .append(lines.get(i))
                            .append("\n");
                    if (i + 1 < lines.size()) {
                        text.append(lines.get(i + 1)).append("\n");
                        i++;
                        if (radio3.isSelected() && i + 1 == lines.size() - 1) {
                            text.append(lines.get(i + 1)).append("\n");
                            i++;
                        }
                    }
                    outputTextArea.appendText(text.toString());
                    i++;
                }
            }
        } else if (this.choiceBoxFormat.getValue().equals("ProPresenter")) {

            outputTextArea.setText("Title: " + this.textFieldTitlu.getText()+ "\n\n");

            int nr = 0;
            for (Integer id : order) {
                Verse verse = verses.get(id);
                List<String> lines = verse.getLines();
                int i = 0;
                while (i < lines.size()) {
                    StringBuilder text = new StringBuilder();
                    text.append(lines.get(i))
                            .append("\n");
                    if (i + 1 < lines.size()) {
                        text.append(lines.get(i + 1)).append("\n\n");
                        i++;
                        if (radio3.isSelected() && i + 1 == lines.size() - 1) {
                            text.append(lines.get(i + 1)).append("\n\n");
                            i++;
                        }
                    }
                    outputTextArea.appendText(text.toString());
                    i++;
                }
            }


        }
    }

    public void initialize() {
        inputTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            processText();
            populateList();
            updateOutput();
        });

        listView.setOnMouseClicked(event -> {
            ObservableList<Integer> l = listView.getSelectionModel().getSelectedIndices();
            if (l.size() == 1) {
                int value = listView.getItems().get(l.get(0));
                System.out.println("Item selected! -> " + value);
                order.add(value);
                listView.getSelectionModel().clearSelection();
            }
            label.setText("Order:");
            for (Integer i : order) {
                label.setText(label.getText() + "  " + i);
            }
            updateOutput();
        });
        button.setOnMouseClicked(event -> {
            order.clear();
            updateOutput();
            label.setText("Order:");
        });
        radio2.setOnMouseClicked(p -> updateOutput());
        radio3.setOnMouseClicked(p -> updateOutput());
        radio2.setSelected(true);

        ObservableList<String> choices = FXCollections.observableArrayList(new ArrayList<String>());

        choices.add("OpenLp");
        choices.add("ProPresenter");

        this.choiceBoxFormat.setItems(choices);
        this.choiceBoxFormat.setValue("OpenLp");

        this.labelTitlu.setVisible(false);
        this.textFieldTitlu.setVisible(false);

    }

    @FXML
    public void checkIfProPresenterChosen(){

        if (this.choiceBoxFormat.getValue().equals("OpenLp")){
            this.labelTitlu.setVisible(false);
            this.textFieldTitlu.setVisible(false);
        } else if (this.choiceBoxFormat.getValue().equals("ProPresenter")){
            this.labelTitlu.setVisible(true);
            this.textFieldTitlu.setVisible(true);
        }

    }


}
