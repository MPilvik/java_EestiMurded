// Impordi paketid
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
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.lang.NullPointerException;

public class EestiMurded extends Application {

    // Loo kõik sellised globaalsed muutujad, mida kasutavad mitu meetodit

    Sonad sonad = new Sonad(); // tee uus Sonad-objekt Sonad-klassist
    ArrayList<String> murdesonad = sonad.kysiListi("murdesonad"); // küsi Sõnad-objektist meetodiga kysiListi murdesõnade list
    ArrayList<String> yldkeele_vasted = sonad.kysiListi("yldkeele_vasted"); // küsi üldkeele vastete list
    ArrayList<String> lausenaited = sonad.kysiListi("lausenaited"); // küsi lausenäidete list

    ArrayList<String> vastatud_sonad = new ArrayList<String>(); // tühi list, kuhu lähevad õigesti vastatud sõnade vasted
    ArrayList<Integer>
            indeksite_list = new ArrayList<Integer>(); // tühi list, kuhu lähevad 3 suvalist indeksit

    Stage alglava; // alguskuva stage, see peab olema siin defineeritud, et saaks vana akna kinni panna
    Stage lava; // mängu stage, see peab olema siin defineeritud, et saaks mängu akna kinni panna

    // suvalised indeksid vastusevariantidele
    int suvaline_indeks0 = juhuslik_indeks(murdesonad);
    int suvaline_indeks1 = juhuslik_indeks(murdesonad);
    int suvaline_indeks2 = juhuslik_indeks(murdesonad);
    int kysitava_indeks;

    Label murdesona = new Label(); // murdesõna label
    String oige_vastus; // õige vastus
    int punkt = 0; // läheb kaunterisse, mis loeb kasutaja punkte

    // vastusevariantide nupud
    RadioButton nupp1 = new RadioButton();
    RadioButton nupp2 = new RadioButton();
    RadioButton nupp3 = new RadioButton();

    ToggleGroup vastused = new ToggleGroup(); // vastusevariantide TG, mis määrab, et saab valida ainult ühe vastuse

    Label sonu = new Label("Sõnu jäänud: " + murdesonad.size()); // näitab kasutajale, kui palju sõnu on veel vaja ära vastata
    Label punktid = new Label("Punkte: " + punkt); // näitab kasutajale, kui palju tal on punkte

    final Text lause = new Text(); // näitelause
    final Text vajutuse_tekst = new Text(); // nupu vajutusel kuvatav tekst (kas vastus õige või vale)
    final Text errori_tekst = new Text(); // tekst, mida kuvatakse siis, kui vajutatakse vastamisnuppu, aga valitud pole midagi

    Button vasta = new Button("Vasta");// vastamise nupp
    Button edasi = new Button("Edasi ->"); // järgmise sõna juurde liikumise nupp

    @Override // Application-klassis olev start-meetod on üle kirjutatud

    //2.põhimeetod
    public void start(Stage primaryStage) throws Exception {
        seadista1Aken(); // algakna kuvamine, mille seest käivitatakse nupulevajutusel ka põhiaken
        game(); // mäng ise, mille seest käivitatakse mängu lõpp
    }

