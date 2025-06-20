package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.CurvePointService;
import com.nnk.springboot.services.UserInfos;
import com.nnk.springboot.domain.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CurveControllerTest {

    @InjectMocks
    private CurveController curveController;

    @Mock
    private CurvePointService curvePointService;

    @Mock
    private UserInfos userInfos;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Mock SecurityContext pour userInfos si besoin
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testuser");
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Setup MockMvc standalone
        mockMvc = MockMvcBuilders.standaloneSetup(curveController).build();

        // Mock userInfos.getUserInfos() pour retourner un User avec username
        User mockUser = new User();
        mockUser.setUsername("testuser");
        when(userInfos.getUserInfos()).thenReturn(mockUser);
    }

    @Test
    public void testHome_ShouldReturnCurvePointListView() throws Exception {
        CurvePoint cp1 = new CurvePoint();
        CurvePoint cp2 = new CurvePoint();
        when(curvePointService.findAll()).thenReturn(List.of(cp1, cp2));

        mockMvc.perform(get("/curvePoint/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/list"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("curvePoints"));

        verify(userInfos, times(1)).getUserInfos();
        verify(curvePointService, times(1)).findAll();
    }

    @Test
    public void testAddCurvePointForm_ShouldReturnAddView() throws Exception {
        mockMvc.perform(get("/curvePoint/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"))
                .andExpect(model().attributeExists("curvePoint"));
    }

    @Test
    public void testValidate_WithValidCurvePoint_ShouldRedirectToList() throws Exception {
        when(curvePointService.save(any(CurvePoint.class))).thenReturn(new CurvePoint());

        mockMvc.perform(post("/curvePoint/validate")
                        .param("curveId", "1")
                        .param("term", "10")
                        .param("value", "20"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        verify(curvePointService, times(1)).save(any(CurvePoint.class));
    }

    @Test
    public void testValidate_WithInvalidCurvePoint_ShouldReturnAddView() throws Exception {
        mockMvc.perform(post("/curvePoint/validate")
                        .param("term", "")  // invalid: empty term
                        .param("value", "20"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"));
    }

    @Test
    public void testShowUpdateForm_ShouldReturnUpdateView() throws Exception {
        CurvePoint cp = new CurvePoint();
        cp.setId(1);
        when(curvePointService.findById(1)).thenReturn(cp);

        mockMvc.perform(get("/curvePoint/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"))
                .andExpect(model().attributeExists("curvePoint"));

        verify(curvePointService, times(1)).findById(1);
    }

    @Test
    public void testUpdateCurvePoint_WithValidData_ShouldRedirectToList() throws Exception {
        when(curvePointService.save(any(CurvePoint.class))).thenReturn(new CurvePoint());

        mockMvc.perform(post("/curvePoint/update/1")
                        .param("curveId", "1")
                        .param("term", "15")
                        .param("value", "30"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        verify(curvePointService, times(1)).update(eq(1), any(CurvePoint.class));
    }

    @Test
    public void testUpdateCurvePoint_WithInvalidData_ShouldReturnUpdateView() throws Exception {
        mockMvc.perform(post("/curvePoint/update/1")
                        .param("term", "")  // invalid term
                        .param("value", "30"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"));
    }

    @Test
    public void testDeleteCurvePoint_ShouldRedirectToList() throws Exception {
        doNothing().when(curvePointService).delete(1);

        mockMvc.perform(get("/curvePoint/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));

        verify(curvePointService, times(1)).delete(1);
    }

    @Test
    public void testDeleteCurvePoint_WhenException_ShouldRedirectToListWithError() throws Exception {
        doThrow(new IllegalArgumentException("Error deleting")).when(curvePointService).delete(1);

        mockMvc.perform(get("/curvePoint/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/curvePoint/list"));
        // Not testing model error here because redirect

        verify(curvePointService, times(1)).delete(1);
    }
}
