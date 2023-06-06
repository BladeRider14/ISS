package controller;

import Model.Book;
import Persistence.Exception.RepositoryException;
import Service.ILibraryService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class AddBookController {

    @FXML
    TextField textFieldTitle;
    @FXML
    TextField textFieldAuthor;
    @FXML
    TextField textFieldExemplars;
    @FXML
    Button buttonAddBook;

    private ILibraryService libraryService;

    public void setLibraryService(ILibraryService libraryService) {
        this.libraryService = libraryService;
    }

    public void handleAddBook(ActionEvent actionEvent) throws IOException {

        String title = textFieldTitle.getText();
        String author = textFieldAuthor.getText();
        Long exemplars = Long.parseLong(textFieldExemplars.getText());
        Book book = new Book(title,author);
        book.setExemplars(exemplars);

        libraryService.addBook(book);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AdminHomeView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = (Stage) buttonAddBook.getScene().getWindow();
        AdminHomeController adminHomeController = fxmlLoader.getController();

        adminHomeController.setLibraryService(libraryService);
        stage.setScene(scene);
    }
}
