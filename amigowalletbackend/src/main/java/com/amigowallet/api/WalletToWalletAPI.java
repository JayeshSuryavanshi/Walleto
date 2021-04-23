package com.amigowallet.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;







import org.springframework.web.server.ResponseStatusException;


import com.amigowallet.service.WalletToWalletService;


@CrossOrigin
@RestController
@RequestMapping("WalletToWalletAPI")
public class WalletToWalletAPI {
	@Autowired
	private Environment environment;
	
	@Autowired
	private WalletToWalletService walletTransferService;  
	
	static Logger logger = LogManager.getLogger(WalletToWalletAPI.class.getName());
	
	/**
	 * This method receives the user model in POST request and calls
	 * WalletToWalletService method to transfer the userID,senderEmail and amount to transfer.
	 * If verification fails then it sends failure message to the client.
	 * 
	 * @param userId,amount,emailIdToTransfer
	 * 
	 * @return ResponseEntity<String> 
	 * It is populated with an error message,if any error occurs
	 */
	@RequestMapping(value="transfertowallet", method = RequestMethod.POST)
	public ResponseEntity<String> transferToWallet(@RequestBody Object[] data){
		try{
			
			logger.info("USER TRYING TO TRANSFER FROM WALLET TO WALLET, USER ID : "+(Integer)data[0]+" TO EMAILID:"+(String)data[2]);
			Integer userId=(Integer)data[0];
			
			Double amount=Double.parseDouble(""+data[1]);
			
			String emailIdToTransfer=(String)data[2];
			walletTransferService.transferToWallet(userId,amount.doubleValue(),emailIdToTransfer);
			
			return new ResponseEntity<String>(environment.getProperty("WalletAPI.SUCCESSFUL_TRANSACTION"),HttpStatus.OK);
			
		}catch(Exception e){
			
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,environment.getProperty(e.getMessage()));
			
		}
		
		
		
	}
		
}
		

	
	


