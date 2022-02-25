/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import entities.Filijala;
import entities.Komitent;
import entities.Mesto;
import entities.Racun;
import entities.Transakcija;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 *
 * @author Ivan
 */
public interface MyService {
    // 1
    @POST("mesto/kreiranje/{naziv}/{pb}")
    Call<String> kreirajMesto(
            @Path("naziv") String Naziv, 
            @Path("pb") String PB);
    
    // 2 @Path("kreiranje/{naziv}/{adresa}/{idm}")
    @POST("filijala/kreiranje/{naziv}/{adresa}/{idm}")
    Call<String> kreirajFilijalu(
            @Path("naziv") String Naziv,
            @Path("adresa") String Adresa,
            @Path("idm") int IdM);
    
    // 3.
    @PUT("komitent/{naziv}/{adresa}/{mesto}")
    Call<String> addKomitent(
            @Path("naziv") String naziv,
            @Path("adresa") String adresa,
            @Path("mesto") int mesto);
    
    // 4.
    @PUT("komitent/{komitent}/{mesto}")
    Call<String> changeKomitentLocation(
            @Path("komitent") int komitent,
            @Path("mesto") int mesto);
    
    // 5
    @PUT("racun/{komitent}/{mesto}/{dm}")
    Call<String> addRacun(
            @Path("komitent") int komitent,
            @Path("mesto") int mesto,
            @Path("dm") double dozvMinus);
    
    // 6.
    @POST("racun/{racun}")
    Call<String> disableRacun(@Path("racun") int racun); 
    
    // 7. 8. 9.
    @PUT("transakcija/{tip}/{racun}/{primfil}/{iznos}/{svrha}")
    Call<ResponseBody> addTransakcija(
            @Path("tip") String tip,
            @Path("racun") int racun,
            @Path("primfil") int primfil,
            @Path("iznos") double iznos,
            @Path("svrha") String svrha);
    
    // 10.
    @GET("mesto")
    Call<List<Mesto>> getMesto();
    
    // 11.
    @GET("filijala")
    Call<List<Filijala>> getFilijala();
    
    // 12.
    @GET("komitent")
    Call<List<Komitent>> getKomitent();
    
    // 13.
    @GET("racun")
    Call<List<Racun>> getRacun(@Query("komitent") int komitent);
    
    // 14.
    @GET("transakcija")
    Call<List<Transakcija>> getTransakcija(@Query("racun") int racun);
}
