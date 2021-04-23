package com.edubank.controller;

import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.edubank.model.SecurityQuestion;
import com.edubank.model.Teller;
import com.edubank.service.CustomerService;
import com.edubank.service.SecurityQuestionService;
import com.edubank.service.TellerService;

/**
 * This controller class has methods to handle requests related to teller login, logout.
 * 
 * @author ETA_JAVA
 *
 */
@Controller
public class TellerLoginController {

	@Autowired
	private Environment environment;

	@Autowired
	TellerService loginService;

	@Autowired
	CustomerService customerService;
	
	@Autowired
	SecurityQuestionService securityQuestionService;
	
	static Logger logger = LogManager.getLogger(TellerLoginController.class);
	
	/**
	 * This method is used to generate the login view for teller
	 * 
	 * @return ModelAndView
	 * 
	 */
	@RequestMapping(value = { "/tellerLogin" })
	public ModelAndView login() {
		ModelAndView model = new ModelAndView("tellerLogin");
		return model;
	}

	/**
	 * This method is used to authenticate the teller login credentials, if the
	 * credential are proper then it sets the teller Id into session and
	 * redirect to teller's home page else show proper error message on the
	 * current view Note: It can accept only post request
	 * 
	 * @param tellerLogin
	 * @param httpSession
	 * 
	 * @return ModelAndView object redirecting to error page if any error
	 *         occurs,<br>
	 *         ModelAndView object redirecting to login page if authentication
	 *         fails,<br>
	 *         ModelAndView object redirecting to home page if authentication
	 *         success. <br>
	 * 
	 */
	@RequestMapping(value = { "/authenticateTeller" }, method = RequestMethod.POST)
	public ModelAndView authenticateTeller(@ModelAttribute Teller teller,
			HttpSession httpSession) {

		ModelAndView model = new ModelAndView("tellerError");
		
		logger.info("TELLER LOGIN ATTEMPT. LOGIN NAME: "+teller.getLoginName());

		try {
			Teller tellerFromService = loginService
					.authenticateTellerLogin(teller);

			/*
			 * Binds an object to this session, using the name specified. If an
			 * object of the same name is already bound to the session, the
			 * object is replaced.
			 * 
			 * After this method executes, and if the new object implements
			 * HttpSessionBindingListener, the container calls
			 * HttpSessionBindingListener.valueBound. The container then
			 * notifies any HttpSessionAttributeListeners in the web
			 * application.
			 * 
			 * If an object was already bound to this session of this name that
			 * implements HttpSessionBindingListener, its
			 * HttpSessionBindingListener.valueUnbound method is called.
			 * 
			 * If the value passed in is null, this has the same effect as
			 * calling removeAttribute().
			 */

			httpSession.setAttribute("tellerId",
					tellerFromService.getTellerId());
			Long noOfCustomers = customerService.getNoOfCustomers();

			/*
			 * model is the data which we are retrieving from the session and
			 * view is returned to the client side to display the values
			 */
			model = new ModelAndView("tellerHome");
			
			
			List<SecurityQuestion> securityQuestions = securityQuestionService.getAllSecurityQuestions();
			httpSession.setAttribute("securityQuestions", securityQuestions);
			
			/*
			 * here we are setting the values to model object which will be sent
			 * to client
			 */
			model.addObject("name", tellerFromService.getLoginName());
			model.addObject("noOfCustomers", noOfCustomers);
			logger.info("TELLER LOGIN CREDENTIALS VALIDATION SUCCESS. LOGIN NAME: "+teller.getLoginName());
		} catch (Exception e) {
			if (e.getMessage().contains("TellerService")
					|| e.getMessage().contains("TellerValidator")) {
				model = new ModelAndView("tellerLogin");
				model.addObject("loginName", teller.getLoginName());
			}

			model.addObject("message", environment.getProperty(e.getMessage()));
		}
		return model;
	}

	/**
	 * This method is used to clear the session data and redirects to the login
	 * page with a success message.
	 * 
	 * @param httpSession
	 * 
	 * @return ModelAndView
	 *
	 */
	@RequestMapping("tellerLogout")
	public ModelAndView logout(HttpSession httpSession) {
		
		logger.info("TELLER LOGOUT ATTEMPT. TELLER ID: "+httpSession.getAttribute("tellerId"));
		
		/*
		 * model is the data which we are retrieving from the session and
		 * view is returned to the client side to display the values
		 */
		ModelAndView model = new ModelAndView("tellerLogin");

		/*
		 * Returns an Enumeration of String objects containing the names of all
		 * the objects bound to this session.
		 */
		Enumeration<String> attributes = httpSession.getAttributeNames();

		/* here we are removing all the data from the session */
		while (attributes.hasMoreElements())
			httpSession.removeAttribute(attributes.nextElement()
					);

		logger.info("TELLER LOGOUT SUCCESS. ALL SESSION DETAILS CREARED. TELLER ID: "+httpSession.getAttribute("tellerId"));
		
		/*
		 * here we are setting the values to model object which will be sent
		 * to client
		 */
		model.addObject("successMessage",
				environment.getProperty("LoginController.LOGOUT_SUCCESS"));
		return model;
	}
	
	@RequestMapping("tellerHome")
	public ModelAndView tellerHome(HttpSession httpSession) {
		ModelAndView model = new ModelAndView("tellerError");
		if(httpSession.getAttribute("tellerId")!=null)
		{
			try {
				model = new ModelAndView("tellerHome");
				
				/*
				 * populating the list of security questions to be displayed on the page
				 */
				//populateSecurityQuestions(model);
			}
			catch (Exception e) {
				if (e.getMessage().contains("TellerService")
						|| e.getMessage().contains("TellerValidator")) {
					model = new ModelAndView("tellerLogin");
				}

				model.addObject("message", environment.getProperty(e.getMessage()));
			}
		}
		return model;
	}
}
