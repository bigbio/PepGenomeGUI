package org.bigbio.pgatk.pepgenome.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PepGenomeController implements Initializable {

    @FXML
    private TextField openTextField;

    @FXML
    private MenuItem quitBtn;

    @FXML
    private Button fileOpenBtn;

    @FXML
    private Button fastaOpenBtn;

    @FXML
    private Button gftOpenBtn;

    @FXML
    private TextField fastaTextFile;

    @FXML
    private TextField gtfTextFile;

    @FXML
    private CheckBox pepBedCheck;

    @FXML
    private CheckBox gtfCheck;

    @FXML
    private CheckBox gctCheck;

    @FXML
    private CheckBox ptmBedCheck;

    @FXML
    private CheckBox allCheck;

    @FXML
    private CheckBox chrCheck;

    @FXML
    private CheckBox mismatchCheckBox;

    @FXML
    private ComboBox numComboBox;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button mapBtn;

    @FXML
    private CheckBox mergeOutput;

    public PepGenomeController() {
    }

    List<File> files = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ObservableList<Integer> mmValues = FXCollections.observableArrayList();
        for (int i = 0; i < 3; i++) {
            mmValues.add(i);
        }
        numComboBox.setItems(mmValues);
        numComboBox.getSelectionModel().select(0);

    }

    public void onQuitButton(ActionEvent actionEvent) {
        Stage stage = (Stage) quitBtn.getParentPopup().getOwnerWindow();
        stage.close();
    }

    public void onAboutButton(ActionEvent actionEvent) throws IOException {

        VBox root = (VBox) FXMLLoader.load(getClass().getClassLoader().getResource("view/about.fxml"));
        Scene scene = new Scene(root, 498, 187);

        Stage stage = new Stage();
        stage.setTitle("About");
        stage.setScene(scene);
        stage.show();



    }

    /**
     * Open file to be process, multiple files are allowed
     * @param actionEvent
     */
    public void onOpenFileAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Input File (mztab, mzIdentML, txt)");
        List<FileChooser.ExtensionFilter> filters = Arrays.asList(new FileChooser.ExtensionFilter("TSV files (*.txt)", "*.txt"),
                new FileChooser.ExtensionFilter("mzIdentML files (*.mzid)", "*.mzid", "*.mzIdentML"),
                new FileChooser.ExtensionFilter("mzTab files (*.mzTab)", "*.mztab"));
        fileChooser.getExtensionFilters().addAll(filters);
        List<File> files = fileChooser.showOpenMultipleDialog(((Node) actionEvent.getTarget()).getScene().getWindow());
        String inputFileStr = "";
        if(files != null && files.size() > 0){
            this.files = files;
            inputFileStr = this.files.stream().map(File::getAbsolutePath).collect(Collectors.joining(","));
        }

        openTextField.setText(inputFileStr);

    }


    /**
     * Open fasta file to be use during the mapping. The Fasta files can be download from this
     * ftp://ftp.ensembl.org/pub/release-96/fasta/
     *
     * @param actionEvent
     */
    public void onOpenFastaFile(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open ENSEMBL FASTA Protein File");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("FASTA files", "*.fasta", "*.fa");
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(((Node) actionEvent.getTarget()).getScene().getWindow());
        String inputFileStr = "";
        if(file != null){
            inputFileStr = file.getAbsolutePath();
        }

        fastaTextFile.setText(inputFileStr);

    }

    /**
     * Open a GTF file to be use during the mapping. The GTF can be downloaded from ftp://ftp.ensembl.org/pub/release-96/gtf/
     * @param actionEvent
     */
    public void onOpenGTFFile(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open ENSEMBL GTF Protein File");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("GTF files", "*.gtf");
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(((Node) actionEvent.getTarget()).getScene().getWindow());
        String inputFileStr = "";
        if(file != null){
            inputFileStr = file.getAbsolutePath();
        }

        gtfTextFile.setText(inputFileStr);

    }


    public void startMapping(ActionEvent actionEvent) {

        Boolean gtf = (allCheck.isSelected() || gtfCheck.isSelected())? true:false;
        Boolean bed = (allCheck.isSelected() || pepBedCheck.isSelected())? true:false;
        Boolean ptmBed = (allCheck.isSelected() || ptmBedCheck.isSelected())? true:false;
        Boolean gct = (allCheck.isSelected() || gctCheck.isSelected())? true:false;

        PepGenomeTask task = new PepGenomeTask(openTextField.getText(), fastaTextFile.getText(), gtfTextFile.getText(), mismatchCheckBox.isSelected(), Integer.parseInt(numComboBox.getSelectionModel().getSelectedItem().toString()),
                chrCheck.isSelected(), mergeOutput.isSelected(), gtf,
                bed, gct, ptmBed);

        progressBar.progressProperty().bind(task.progressProperty());

        task.setOnRunning((runningEvent) -> {
            mapBtn.setDisable(true);
            fastaOpenBtn.setDisable(true);
            gftOpenBtn.setDisable(true);
            fileOpenBtn.setDisable(true);
            openTextField.setDisable(true);
            fastaTextFile.setDisable(true);
            gtfTextFile.setDisable(true);
        });

        task.setOnSucceeded((successEvent) -> {
            mapBtn.setDisable(false);
            fastaOpenBtn.setDisable(false);
            gftOpenBtn.setDisable(false);
            fileOpenBtn.setDisable(false);
            progressBar.progressProperty().unbind();
            progressBar.setProgress(0);

            openTextField.setDisable(false);
            fastaTextFile.setDisable(false);
            gtfTextFile.setDisable(false);
        });

        Thread th = new Thread(task);
        th.start();

    }
}
