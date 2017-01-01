import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Scanner;

public class Main extends Application {

    // Muutujad, mida saab igast meetodist kätte
    BorderPane borderPane = new BorderPane();
    Stage stage = new Stage();
    Sõnad words = new Sõnad();
    ArrayList<String> murdesõnad = (ArrayList) words.sõnad().get("murdesõnad");
    ArrayList<String> üldkeele_vasted = (ArrayList) words.sõnad().get("üldkeele_vasted");
    ToggleGroup vastusteBlokk = new ToggleGroup(); // vastusteBloki grupp paika
    Button button = new Button("Vasta"); // vastuse nupp paika
    VBox vbox = new VBox(); // VBox paika
    Label label = new Label("Arva murdesõnade tähendusi"); // tekst stseeni aknas paika

    @Override // Application-klassis olev start-meetod on üle kirjutatud

    public void start(Stage primaryStage) throws Exception { // primaryStage on esimene aken
        seadistaLava();

        while(murdesõnad.size() > 3){
            System.out.println(murdesõnad.size());

            // Esmalt määra uued juhuslikud indeksid
            int vastuse_indeks = juhuslik_indeks();
            int teine_indeks = juhuslik_indeks();
            while (vastuse_indeks == teine_indeks) teine_indeks = juhuslik_indeks();
            int kolmas_indeks = juhuslik_indeks();
            while (vastuse_indeks == kolmas_indeks || teine_indeks == kolmas_indeks) kolmas_indeks = juhuslik_indeks();

            // Leia indeksitele vastavad sõnad ja määra need küsitava sõna ning vastusevariantide siltideks
            Label murdesõna = new Label((String) murdesõnad.get(vastuse_indeks));
            RadioButton nupp1 = new RadioButton((String) üldkeele_vasted.get(vastuse_indeks));
            RadioButton nupp2 = new RadioButton((String) üldkeele_vasted.get(teine_indeks));
            RadioButton nupp3 = new RadioButton((String) üldkeele_vasted.get(kolmas_indeks));

            // Määra raadionupud gruppi
            nupp1.setToggleGroup(vastusteBlokk);
            nupp2.setToggleGroup(vastusteBlokk);
            nupp3.setToggleGroup(vastusteBlokk);

            // Määra elemendid VBoxi
            vbox.getChildren().addAll(murdesõna, nupp1, nupp2, nupp3, button);

            // Kui nupule vajutada, siis
            button.setOnAction((event) -> {
                RadioButton rb_selected = (RadioButton) vastusteBlokk.getSelectedToggle(); // tee valikust radio button
                String vastus = rb_selected.getText(); // salvesta rb nimi stringi 'vastus'
                if (vastus.equals(üldkeele_vasted.get(vastuse_indeks))) { // kui vastus on õige
                    System.out.println("Õige!"); // prindi välja "Õige!"
                    // ja eemalda sõna listidest
                    System.out.println(murdesõnad);
                    murdesõnad.remove(vastuse_indeks);
                    üldkeele_vasted.remove(vastuse_indeks);
                    System.out.println(murdesõnad);
                    System.out.println(murdesõnad.size() + " " + üldkeele_vasted.size()); // kontrolli, kas listid läksid tühjemaks

                } else { // kui vastus on vale
                    System.out.println("Vale!"); // prindi välja "Vale!"
                    // ja jätka sama listiga
                }
            });
        }
    }

    // vali suvaline rea number 0st kuni tabeli pikkuseni
    private int juhuslik_indeks(){
        int juhuslik_indeks = (int)(Math.round(Math.random() * murdesõnad.size()));
        return juhuslik_indeks;
    }

    private void seadistaLava() {
        borderPane = new BorderPane();
        Scene scene = new Scene(borderPane, 600, 500); // stseen
        stage.setScene(scene); // määra, mis stseen on aktiivne
        stage.setTitle("Eesti murded"); // akna pealkiri
        stage.setResizable(true); // akent saab teha suuremaks ja väiksemaks
        borderPane.setLeft(vbox);
        borderPane.setTop(label);
        // borderPane.setCenter(imageview);
        stage.show(); // näita esimest akent
    }
}



/*    Image image = new Image("Eesti-murded.png"); // lae programmi pilt
    ImageView imageview = new ImageView(); // pildivaate aken
        imageview.setImage(image); // pane pilt pildivaate aknasse
        imageview.setFitWidth(400); // määra pildi laiuseks 400 pikslit
        imageview.setPreserveRatio(true); // säilita külgede suhe

    Group root = new Group();
    HBox box = new HBox();
    box.getChildren().add(imageview);
    root.getChildren().add(box);








    });

        *//*
        Button button = new Button("Vajuta siia!");
        button.setLayoutX(70);
        button.setLayoutY(150);
        button.setOnAction((event) -> {
            primaryStage.setScene(scene2);
        });
        *//*

}

    *//*
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
    }*//*

}*/
