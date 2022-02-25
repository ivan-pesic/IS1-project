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
import retrofit2.Call;
import services.MyService;

/**
 *
 * @author Ivan
 */
public class KreirajKomitenta implements MyRequest {

    @Override
    public void execute(MyService service) {
        try {
            Scanner s = new Scanner(System.in);
            System.out.println("Uneti naziv komitenta: ");
            String Naziv = s.nextLine();
            System.out.println("Uneti adresu komitenta: ");
            String Adresa = s.nextLine();
            System.out.println("Uneti IdM komitenta: ");
            int IdM = Integer.parseInt(s.nextLine());
            
            Call<String> req = service.kreirajKomitenta(Naziv, Adresa, IdM);
            String result = req.execute().body();
            System.out.println(result);
        } catch (IOException ex) {
            Logger.getLogger(KreirajMesto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String message() {
        return "03. Kreiranje komitenta.";
    }
    
}

