package com.edubank.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.edubank.model.Customer;
import com.edubank.model.SecurityQuestion;
import com.edubank.service.CustomerService;
import com.edubank.service.SecurityQuestionService;
import com.edubank.utility.ApplicationConstants;

@Controller
public class CustomerDetailsController {

	@Autowired
	private Environment environment;

	@Autowired
	SecurityQuestionService securityQuestionService;
	
	@Autowired
	CustomerService customerService;
	
	static Logger logger = LogManager.getLogger(AddMoneyController.class);

	/**
	 * This attribute is used to keep the number of rows displayed in pagination of
	 * Customers. <br>
	 * Value is fixed (final) in {@link ApplicationConstants} to 10 and it is taken
	 * here to display 10 rows at a time. As the value is fixed, it is made as an
	 * instance variable instead of keeping it as a local variable in methods in
	 * which pagination logic is written.
	 */
	private final Integer paginationRows = ApplicationConstants.CUSTOMER_PAGINATION_ROWS;

	@RequestMapping(value = { "/customerDetails" })
	public ModelAndView getCustomerDetails(HttpSession httpSession) {

		ModelAndView model = new ModelAndView("tellerError");
		if (httpSession.getAttribute("tellerId") != null) {
			try {
				Integer tellerId = (Integer) httpSession.getAttribute("tellerId");

				/*
				 * model is the data which we are retrieving from the session and view is
				 * returned to the client side to display the values
				 */
				model = new ModelAndView("customerDetails");

				List<SecurityQuestion> securityQuestions = securityQuestionService.getAllSecurityQuestions();
				httpSession.setAttribute("securityQuestions", securityQuestions);

				/*
				 * here we are setting the values to model object which will be sent to client
				 */
				model.addObject("name", tellerId);
				model = getAllCustomersOfTeller(model, httpSession);
				Integer paginationIndex1 = 0;
				Integer paginationIndex2 = paginationRows;
				model.addObject("paginationBeginIndex", paginationIndex1);
				model.addObject("paginationEndIndex", paginationIndex2);

			} catch (Exception e) {
				if (e.getMessage().contains("TellerService") || e.getMessage().contains("TellerValidator")) {
					model = new ModelAndView("customerDetails");
				}

				model.addObject("message", environment.getProperty(e.getMessage()));
			}
		}
		return model;

	}


	@RequestMapping(value = "/NextSetCustomers", method = RequestMethod.POST)
	public ModelAndView getNextSet(@RequestParam("indexFrom") Integer paginationIndex1,
			@RequestParam("indexTo") Integer paginationIndex2, HttpSession httpSession) {
		ModelAndView model = new ModelAndView("error");
		try {
				model = new ModelAndView("customerDetails");
				paginationIndex1 = paginationIndex1 + paginationRows;
				paginationIndex2 = paginationIndex2 + paginationRows;
				model.addObject("paginationBeginIndex", paginationIndex1);
				model.addObject("paginationEndIndex", paginationIndex2);

				model = getAllCustomersOfTeller(model, httpSession);
		} catch (Exception e) {
			model.addObject("style", "text-danger");
			model.addObject("message", environment.getProperty(e.getMessage()));
		}
		return model;

	}

	@RequestMapping(value = "/PreviousSetCustomers", method = RequestMethod.POST)
	public ModelAndView getPreviousSet(@RequestParam("indexFrom") Integer paginationIndex1,
			@RequestParam("indexTo") Integer paginationIndex2, HttpSession httpSession) {
		ModelAndView model = new ModelAndView("error");
		try {
			model = new ModelAndView("customerDetails");
			paginationIndex1 = ((paginationIndex1 - paginationRows) <= 0) ? 0 : paginationIndex1 - paginationRows;
			paginationIndex2 = ((paginationIndex2 - paginationRows) <= 0) ? 0 : paginationIndex2 - paginationRows;
			model.addObject("paginationBeginIndex", paginationIndex1);
			model.addObject("paginationEndIndex", paginationIndex2);
			model = getAllCustomersOfTeller(model, httpSession);

		} catch (Exception e) {
			model.addObject("style", "text-danger");
			model.addObject("message", environment.getProperty(e.getMessage()));
			model = getAllCustomersOfTeller(model, httpSession);
		}
		return model;

	}
	

	private ModelAndView getAllCustomersOfTeller(ModelAndView model, HttpSession httpSession) {

		try {
			if (httpSession.getAttribute("tellerId") != null) {
				Integer tellerId = (Integer) httpSession.getAttribute("tellerId");

				List<Customer> listOfCustomers;
				listOfCustomers = customerService.getAllCustomersOfTeller(tellerId);

				model.addObject("listOfCustomers", listOfCustomers);
			}
		} catch (Exception e) {
			model.addObject("style", "text-danger");
			model.addObject("message", environment.getProperty(e.getMessage()));
		}
		return model;
	}
	
}
