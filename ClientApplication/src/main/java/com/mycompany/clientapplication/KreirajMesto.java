/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.clientapplication;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import message.ReturnMessage;
import retrofit2.Call;
import services.MyService;

/**
 *
 * @author Ivan
 */
public class KreirajMesto implements MyRequest {

    @Override
    public void execute(MyService service) {
        try {
            Scanner s = new Scanner(System.in);
            System.out.println("Uneti naziv mesta: ");
            String Naziv = s.nextLine();
            System.out.println("Uneti postanski broj: ");
            String PB = s.nextLine();
            
            Call<String> req = service.kreirajMesto(Naziv, PB);
//            int resut = req.execute().code();
            String result = req.execute().body();
            System.out.println(result);
        } catch (IOException ex) {
            Logger.getLogger(KreirajMesto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String message() {
        return "01. Kreiranje mesta.";
    }
    
}