    // mäng
    private void game(){

        edasi.setVisible(false); // edasi-nuppu algul kasutajale ei näita

        // kontrolli, ega juhuslikud indeksid omavahel ei kattu (kui kattuvad, määra uued, kuni enam ei kattu)
        while(suvaline_indeks0 == suvaline_indeks1)suvaline_indeks1 = juhuslik_indeks(murdesonad);
        while(suvaline_indeks0 == suvaline_indeks2 || suvaline_indeks1 == suvaline_indeks2)suvaline_indeks2 = juhuslik_indeks(murdesonad);
        System.out.println("suvaline_indeks0: " + suvaline_indeks0 + ", suvaline_indeks1: " + suvaline_indeks1 + ", suvaline_indeks2: " + suvaline_indeks2);

        // säti (uued) nuppude ja küsitava sõna väärtused (ühtlasi tühjenda ja täida uuesti indeksite list)
        muudaKysimust(false, yldkeele_vasted);

        // leia õige vastusevariantide seast suvaliselt valitud murdesõnale õige vaste
        kysitava_indeks = indeksite_list.indexOf(murdesonad.indexOf(murdesona.getText()));
        oige_vastus = yldkeele_vasted.get(indeksite_list.get(kysitava_indeks));

        // määra, mis juhtub vastamisnupule vajutamisel (teist nuppu pole niikuinii näha)
        vasta.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                vasta.setVisible(false); //pärast vastamist peida korraks vastuse nupp (et ei hakataks uuesti vastama)
                nupp1.setDisable(true); nupp2.setDisable(true); nupp3.setDisable(true); // ära lase rohkem vastuseid klõpsida
                edasi.setVisible(true); // näita edasi-nuppu
                errori_tekst.setText(""); // tee tühja vastuse veateade tühjaks

                // määra, mis peaks juhtuma, kui kõik läheb plaanipäraselt
                try {
                    kontrolliVastust(); // kontrolli, kas kasutaja vastus on õige vastus (vastavalt tegutse listidega edasi)

                    // määra, mis juhtub edasi-nupule vajutamisel
                    edasi.setOnAction(new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent event) {

                            edasi.setVisible(false); // pärast vajutamist peida kohe edasi-nupp
                            nupp1.setDisable(false); nupp2.setDisable(false); nupp3.setDisable(false); // võimalda vastustel klikkida
                            vasta.setVisible(true); // näita vastamisnuppu
                            vajutuse_tekst.setText(""); // ära näita eelmise vastuse "Õiget" ega "Valet"
                            lause.setText(""); // kustuta eelmise vastuse näitelause
                            suvaline_indeks0 = juhuslik_indeks(murdesonad); // genereeri uus suvaline_indeks0 (alati murdesõnade/üldkeele vastete listi pikkuse järgi)

                            if (murdesonad.size() >= 3) { // kui murdesõnade listi pikkus on 3 või suurem
                                // genereeri teised 2 uut indeksit ka murdesõnade listi pikkuse põhjal
                                suvaline_indeks1 = juhuslik_indeks(murdesonad);
                                while (suvaline_indeks0 == suvaline_indeks1) suvaline_indeks1 = juhuslik_indeks(murdesonad);
                                suvaline_indeks2 = juhuslik_indeks(murdesonad);
                                while (suvaline_indeks0 == suvaline_indeks2 || suvaline_indeks1 == suvaline_indeks2) suvaline_indeks2 = juhuslik_indeks(murdesonad);

                                // kuva uus küsitav sõna ja vastusevariandid
                                muudaKysimust(false, yldkeele_vasted);

                                // leia uuele sõnale vastav õige üldkeelne vaste
                                kysitava_indeks = indeksite_list.indexOf(murdesonad.indexOf(murdesona.getText()));
                                oige_vastus = yldkeele_vasted.get(indeksite_list.get(kysitava_indeks));

                            } else if (!murdesonad.isEmpty()) { // kui murdesõnade listis on 1 või 2 sõna järel

                                // genereeri 2 vale vastuse indeksit juba vastatud sõnade listi pikkuse põhjal
                                suvaline_indeks1 = juhuslik_indeks(vastatud_sonad);
                                suvaline_indeks2 = juhuslik_indeks(vastatud_sonad);
                                while (suvaline_indeks1 == suvaline_indeks2) suvaline_indeks2 = juhuslik_indeks(vastatud_sonad);

                                // kuva uus küsitav sõna ja vastusevariandid
                                muudaKysimust(true, vastatud_sonad);

                                oige_vastus = yldkeele_vasted.get(suvaline_indeks0); // leia õige vastus

                            } else lopetaGame(); // kui murdesõnade list on tühi, siis lõpeta mäng
                        }
                    });


