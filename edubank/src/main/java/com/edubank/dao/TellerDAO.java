package com.edubank.dao;

import com.edubank.model.Teller;

/**
 * This Interface contains the method responsible for interacting the database with
 * respect to Login module like getLoginOfTeller
 * 
 * @author ETA_JAVA
 *
 */
public interface TellerDAO {

	/**
	 * This method is used to get a Teller model corresponding to its
	 * loginName<br>
	 * 
	 * @param loginName
	 * 
	 * @return teller
	 */
	public Teller getLoginForTeller(String loginName) throws Exception;

}
