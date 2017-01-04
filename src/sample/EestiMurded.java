package sample;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import java.util.ArrayList;


public class EestiMurded extends Application {

    //1. Sõnade listid
    Sõnad words = new Sõnad();
    ArrayList<String> murdesõnad = (ArrayList) words.sõnad().get("murdesõnad");
    ArrayList<String> üldkeele_vasted = (ArrayList) words.sõnad().get("üldkeele_vasted");

    Stage alglava; // see peab olema siin defineeritud, et mäng() saaks vana akna kinni panna
    Stage lava; // see peab olema siin defineeritud, et lõpetaMäng() saaks mängu akna kinni panna

    int vastuse_indeks = juhuslik_indeks(); // indeksid on vaja defineerida siin globaalselt, et neid saaks nupulevajutusel uuesti genereerida, aga et mitte kasutada final-inte
    int suvaline_indeks1 = juhuslik_indeks();
    int suvaline_indeks2 = juhuslik_indeks();

    @Override // Application-klassis olev start-meetod on üle kirjutatud

    //2.põhimeetod
    public void start(Stage primaryStage) throws Exception {
        seadista1Aken();
    }

    //3. muud meetodid, mida põhimeetod kasutab
    /// 3.1. seadista mäng
    private void mäng(){
        alglava.close();

        ////3.1.1. seadista uus aken
        StackPane asetus = new StackPane();
        lava = new Stage();
        Scene stseen = new Scene(asetus, 650, 500);
        VBox vbox = new VBox(10);

        lava.setScene(stseen);
        lava.setTitle("Eesti murdesõnade arvamise mäng");
        lava.setResizable(true);
        lava.show();

        final Text vajutuse_tekst = new Text();

        Button vasta = new Button("Vasta");
        Label murdesõna = new Label();
        ToggleGroup vastused = new ToggleGroup();
        RadioButton nupp1 = new RadioButton();nupp1.setToggleGroup(vastused);
        RadioButton nupp2 = new RadioButton();nupp2.setToggleGroup(vastused);
        RadioButton nupp3 = new RadioButton();nupp3.setToggleGroup(vastused);

        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(murdesõna, nupp1, nupp2, nupp3, vasta, vajutuse_tekst);
        asetus.getChildren().addAll(vbox);

        ////3.1.2. määra murdesõnale ja vastusevariantidele väärtused
        /////3.1.2.1. Kontrolli, kas globaalselt defineeritud indeksid ei kattu
        while(vastuse_indeks == suvaline_indeks1)suvaline_indeks1 = juhuslik_indeks();
        while(vastuse_indeks == suvaline_indeks2 || suvaline_indeks1 == suvaline_indeks2)suvaline_indeks2 = juhuslik_indeks();

        /////3.1.2.2. leia indeksite põhjal listidest sõnad
        murdesõna.setText(murdesõnad.get(vastuse_indeks));
        nupp1.setText(üldkeele_vasted.get(vastuse_indeks));
        nupp2.setText(üldkeele_vasted.get(suvaline_indeks1));
        nupp3.setText(üldkeele_vasted.get(suvaline_indeks2));

        ////3.1.3. määra, mis juhtub vastamise nupule vajutusel
        vasta.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println(murdesõnad.size());

                RadioButton rb = (RadioButton)vastused.getSelectedToggle();
                String vastus = rb.getText();
                System.out.println(vastus);

                if(vastus.equals(nupp1.getText())){
                    vajutuse_tekst.setText("Õige!");
                    murdesõnad.remove(vastuse_indeks);üldkeele_vasted.remove(vastuse_indeks);
                    System.out.println(murdesõnad.size());
                }
                else vajutuse_tekst.setText("Vale!");

                vastuse_indeks = juhuslik_indeks();

                if(murdesõnad.size()>=3) {

                    suvaline_indeks1 = juhuslik_indeks();while (vastuse_indeks == suvaline_indeks1) suvaline_indeks1 = juhuslik_indeks();
                    suvaline_indeks2 = juhuslik_indeks();while (vastuse_indeks == suvaline_indeks2 || suvaline_indeks1 == suvaline_indeks2) suvaline_indeks2 = juhuslik_indeks();
                    murdesõna.setText(murdesõnad.get(vastuse_indeks));
                    nupp1.setText(üldkeele_vasted.get(vastuse_indeks));
                    nupp2.setText(üldkeele_vasted.get(suvaline_indeks1));
                    nupp3.setText(üldkeele_vasted.get(suvaline_indeks2));
                }

                else lõpetaMäng();
                // vastasel juhul võta äraarvatud sõnade listist juhuslik indeks ja vaste
                // Vaja teha: äraarvatud sõnade list; kaunter; sega vastuseid


            }
        });
    }

    ///3.2. seadista mängu alguse aken
    private void seadista1Aken(){

        StackPane algasetus = new StackPane();
        alglava = new Stage();
        Scene algstseen = new Scene(algasetus, 650, 500);
        VBox algvbox = new VBox(20);

        alglava.setScene(algstseen);
        alglava.setTitle("Eesti murdesõnade arvamise mäng");
        alglava.setResizable(true);
        alglava.show();

        final Text vajutuse_tekst = new Text();
        algvbox.getChildren().add(vajutuse_tekst);

        Text tervitustekst = new Text("See mäng laseb sul arvata erinevate murdesõnade tähendust. \n Kui oled õigesti arvanud, saad punkti. Kui arvad valesti, läheb punkt maha. \n Lõpetamiseks vajuta Enterit.");
        tervitustekst.setTextAlignment(TextAlignment.CENTER);
        tervitustekst.setFont(Font.font(15));
        Label suvatekst = new Label();
        Button nupp = new Button("Alusta");
        nupp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                vajutuse_tekst.setFill(Color.FIREBRICK);
                vajutuse_tekst.setText("Vajutasid nupule!");
                mäng();
            }
        });

        algstseen.getStylesheets().add
                (EestiMurded.class.getResource("Main.css").toExternalForm());

        algvbox.setAlignment(Pos.CENTER);
        algvbox.getChildren().addAll(tervitustekst, nupp, suvatekst);
        algasetus.getChildren().addAll(algvbox);
    }

    //3.3. juhuslike indeksite leidmise meetod
    private int juhuslik_indeks(){
        return (int)(Math.round(Math.random() * (murdesõnad.size() - 1)));
    }

    //3.4. mängu lõpp
    private void lõpetaMäng(){
        lava.close();
    }
}
