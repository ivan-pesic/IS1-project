
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
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
            .baseUrl("http://localhost:8080/centralniServer/resources/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build();
    
    public static void main(String[] args) {
        System.out.println("Klijentska aplikacija startovana.");
        
    }
}
