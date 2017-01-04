package sample;
// Impordi vajalikud paketid
import java.io.BufferedReader;
import java.io.FileReader; // pakett failide sisselugemiseks
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Sõnad {

    public HashMap sõnad() {

        // Määratle muutujate nimed
        String csvFile = "C:\\Users\\a71386\\Desktop\\java_EestiMurded\\src\\sample\\murded.csv"; // sõnade fail csv-formaadis
        String line = "";
        String csvSplitBy = ";"; // eralda väljad semikooloni kohalt

        // Loe faili tulbad erinevatesse listidesse
        ArrayList<String> murdesõnad = new ArrayList<>();
        ArrayList<String> üldkeele_vasted = new ArrayList<>();
        ArrayList<String> sõnaliik = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while((line = br.readLine()) != null) { // kuni rida ei ole tühi
                String[] words = line.split(csvSplitBy); // tee tabeli ridadest sõnejärjend (eralda väljad ; koha pealt)
                murdesõnad.add(words[0]);
                üldkeele_vasted.add(words[1]);
                sõnaliik.add(words[2]);
            }

        } catch(IOException e) {
            e.printStackTrace();
        }

        HashMap map = new HashMap();
        map.put("murdesõnad", murdesõnad);
        map.put("üldkeele_vasted", üldkeele_vasted);
        map.put("sõnaliik", sõnaliik);

        return map;

    }

}