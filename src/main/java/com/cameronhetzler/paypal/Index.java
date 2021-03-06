package com.cameronhetzler.paypal;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.BasicConfigurator;
import org.jasypt.util.text.BasicTextEncryptor;

import com.cameronhetzler.paypal.flows.AddBillingInfo;
import com.cameronhetzler.paypal.flows.CreateSupportedService;
import com.cameronhetzler.paypal.flows.ApplicationFlow;
import com.cameronhetzler.paypal.flows.CancelServiceInvoices;
import com.cameronhetzler.paypal.flows.CancelSingleServiceInvoice;
import com.cameronhetzler.paypal.flows.CreateServiceProductFromTemplates;
import com.cameronhetzler.paypal.flows.CreateServiceSubscriptionPlanFromTemplates;
import com.cameronhetzler.paypal.flows.SendServiceInvoices;
import com.cameronhetzler.paypal.flows.SendServiceInvoicesFromTemplates;
import com.cameronhetzler.paypal.flows.SendServiceSubscriptionsFromTemplates;
import com.cameronhetzler.paypal.flows.SendSingleServiceInvoice;
import com.cameronhetzler.paypal.payload.Classifications;
import com.cameronhetzler.paypal.payload.Payload;
import com.cameronhetzler.paypal.result.Result;
import com.cameronhetzler.paypal.common.Constants;
import com.cameronhetzler.paypal.common.Services;

/**
 * 
 * @author Cameron Hetzler
 *
 */
public class Index {

	public static void main(String[] args) {

		if (args.length < 4) {
			System.out.println("Please make sure to supply args. Exiting...");
			System.exit(-1);
		}

		String classification = null;
		String clientID = null;
		String clientSecret = null;
		String environment = null;

		try {
			classification = args[0].strip();
			clientID = args[1].strip();
			clientSecret = args[2].strip();
			environment = args[3].strip();
		} catch (Exception e) {
			System.out.println("Please make sure to supply valid args. Exiting...");
			System.exit(-1);
		}

		BasicConfigurator.configure();

		// Build a payload
		Payload request = new Payload(classification);
		Map<String, Object> table = new HashMap<String, Object>();

		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword("this-is-not-your-normal-password");

		table.put(Constants.CLASSIFICATION, classification);
		table.put(Constants.CLIENT_ID, textEncryptor.encrypt(clientID));
		table.put(Constants.CLIENT_SECRET, textEncryptor.encrypt(clientSecret));
		table.put(Constants.ENVIRONMENT, environment);
		table.put(Constants.SERVICE, Services.NETFLIX.toString());

		request.setTable(table);

		System.out.println(handleRequest(request).toString());
	}

	public static Result handleRequest(Payload request) {
		Result result = null;

		ApplicationFlow flow = null;
		Classifications classification = null;
		try {
			Object obj = request.getTable().get(Constants.CLASSIFICATION);
			String str = (String) obj;
			classification = Classifications.valueOf(str);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		switch (classification) {
		case EXAMPLE:
			flow = new SendServiceInvoicesFromTemplates();
			break;
		case ADD_BILLINGINFO:
			flow = new AddBillingInfo();
			break;
		case ADD_SERVICE_ITEM:
			flow = new CreateSupportedService();
			break;
		case SEND_SERVICE_INVOICES:
			flow = new SendServiceInvoices();
			break;
		case SEND_SINGLE_SERVICE_INVOICE:
			flow = new SendSingleServiceInvoice();
			break;
		case CANCEL_SERVICE_INVOICES:
			flow = new CancelServiceInvoices();
			break;
		case CANCEL_SINGLE_SERVICE_INVOICE:
			flow = new CancelSingleServiceInvoice();
			break;
		case SEND_SERVICE_INVOICES_FROM_TEMPLATES:
			flow = new SendServiceInvoicesFromTemplates();
			break;
		case SEND_SERVICE_SUBSCRIPTIONS_FROM_TEMPLATES:
			flow = new SendServiceSubscriptionsFromTemplates();
			break;
		case CREATE_SERVICE_PRODUCT_FROM_TEMPLATES:
			flow = new CreateServiceProductFromTemplates();
			break;
		case CREATE_SERVICE_SUBSCRIPTION_PLAN_FROM_TEMPLATES:
			flow = new CreateServiceSubscriptionPlanFromTemplates();
			break;
		case CREATE_SUPPORTED_SERVICE:
			flow = new CreateSupportedService();
			break;
		default:

		}
		result = flow.configureAndBuildRequest(request);

		return result;
	}

}
