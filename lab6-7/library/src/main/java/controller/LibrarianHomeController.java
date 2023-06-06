package controller;

import Model.Book;
import Model.Borrowed;
import Model.BorrowedDTO;
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

public class LibrarianHomeController {
    ObservableList<Book> modelBooks = FXCollections.observableArrayList();
    ObservableList<BorrowedDTO> modelBorrowed = FXCollections.observableArrayList();
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
    TableView<BorrowedDTO> tableViewBorrowedBooks;
    @FXML
    TableColumn<BorrowedDTO,String> columnBorrowedTitle;
    @FXML
    TableColumn<BorrowedDTO,String> columnBorrowedSubscriber;
    @FXML
    TableColumn<BorrowedDTO,Long> columnBorrowedReturnDate;

    @FXML
    TextField textFieldFilterString;
    @FXML
    Button buttonReturnBook;
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
        columnBorrowedTitle.setCellValueFactory(new PropertyValueFactory<BorrowedDTO,String>("book"));
        columnBorrowedSubscriber.setCellValueFactory(new PropertyValueFactory<BorrowedDTO,String>("subscriber"));
        columnBorrowedReturnDate.setCellValueFactory(new PropertyValueFactory<BorrowedDTO,Long>("returnDate"));

        tableViewBooks.setItems(modelBooks);
        tableViewBorrowedBooks.setItems(modelBorrowed);

        filterCriteria.add("author");
        filterCriteria.add("title");
        filterCriteria.add("none");
        comboBoxFilterBy.setItems(filterCriteria);
    }
    private ILibraryService libraryService;

    public void setLibraryService(ILibraryService libraryService) {
        this.libraryService = libraryService;
        modelBooks.setAll(getAllBooks());
        modelBorrowed.setAll(getAllBorrowed());
    }

    public List<Book> getAllBooks(){
        List<Book> books = new ArrayList<>();
        books = StreamSupport.stream(libraryService.getALlBooks().spliterator(), false)
                .toList();
        return books;
    }
    public List<BorrowedDTO> getAllBorrowed(){
        List<Borrowed> borrowedBooks = new ArrayList<>();
        List<BorrowedDTO> borrowedDTOS=new ArrayList<>();
        borrowedBooks = StreamSupport.stream(libraryService.getAllBorrowedBooks().spliterator(), false)
                .toList();
        borrowedBooks.forEach(x-> {
            BorrowedDTO dto = new BorrowedDTO(x);
            borrowedDTOS.add(dto);
        });
        borrowedBooks.forEach(System.out::println);
        return borrowedDTOS;
    }

    public void handleReturnBook(ActionEvent actionEvent) {
        BorrowedDTO borrowedDTO = tableViewBorrowedBooks.getSelectionModel().getSelectedItem();
        Borrowed borrowed =borrowedDTO.getBurrowed();
        libraryService.returnBook(borrowed);
        modelBooks.setAll(getAllBooks());
        modelBorrowed.setAll(getAllBorrowed());
    }

    public void handleFilter(ActionEvent actionEvent) {
        String criteria = comboBoxFilterBy.getValue();
        String filterString = textFieldFilterString.getText();
        List<Book> books = getAllBooks();
        List<BorrowedDTO> borrowedDTOS = getAllBorrowed();
        switch (criteria){
            case "author":
                books = books.stream().filter(book -> book.getAuthor().equals(filterString)).toList();
                break;
            case "title":
                books = books.stream().filter(book -> book.getTitle().equals(filterString)).toList();
                borrowedDTOS = borrowedDTOS.stream().filter(borrowedDTO -> borrowedDTO.getBook().equals(filterString)).toList();
                break;
            case "subscriber":
                borrowedDTOS=borrowedDTOS.stream().filter(borrowedDTO -> borrowedDTO.getSubscriber().equals(filterString)).toList();
        }
        modelBooks.setAll(books);
        modelBorrowed.setAll(borrowedDTOS);
    }
}
