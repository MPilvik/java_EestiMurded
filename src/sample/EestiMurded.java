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
    ArrayList<String> vastatud_sõnad = new ArrayList<String>();

    Stage alglava; // see peab olema siin defineeritud, et mäng() saaks vana akna kinni panna
    Stage lava; // see peab olema siin defineeritud, et lõpetaMäng() saaks mängu akna kinni panna

    int vastuse_indeks = juhuslik_indeks(murdesõnad); // indeksid on vaja defineerida siin globaalselt, et neid saaks nupulevajutusel uuesti genereerida, aga et mitte kasutada final-inte
    int suvaline_indeks1 = juhuslik_indeks(murdesõnad);
    int suvaline_indeks2 = juhuslik_indeks(murdesõnad);

    int punkt = 0;
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

        Label sõnu = new Label("Sõnu jäänud: " + murdesõnad.size());
        Label punktid = new Label("Punkte: " + punkt);
        Button vasta = new Button("Vasta");
        Label murdesõna = new Label();
        ToggleGroup vastused = new ToggleGroup();
        RadioButton nupp1 = new RadioButton();nupp1.setToggleGroup(vastused);
        RadioButton nupp2 = new RadioButton();nupp2.setToggleGroup(vastused);
        RadioButton nupp3 = new RadioButton();nupp3.setToggleGroup(vastused);

        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(sõnu, murdesõna, nupp1, nupp2, nupp3, vasta, vajutuse_tekst, punktid);
        asetus.getChildren().addAll(vbox);

        ////3.1.2. määra murdesõnale ja vastusevariantidele väärtused
        /////3.1.2.1. Kontrolli, ega globaalselt defineeritud indeksid ei kattu
        while(vastuse_indeks == suvaline_indeks1)suvaline_indeks1 = juhuslik_indeks(murdesõnad);
        while(vastuse_indeks == suvaline_indeks2 || suvaline_indeks1 == suvaline_indeks2)suvaline_indeks2 = juhuslik_indeks(murdesõnad);

        /////3.1.2.2. leia indeksite põhjal listidest sõnad
        murdesõna.setText(murdesõnad.get(vastuse_indeks));
        nupp1.setText(üldkeele_vasted.get(vastuse_indeks));
        nupp2.setText(üldkeele_vasted.get(suvaline_indeks1));
        nupp3.setText(üldkeele_vasted.get(suvaline_indeks2));

        ////3.1.3. määra, mis juhtub vastamise nupule vajutusel
        vasta.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Murdesõnade list: " + murdesõnad.size() + ", vastete list: " + üldkeele_vasted.size() + " ja vastatud sõnu: " + vastatud_sõnad.size());

                RadioButton rb = (RadioButton)vastused.getSelectedToggle();
                String vastus = rb.getText();
                System.out.println(vastus);

                if(vastus.equals(nupp1.getText())){
                    vajutuse_tekst.setText("Õige!");
                    punkt++;
                    punktid.setText("Punkte: " + punkt);
                    murdesõnad.remove(vastuse_indeks);
                    sõnu.setText("Sõnu jäänud: " + murdesõnad.size());
                    üldkeele_vasted.remove(vastuse_indeks);
                    vastatud_sõnad.add(vastus);
                    System.out.println("Pärast vastamist on murdesõnade list: " + murdesõnad.size() + ", vastete list: " + üldkeele_vasted.size() + " ja vastatud sõnu: " + vastatud_sõnad.size());
                }
                else {
                    vajutuse_tekst.setText("Vale!");
                    punkt--;
                    punktid.setText("Punkte:" + punkt);
                }

                vastuse_indeks = juhuslik_indeks(murdesõnad);

                if(murdesõnad.size()>=3) {

                    suvaline_indeks1 = juhuslik_indeks(murdesõnad); while(vastuse_indeks == suvaline_indeks1) suvaline_indeks1 = juhuslik_indeks(murdesõnad);
                    suvaline_indeks2 = juhuslik_indeks(murdesõnad);while (vastuse_indeks == suvaline_indeks2 || suvaline_indeks1 == suvaline_indeks2) suvaline_indeks2 = juhuslik_indeks(murdesõnad);
                    murdesõna.setText(murdesõnad.get(vastuse_indeks));
                    nupp1.setText(üldkeele_vasted.get(vastuse_indeks));
                    nupp2.setText(üldkeele_vasted.get(suvaline_indeks1));
                    nupp3.setText(üldkeele_vasted.get(suvaline_indeks2));
                    rb.setSelected(false);
                }

                else if(!murdesõnad.isEmpty()){
                    suvaline_indeks1 = juhuslik_indeks(vastatud_sõnad);
                    suvaline_indeks2 = juhuslik_indeks(vastatud_sõnad);while (suvaline_indeks1 == suvaline_indeks2) suvaline_indeks2 = juhuslik_indeks(vastatud_sõnad);
                    murdesõna.setText(murdesõnad.get(vastuse_indeks));
                    nupp1.setText(üldkeele_vasted.get(vastuse_indeks));
                    nupp2.setText(vastatud_sõnad.get(suvaline_indeks1));
                    nupp3.setText(vastatud_sõnad.get(suvaline_indeks2));
                    rb.setSelected(false);
                }

                else lõpetaMäng();
                // Vaja teha: kaunter; sega vastuseid

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
        Button nupp = new Button("Alusta");
        nupp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                mäng();
            }
        });

        algstseen.getStylesheets().add
                (EestiMurded.class.getResource("Main.css").toExternalForm());

        algvbox.setAlignment(Pos.CENTER);
        algvbox.getChildren().addAll(tervitustekst, nupp);
        algasetus.getChildren().addAll(algvbox);
    }

    //3.3. juhuslike indeksite leidmine
    private int juhuslik_indeks(ArrayList list){
        return (int)(Math.round(Math.random() * (list.size() - 1)));
    }

    //3.4. mängu lõpp
    private void lõpetaMäng(){

        lava.close();

        StackPane lõppasetus = new StackPane();
        lava = new Stage();
        Scene lõppstseen = new Scene(lõppasetus, 650, 500);
        lava.setScene(lõppstseen);
        Label lõpp = new Label("Mäng läbi! Kogusid " + punkt + " punkti.");
        lõppasetus.getChildren().add(lõpp);
        lava.show();
    }
}
