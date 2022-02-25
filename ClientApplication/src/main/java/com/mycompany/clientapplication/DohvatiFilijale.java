/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.clientapplication;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import retrofit2.Call;
import retrofit2.Response;
import services.MyService;

/**
 *
 * @author Ivan
 */
public class DohvatiFilijale  implements MyRequest {

    @Override
    public void execute(MyService service) {
        try {
            Call<String> req = service.dohvatiFilijale();
            Response<String> execute = req.execute();
            
            System.out.println(execute.body());
            
        } catch (IOException ex) {
            Logger.getLogger(DohvatiMesta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String message() {
        return "11. Dohvatanje filijala.";
    }
    
}
