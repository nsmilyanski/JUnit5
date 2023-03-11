package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.fauxspring.BindingResult;
import guru.springframework.sfgpetclinic.fauxspring.Model;
import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.services.OwnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    private static final String OWNERS_CREATE_OR_UPDATE_OWNER_FORM = "owners/createOrUpdateOwnerForm";
    private static final String REDIRECT_OWNERS_5 = "redirect:/owners/5";

    @Mock
    OwnerService ownerService;

    @Mock
    Model model;

    @InjectMocks
    OwnerController controller;

    @Mock
    BindingResult bindingResult;

    @Captor
    ArgumentCaptor<String> argumentCaptor;

    @BeforeEach
    void setup() {
        given(ownerService.findAllByLastNameLike(argumentCaptor.capture()))
                .willAnswer(invocation -> {
                    List<Owner> owners = new ArrayList<>();
                    String name = invocation.getArgument(0);

                    if (name.equals("%Buck%")) {
                        owners.add(new Owner(1l, "Joe", "Buck"));
                        return owners;
                    }else if (name.equals("%DontFindMe%")) {
                        owners.add(new Owner(1l, "Joe", "DontFindMe"));
                        return owners;
                    }else if (name.equals("%FindMe%")) {
                        owners.add(new Owner(1l, "Joe", "Buck"));
                        owners.add(new Owner(2l, "Joe2", "Buck2"));
                        return owners;
                    }

                    throw new RuntimeException("Name not found");
                });
    }

    @Test
    void processFindFormWildcardFound() {
        //given
        Owner owner = new Owner(1l, "Joe", "FindMe");

        InOrder inOrder = Mockito.inOrder(ownerService, model);

        //when
        String viewName = controller.processFindForm(owner, bindingResult, model);
        verifyNoMoreInteractions(ownerService);

        //then
        assertThat("%FindMe%").isEqualToIgnoringCase(argumentCaptor.getValue());
        assertThat("owners/ownersList").isEqualToIgnoringCase(viewName);

        verify(ownerService).findAllByLastNameLike(anyString());
        verify(model).addAttribute(anyString(), anyList());
        verifyNoMoreInteractions(model);
    }



    @Test()
    void processFindFormWildcardStringAnnotation() {
        //given
        Owner owner = new Owner(1l, "Joe", "Buck");

        //when
        String viewName = controller.processFindForm(owner, bindingResult, null);

        //then
        assertThat("%Buck%").isEqualToIgnoringCase(argumentCaptor.getValue());
        verifyZeroInteractions(model);
    }

    @Test
    void processFindFormWildcardNotFound() {
        //given
        Owner owner = new Owner(1l, "Joe", "DontFindMe");

        //when
        String viewName = controller.processFindForm(owner, bindingResult, null);

        //then
        assertThat("%DontFindMe%").isEqualToIgnoringCase(argumentCaptor.getValue());
        assertThat("redirect:/owners/1").isEqualToIgnoringCase(viewName);
        verifyZeroInteractions(model);
    }



//    @Test()
//    void processCreationFormHasErrors() {
//        //given
//        Owner owner = new Owner(1l, "Jim", "Bob");
//        given(bindingResult.hasErrors()).willReturn(true);
//
//        //when
//        String viewName = controller.processCreationForm(owner, bindingResult);
//
//        //then
//        assertThat(viewName).isEqualToIgnoringCase(OWNERS_CREATE_OR_UPDATE_OWNER_FORM);
//    }
//
//    @Test
//    void processCreationFormNoErrors() {
//        //given
//        Owner owner = new Owner(5l, "Jim", "Bob");
//        given(bindingResult.hasErrors()).willReturn(false);
//        given(ownerService.save(any())).willReturn(owner);
//
//        //when
//        String viewName = controller.processCreationForm(owner, bindingResult);
//
//        //then
//        assertThat(viewName).isEqualToIgnoringCase(REDIRECT_OWNERS_5);
//    }
}