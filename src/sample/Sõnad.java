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
        ArrayList<String> murdesonad = new ArrayList<>();
        ArrayList<String> yldkeele_vasted = new ArrayList<>();
        ArrayList<String> sonaliik = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while((line = br.readLine()) != null) { // kuni rida ei ole tühi
                String[] words = line.split(csvSplitBy); // tee tabeli ridadest sõnejärjend (eralda väljad ; koha pealt)
                murdesonad.add(words[0]);
                yldkeele_vasted.add(words[1]);
                sonaliik.add(words[2]);
            }

        } catch(IOException e) {
            e.printStackTrace();
        }

        HashMap map = new HashMap();
        map.put("murdesonad", murdesonad);
        map.put("yldkeele_vasted", yldkeele_vasted);
        map.put("sonaliik", sonaliik);

        return map;

    }

}