package com.edubank.utility;

//import org.apache.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * This class is to log exceptions which come while application is running.
 * 
 * @author ETA_JAVA
 *
 */
@Component
@Aspect
public class LoggingAspect {

	/**
	 * This method is to log any exception coming from any DAO class method
	 * and to re-throw a new exception with message "DAO.TECHNICAL_ERROR".
	 * <br>
	 * In the statement <i>@AfterThrowing(pointcut = "execution(* com.edubank.dao.*Impl.*(..))", throwing = "exception")</i>
	 * the point cut expression matches all the methods
	 * with any parameters of DAO classes.
	 * 
	 */
	@AfterThrowing(pointcut = "execution(* com.edubank.dao.*Impl.*(..))", throwing = "exception")
	public void logExceptionFromDAO(Exception exception) throws Exception {
		
		log(exception);
//		throw new Exception("DAO.TECHNICAL_ERROR");

	}

	/**
	 * This method is to log any exception coming from any service class method
	 * and to re-throw the same. <br>
	 * 
	 * In statement <i>@AfterThrowing(pointcut = "execution(* com.edubank.service.*Impl.*(..))", throwing = "exception")</i>
	 * the point cut expression matches all the 'Impl' methods with any parameters of service classes and validator classes.
	 * 
	 */
	@AfterThrowing(pointcut = "execution(* com.edubank.service.*Impl.*(..))", throwing = "exception")
	public void logExceptionFromService(Exception exception) throws Exception {
		if (exception.getMessage()!=null && (exception.getMessage().contains("Service") || 
											 exception.getMessage().contains("Validator"))) {
			log(exception);
		}
	}
	

	/**
	 * The following method accepts exception class object, 
	 * initiates the Logger using Log4j.xml file and logs the 
	 * Exception details in a log file present at following 
	 * location "Desktop/log/EDUBankLogger.log"
	 * 
	 * @param exception
	 */
	private void log(Exception exception) {
		
		Logger logger = LogManager.getLogger(this.getClass());
		logger.error(exception.getMessage(), exception);
	}

}
