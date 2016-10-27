import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Scanner;

public class EestiMurded extends Application {

    @Override

    public void start(Stage primaryStage) throws Exception {
        Pane pane = new Pane();
        Scene scene = new Scene(pane, 300, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Eesti murded");
        primaryStage.setResizable(true);

        Label label = new Label("Siin saad vaadata Eesti murrete kaarti.");
        label.setTranslateX(70);
        label.setTranslateY(100);

        Image image = new Image("Eesti-murded.png");
        ImageView imageview = new ImageView();
        imageview.setImage(image);
        imageview.setFitWidth(600);
        imageview.setPreserveRatio(true);

        Group root = new Group();
        Scene scene2 = new Scene(root);
        HBox box = new HBox();
        box.getChildren().add(imageview);
        root.getChildren().add(box);

        Button button = new Button("Vajuta siia!");
        button.setLayoutX(70);
        button.setLayoutY(150);
        button.setOnAction((event) -> {
            System.out.println("Jaa");
            primaryStage.setScene(scene2);
        });
        pane.getChildren().addAll(label, button);
        primaryStage.show();

    }

    /*
    public static void main(String args[]) {
        System.out.println("See on Eesti murdeid tutvustav projekt");

        Scanner input = new Scanner(System.in);
        System.out.println("Kas soovid kõigepealt näha murrete kaarti? (jah/ei)");
        String vastus = input.next().toLowerCase();

        if (vastus.equals("jah")) {
            System.out.println("Varsti saab!");
        } else if (vastus.equals("ei")) {
            System.out.println("Hästi!");
        }
    }*/
}
