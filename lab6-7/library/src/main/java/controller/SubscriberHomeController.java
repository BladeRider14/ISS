package controller;

import Model.Book;
import Model.Borrowed;
import Model.User;
import Service.ILibraryService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

public class SubscriberHomeController {

    private User loggedUser;
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
    TextField textFieldFilterString;
    @FXML
    TextField textFieldReturnDate;

    @FXML
    Button buttonBorrowBook;
    @FXML
    Button buttonFilter;

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

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public List<Book> getAllBooks(){
        List<Book> books = new ArrayList<>();
        books = StreamSupport.stream(libraryService.getALlBooks().spliterator(), false)
                .toList();
        return books;
    }

    public void handleBorrowBook(ActionEvent actionEvent) {
        Book book = tableViewBooks.getSelectionModel().getSelectedItem();
        String returnDate = textFieldReturnDate.getText();
        String borrowedDate = LocalDate.now().toString();
        Borrowed borrowed = new Borrowed(borrowedDate,returnDate);
        borrowed.setBook(book);
        borrowed.setSubscriber(loggedUser);
        libraryService.borrowBook(borrowed);
        modelBooks.setAll(getAllBooks());
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
