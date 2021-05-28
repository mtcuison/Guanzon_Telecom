package org.rmj.ggc.telecom.utils;

import java.math.BigDecimal;

public class CashCount {
    public BigDecimal calculate(float Bill, float quantity){
        float total = 0;
        try{
            total = Bill * quantity;
        } catch (Exception e){
            e.printStackTrace();
        }
        return BigDecimal.valueOf(total);
    }
}
