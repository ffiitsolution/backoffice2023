/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ffi.api.backoffice.services;

import com.ffi.api.backoffice.dao.ProcessDao;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author IT
 */
@Service
public class ProcessServices {

    @Autowired
    ProcessDao dao;
    ///////////////new method from dona 27-02-2023////////////////////////////

    public void insertSupplier(Map<String, String> balance) {
        dao.insertSupplier(balance);
    }
    ////////////////////done

    ///////////////new method from dona 28-02-2023////////////////////////////
    public void updateSupplier(Map<String, String> balance) {
        dao.updateSupplier(balance);
    }
    ///////////////////////done
    ///////////////new method from dona 03-03-2023////////////////////////////
    public void insertItemSupplier(Map<String, String> balance) {
        dao.insertItemSupplier(balance);
    }

    public void updateItemSupplier(Map<String, String> balance) {
        dao.updateItemSupplier(balance);
    }

    ///////////////////////done
    ///////////////new method from dona 06-03-2023////////////////////////////
    public void updateMpcs(Map<String, String> balance) {
        dao.updateMpcs(balance);
    }
    ///////////////////////done
}
