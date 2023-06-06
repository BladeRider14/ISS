import Model.Book;
import Model.User;
import Model.UserType;
import Persistence.*;
import Service.ILibraryService;
import Service.LibraryService;
import controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;

public class MainApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        Properties serverProps=new Properties();
        try {
            serverProps.load(MainApp.class.getResourceAsStream("db.properties"));
            System.out.println("db properties set. ");
            serverProps.list(System.out);
        } catch (IOException var21) {
            System.err.println("Cannot find db.properties " + var21);
            return;
        }
        //IUserRepo userRepo = new UserDBRepo(serverProps);
        IUserRepo userRepo = new UserHibernateRepo();
        UserHibernateRepo userRepo2 = new UserHibernateRepo();
        User admin =new User("mike","mike");
        admin.setUserType(UserType.SUBSCRIBER);
        userRepo2.add(admin);
//        IBookRepo bookRepo = new BookDBRepo(serverProps);
        IBookRepo bookRepo = new BookHibernateRepo();
//        Book book = new Book("title","author");
//        book.setExemplars(30l);
//        bookRepo.add(book);
        ILibraryService libraryService = new LibraryService(userRepo,bookRepo);

        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("LogInView.fxml"));

        AnchorPane appLayout = fxmlLoader.load();
        primaryStage.setScene(new Scene(appLayout));

        LoginController logInController = fxmlLoader.getController();
        logInController.setLibraryService(libraryService);

        primaryStage.setTitle("Travel Agency");
        primaryStage.show();
    }
}
