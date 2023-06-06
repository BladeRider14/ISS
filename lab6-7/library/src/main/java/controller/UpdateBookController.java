package controller;

import Model.Book;
import Service.ILibraryService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class UpdateBookController {

    @FXML
    TextField textFieldTitle;
    @FXML
    TextField textFieldAuthor;
    @FXML
    TextField textFieldExemplars;
    @FXML
    Button buttonUpdateBook;

    private ILibraryService libraryService;
    private Book selectedBook;

    public void setLibraryService(ILibraryService libraryService) {
        this.libraryService = libraryService;
    }

    public void setSelectedBook(Book selectedBook) {
        this.selectedBook = selectedBook;
        textFieldAuthor.setText(selectedBook.getAuthor());
        textFieldTitle.setText(selectedBook.getTitle());
        textFieldExemplars.setText(selectedBook.getExemplars().toString());
    }

    public void handleUpdateBook(ActionEvent actionEvent) throws IOException {
        String title = textFieldTitle.getText();
        String author = textFieldAuthor.getText();
        Long exemplars = Long.parseLong(textFieldExemplars.getText());
        Book book = new Book(title,author);
        book.setExemplars(exemplars);

        libraryService.updateBook(selectedBook,book);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AdminHomeView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = (Stage) buttonUpdateBook.getScene().getWindow();
        AdminHomeController adminHomeController = fxmlLoader.getController();

        adminHomeController.setLibraryService(libraryService);
        stage.setScene(scene);
    }
}
