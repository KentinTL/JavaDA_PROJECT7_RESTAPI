package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.RuleNameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RuleNameController.class)
@AutoConfigureMockMvc
public class RuleNameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RuleNameService ruleNameService;

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testGetRuleNameList_ShouldReturnListView() throws Exception {
        when(ruleNameService.findAll()).thenReturn(List.of(new RuleName(), new RuleName()));

        mockMvc.perform(get("/ruleName/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/list"))
                .andExpect(model().attributeExists("ruleNames"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testGetAddRuleNameForm_ShouldReturnAddView() throws Exception {
        mockMvc.perform(get("/ruleName/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testValidateRuleName_Valid_ShouldRedirect() throws Exception {
        mockMvc.perform(post("/ruleName/validate")
                        .with(csrf())
                        .param("name", "Rule1")
                        .param("description", "Description1")
                        .param("json", "jsonString")
                        .param("template", "templateString")
                        .param("sqlStr", "sqlString")
                        .param("sqlPart", "sqlPartString"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        verify(ruleNameService, times(1)).save(any(RuleName.class));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testValidateRuleName_Invalid_ShouldReturnAddView() throws Exception {
        mockMvc.perform(post("/ruleName/validate")
                        .with(csrf())  // CSRF token simulé
                        .param("name", "")
                        .param("description", "")
                        .param("json", "")
                        .param("template", "")
                        .param("sqlStr", "")
                        .param("sqlPart", ""))
                .andExpect(status().isOk())  // On attend 200 et non 302
                .andExpect(view().name("ruleName/add"))
                .andExpect(model().attributeHasErrors("ruleName"));

        verify(ruleNameService, never()).save(any(RuleName.class));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testShowUpdateForm_ShouldReturnUpdateView() throws Exception {
        RuleName ruleName = new RuleName();
        ruleName.setId(1);
        when(ruleNameService.findById(1)).thenReturn(ruleName);

        mockMvc.perform(get("/ruleName/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"))
                .andExpect(model().attributeExists("ruleName"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testUpdateRuleName_Valid_ShouldRedirect() throws Exception {
        mockMvc.perform(post("/ruleName/update/1")
                        .with(csrf())
                        .param("name", "UpdatedRule")
                        .param("description", "UpdatedDescription")
                        .param("json", "updatedJson")
                        .param("template", "updatedTemplate")
                        .param("sqlStr", "updatedSqlStr")
                        .param("sqlPart", "updatedSqlPart"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        verify(ruleNameService, times(1)).update(eq(1), any(RuleName.class));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testUpdateRuleName_Invalid_ShouldReturnUpdateView() throws Exception {
        mockMvc.perform(post("/ruleName/update/1")
                        .with(csrf())
                        .param("name", "")  // invalid case
                        .param("description", "")
                        .param("json", "")
                        .param("template", "")
                        .param("sqlStr", "")
                        .param("sqlPart", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"))
                .andExpect(model().attributeHasErrors("ruleName"));

        verify(ruleNameService, never()).update(anyInt(), any(RuleName.class));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testDeleteRuleName_ShouldRedirect() throws Exception {
        doNothing().when(ruleNameService).delete(1);

        mockMvc.perform(get("/ruleName/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        verify(ruleNameService, times(1)).delete(1);
    }
}
