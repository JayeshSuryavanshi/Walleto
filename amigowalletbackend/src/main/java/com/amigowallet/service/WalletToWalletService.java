package com.amigowallet.service;



public interface WalletToWalletService {
	
	

	public Integer transferToWallet(Integer userId,Double amountToTransfer,String emailIdToTransfer) throws Exception;

}
