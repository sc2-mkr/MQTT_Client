package fxml.gui.handlers;

import fxml.io.logExporters.ExporterFactory;
import fxml.io.logExporters.PlainTextExporterFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.util.HashMap;
import java.util.Map;

public class ExportersHandler {

    private ComboBox combo_exporters;
    private Button btn_exportLogs;

    private String selectedExporter;

    private Map<String, ExporterFactory> exporters = new HashMap<>();

    public ExportersHandler(ComboBox combo_exporters, Button btn_exportLogs) {
        this.combo_exporters = combo_exporters;
        this.btn_exportLogs = btn_exportLogs;

        addExporters();

        setBtn_exportLogsAction();

        setCombo_exportersAction();
        setCombo_exportersContent();
    }


    private void setBtn_exportLogsAction() {
        btn_exportLogs.setOnMouseClicked(event -> export(selectedExporter));
    }

    private void setCombo_exportersAction() {
        combo_exporters.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) selectedExporter = newValue.toString();
        });
    }

    private void setCombo_exportersContent() {
        ObservableList<String> exps = FXCollections.observableArrayList();
        exporters.entrySet().forEach(exporter -> exps.add(exporter.getKey()));
        combo_exporters.setItems(exps);

        if(exps.size() > 0) combo_exporters.getSelectionModel().select(0); // Select first item if present
    }


    // Add here the exporters
    private void addExporters() {
        exporters.put("Plain text", new PlainTextExporterFactory());
    }

    public void export(String exporterName) {
        exporters.get(exporterName).export();
    }
}
