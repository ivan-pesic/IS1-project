/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.clientapplication;

import services.*;
/**
 *
 * @author Ivan
 */
public interface MyRequest {
    public void execute(MyService service);
    public String message();
}