                    // määra, mis juhtub, kui kõik ei lähe õigesti
                } catch (NullPointerException e){ // antud juhul kõige tõenäolisem on see, et klikkimine ei ole päris pihta läinud
                    nupp1.setDisable(false);nupp2.setDisable(false);nupp3.setDisable(false); // võimalda endiselt vastata
                    vasta.setVisible(true); // näita vastamisnuppu
                    edasi.setVisible(false); // peida edasimineku nupp
                    vajutuse_tekst.setText(""); // ära kuva "Õige!" ega "Vale!" teksti
                    errori_tekst.setText("Vali üks vastusevariantidest!"); // vaid anna teade, et kasutaja peab vastamiseks ühe vastuse valima
                }
            }
        });
    }

    // seadista mängu aken
    private void seadista2aken() {
        alglava.close(); // pane alguskuva aken kinni

        GridPane asetus = new GridPane(); // kasuta gridpane'i
        lava = new Stage();
        Scene stseen = new Scene(asetus, 650, 500);
        VBox vbox_vastused = new VBox(10); // vastusevariantide jaoks kasuta "üksteise all"-varianti
        vbox_vastused.setId("vbox_vastused"); // määra vboxi ID, et CSS pääseks sellele ligi

        lava.setScene(stseen);
        lava.setTitle("Eesti murdesõnade arvamise mäng");
        lava.show();

        vajutuse_tekst.setId("vajutus"); // määra vajutuse teksti ID, et CSS pääseks sellele ligi
        murdesona.setId("murdesona"); // määra murdesõna ID, et CSS pääseks sellele ligi
        lause.setId("lause"); // määra näitelause ID, et CSS pääseks sellele ligi
        asetus.setPrefSize(650, 500); // määra gridpane'i suurus
        lause.wrappingWidthProperty().bind(asetus.widthProperty().subtract(40)); // murra ridu, kui näitelause on hõivanud gridpane'i laius - 40 ühikut

        // määra raadionupud vastuste gruppi
        nupp1.setToggleGroup(vastused);
        nupp2.setToggleGroup(vastused);
        nupp3.setToggleGroup(vastused);

        vbox_vastused.getChildren().addAll(nupp1, nupp2, nupp3); // määra vboxi elemendid (vastusevariandid)
        vbox_vastused.setAlignment(Pos.CENTER_LEFT); // nupud ja tekst on vasakule joondusega

        // lisa elemendid gridpane'ile, vajadusel säti nende joondust ja nende ümber jäävat ruumi
        asetus.add(sonu, 0, 0, 2, 1); asetus.setHalignment(sonu, HPos.LEFT);asetus.setMargin(sonu, new Insets(0, 20, 0, 20));
        asetus.add(punktid, 4, 0, 2, 1); asetus.setHalignment(punktid, HPos.RIGHT); asetus.setMargin(punktid, new Insets(0, 20, 0, 20));
        asetus.add(murdesona, 2, 1, 2, 1); asetus.setMargin(murdesona, new Insets(40, 20, 0, 20));asetus.setHalignment(murdesona, HPos.CENTER);
        asetus.add(vbox_vastused, 2, 2, 3, 1); asetus.setMargin(vbox_vastused, new Insets(0, 20, 30, 20));
        asetus.add(vasta, 2, 3, 2, 1); asetus.setMargin(vasta, new Insets(0, 20, 20, 20));asetus.setHalignment(vasta, HPos.CENTER);
        asetus.add(vajutuse_tekst, 4, 2, 2, 2); asetus.setMargin(vajutuse_tekst, new Insets(0, 10, 40, 60)); asetus.setHalignment(vajutuse_tekst, HPos.CENTER);
        asetus.add(errori_tekst, 4, 2, 2, 2); asetus.setMargin(errori_tekst, new Insets(0, 20, 40, 20));asetus.setHalignment(errori_tekst, HPos.CENTER);
        asetus.add(lause, 1, 4, 4, 1); asetus.setMargin(lause, new Insets(20, 20, 0, 20)); asetus.setHalignment(lause, HPos.CENTER);
        asetus.add(edasi, 2, 5, 2, 1); asetus.setMargin(edasi, new Insets(0, 20, 0, 20)); asetus.setHalignment(edasi, HPos.CENTER);

        // määra gridpane'i tulpade laius ja ridade kõrgus akna suurusega proportsionaalselt
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

        // kasuta kujunduses CSS-i
        stseen.getStylesheets().add(EestiMurded.class.getResource("Main.css").toExternalForm());
    }


    // kontrolli kasutaja vastust
    private void kontrolliVastust(){

        RadioButton rb = (RadioButton) vastused.getSelectedToggle(); // küsi kasutaja vastust (tuleb raadionupu vormis)
        String vastus = rb.getText(); // küsi klikitud raadionupu teksti

        if(vastus.equals(oige_vastus)){ // kui vastus on õige
            vajutuse_tekst.setText("Õige!"); // määra kasutajale kuvatavaks tekstiks "Õige!"
            lause.setText(lausenaited.get(murdesonad.indexOf(murdesona.getText()))); // kuva vastava sõnaga lausenäide
            punkt++; // lisa skoorile üks punkt
            punktid.setText("Punkte: " + punkt); // kuva uus punktide arv
            lausenaited.remove(lause.getText()); // eemalda õigesti vastatud sõna näitelause näitelausete listist
            murdesonad.remove(murdesona.getText()); // eemalda murdesõna murdesõnade listist
            sonu.setText("Sõnu jäänud: " + murdesonad.size()); // kuva vastata jäänud sõnade arv
            yldkeele_vasted.remove(oige_vastus); // eemalda üldkeelne vaste vastete listist
            vastatud_sonad.add(vastus); // lisa õigesti vastatud sõna vastatud sõnade listi
        }

        else { // kui vastus on vale
            vajutuse_tekst.setText("Vale!"); // kuva kasutajale, et vastus on vale
            punkt--; // võta punkt maha
            punktid.setText("Punkte: " + punkt); // kuva uus punktide arv
            // listide sisu jääb samaks
        }

        rb.setSelected(false); // eemalda valiku märge raadionupu küljest
    }

    // muuda kuvatavat sõna ja vastusevariante
    private void muudaKysimust(boolean kasvahemkui3, ArrayList<String> teistevastustelist){

        indeksite_list.clear(); // tee 3 indeksi list puhtaks
        if(!kasvahemkui3) indeksite_list.add(suvaline_indeks0); // kui listis rohkem kui 3 sõna, siis lisa indeksite listi ka suvaline_indeks0
        indeksite_list.add(suvaline_indeks1); indeksite_list.add(suvaline_indeks2); // teised indeksid lisa igal juhul

        if(kasvahemkui3) nupp1.setText(yldkeele_vasted.get(suvaline_indeks0)); // kui < 3, siis õige vastus alati 1. nupu alla
        else nupp1.setText(yldkeele_vasted.get(indeksite_list.get(juhuslik_indeks(indeksite_list)))); // vastasel juhul genereeri indeksite listi pikkuse põhjal (3) üks juhuslik indeks (0-st 2-ni), selle indeksi järgi küsi indeksite listist ühe indeksi väärtus ja küsi sõna, mis on selle indeksiga vastete listis. Määra see sõna 1. raadionupu vasteks. Tee sama teistega.

        nupp2.setText(teistevastustelist.get(indeksite_list.get(juhuslik_indeks(indeksite_list)))); while(nupp1.getText().equals(nupp2.getText())) nupp2.setText(teistevastustelist.get(indeksite_list.get(juhuslik_indeks(indeksite_list))));

        nupp3.setText(teistevastustelist.get(indeksite_list.get(juhuslik_indeks(indeksite_list)))); while(nupp1.getText().equals(nupp3.getText()) || nupp2.getText().equals(nupp3.getText())) nupp3.setText(teistevastustelist.get(indeksite_list.get(juhuslik_indeks(indeksite_list))));

        if(kasvahemkui3) murdesona.setText(murdesonad.get(suvaline_indeks0)); // kui < 3, siis võta murdesõna listist suvaline_indeks0 põhjal
        else murdesona.setText(murdesonad.get(indeksite_list.get(juhuslik_indeks(indeksite_list)))); // vastasel juhul (st tavaliselt) tee asja läbi 3 indeksi listi.
    }

    // seadista mängu alguse aken
    private void seadista1Aken(){

        StackPane algasetus = new StackPane(); // kasuta stackpane'i
        alglava = new Stage();
        Scene algstseen = new Scene(algasetus, 650, 500);
        VBox algvbox = new VBox(20);

        alglava.setScene(algstseen);
        alglava.setTitle("Eesti murdesõnade arvamise mäng");
        alglava.show();

        Text tervitustekst = new Text("See mäng laseb sul arvata erinevate murdesõnade tähendust. \n Kui oled õigesti arvanud, saad punkti. Kui arvad valesti, läheb punkt maha. \n Alustamiseks vajuta nupule 'Alusta'.");
        tervitustekst.setTextAlignment(TextAlignment.CENTER);
        tervitustekst.setId("tervitustekst");
        Button nupp = new Button("Alusta");

        // määra, mis juhtub alustamisnupule vajutamisel
        nupp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                seadista2aken(); // seadista mängu aken
            }
        });

        // säti algakna välimust css-i stylesheeti abil (eraldi fail samas kaustas)
        algstseen.getStylesheets().add
                (EestiMurded.class.getResource("Main.css").toExternalForm());

        algvbox.setAlignment(Pos.CENTER);
        algvbox.getChildren().addAll(tervitustekst, nupp);
        algasetus.getChildren().addAll(algvbox);
    }

    // juhuslike indeksite leidmine (võtab argumendiks suvalise ArrayListi)
    private int juhuslik_indeks(ArrayList list){
        return (int)(Math.round(Math.random() * (list.size() - 1)));
    }

    // mängu lõpp
    private void lopetaGame(){

        lava.close(); // pane mängu aken kinni

        // loo uus aken
        StackPane loppasetus = new StackPane();
        lava = new Stage();
        Scene loppstseen = new Scene(loppasetus, 650, 500);
        lava.setScene(loppstseen);
        Label lopp = new Label("Mäng läbi! Kogusid " + punkt + " punkti.");// kuva kasutajale, et mäng on läbi ja punktide arv
        lopp.setId("lopp");
        loppasetus.getChildren().add(lopp);
        lava.show();
        loppstseen.getStylesheets().add
                (EestiMurded.class.getResource("Main.css").toExternalForm());
    }
}