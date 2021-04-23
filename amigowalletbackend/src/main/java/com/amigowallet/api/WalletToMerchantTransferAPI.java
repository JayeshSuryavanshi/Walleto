package com.amigowallet.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.amigowallet.service.BillPaymentService;

@CrossOrigin
@RestController
@RequestMapping("WalletToMerchantTransferAPI")
public class WalletToMerchantTransferAPI {
	@Autowired
	private Environment environment;

	@Autowired
	BillPaymentService billPaymentService;

	static Logger logger = LogManager.getLogger(WalletToMerchantTransferAPI.class.getName());

	@RequestMapping(value = "serviceType", method = RequestMethod.GET)
	public ResponseEntity<List<String>> displayServiceType() {
		ResponseEntity<List<String>> responseEntity = null;
		List<String> list = new ArrayList<String>();
		list = billPaymentService.displayServiceType();
		responseEntity = new ResponseEntity<List<String>>(list, HttpStatus.OK);
		return responseEntity;

	}

	@RequestMapping(value = "merchantType", method = RequestMethod.POST)
	public ResponseEntity<List<String>> displayMerchantName(@RequestBody String name) {
		ResponseEntity<List<String>> responseEntity = null;
		List<String> list = new ArrayList<String>();
		list = billPaymentService.displayMerchantName(name);
		responseEntity = new ResponseEntity<List<String>>(list, HttpStatus.OK);
		return responseEntity;
	}

	@RequestMapping(value = "payBill/{amount:.+}/{userId}", method = RequestMethod.POST)
	public ResponseEntity<String> payBill(@PathVariable Integer userId, @PathVariable Double amount,
			@RequestBody String name) {
		try {
			ResponseEntity<String> responseEntity = null;
			Integer reward = billPaymentService.payBill(userId, amount, name);

			logger.info("USER TRYING TO MAKE PAYMENT TO MERCHANT, USER ID : " + userId + ", MERCHANT : " + name);
			logger.info("PAYMENT DONE TO MERCHANT, USER ID : " + userId + ", MERCHANT : " + name);
			String message = environment.getProperty("WalletToMerchantTransferAPI.SUCCESSFUL_TRANSACTION1") + " "
					+ reward + " " + environment.getProperty("WalletToMerchantTransferAPI.SUCCESSFUL_TRANSACTION2");

			responseEntity = new ResponseEntity<String>(message, HttpStatus.OK);
			return responseEntity;

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, environment.getProperty(e.getMessage()));

		}
	}

}
