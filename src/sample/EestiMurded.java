package sample;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.lang.NullPointerException;


public class EestiMurded extends Application {

    //1. loo globaalsed muutujad

    ///1.1. sõnade ja indeksite listid
    Sonad sonad = new Sonad(); // tee uus Sonad-objekt Sonad-klassist
    ArrayList<String> murdesonad = sonad.kysiListi("murdesonad"); // küsi Sõnad-objektist meetodiga kysiListi murdesõnade list
    ArrayList<String> yldkeele_vasted = sonad.kysiListi("yldkeele_vasted"); // üldkeele vastete list
    ArrayList<String> lausenaited = sonad.kysiListi("lausenaited");

    ArrayList<String> vastatud_sonad = new ArrayList<String>(); // tühi list, kuhu lähevad õigesti vastatud sõnade vasted
    ArrayList<Integer> testlist = new ArrayList<Integer>(); // tühi list, kuhu lähevad 3 suvalist indeksit

    ///1.2. stage'id
    Stage alglava; // alguskuva stage, see peab olema siin defineeritud, et seadista2aken() saaks vana akna kinni panna
    Stage lava; // mängu stage, see peab olema siin defineeritud, et lopetaGame() saaks mängu akna kinni panna

    ///1.3. juhuslikud indeksid, need on vaja defineerida siin globaalselt, et neid saaks nupulevajutusel uuesti genereerida
    int suvaline_indeks0 = juhuslik_indeks(murdesonad);
    int suvaline_indeks1 = juhuslik_indeks(murdesonad);
    int suvaline_indeks2 = juhuslik_indeks(murdesonad);
    int kysitava_indeks;

    ///1.4. veel muutujaid, mida try-blokis nupulevajutusel muudetakse
    Label murdesona = new Label();
    String oige_vastus;
    int punkt = 0; // läheb kaunterisse, mis loeb kasutaja punkte

    ///1.5. raadionupud (neid kasutab nii akna seadistus kui ka muudaKysimust
    RadioButton nupp1 = new RadioButton();
    RadioButton nupp2 = new RadioButton();
    RadioButton nupp3 = new RadioButton();

    /// kasutab nii akna seadistus kui ka kontrolliVastust
    ToggleGroup vastused = new ToggleGroup();
    Label sonu = new Label("Sõnu jäänud: " + murdesonad.size()); // näitab kasutajale, kui palju sõnu on veel vaja ära vastata
    Label punktid = new Label("Punkte: " + punkt); // näitab kasutajale, kui palju tal on punkte
    final Text lause = new Text();
    final Text vajutuse_tekst = new Text();

    final Text errori_tekst = new Text();
    Button vasta = new Button("Vasta");// vastamise nupp
    Button edasi = new Button("Edasi ->");

    @Override // Application-klassis olev start-meetod on üle kirjutatud

    //2.põhimeetod
    public void start(Stage primaryStage) throws Exception {
        seadista1Aken();
        game();
    }

