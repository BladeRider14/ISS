package controller;

import Model.User;
import Model.UserType;
import Persistence.Exception.RepositoryException;
import Service.ILibraryService;
import controller.utils.MessageAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    TextField textFieldUsername;
    @FXML
    TextField textFieldPassword;
    @FXML
    Button buttonLogin;

    private ILibraryService libraryService;

    public void setLibraryService(ILibraryService libraryService) {
        this.libraryService = libraryService;
    }

    public void handleLogin(ActionEvent actionEvent) throws IOException {
        String username = textFieldUsername.getText();
        String password = textFieldPassword.getText();
        try {
            User loggedUser = libraryService.checkLogin(new User(username,password));

            FXMLLoader fxmlLoader;
            Stage stage = (Stage) buttonLogin.getScene().getWindow();
            Scene scene;
            switch (loggedUser.getUserType()){
                case ADMIN:
                    fxmlLoader = new FXMLLoader(getClass().getResource("/AdminHomeView.fxml"));
                    scene = new Scene(fxmlLoader.load());

                    AdminHomeController adminHomeController = fxmlLoader.getController();

                    adminHomeController.setLibraryService(libraryService);
                    stage.setScene(scene);
                    break;
                case SUBSCRIBER:
                    fxmlLoader = new FXMLLoader(getClass().getResource("/SubscriberHomeView.fxml"));
                    scene = new Scene(fxmlLoader.load());

                    SubscriberHomeController subscriberHomeController = fxmlLoader.getController();

                    subscriberHomeController.setLibraryService(libraryService);
                    subscriberHomeController.setLoggedUser(loggedUser);
                    stage.setScene(scene);
                    break;
                case LIBRARIAN:
                    fxmlLoader = new FXMLLoader(getClass().getResource("/LibrarianHomeView.fxml"));
                    scene = new Scene(fxmlLoader.load());

                    LibrarianHomeController librarianHomeController = fxmlLoader.getController();

                    librarianHomeController.setLibraryService(libraryService);
                    stage.setScene(scene);
                    break;
            }


        }catch (RepositoryException re){
            System.out.println(re);
            MessageAlert.showMessage(null, Alert.AlertType.ERROR,"Failed Login",re.getMessage());
        }
    }
}
