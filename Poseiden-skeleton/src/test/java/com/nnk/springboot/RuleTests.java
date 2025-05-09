package com.nnk.springboot;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RuleTests {

	@Autowired
	private RuleNameRepository ruleNameRepository;

	private Validator validator;

	@Before
	public void setUpValidator() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}

	@Test
	public void ruleTest() {
		// Création
		RuleName rule = new RuleName();
		rule.setName("Rule Name");
		rule.setDescription("Description");
		rule.setJson("Json");
		rule.setTemplate("Template");
		rule.setSqlStr("SQL");
		rule.setSqlPart("SQL Part");

		// Validation avant enregistrement
		validate(rule);

		// Save
		rule = ruleNameRepository.save(rule);
		assert rule.getId() != null : "L'ID ne doit pas être null après sauvegarde";

		// Validation après sauvegarde
		validate(rule);

		// Update
		rule.setName("Rule Name Update");
		rule = ruleNameRepository.save(rule);
		validate(rule);
		assert "Rule Name Update".equals(rule.getName()) : "Le nom n'a pas été mis à jour";

		// Find
		List<RuleName> listResult = ruleNameRepository.findAll();
		assert !listResult.isEmpty() : "La liste ne doit pas être vide";

		// Delete
		Integer id = rule.getId();
		ruleNameRepository.delete(rule);
		Optional<RuleName> ruleList = ruleNameRepository.findById(id);
		assert ruleList.isEmpty() : "L'entité devrait avoir été supprimée";
	}

	private void validate(RuleName rule) {
		Set<ConstraintViolation<RuleName>> violations = validator.validate(rule);
		assert violations.isEmpty() : "Erreurs de validation : " + violations;
	}
}
