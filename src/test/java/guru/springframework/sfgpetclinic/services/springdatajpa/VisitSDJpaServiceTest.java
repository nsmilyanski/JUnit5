package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.Speciality;
import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.repositories.VisitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VisitSDJpaServiceTest {

    @Mock
    VisitRepository visitRepository;

    @InjectMocks
    VisitSDJpaService service;

    @Test
    void testFindAll() {
        Visit visit1 = new Visit();
        visit1.setId(1L);
        Visit visit = new Visit();
        visit.setId(1L);

        Set<Visit> set = new HashSet<>();
        set.add(visit1);
        set.add(visit);

        when(visitRepository.findAll()).thenReturn(set);

        Set<Visit> all = service.findAll();

        assertNotNull(all);

        assertEquals(2, all.size());
    }

    @Test
    void findById() {
        Visit visit = new Visit();

        when(visitRepository.findById(1L)).thenReturn(Optional.of(visit));


        Optional<Visit> foundedById = visitRepository.findById(1l);

        assertNotNull(foundedById);

        verify(visitRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdIsNull() {
        Visit byId = service.findById(1L);

        assertNull(byId);
    }

    @Test
    void save() {
        Visit visit = new Visit();
        visit.setId(1L);

        when(visitRepository.save(visit)).thenReturn(visit);

        Visit save = service.save(visit);

        assertNotNull(save);

        assertEquals(Long.valueOf(1L), save.getId());

        verify(visitRepository, times(1)).save(any(Visit.class));

    }

    @Test
    void delete() {
        Visit visit = new Visit();
        visit.setId(1L);

        service.delete(visit);

        verify(visitRepository, times(1)).delete(any(Visit.class));
    }

    @Test
    void deleteById() {
        service.deleteById(1L);

        verify(visitRepository, times(1)).deleteById(1L);
    }
}