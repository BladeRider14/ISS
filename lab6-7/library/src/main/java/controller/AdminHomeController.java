package controller;

import Model.Book;
import Persistence.Exception.RepositoryException;
import Service.ILibraryService;
import controller.utils.MessageAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;


public class AdminHomeController {
    ObservableList<Book> modelBooks = FXCollections.observableArrayList();
    ObservableList<String> filterCriteria = FXCollections.observableArrayList();
    @FXML
    TableView<Book> tableViewBooks;
    @FXML
    TableColumn<Book,String> columnTitle;
    @FXML
    TableColumn<Book,String> columnAuthor;
    @FXML
    TableColumn<Book,Long> columnExemplars;

    @FXML
    Button buttonUpdateBook;
    @FXML
    private Button buttonAddBook;
    @FXML
    Button buttonDeleteBook;
    @FXML
    Button buttonFilter;
    @FXML
    TextField textFieldFilterString;
    @FXML
    ComboBox<String> comboBoxFilterBy;

    @FXML
    public void initialize(){
        initializeView();
    }
    public void initializeView(){
        columnTitle.setCellValueFactory(new PropertyValueFactory<Book,String>("title"));
        columnAuthor.setCellValueFactory(new PropertyValueFactory<Book,String>("author"));
        columnExemplars.setCellValueFactory(new PropertyValueFactory<Book,Long>("exemplars"));

        tableViewBooks.setItems(modelBooks);

        filterCriteria.add("author");
        filterCriteria.add("title");
        filterCriteria.add("none");
        comboBoxFilterBy.setItems(filterCriteria);
    }
    private ILibraryService libraryService;

    public void setLibraryService(ILibraryService libraryService) {
        this.libraryService = libraryService;
        modelBooks.setAll(getAllBooks());
    }
    public List<Book> getAllBooks(){
        List<Book> books = new ArrayList<>();
        books = StreamSupport.stream(libraryService.getALlBooks().spliterator(), false)
                .toList();
        return books;
    }
    public void handleAddBook(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/AddBookView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = (Stage) buttonAddBook.getScene().getWindow();
        AddBookController addBookController = fxmlLoader.getController();

        addBookController.setLibraryService(libraryService);
        stage.setScene(scene);
    }

    public void handleUpdateBook(ActionEvent actionEvent) throws IOException {
        Book book = tableViewBooks.getSelectionModel().getSelectedItem();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/UpdateBookView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = (Stage) buttonUpdateBook.getScene().getWindow();
        UpdateBookController updateBookController = fxmlLoader.getController();

        updateBookController.setLibraryService(libraryService);
        updateBookController.setSelectedBook(book);
        stage.setScene(scene);
    }

    public void handleDeleteBook(ActionEvent actionEvent) {
        Book book = tableViewBooks.getSelectionModel().getSelectedItem();
        libraryService.deleteBook(book);
        modelBooks.remove(book);
        tableViewBooks.setItems(modelBooks);
    }
    public void handleFilter(ActionEvent actionEvent) {
        String criteria = comboBoxFilterBy.getValue();
        String filterString = textFieldFilterString.getText();
        List<Book> books = getAllBooks();
        switch (criteria){
            case "author":
                books = books.stream().filter(book -> book.getAuthor().equals(filterString)).toList();
                break;
            case "title":
                books = books.stream().filter(book -> book.getTitle().equals(filterString)).toList();
                break;
        }
        modelBooks.setAll(books);
    }
}
