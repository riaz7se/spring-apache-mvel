package com.iz.apache;

import com.iz.model.PriceClass;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TestService {

    public PriceClass testService() {
        PriceClass priceClass = new PriceClass();
        priceClass.setStatus("testCharged");
        return priceClass;
    }
}
