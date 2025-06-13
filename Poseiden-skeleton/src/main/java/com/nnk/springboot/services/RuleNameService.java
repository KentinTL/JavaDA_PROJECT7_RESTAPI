package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleNameService {

    @Autowired
    private RuleNameRepository ruleNameRepository;

    public RuleName save(RuleName ruleName) {
        return ruleNameRepository.save(ruleName);
    }

    public RuleName update(Integer id, RuleName newRuleName) {
        RuleName existing = ruleNameRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No RuleName with id: " + id));

        existing.setName(newRuleName.getName());
        existing.setDescription(newRuleName.getDescription());
        existing.setJson(newRuleName.getJson());
        existing.setTemplate(newRuleName.getTemplate());
        existing.setSqlStr(newRuleName.getSqlStr());
        existing.setSqlPart(newRuleName.getSqlPart());

        return ruleNameRepository.save(existing);
    }

    public void delete(Integer id) {
        RuleName ruleName = ruleNameRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No RuleName with id: " + id));
        ruleNameRepository.delete(ruleName);
    }

    public RuleName findById(Integer id) {
        return ruleNameRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No RuleName with id: " + id));
    }

    public List<RuleName> findAll() {
        return ruleNameRepository.findAll();
    }
}