    private void game(){

        edasi.setVisible(false); // edasi-nuppu algul kasutajale ei näita
        ////3.1.2. määra murdesõnale ja vastusevariantidele väärtused
        while(suvaline_indeks0 == suvaline_indeks1)suvaline_indeks1 = juhuslik_indeks(murdesonad);
        while(suvaline_indeks0 == suvaline_indeks2 || suvaline_indeks1 == suvaline_indeks2)suvaline_indeks2 = juhuslik_indeks(murdesonad);
        System.out.println("suvaline_indeks0: " + suvaline_indeks0 + ", suvaline_indeks1: " + suvaline_indeks1 + ", suvaline_indeks2: " + suvaline_indeks2);

        // seti nuppude ja küsitava sõna väärtused (ühtlasi tühjenda ja täida uuesti testlist)
        muudaKysimust(false, yldkeele_vasted);

        kysitava_indeks = testlist.indexOf(murdesonad.indexOf(murdesona.getText()));
        oige_vastus = yldkeele_vasted.get(testlist.get(kysitava_indeks));

        ////3.1.3. määra, mis juhtub vastamise nupule vajutusel

        vasta.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                vasta.setVisible(false); //pärast vastamist peida korraks vastuse nupp (et ei hakataks uuesti vastama)
                nupp1.setDisable(true); nupp2.setDisable(true); nupp3.setDisable(true); // ära lase rohkem vastata
                edasi.setVisible(true); // näita edasi-nuppu
                errori_tekst.setText(""); // tee tühja vastuse veateade tühjaks

                /////3.1.3.1. seadista, mis peaks juhtuma, kui kõik läheb plaanipäraselt
                try {
                    kontrolliVastust();

                    edasi.setOnAction(new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent event) {

                            edasi.setVisible(false);
                            nupp1.setDisable(false); nupp2.setDisable(false); nupp3.setDisable(false);
                            vasta.setVisible(true);
                            vajutuse_tekst.setText("");
                            lause.setText("");
                            suvaline_indeks0 = juhuslik_indeks(murdesonad);

                            if (murdesonad.size() >= 3) {
                                suvaline_indeks1 = juhuslik_indeks(murdesonad);
                                while (suvaline_indeks0 == suvaline_indeks1) suvaline_indeks1 = juhuslik_indeks(murdesonad);
                                suvaline_indeks2 = juhuslik_indeks(murdesonad);
                                while (suvaline_indeks0 == suvaline_indeks2 || suvaline_indeks1 == suvaline_indeks2) suvaline_indeks2 = juhuslik_indeks(murdesonad);

                                muudaKysimust(false, yldkeele_vasted);

                                kysitava_indeks = testlist.indexOf(murdesonad.indexOf(murdesona.getText()));
                                oige_vastus = yldkeele_vasted.get(testlist.get(kysitava_indeks));

                                System.out.println("suvaline_indeks0: " + suvaline_indeks0 + ", suvaline_indeks1: " + suvaline_indeks1 + ", suvaline_indeks2: " + suvaline_indeks2);

                            } else if (!murdesonad.isEmpty()) {
                                suvaline_indeks1 = juhuslik_indeks(vastatud_sonad);
                                suvaline_indeks2 = juhuslik_indeks(vastatud_sonad);
                                while (suvaline_indeks1 == suvaline_indeks2) suvaline_indeks2 = juhuslik_indeks(vastatud_sonad);

                                System.out.println("suvaline_indeks1: " + suvaline_indeks1);
                                System.out.println("suvaline_indeks2: " + suvaline_indeks2);
                                muudaKysimust(true, vastatud_sonad);

                                oige_vastus = yldkeele_vasted.get(suvaline_indeks0);

                            } else lopetaGame();
                        }
                    });



