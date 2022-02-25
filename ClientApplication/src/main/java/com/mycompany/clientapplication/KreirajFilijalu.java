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
public class KreirajFilijalu implements MyRequest {

    @Override
    public void execute(MyService service) {
        try {
            Scanner s = new Scanner(System.in);
            System.out.println("Uneti naziv filijale: ");
            String Naziv = s.nextLine();
            System.out.println("Uneti adresu filijale: ");
            String Adresa = s.nextLine();
            System.out.println("Uneti IdM filijale: ");
            int IdM = Integer.parseInt(s.nextLine());
            
            Call<String> req = service.kreirajFilijalu(Naziv, Adresa, IdM);
            String result = req.execute().body();
            System.out.println(result);
        } catch (IOException ex) {
            Logger.getLogger(KreirajFilijalu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public String message() {
        return "02. Kreiranje filijale u mestu.";
    }
    
}
