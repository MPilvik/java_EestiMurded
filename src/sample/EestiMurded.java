package sample;
import com.sun.rowset.internal.Row;
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
    Stage alglava; // alguskuva stage, see peab olema siin defineeritud, et game() saaks vana akna kinni panna
    Stage lava; // mängu stage, see peab olema siin defineeritud, et lopetaGame() saaks mängu akna kinni panna

    ///1.3. juhuslikud indeksid, need on vaja defineerida siin globaalselt, et neid saaks nupulevajutusel uuesti genereerida
    int suvaline_indeks0 = juhuslik_indeks(murdesonad);
    int suvaline_indeks1 = juhuslik_indeks(murdesonad);
    int suvaline_indeks2 = juhuslik_indeks(murdesonad);
    int murdesona_indeks;

    ///1.4. veel muutujaid, mida try-blokis nupulevajutusel muudetakse
    Label murdesona = new Label();
    String oige_vastus;
    int punkt = 0; // läheb kaunterisse, mis loeb kasutaja punkte

    @Override // Application-klassis olev start-meetod on üle kirjutatud

    //2.põhimeetod
    public void start(Stage primaryStage) throws Exception {
        seadista1Aken();
        // võiks olla iseenesest veel üks, mis laseks mängijal poole pealt katkestada ehk nt Esc-vajutusel kuvab lopetaGame()
    }

    //3. muud meetodid ja konstruktorid, mida põhimeetod kasutab

    /// 3.1. seadista game ehk põhimäng
    private void game(){
        alglava.close(); // pane alguskuva aken kinni

        ////3.1.1. seadista uus aken
        GridPane asetus = new GridPane();
        lava = new Stage();
        Scene stseen = new Scene(asetus, 650, 500);
        VBox vbox_vastused = new VBox(10);vbox_vastused.setId("vbox_vastused");

        lava.setScene(stseen);
        lava.setTitle("Eesti murdesõnade arvamise mäng");
        //lava.setResizable(false);
        lava.show();

        final Text vajutuse_tekst = new Text();vajutuse_tekst.setId("vajutus"); // kuvab vastates, kas vastus oli õige või vale
        final Text errori_tekst = new Text();
        final Text lause = new Text();lause.setId("lause");lause.wrappingWidthProperty().bind(asetus.widthProperty().subtract(40));
        murdesona.setId("murdesona");

        Label sonu = new Label("Sõnu jäänud: " + murdesonad.size()); // näitab kasutajale, kui palju sõnu on veel vaja ära vastata
        Label punktid = new Label("Punkte: " + punkt); // näitab kasutajale, kui palju tal on punkte

        Button vasta = new Button("Vasta");// vastamise nupp
        Button edasi = new Button("Edasi ->");

        ToggleGroup vastused = new ToggleGroup(); // vastuste grupp, mille variantidest saab valida ainult ühe
        RadioButton nupp1 = new RadioButton();nupp1.setToggleGroup(vastused);
        RadioButton nupp2 = new RadioButton();nupp2.setToggleGroup(vastused);
        RadioButton nupp3 = new RadioButton();nupp3.setToggleGroup(vastused);

        vbox_vastused.getChildren().addAll(nupp1, nupp2, nupp3); // määra vboxi elemendid
        vbox_vastused.setAlignment(Pos.CENTER_LEFT);

        Insets aared = new Insets(0,20,0,20);

        asetus.setPrefSize(650, 500);
        asetus.add(sonu, 0, 0, 2, 1);asetus.setHalignment(sonu, HPos.LEFT);asetus.setMargin(sonu, aared);
        asetus.add(punktid, 5, 0, 2, 1);asetus.setHalignment(punktid, HPos.RIGHT);asetus.setMargin(punktid, aared);
        asetus.add(murdesona, 2, 1, 2, 1);asetus.setMargin(murdesona, new Insets(40, 20, 0, 20));asetus.setHalignment(murdesona, HPos.CENTER);
        asetus.add(vbox_vastused, 2, 2, 3, 1);asetus.setMargin(vbox_vastused, new Insets(0, 20, 30, 20));
        asetus.add(vasta, 2, 3, 2, 1);asetus.setMargin(vasta, new Insets(0, 20, 20, 20));asetus.setHalignment(vasta, HPos.CENTER);
        asetus.add(vajutuse_tekst, 4, 2, 2, 2);asetus.setMargin(vajutuse_tekst, new Insets(0, 10, 40, 60));asetus.setHalignment(vajutuse_tekst, HPos.CENTER);
        asetus.add(errori_tekst, 4, 2, 2, 2);asetus.setMargin(errori_tekst, new Insets(0, 20, 40, 20));asetus.setHalignment(errori_tekst, HPos.CENTER);
        asetus.add(lause, 1, 4, 4, 1);asetus.setMargin(lause, new Insets(20, 20, 0, 20));asetus.setHalignment(lause, HPos.CENTER);
        asetus.add(edasi, 2, 5, 2, 1);asetus.setMargin(edasi, aared);asetus.setHalignment(edasi, HPos.CENTER);

        ColumnConstraints column1 = new ColumnConstraints();ColumnConstraints column2 = new ColumnConstraints();ColumnConstraints column3 = new ColumnConstraints();ColumnConstraints column4 = new ColumnConstraints();ColumnConstraints column5 = new ColumnConstraints(); ColumnConstraints column6 = new ColumnConstraints();
        column1.setPercentWidth(16.6);column2.setPercentWidth(16.6);column3.setPercentWidth(16.8);column4.setPercentWidth(16.8);column5.setPercentWidth(16.6);column6.setPercentWidth(16.6);
        asetus.getColumnConstraints().add(column1);asetus.getColumnConstraints().add(column2);asetus.getColumnConstraints().add(column3);asetus.getColumnConstraints().add(column4);asetus.getColumnConstraints().add(column5);asetus.getColumnConstraints().add(column6);
        RowConstraints row1 = new RowConstraints();RowConstraints row2 = new RowConstraints();RowConstraints row3 = new RowConstraints();RowConstraints row4 = new RowConstraints();RowConstraints row5 = new RowConstraints();RowConstraints row6 = new RowConstraints();
        row1.setPercentHeight(10);row2.setPercentHeight(20);row3.setPercentHeight(20);row4.setPercentHeight(10);row5.setPercentHeight(20);row6.setPercentHeight(10);
        asetus.getRowConstraints().addAll(row1, row2, row3, row4, row5, row6);

        stseen.getStylesheets().add
                (EestiMurded.class.getResource("Main.css").toExternalForm());

        ////3.1.2. määra murdesõnale ja vastusevariantidele väärtused

        /////3.1.2.1. Kontrolli, ega globaalselt defineeritud juhuslikud indeksid ei kattu. Kui kattuvad, genereeri uued, kuni enam ei kattu
        while(suvaline_indeks0 == suvaline_indeks1)suvaline_indeks1 = juhuslik_indeks(murdesonad);
        while(suvaline_indeks0 == suvaline_indeks2 || suvaline_indeks1 == suvaline_indeks2)suvaline_indeks2 = juhuslik_indeks(murdesonad);

        /////3.1.2.2. leia indeksite põhjal listidest sõnad

        ////// pane 3 genereeritud juhuslikku indeksit (0-st kuni murdesõnade listi pikkuseni) ühte arrayListi
        testlist.add(suvaline_indeks0); testlist.add(suvaline_indeks1); testlist.add(suvaline_indeks2);

        ////// võta iga raadionupu kohta 3 suvalisest indeksist üks, mille järgi määrad nupu sildi. Sildid ei tohi kattuda.
        nupp1.setText(yldkeele_vasted.get(testlist.get(juhuslik_indeks(testlist))));
        nupp2.setText(yldkeele_vasted.get(testlist.get(juhuslik_indeks(testlist))));
        while(nupp1.getText().equals(nupp2.getText())) nupp2.setText(yldkeele_vasted.get(testlist.get(juhuslik_indeks(testlist))));
        nupp3.setText(yldkeele_vasted.get(testlist.get(juhuslik_indeks(testlist))));
        while(nupp1.getText().equals(nupp3.getText()) || nupp2.getText().equals(nupp3.getText())) nupp3.setText(yldkeele_vasted.get(testlist.get(juhuslik_indeks(testlist))));

        ////// võta kolmest suvalisest indeksist suvaliselt üks ja võta selle järgi välja murdesõnade listist küsitav sõna
        murdesona.setText(murdesonad.get(testlist.get(juhuslik_indeks(testlist))));

        ////// küsi, milline on murdesõna indeks murdesõnade listis, ja seejärel, milline on selle indeksi indeks 3 indeksi listis
        murdesona_indeks = testlist.indexOf(murdesonad.indexOf(murdesona.getText()));

        ////// leia selle testlisti indeksiga indeksi järgi murdesõna üldkeelne vaste
        oige_vastus = yldkeele_vasted.get(testlist.get(murdesona_indeks));

        ////// kontrolliks prindi konsooli välja teateid
        System.out.println("Murdesõnade listi pikkus on " + murdesonad.size() + ", üldkeele listi pikkus on " + yldkeele_vasted.size() + " ja vastatud sõnade listi pikkus on " + vastatud_sonad.size());
        System.out.println("3 indeksit testlistis on: " + testlist + ", nendele vastavad üldkeele listis sõnad " + yldkeele_vasted.get(testlist.get(0)) + ", " + yldkeele_vasted.get(testlist.get(1)) + " ja " + yldkeele_vasted.get(testlist.get(2)));
        System.out.println("Nupp1 all on " + nupp1.getText() + ", nupp2 all on " + nupp2.getText() + " ja nupp3 all on " + nupp3.getText());
        System.out.println("Küsitakse murdesõna " + murdesona.getText() + ", selle indeks murdesõnade listis on " + murdesonad.indexOf(murdesona.getText()) + " ja see indeks on indeksite listis " + murdesona_indeks + ". element");
        System.out.println("Õige vastus on seega " + oige_vastus + ", sest " + oige_vastus + " on üldkeele listi " + murdesonad.indexOf(murdesona.getText()) + ". element ehk " + yldkeele_vasted.get(murdesonad.indexOf(murdesona.getText())));


        ////3.1.3. määra, mis juhtub vastamise nupule vajutusel

        vasta.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                errori_tekst.setText(""); // tee tühja vastuse veateade tühjaks

                /////3.1.3.1. seadista, mis peaks juhtuma, kui kõik läheb plaanipäraselt
                try {

                    ////// prindi konsooli kontrollteated
                    System.out.println("Vajutas!");
                    System.out.println("Murdesõnade listi pikkus on " + murdesonad.size() + ", üldkeele listi pikkus on " + yldkeele_vasted.size() + " ja vastatud sõnade listi pikkus on " + vastatud_sonad.size());

                    ////// küsi valitud vastusevarianti ja salvesta see stringina. Prindi kontrollteade
                    RadioButton rb = (RadioButton) vastused.getSelectedToggle();
                    String vastus = rb.getText();
                    System.out.println("Kasutaja vastas, et " + murdesona.getText() + " vaste üldkeeles on " + vastus);

                    ////// kui kasutaja vastus on õige vastus
                    if (vastus.equals(oige_vastus)) {

                        /////// prindi konsooli kontrollteade
                        System.out.println("See vastus on õige. Lisan ühe punkti ja eemaldan murdesõna listist.");

                        vajutuse_tekst.setText("Õige!"); // kuva kasutajale, et vastus oli õige
                        punkt++; // lisa üks punkt
                        punktid.setText("Punkte: " + punkt); // kuva kasutajale uus punktide arv
                        lause.setText(lausenaited.get(murdesonad.indexOf(murdesona.getText())));
                        murdesonad.remove(murdesona.getText()); // eemalda murdesõna listist
                        sonu.setText("Sõnu jäänud: " + murdesonad.size()); // kuva kasutajale arvata jäänud sõnade arv
                        yldkeele_vasted.remove(oige_vastus); // eemalda üldkeele vastus listist
                        vastatud_sonad.add(vastus); // lisa vastus vastatud sõnade listi

                        /////// prindi konsooli kontrollteade
                        System.out.println("Selle tulemusel on kasutajal punke " + punkt + ", murdesõnade listi pikkus on " + murdesonad.size() + ", üldkeele listi pikkus on " + yldkeele_vasted.size() + " ja vastatud sõnade listi pikkus on " + vastatud_sonad.size());

                        ////// kui kasutaja vastus ei ole õige vastus
                    } else {

                        /////// prindi konsooli kontrollteade
                        System.out.println("See vastus on vale. Eemaldan ühe punkti, aga listid jäävad samaks.");

                        vajutuse_tekst.setText("Vale!"); // kuva kasutajale, et vastus oli vale
                        punkt--; // võta üks punkt maha
                        punktid.setText("Punkte:" + punkt); // kuva kasutajale uus punktide arv

                        /////// prindi konsooli kontrollteade
                        System.out.println("Selle tulemusel on kasutajal punke " + punkt + ", murdesõnade listi pikkus on endiselt " + murdesonad.size() + ", üldkeele listi pikkus on endiselt " + yldkeele_vasted.size() + " ja vastatud sõnade listi pikkus on endiselt " + vastatud_sonad.size());
                    }

                    ////// prindi konsooli kontrollteade
                    System.out.println("Määran kasutajale kuvamiseks uued sõnad.");

                    ////// määra uus suvaline_indeks0 (selle saab alati murdesõnade listi pikkuse järgi, isegi kui listi on jäänud vähem kui 3 sõna)
                    suvaline_indeks0 = juhuslik_indeks(murdesonad);

                    ////// prindi konsooli kontrollteade
                    System.out.println("Murdesõnade listi pikkus on " + murdesonad.size() + " ja suvaline_indeks0 = " + suvaline_indeks0);

                    ////// kontrolli järelejäänud sõnade arvu. Kui jäänud on 3 või rohkem sõna...
                    if (murdesonad.size() >= 3) {

                        /////// määra ülejäänund uued indeksid ka murdesõnade listi pikkuse järgi
                        suvaline_indeks1 = juhuslik_indeks(murdesonad);
                        while (suvaline_indeks0 == suvaline_indeks1) suvaline_indeks1 = juhuslik_indeks(murdesonad);
                        suvaline_indeks2 = juhuslik_indeks(murdesonad);
                        while (suvaline_indeks0 == suvaline_indeks2 || suvaline_indeks1 == suvaline_indeks2) suvaline_indeks2 = juhuslik_indeks(murdesonad);

                        /////// prindi konsooli kontrollteade
                        System.out.println("Murdesõnade listi pikkus on >= 3. suvaline_indeks1 = " + suvaline_indeks1 + " ja suvaline_indeks2 = " + suvaline_indeks2);

                        /////// tühjenda 3 indeksi list ja pane sinna uued saadud indeksid asemele
                        testlist.clear();
                        System.out.println("Teen testlisti tühjaks, et panna sinna uued indeksid. Testlisti pikkus on: " + testlist.size()); // kontrollteade
                        testlist.add(suvaline_indeks0);
                        testlist.add(suvaline_indeks1);
                        testlist.add(suvaline_indeks2);

                        /////// prindi konsooli kontrollteade
                        System.out.println("Nüüd on testlistis 3 indeksit: " + testlist + ", nendele vastavad üldkeele listis sõnad " + yldkeele_vasted.get(testlist.get(0)) + ", " + yldkeele_vasted.get(testlist.get(1)) + " ja " + yldkeele_vasted.get(testlist.get(2)));

                        /////// määra raadionuppudele uued tekstid
                        nupp1.setText(yldkeele_vasted.get(testlist.get(juhuslik_indeks(testlist))));
                        nupp2.setText(yldkeele_vasted.get(testlist.get(juhuslik_indeks(testlist))));
                        while(nupp1.getText().equals(nupp2.getText()))nupp2.setText(yldkeele_vasted.get(testlist.get(juhuslik_indeks(testlist))));
                        nupp3.setText(yldkeele_vasted.get(testlist.get(juhuslik_indeks(testlist))));
                        while(nupp1.getText().equals(nupp3.getText()) || nupp2.getText().equals(nupp3.getText()))nupp3.setText(yldkeele_vasted.get(testlist.get(juhuslik_indeks(testlist))));

                        /////// prindi konsooli kontrollteade
                        System.out.println("Nupp1 all on " + nupp1.getText() + ", nupp2 all on " + nupp2.getText() + " ja nupp3 all on " + nupp3.getText());

                        /////// määra murdesõnaks uus sõna
                        murdesona.setText(murdesonad.get(testlist.get(juhuslik_indeks(testlist))));
                        murdesona_indeks = testlist.indexOf(murdesonad.indexOf(murdesona.getText()));
                        oige_vastus = yldkeele_vasted.get(testlist.get(murdesona_indeks));

                        /////// prindi konsooli kontrollteated
                        System.out.println("Küsitakse murdesõna " + murdesona.getText() + ", selle indeks murdesõnade listis on " + murdesonad.indexOf(murdesona.getText()) + " ja see indeks on indeksite listis " + murdesona_indeks + ". element");
                        System.out.println("Õige vastus on seega " + oige_vastus + ", sest " + oige_vastus + " on üldkeele listi " + murdesonad.indexOf(murdesona.getText()) + ". element ehk " + yldkeele_vasted.get(murdesonad.indexOf(murdesona.getText())));

                        /////// tühista nupu valik
                        rb.setSelected(false);

                        ////// kui listi on jäänud vähem kui 3, aga rohkem kui 0 sõna, siis...
                    } else if (!murdesonad.isEmpty()) {

                        /////// võta ülejäänud kaks vastusevarianti juba vastatud sõnade hulgast
                        suvaline_indeks1 = juhuslik_indeks(vastatud_sonad);
                        suvaline_indeks2 = juhuslik_indeks(vastatud_sonad);
                        while (suvaline_indeks1 == suvaline_indeks2) suvaline_indeks2 = juhuslik_indeks(vastatud_sonad);

                        /////// prindi konsooli kontrollteade
                        System.out.println("Murdesõnade listi pikkus on 0 < list < 3 ehk 1 või 2. suvaline_indeks0 küsitakse murdesõnade listi pikkuse järgi ja see on " + suvaline_indeks0 + ". Murdesõnade listi pikkus peaks olema sama, mis üldkeele vastete listi pikkus, milleks on " + yldkeele_vasted.size() + ". Teised indeksid küsitakse nüüd vastatud sõnade listist, mille pikkus on " + vastatud_sonad.size() + ". suvaline_indeks1 = " + suvaline_indeks1 + " ja suvaline_indeks2 = " + suvaline_indeks2);

                        testlist.clear();

                        /////// prindi konsooli kontrollteade
                        System.out.println("Teen testlisti tühjaks, et panna sinna uued indeksid. Testlisti pikkus on: " + testlist.size());

                        testlist.add(suvaline_indeks1);
                        testlist.add(suvaline_indeks2);

                        /////// prindi konsooli kontrollteade
                        System.out.println("Nüüd on testlistis 2 indeksit: " + testlist + ", nendele vastavad vastatud sõnade listis sõnad " + vastatud_sonad.get(testlist.get(0)) + " ja " + vastatud_sonad.get(testlist.get(1)));

                        /////// siin annan praegu alla. Kahe viimase vastatava sõna puhul on õige vastus esimene nupu all
                        nupp1.setText(yldkeele_vasted.get(suvaline_indeks0)); // murdesõnade listi indeks (listis max. 2 indeksit)
                        nupp2.setText(vastatud_sonad.get(testlist.get(juhuslik_indeks(testlist)))); // vastuste listist
                        nupp3.setText(vastatud_sonad.get(testlist.get(juhuslik_indeks(testlist)))); // vastuste listist
                        while (nupp2.getText().equals(nupp3.getText()))nupp3.setText(vastatud_sonad.get(testlist.get(juhuslik_indeks(testlist))));

                        /////// prindi konsooli kontrollteade
                        System.out.println("Nupp1 all on " + nupp1.getText() + ", nupp2 all on " + nupp2.getText() + " ja nupp3 all on " + nupp3.getText());

                        /////// määra murdesõnaks uus sõna
                        murdesona.setText(murdesonad.get(suvaline_indeks0));
                        murdesona_indeks = murdesonad.indexOf(murdesona.getText());
                        oige_vastus = yldkeele_vasted.get(murdesona_indeks);

                        /////// prindi konsooli kontrollteated
                        System.out.println("Küsitakse murdesõna " + murdesona.getText() + ", selle indeks murdesõnade listis on " + murdesonad.indexOf(murdesona.getText()) + " ja see indeks on indeksite listis " + murdesona_indeks + ". element");
                        System.out.println("Õige vastus on seega " + oige_vastus + ", sest " + oige_vastus + " on üldkeele listi " + murdesonad.indexOf(murdesona.getText()) + ". element ehk " + yldkeele_vasted.get(murdesonad.indexOf(murdesona.getText())));

                        /////// tühista nupu valik
                        rb.setSelected(false);

                        /////// kui list on tühi, siis lõpeta mäng
                    } else lopetaGame();

                    /////3.1.3.2. määra, mis juhtub, kui kõik ei lähe õigesti
                } catch (NullPointerException e){ // antud juhul kõige tõenäolisem on see, et klikkimine ei ole päris pihta läinud
                    vajutuse_tekst.setText(""); // ära kuva üldse "Õige!" ega "Vale!" teksti
                    errori_tekst.setText("Vali üks vastusevariantidest!"); // vaid anna teade, et kasutaja peab vastamiseks ühe vastuse valima
                }
            }
        });


        /*edasi.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
              try {

              } catch(){

                }
            }
        });*/
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
                game();
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