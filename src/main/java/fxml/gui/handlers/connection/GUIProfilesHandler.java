package fxml.gui.handlers.connection;

import configs.BrokerConfiguration;
import configs.ConnectionProfile;
import fxml.gui.handlers.GUIHandler;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import services.utils.fxUtils.AlertUtil;
import services.utils.logs.Logger;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Optional;

public class GUIProfilesHandler implements GUIHandler {

    private ComboBox combo_profiles;
    private Button btn_loadProfiles;
    private TextField tf_ipAddress;
    private TextField tf_port;
    private TextField tf_clientId;
    private TextField tf_profileName;
    private Button btn_saveProfile;

    private String selectedProfile = "";

    public GUIProfilesHandler(
            ComboBox combo_profiles,
            Button btn_loadProfiles,
            TextField tf_ipAddress,
            TextField tf_port,
            TextField tf_clientId,
            TextField tf_profileName,
            Button btn_saveProfile
    ) {
        this.combo_profiles = combo_profiles;
        this.btn_loadProfiles = btn_loadProfiles;
        this.tf_ipAddress = tf_ipAddress;
        this.tf_port = tf_port;
        this.tf_clientId = tf_clientId;
        this.tf_profileName = tf_profileName;
        this.btn_saveProfile = btn_saveProfile;

        loadProfiles();
        setFirstComboboxItem();
        setCombo_profilesAction();
        setProfilesObserver();

        setBtn_loadProfilesAction();
        setBtn_saveProfileAction();
    }

    private void setProfilesObserver() {
        JavaFxObservable.emitOnChanged(BrokerConfiguration.getInstance().getProfiles())
                .subscribe(
                        list -> loadProfiles(),
                        error -> Logger.getInstance().logError(MessageFormat.format("GUI Profile Handler: {0}", error.getMessage())),
                        () -> {
                        }
                );
    }

    private void setFirstComboboxItem() {
        if (combo_profiles.getItems().stream().findFirst().isPresent())
            combo_profiles.setValue(combo_profiles.getItems().get(0));
    }

    private void setCombo_profilesAction() {
        combo_profiles.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) selectedProfile = newValue.toString();
        });
    }

    private void setBtn_loadProfilesAction() {
        btn_loadProfiles.setOnMouseClicked(event -> {
            Optional<ConnectionProfile> profile = BrokerConfiguration.getInstance()
                    .getProfileByName(combo_profiles.getValue().toString());
            if (profile.isPresent()) {
                tf_ipAddress.setText(profile.get().getAddress());
                tf_port.setText(profile.get().getPort());
                tf_clientId.setText(profile.get().getClientName());
            } else
                Logger.getInstance().logError("GUI Profiles Handler: Impossible to load the connection profile");
        });
    }

    private void setBtn_saveProfileAction() {
        btn_saveProfile.setOnMouseClicked(event -> {
            if (tf_profileName.getText().isEmpty()) {
                AlertUtil.getInstance().showErrorAndWait(
                        "Connection Profile",
                        "",
                        "Invalid connection profile's name");
            } else {
                BrokerConfiguration.getInstance().addConnectionProfile(
                        tf_profileName.getText(),
                        tf_ipAddress.getText(),
                        tf_port.getText(),
                        tf_clientId.getText(),
                        true
                );
            }
        });
    }

    private void loadProfiles() {
        ArrayList<String> profilesName = new ArrayList<>();
        BrokerConfiguration.getInstance().getProfiles()
                .forEach(p -> profilesName.add(p.getName()));
        combo_profiles.setItems(FXCollections.observableArrayList(profilesName));
    }

    /**
     * Method to be called for updating the graphics
     */
    @Override
    public void updateGUI() {

    }
}
