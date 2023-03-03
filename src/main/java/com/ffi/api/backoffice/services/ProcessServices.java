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

    public void insertSupplier(Map<String, String> balancing) {
        dao.insertSupplier(balancing);
    }
    ////////////////////done

    ///////////////new method from dona 28-02-2023////////////////////////////
    public void updateSupplier(Map<String, String> balancing) {
        dao.updateSupplier(balancing);
    }
    ///////////////////////done
        ///////////////new method from dona 03-03-2023////////////////////////////
        public void insertItemSupplier(Map<String, String> balancing) {
        dao.insertItemSupplier(balancing);
    }

    public void updateItemSupplier(Map<String, String> balancing) {
        dao.updateItemSupplier(balancing);
    }

        ///////////////////////done
}
