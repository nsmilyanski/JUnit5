package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Speciality;
import guru.springframework.sfgpetclinic.repositories.SpecialtyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SpecialitySDJpaServiceTest {

    @Mock
    SpecialtyRepository specialtyRepository;

    @InjectMocks
    SpecialitySDJpaService service;

    @Test
    void deleteBySpecialty() {
        Speciality speciality = new Speciality();

        //when
        service.delete(speciality);

        //then
        then(specialtyRepository).should( times(1)).delete(any(Speciality.class));
    }

    @Test
    void findByIdBddTest() {
        Speciality speciality = new Speciality();

        given(specialtyRepository.findById(1L)).willReturn(Optional.of(speciality));


        Optional<Speciality> foundedById = specialtyRepository.findById(1l);

        assertNotNull(foundedById);

//        verify(specialtyRepository, times(1)).findById(1L);

        then(specialtyRepository).should(timeout(100).times(1)).findById(anyLong());
    }

    @Test
    void deleteById() {
        service.deleteById(1L);
    }

    @Test
    void deleteByIdVerifyTwoTimes() {

        //when
        service.deleteById(1L);
        service.deleteById(1L);

        //then
        then(specialtyRepository).should(times(2)).deleteById(1L);
    }

    @Test
    void deleteByIdAtLeastOne() {

        //when
        service.deleteById(1L);
        service.deleteById(1L);

        //then
        then(specialtyRepository).should(atLeastOnce()).deleteById(1L);
    }

    @Test
    void deleteByIdMost() {

        //when
        service.deleteById(1L);
        service.deleteById(1L);

        //then
        then(specialtyRepository).should(atMost(5)).deleteById(1L);
    }

    @Test
    void deleteByIdNever() {

        //when
        service.deleteById(1L);
        service.deleteById(1L);

        //then
        then(specialtyRepository).should(never()).deleteById(5L);
    }

    @Test
    void deleteTest() {
        //when
        service.delete(new Speciality());

        //then
        then(specialtyRepository).should(timeout(100).times(1)).delete(any(Speciality.class));
    }


    @Test
    void testDoThrow() {
        doThrow(new RuntimeException("boom")).when(specialtyRepository).delete(any());

        assertThrows(RuntimeException.class, () -> specialtyRepository.delete(new Speciality()));

        verify(specialtyRepository).delete(any());
    }

    @Test
    void testFindByIDThrows() {
        given(specialtyRepository.findById(1L)).willThrow(new RuntimeException("boom"));

        assertThrows(RuntimeException.class, () -> service.findById(1L));

        then(specialtyRepository).should(timeout(100)).findById(1L);
    }

    @Test
    void testDeleteBDD() {
        willThrow(new RuntimeException("boom")).given(specialtyRepository).delete(any());

        assertThrows(RuntimeException.class, () -> specialtyRepository.delete(new Speciality()));

        then(specialtyRepository).should(timeout(100)).delete(any());
    }

    @Test
    void testlLambda() {
        final String match = "MATCH";
        Speciality speciality = new Speciality();
        speciality.setDescription(match);

        Speciality savedSpecialty = new Speciality();
        savedSpecialty.setId(1L);

        when(specialtyRepository.save(argThat(argument -> argument.getDescription().equals(match))))
                .thenReturn(savedSpecialty);

        Speciality save = service.save(speciality);


        assertEquals(Long.valueOf(1L), save.getId());
    }

}