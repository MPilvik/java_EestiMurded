package sample;
// Impordi vajalikud paketid
import java.io.BufferedReader;
import java.io.FileReader; // pakett failide sisselugemiseks
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Sonad {

    // Määratle muutujate nimed
    String csvFile = "C:\\Users\\a71386\\Desktop\\java_EestiMurded\\src\\sample\\murded_testlist_V.csv"; // sõnade fail csv-formaadis
    String line = "";
    String csvSplitBy = ";"; // eralda väljad semikooloni kohalt
    HashMap map = new HashMap();

    private void loefailhashmapi() { // selleks, et saaks pärast stringi kaudu küsida

        // Tee tühjad listid
        ArrayList<String> murdesonad = new ArrayList<String>();
        ArrayList<String> yldkeele_vasted = new ArrayList<String>();
        ArrayList<String> lausenaited = new ArrayList<String>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) { // kuni rida ei ole tühi
                String[] tulbad = line.split(csvSplitBy); // tee tabeli ridadest sõnejärjend (eralda väljad ; koha pealt)
                murdesonad.add(tulbad[0]);
                yldkeele_vasted.add(tulbad[1]);
                lausenaited.add(tulbad[2]);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // HashMapi teen siin tegelt ainult selleks, et saaks returnida kõik listid korraga ja et pärast saaks stringi järgi listi nime küsida
        //HashMap map = new HashMap();
        map.put("murdesonad", murdesonad);
        map.put("yldkeele_vasted", yldkeele_vasted);
        map.put("lausenaited", lausenaited);

    }

    public ArrayList<String> kysiListi(String listinimi){
        loefailhashmapi();
        return (ArrayList) map.get(listinimi);
    }
}