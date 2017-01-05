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
    ArrayList<Integer> testlist = new ArrayList<Integer>();

    Stage alglava; // see peab olema siin defineeritud, et mäng() saaks vana akna kinni panna
    Stage lava; // see peab olema siin defineeritud, et lõpetaMäng() saaks mängu akna kinni panna

    int suvaline_indeks0 = juhuslik_indeks(murdesõnad); // indeksid on vaja defineerida siin globaalselt, et neid saaks nupulevajutusel uuesti genereerida
    int suvaline_indeks1 = juhuslik_indeks(murdesõnad);
    int suvaline_indeks2 = juhuslik_indeks(murdesõnad);
    int murdesõna_indeks;
    Label murdesõna = new Label();
    String õige_vastus;

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

        ToggleGroup vastused = new ToggleGroup();
        RadioButton nupp1 = new RadioButton();nupp1.setToggleGroup(vastused);
        RadioButton nupp2 = new RadioButton();nupp2.setToggleGroup(vastused);
        RadioButton nupp3 = new RadioButton();nupp3.setToggleGroup(vastused);

        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(sõnu, murdesõna, nupp1, nupp2, nupp3, vasta, vajutuse_tekst, punktid);
        asetus.getChildren().addAll(vbox);

        ////3.1.2. määra murdesõnale ja vastusevariantidele väärtused
        /////3.1.2.1. Kontrolli, ega globaalselt defineeritud indeksid ei kattu
        while(suvaline_indeks0 == suvaline_indeks1)suvaline_indeks1 = juhuslik_indeks(murdesõnad);
        while(suvaline_indeks0 == suvaline_indeks2 || suvaline_indeks1 == suvaline_indeks2)suvaline_indeks2 = juhuslik_indeks(murdesõnad);

        /////3.1.2.2. leia indeksite põhjal listidest sõnad
        //////3.1.2.2.1. pane 3 genereeritud indeksit (0-st kuni murdesõnade listi pikkuseni) ühte arrayListi
        testlist.add(suvaline_indeks0); testlist.add(suvaline_indeks1); testlist.add(suvaline_indeks2);

        //////3.1.2.2.2. võta iga raadionupu kohta 3 suvalisest indeksist üks, mille järgi määrad nupu sildi. Sildid ei tohi kattuda.
        nupp1.setText(üldkeele_vasted.get(testlist.get(juhuslik_indeks(testlist))));
        nupp2.setText(üldkeele_vasted.get(testlist.get(juhuslik_indeks(testlist))));
        while(nupp1.getText().equals(nupp2.getText())) nupp2.setText(üldkeele_vasted.get(testlist.get(juhuslik_indeks(testlist))));
        nupp3.setText(üldkeele_vasted.get(testlist.get(juhuslik_indeks(testlist))));
        while(nupp1.getText().equals(nupp3.getText()) || nupp2.getText().equals(nupp3.getText())) nupp3.setText(üldkeele_vasted.get(testlist.get(juhuslik_indeks(testlist))));

        // Määra murdesõna labelile üks kolmest suvalisest indeksist üks suvaline ja võta selle järgi välja murdesõnade listist küsitav sõna
        murdesõna.setText(murdesõnad.get(testlist.get(juhuslik_indeks(testlist))));

        // Millisele testlisti indeksile vastab murdesõna indeks?
        murdesõna_indeks = testlist.indexOf(murdesõnad.indexOf(murdesõna.getText()));

        // Mis on selle testlisti indeksiga üldkeelne vaste?
        õige_vastus = üldkeele_vasted.get(testlist.get(murdesõna_indeks));

        // Kontrolliks prindi välja teated
        System.out.println(
                "Murdesõnade listi pikkus on " + murdesõnad.size() + ", üldkeele listi pikkus on " + üldkeele_vasted.size() + " ja vastatud sõnade listi pikkus on " + vastatud_sõnad.size());
        System.out.println(
                "3 indeksit testlistis on: " + testlist
                + ", nendele vastavad üldkeele listis sõnad " + üldkeele_vasted.get(testlist.get(0)) + ", " + üldkeele_vasted.get(testlist.get(1)) + " ja " + üldkeele_vasted.get(testlist.get(2)));
        System.out.println(
                "Nupp1 all on " + nupp1.getText() + ", nupp2 all on " + nupp2.getText() + " ja nupp3 all on " + nupp3.getText());
        System.out.println("Küsitakse murdesõna " + murdesõna.getText() + ", selle indeks murdesõnade listis on " + murdesõnad.indexOf(murdesõna.getText()) + " ja see indeks on indeksite listis " + murdesõna_indeks + ". element");
        System.out.println("Õige vastus on seega " + õige_vastus + ", sest " + õige_vastus + " on üldkeele listi " + murdesõnad.indexOf(murdesõna.getText()) + ". element ehk " + üldkeele_vasted.get(murdesõnad.indexOf(murdesõna.getText())));


        ////3.1.3. määra, mis juhtub vastamise nupule vajutusel
        vasta.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Vajutas!");
                System.out.println(
                        "Murdesõnade listi pikkus on " + murdesõnad.size() + ", üldkeele listi pikkus on " + üldkeele_vasted.size() + " ja vastatud sõnade listi pikkus on " + vastatud_sõnad.size());

                RadioButton rb = (RadioButton)vastused.getSelectedToggle();
                String vastus = rb.getText();
                System.out.println("Kasutaja vastas, et " + murdesõna.getText() + " vaste üldkeeles on " + vastus);

                if(vastus.equals(õige_vastus)){
                    System.out.println("See vastus on õige. Lisan ühe punkti ja eemaldan murdesõna listist.");
                    vajutuse_tekst.setText("Õige!");
                    punkt++;
                    punktid.setText("Punkte: " + punkt);
                    murdesõnad.remove(murdesõna.getText());
                    sõnu.setText("Sõnu jäänud: " + murdesõnad.size());
                    üldkeele_vasted.remove(õige_vastus);
                    vastatud_sõnad.add(vastus);
                    System.out.println("Selle tulemusel on kasutajal punke " + punkt + ", murdesõnade listi pikkus on " + murdesõnad.size() + ", üldkeele listi pikkus on " + üldkeele_vasted.size() + " ja vastatud sõnade listi pikkus on " + vastatud_sõnad.size());
                }
                else {
                    System.out.println("See vastus on vale. Eemaldan ühe punkti, aga listid jäävad samaks.");
                    vajutuse_tekst.setText("Vale!");
                    punkt--;
                    punktid.setText("Punkte:" + punkt);
                    System.out.println("Selle tulemusel on kasutajal punke " + punkt + ", murdesõnade listi pikkus on endiselt " + murdesõnad.size() + ", üldkeele listi pikkus on endiselt " + üldkeele_vasted.size() + " ja vastatud sõnade listi pikkus on endiselt " + vastatud_sõnad.size());
                }

                System.out.println("Määran kasutajale kuvamiseks uued sõnad.");
                suvaline_indeks0 = juhuslik_indeks(murdesõnad);
                System.out.println("Murdesõnade listi pikkus on " + murdesõnad.size() + " ja suvaline_indeks0 = " + suvaline_indeks0);

                if(murdesõnad.size()>=3) {

                    suvaline_indeks1 = juhuslik_indeks(murdesõnad); while(suvaline_indeks0 == suvaline_indeks1) suvaline_indeks1 = juhuslik_indeks(murdesõnad);
                    suvaline_indeks2 = juhuslik_indeks(murdesõnad);while (suvaline_indeks0 == suvaline_indeks2 || suvaline_indeks1 == suvaline_indeks2) suvaline_indeks2 = juhuslik_indeks(murdesõnad);
                    System.out.println("Murdesõnade listi pikkus on >= 3. suvaline_indeks1 = " + suvaline_indeks1 + " ja suvaline_indeks2 = " + suvaline_indeks2);

                    testlist.clear();
                    System.out.println("Teen testlisti tühjaks, et panna sinna uued indeksid. Testlisti pikkus on: " + testlist.size());
                    testlist.add(suvaline_indeks0); testlist.add(suvaline_indeks1); testlist.add(suvaline_indeks2);
                    System.out.println("Nüüd on testlistis 3 indeksit: " + testlist
                            + ", nendele vastavad üldkeele listis sõnad " + üldkeele_vasted.get(testlist.get(0)) + ", " + üldkeele_vasted.get(testlist.get(1)) + " ja " + üldkeele_vasted.get(testlist.get(2)));

                    nupp1.setText(üldkeele_vasted.get(testlist.get(juhuslik_indeks(testlist))));
                    nupp2.setText(üldkeele_vasted.get(testlist.get(juhuslik_indeks(testlist))));
                    while(nupp1.getText().equals(nupp2.getText())) nupp2.setText(üldkeele_vasted.get(testlist.get(juhuslik_indeks(testlist))));
                    nupp3.setText(üldkeele_vasted.get(testlist.get(juhuslik_indeks(testlist))));
                    while(nupp1.getText().equals(nupp3.getText()) || nupp2.getText().equals(nupp3.getText())) nupp3.setText(üldkeele_vasted.get(testlist.get(juhuslik_indeks(testlist))));

                    System.out.println("Nupp1 all on " + nupp1.getText() + ", nupp2 all on " + nupp2.getText() + " ja nupp3 all on " + nupp3.getText());

                    murdesõna.setText(murdesõnad.get(testlist.get(juhuslik_indeks(testlist))));
                    murdesõna_indeks = testlist.indexOf(murdesõnad.indexOf(murdesõna.getText()));
                    õige_vastus = üldkeele_vasted.get(testlist.get(murdesõna_indeks));

                    System.out.println("Küsitakse murdesõna " + murdesõna.getText() + ", selle indeks murdesõnade listis on " + murdesõnad.indexOf(murdesõna.getText()) + " ja see indeks on indeksite listis " + murdesõna_indeks + ". element");
                    System.out.println("Õige vastus on seega " + õige_vastus + ", sest " + õige_vastus + " on üldkeele listi " + murdesõnad.indexOf(murdesõna.getText()) + ". element ehk " + üldkeele_vasted.get(murdesõnad.indexOf(murdesõna.getText())));


                    rb.setSelected(false);
                }

                else if(!murdesõnad.isEmpty()){
                    suvaline_indeks1 = juhuslik_indeks(vastatud_sõnad);
                    suvaline_indeks2 = juhuslik_indeks(vastatud_sõnad);while (suvaline_indeks1 == suvaline_indeks2) suvaline_indeks2 = juhuslik_indeks(vastatud_sõnad);

                    System.out.println("Murdesõnade listi pikkus on 0 < list < 3 ehk 1 või 2. suvaline_indeks0 küsitakse murdesõnade listi pikkuse järgi ja see on " + suvaline_indeks0 + ". Murdesõnade listi pikkus peaks olema sama, mis üldkeele vastete listi pikkus, milleks on " + üldkeele_vasted.size() + ". Teised indeksid küsitakse nüüd vastatud sõnade listist, mille pikkus on " + vastatud_sõnad.size() + ". suvaline_indeks1 = " + suvaline_indeks1 + " ja suvaline_indeks2 = " + suvaline_indeks2);

                    testlist.clear();
                    System.out.println("Teen testlisti tühjaks, et panna sinna uued indeksid. Testlisti pikkus on: " + testlist.size());
                    testlist.add(suvaline_indeks1); testlist.add(suvaline_indeks2);
                    System.out.println("Nüüd on testlistis 2 indeksit: " + testlist + ", nendele vastavad vastatud sõnade listis sõnad " + vastatud_sõnad.get(testlist.get(0)) + " ja " + vastatud_sõnad.get(testlist.get(1)));

                    // Siin annan praegu alla. Kahe viimase sõna puhul on õige vastus esimene.
                    nupp1.setText(üldkeele_vasted.get(suvaline_indeks0));
                    nupp2.setText(vastatud_sõnad.get(testlist.get(juhuslik_indeks(testlist))));
                    nupp3.setText(vastatud_sõnad.get(testlist.get(juhuslik_indeks(testlist))));
                    while(nupp2.getText().equals(nupp3.getText())) nupp3.setText(vastatud_sõnad.get(testlist.get(juhuslik_indeks(testlist))));

                    System.out.println("Nupp1 all on " + nupp1.getText() + ", nupp2 all on " + nupp2.getText() + " ja nupp3 all on " + nupp3.getText());

                    murdesõna.setText(murdesõnad.get(suvaline_indeks0));
                    murdesõna_indeks = murdesõnad.indexOf(murdesõna.getText());
                    õige_vastus = üldkeele_vasted.get(murdesõna_indeks);
                    System.out.println("Küsitakse murdesõna " + murdesõna.getText() + ", selle indeks murdesõnade listis on " + murdesõnad.indexOf(murdesõna.getText()) + " ja see indeks on indeksite listis " + murdesõna_indeks + ". element");
                    System.out.println("Õige vastus on seega " + õige_vastus + ", sest " + õige_vastus + " on üldkeele listi " + murdesõnad.indexOf(murdesõna.getText()) + ". element ehk " + üldkeele_vasted.get(murdesõnad.indexOf(murdesõna.getText())));


                    rb.setSelected(false);
                }

                else lõpetaMäng();

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
