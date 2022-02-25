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
import message.ReturnMessage;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import testiranje.MojObjekat;

/**
 *
 * @author Ivan
 */
public interface MyService {
    // test
    @GET("test")
    Call<String>test();
    
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
    
    // 3. @Path("kreiranje/{naziv}/{adresa}/{idm}")
    @POST("komitent/kreiranje/{naziv}/{adresa}/{idm}")
    Call<String> kreirajKomitenta(
            @Path("naziv") String Naziv,
            @Path("adresa") String Adresa,
            @Path("idm") int IdM);
    
    // 4. @Path("mesto/{idk}/{idm}")
    @POST("komitent/mesto/{idk}/{idm}")
    Call<String> promeniSedisteKomitenta(
            @Path("idk") int IdK,
            @Path("idm") int IdM);
    
    // 5 @Path("kreiranje/{idk}/{idm}/{dozvM}")
    @POST("racun/kreiranje/{idk}/{idm}/{dozvM}")
    Call<String> kreiranjeRacuna(
            @Path("idk") int IdK,
            @Path("idm") int IdM,
            @Path("dozvM") double DozvMinus);
    
    // 6. @Path("zatvaranje/{idr}")
    @POST("racun/zatvaranje/{idr}")
    Call<String> zatvaranjeRacuna(@Path("idr") int IdR);
    
    // 7. @Path("prenos/{idR1}/{idR2}/{iznos}/{svrha}")
    @POST("transakcija/prenos/{idR1}/{idR2}/{iznos}/{svrha}")
    Call<String> prenosSaNa(
            @Path("idr1") int IdR1, 
            @Path("idr2") int IdR2, 
            @Path("iznos") double Iznos, 
            @Path("svrha") String Svrha);
    
    // 8. @Path("uplata/{idR}/{idF}/{iznos}/{svrha}")
    @POST("transakcija/uplata/{idR}/{idF}/{iznos}/{svrha}")
    Call<String> uplataNa(
            @Path("idR") int IdR, 
            @Path("idF") int IdF, 
            @Path("iznos") double Iznos, 
            @Path("svrha") String Svrha);
    
    // 9
    @POST("transakcija/isplata/{idR}/{idF}/{iznos}/{svrha}")
    Call<String> isplataSa(
            @Path("idR") int IdR, 
            @Path("idF") int IdF, 
            @Path("iznos") double Iznos, 
            @Path("svrha") String Svrha);
    // 10
    @GET("mesto/sve")
    Call<String> dohvatiMesta();
    
    // 11
    @GET("filijala/sve")
    Call<String> dohvatiFilijale();
    
    // 12
    @GET("komitent/sve")
    Call<String> dohvatiKomitente();
    
    // 13
    @GET("racun/sve/{idk}")
    Call<String> dohvatiRacuneZaKomitenta(@Path("idk") int IdK);
    
    // 14
    @GET("transakcija/sve/{idr}")
    Call<String> dohvatiTransakcijeZaRacun(@Path("idr") int IdR);
}
