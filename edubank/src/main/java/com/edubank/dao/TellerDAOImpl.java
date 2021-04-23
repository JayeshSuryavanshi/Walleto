package com.edubank.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.edubank.entity.TellerEntity;
import com.edubank.model.Teller;

/**
 * This Interface contains the method responsible for interacting the database
 * with respect to Login module like getLoginOfTeller
 * 
 * @author ETA_JAVA
 *
 */
@Repository("tellerDAO")
public class TellerDAOImpl implements TellerDAO {

	@PersistenceContext
	EntityManager entityManager;

	/**
	 * This method is used to get a Teller model corresponding to its loginName<br>
	 * 
	 * @param loginName
	 * 
	 * @return teller
	 */
	@Override
	public Teller getLoginForTeller(String loginName) throws Exception {

		loginName = loginName.toUpperCase();
		Teller teller = null;

		Query query = entityManager.createQuery("from TellerEntity where loginName=:loginName");
		query.setParameter("loginName", loginName);

		List<TellerEntity> tellerEntities = query.getResultList();

		/*
		 * here if we get the entity then we are setting the entity values to bean class
		 */
		if (!tellerEntities.isEmpty()) {
			TellerEntity tellerEntity = tellerEntities.get(0);
			teller = new Teller();
			teller.setTellerId(tellerEntity.getTellerId());
			teller.setPassword(tellerEntity.getPassword());
			teller.setLoginName(tellerEntity.getLoginName());
		}

		return teller;
	}

}
