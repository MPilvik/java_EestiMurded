import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
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


        // kasuta Sõnade klassi meetodit sõnad, et tabeli sõnade nimekirjad kätte saada
        Sõnad words = new Sõnad();
        ArrayList murdesõnad = (ArrayList) words.sõnad().get("murdesõnad");
        ArrayList üldkeele_vasted = (ArrayList) words.sõnad().get("üldkeele_vasted");
        ArrayList sõnaliik = (ArrayList) words.sõnad().get("sõnaliik");

        // vali 3 suvalist erinevat rea numbrit 0st kuni tabeli pikkuseni
        int suvaline_arv1 = (int)(Math.round(Math.random()*murdesõnad.size()));
        int suvaline_arv2 = (int)(Math.round(Math.random()*murdesõnad.size()));
        // kui 1. ja 2. suvaline arv on võrdsed, siis võta uus 2. suvaline arv
        while(suvaline_arv1 == suvaline_arv2){
            suvaline_arv2 = (int)(Math.round(Math.random()*murdesõnad.size()));
        }
        int suvaline_arv3 = (int)(Math.round(Math.random()*460));
        // kui 3. suvaline arv on võrdne kas 1. või 2. suvalise arvuga, siis vali uus 3. suvaline arv
        while(suvaline_arv3 == suvaline_arv1 || suvaline_arv3 == suvaline_arv2){
            suvaline_arv3 = (int)(Math.round(Math.random()*460));
        }

        VBox vbox = new VBox();
        ToggleGroup g = new ToggleGroup();
        RadioButton e1 = new RadioButton((String) üldkeele_vasted.get(suvaline_arv1));
        RadioButton e2 = new RadioButton((String) üldkeele_vasted.get(suvaline_arv2));
        RadioButton e3 = new RadioButton((String) üldkeele_vasted.get(suvaline_arv3));
        e1.setToggleGroup(g);
        e2.setToggleGroup(g);
        e3.setToggleGroup(g);
        vbox.getChildren().addAll(e1, e2, e3);
        Button button = new Button("Vasta");
        button.setLayoutX(70);
        button.setLayoutY(150);
        button.setOnAction((event) -> {
            primaryStage.setScene(scene2);
        });

        /*
        Button button = new Button("Vajuta siia!");
        button.setLayoutX(70);
        button.setLayoutY(150);
        button.setOnAction((event) -> {
            primaryStage.setScene(scene2);
        });
        */
        pane.getChildren().addAll(label, button, vbox);
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