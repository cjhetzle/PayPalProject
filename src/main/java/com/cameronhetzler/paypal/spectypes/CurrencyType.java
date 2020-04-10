package com.cameronhetzler.paypal.spectypes;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import com.cameronhetzler.paypal.exceptions.ServicesException;
import com.google.gson.reflect.TypeToken;
import com.paypal.api.payments.Currency;

/**
 * 
 * @author Cameron Hetzler
 *
 */
public class CurrencyType extends BaseType<Currency> {
	
	public CurrencyType() {
		super(new Currency());
	}

	public Type getType() {
		// TODO Auto-generated method stub
		return new TypeToken<List<Currency>>(){}.getType();
	}

	public Class<Currency> getSpecType() {
		// TODO Auto-generated method stub
		return Currency.class;
	}

	public List<Currency> create(String jsonFile) throws ServicesException {
		// TODO Auto-generated method stub
		try {
			return load(jsonFile, getType());
		} catch (Exception e) {
			return Arrays.asList( load(jsonFile, getSpecType()) );
		}
	}

}
