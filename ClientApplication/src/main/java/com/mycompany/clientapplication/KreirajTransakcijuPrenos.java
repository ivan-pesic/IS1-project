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
public class KreirajTransakcijuPrenos implements MyRequest {

    @Override
    public void execute(MyService service) {
        try {
            Scanner s = new Scanner(System.in);
            System.out.println("Uneti IdR1: ");
            int IdR1 = Integer.parseInt(s.nextLine());
            System.out.println("Uneti IdR2: ");
            int IdR2 = Integer.parseInt(s.nextLine());
            System.out.println("Uneti iznos: ");
            double Iznos = Double.parseDouble(s.nextLine());
            System.out.println("Uneti svrhu: ");
            String Svrha = s.nextLine();
            
            Call<String> req = service.prenosSaNa(IdR1, IdR2, Iznos, Svrha);
            String result = req.execute().body();
            System.out.println(result);
        } catch (IOException ex) {
            Logger.getLogger(KreirajTransakcijuPrenos.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public String message() {
        return "07. Kreiranje transakcije koja je prenos sume sa jednog računa na drugi račun";
    }
    
}
