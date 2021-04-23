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

import com.amigowallet.model.User;
import com.amigowallet.service.RewardPointsService;

/**
 * This class has methods to handle requests related to reward points.
 * 
 *  @author ETA_JAVA
 * 
 */
@RestController
@CrossOrigin
@RequestMapping("RewardPointsAPI")
public class RewardPointsAPI {
	
	/**
	 * This attribute is used for getting property values from
	 * <b>configuration.properties</b> file
	 */
	@Autowired
	private Environment environment;
	
	@Autowired
	RewardPointsService rewardPointsService;
	
	static Logger logger = LogManager.getLogger(RewardPointsAPI.class.getName());
	
	/**
	 * This method receives the user model in POST request and calls
	 * RewardPointsService method to redeem the Reward points. <br>
	 * 
	 * @param user
	 * 
	 * @return ResponseEntity<User> populated with 
	 * 					successMessage,
	 * 								if successfully deleted
	 * 					errorMessage,
	 * 								if any error occurs
	 */
	@RequestMapping(value = "redeemRewardPoints", method = RequestMethod.POST)
	public ResponseEntity<User> redeemRewardPoints(@RequestBody User user) {
		Integer userId=user.getUserId();
		ResponseEntity<User> responseEntity = null;
		try {
			logger.info("USER TRYING TO REDEEM REWARD POINTS, USER ID : "+user.getUserId());
			
			rewardPointsService.redeemRewardPoints(userId);
			
			logger.info("ALL REWARD POINTS REDEEMED SUCCESSFULLY, USER ID : "+user.getUserId());
			
			/*
			 * The following code populates a user model with a success message
			 */
			user.setSuccessMessage(environment.getProperty("RewardPointsAPI.REWARD_POINTS_REDEEMED_SUCCESS"));
			responseEntity = new ResponseEntity<User>(user, HttpStatus.OK);
		} catch (Exception e) {
			
			throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, environment.getProperty(e.getMessage()));
		}
		
		return responseEntity;
	}

}
