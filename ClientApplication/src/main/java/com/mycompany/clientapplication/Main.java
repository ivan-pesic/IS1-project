
import com.mycompany.clientapplication.DohvatiFilijale;
import com.mycompany.clientapplication.DohvatiKomitente;
import com.mycompany.clientapplication.DohvatiMesta;
import com.mycompany.clientapplication.DohvatiRacuneZaKomitenta;
import com.mycompany.clientapplication.DohvatiTransakcijeZaRacun;
import com.mycompany.clientapplication.KreirajFilijalu;
import com.mycompany.clientapplication.KreirajKomitenta;
import com.mycompany.clientapplication.KreirajMesto;
import com.mycompany.clientapplication.KreirajTransakcijuIsplata;
import com.mycompany.clientapplication.KreirajTransakcijuPrenos;
import com.mycompany.clientapplication.KreirajTransakcijuUplata;
import com.mycompany.clientapplication.MyRequest;
import com.mycompany.clientapplication.OtvaranjeRacuna;
import com.mycompany.clientapplication.PromeniSedisteKomitentu;
import com.mycompany.clientapplication.ZatvaranjeRacuna;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.ReturnMessage;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import services.MyService;
import testiranje.MojObjekat;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ivan
 */
public class Main {
    static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build();

    static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://localhost:8080/CentralServerTest/resources/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(okHttpClient)
            .build();
    
    public static void main(String[] args) {
        System.out.println("Klijentska aplikacija startovana.");
        MyService service = Main.retrofit.create(MyService.class);
        
        Scanner scanner = new Scanner(System.in);
        
        ArrayList<MyRequest> requests = new ArrayList<>();
        requests.add(new KreirajMesto());
        requests.add(new KreirajFilijalu());
        requests.add(new KreirajKomitenta());
        requests.add(new PromeniSedisteKomitentu());
        requests.add(new OtvaranjeRacuna());
        requests.add(new ZatvaranjeRacuna());
        requests.add(new KreirajTransakcijuPrenos());
        requests.add(new KreirajTransakcijuUplata());
        requests.add(new KreirajTransakcijuIsplata());
        requests.add(new DohvatiMesta());
        requests.add(new DohvatiFilijale());
        requests.add(new DohvatiKomitente());
        requests.add(new DohvatiRacuneZaKomitenta());
        requests.add(new DohvatiTransakcijeZaRacun());
        
        while(true) {
            for (MyRequest request : requests) {
                
                System.out.println(request.message());
            }
            System.out.println("\nUneti zeljenu opciju: ");
            int response = Integer.parseInt(scanner.nextLine());
            if(response == 0) break;
            
            MyRequest request = requests.get(response - 1);
            if(request != null) request.execute(service);
            else System.out.println("Greska: zadati zahtev ne postoji!");
        }
    }
}   