                    /////3.1.3.2. määra, mis juhtub, kui kõik ei lähe õigesti
                } catch (NullPointerException e){ // antud juhul kõige tõenäolisem on see, et klikkimine ei ole päris pihta läinud
                    nupp1.setDisable(false);nupp2.setDisable(false);nupp3.setDisable(false);
                    vasta.setVisible(true);
                    edasi.setVisible(false);
                    vajutuse_tekst.setText(""); // ära kuva üldse "Õige!" ega "Vale!" teksti
                    errori_tekst.setText("Vali üks vastusevariantidest!"); // vaid anna teade, et kasutaja peab vastamiseks ühe vastuse valima
                }
            }
        });
    }

    private void seadista2aken() {
        alglava.close(); // pane alguskuva aken kinni

        ////3.1.1. seadista uus aken
        GridPane asetus = new GridPane();
        lava = new Stage();
        Scene stseen = new Scene(asetus, 650, 500);
        VBox vbox_vastused = new VBox(10);
        vbox_vastused.setId("vbox_vastused");

        lava.setScene(stseen);
        lava.setTitle("Eesti murdesõnade arvamise mäng");
        //lava.setResizable(false);
        lava.show();

        vajutuse_tekst.setId("vajutus"); // kuvab vastates, kas vastus oli õige või vale

        murdesona.setId("murdesona");
        lause.setId("lause");
        lause.wrappingWidthProperty().bind(asetus.widthProperty().subtract(40));


        // vastuste grupp, mille variantidest saab valida ainult ühe
        nupp1.setToggleGroup(vastused);
        nupp2.setToggleGroup(vastused);
        nupp3.setToggleGroup(vastused);

        vbox_vastused.getChildren().addAll(nupp1, nupp2, nupp3); // määra vboxi elemendid
        vbox_vastused.setAlignment(Pos.CENTER_LEFT);

        asetus.setPrefSize(650, 500);
        asetus.add(sonu, 0, 0, 2, 1); asetus.setHalignment(sonu, HPos.LEFT);asetus.setMargin(sonu, new Insets(0, 20, 0, 20));
        asetus.add(punktid, 4, 0, 2, 1); asetus.setHalignment(punktid, HPos.RIGHT); asetus.setMargin(punktid, new Insets(0, 20, 0, 20));
        asetus.add(murdesona, 2, 1, 2, 1); asetus.setMargin(murdesona, new Insets(40, 20, 0, 20));asetus.setHalignment(murdesona, HPos.CENTER);
        asetus.add(vbox_vastused, 2, 2, 3, 1); asetus.setMargin(vbox_vastused, new Insets(0, 20, 30, 20));
        asetus.add(vasta, 2, 3, 2, 1); asetus.setMargin(vasta, new Insets(0, 20, 20, 20));asetus.setHalignment(vasta, HPos.CENTER);
        asetus.add(vajutuse_tekst, 4, 2, 2, 2); asetus.setMargin(vajutuse_tekst, new Insets(0, 10, 40, 60)); asetus.setHalignment(vajutuse_tekst, HPos.CENTER);
        asetus.add(errori_tekst, 4, 2, 2, 2); asetus.setMargin(errori_tekst, new Insets(0, 20, 40, 20));asetus.setHalignment(errori_tekst, HPos.CENTER);
        asetus.add(lause, 1, 4, 4, 1); asetus.setMargin(lause, new Insets(20, 20, 0, 20)); asetus.setHalignment(lause, HPos.CENTER);
        asetus.add(edasi, 2, 5, 2, 1); asetus.setMargin(edasi, new Insets(0, 20, 0, 20)); asetus.setHalignment(edasi, HPos.CENTER);

        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();
        ColumnConstraints column3 = new ColumnConstraints();
        ColumnConstraints column4 = new ColumnConstraints();
        ColumnConstraints column5 = new ColumnConstraints();
        ColumnConstraints column6 = new ColumnConstraints();
        column1.setPercentWidth(16.6);
        column2.setPercentWidth(16.6);
        column3.setPercentWidth(16.8);
        column4.setPercentWidth(16.8);
        column5.setPercentWidth(16.6);
        column6.setPercentWidth(16.6);
        asetus.getColumnConstraints().addAll(column1, column2, column3, column4, column5, column6);
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();
        RowConstraints row3 = new RowConstraints();
        RowConstraints row4 = new RowConstraints();
        RowConstraints row5 = new RowConstraints();
        RowConstraints row6 = new RowConstraints();
        row1.setPercentHeight(10);
        row2.setPercentHeight(20);
        row3.setPercentHeight(20);
        row4.setPercentHeight(10);
        row5.setPercentHeight(20);
        row6.setPercentHeight(10);
        asetus.getRowConstraints().addAll(row1, row2, row3, row4, row5, row6);

        stseen.getStylesheets().add(EestiMurded.class.getResource("Main.css").toExternalForm());
    }


    private void kontrolliVastust(){

        RadioButton rb = (RadioButton) vastused.getSelectedToggle();
        String vastus = rb.getText();

        if(vastus.equals(oige_vastus)){
            vajutuse_tekst.setText("Õige!");
            lause.setText(lausenaited.get(murdesonad.indexOf(murdesona.getText())));
            punkt++;
            punktid.setText("Punkte: " + punkt);
            lausenaited.remove(lause.getText());
            murdesonad.remove(murdesona.getText());
            sonu.setText("Sõnu jäänud: " + murdesonad.size());
            yldkeele_vasted.remove(oige_vastus);
            vastatud_sonad.add(vastus);
            System.out.println("Murdesonad: " + murdesonad.size() + ", yldkeele_vasted: " + yldkeele_vasted.size() + ", lausenaited: " + lausenaited.size() + ", vastatud_sonad: " + vastatud_sonad.size());
        }

        else {
            vajutuse_tekst.setText("Vale!");
            punkt--;
            punktid.setText("Punkte: " + punkt);
        }

        rb.setSelected(false);
    }

    private void muudaKysimust(boolean kasvahemkui3, ArrayList<String> teistevastustelist){

        testlist.clear(); // tee indeksite list puhtaks
        if(!kasvahemkui3) testlist.add(suvaline_indeks0); // kui listis rohkem kui 3 sõna, siis lisa indeksite listi ka suvaline_indeks0
        testlist.add(suvaline_indeks1); testlist.add(suvaline_indeks2); // need lisa igal juhul
        System.out.println("Testlist: " + testlist);

        if(kasvahemkui3) nupp1.setText(yldkeele_vasted.get(suvaline_indeks0)); // kui<3, siis õige vastus alati 1. nupu alla
        else nupp1.setText(yldkeele_vasted.get(testlist.get(juhuslik_indeks(testlist)))); // vastasel juhul tee läbi teslisti

        nupp2.setText(teistevastustelist.get(testlist.get(juhuslik_indeks(testlist)))); while(nupp1.getText().equals(nupp2.getText())) nupp2.setText(teistevastustelist.get(testlist.get(juhuslik_indeks(testlist))));

        nupp3.setText(teistevastustelist.get(testlist.get(juhuslik_indeks(testlist)))); while(nupp1.getText().equals(nupp3.getText()) || nupp2.getText().equals(nupp3.getText())) nupp3.setText(teistevastustelist.get(testlist.get(juhuslik_indeks(testlist))));

        if(kasvahemkui3) murdesona.setText(murdesonad.get(suvaline_indeks0));
        else murdesona.setText(murdesonad.get(testlist.get(juhuslik_indeks(testlist))));
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

        Text tervitustekst = new Text("See mäng laseb sul arvata erinevate murdesõnade tähendust. \n Kui oled õigesti arvanud, saad punkti. Kui arvad valesti, läheb punkt maha. \n Alustamiseks vajuta nupule 'Alusta'.");
        tervitustekst.setTextAlignment(TextAlignment.CENTER);
        tervitustekst.setFont(Font.font(15));
        tervitustekst.setId("tervitustekst");
        Button nupp = new Button("Alusta");

        ////3.2.1. määra, mis juhtub nupule vajutamisel
        nupp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                seadista2aken();
            } // käivita mäng
        });

        ////3.2.2. säti algakna välimust css-i stylesheeti abil (eraldi fail samas kaustas)
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
    private void lopetaGame(){

        lava.close(); // pane mängu aken kinni

        StackPane loppasetus = new StackPane();
        lava = new Stage();
        Scene loppstseen = new Scene(loppasetus, 650, 500);
        lava.setScene(loppstseen);
        Label lopp = new Label("Mäng läbi! Kogusid " + punkt + " punkti."); // kuva kasutajale, et mäng on läbi ja tema kogutud punktide arv
        loppasetus.getChildren().add(lopp);
        lava.show();
    }
}