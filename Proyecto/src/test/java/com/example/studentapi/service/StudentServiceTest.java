package com.example.studentapi.service;

import com.example.studentapi.dto.StudentDTO;
import com.example.studentapi.exception.BadRequestException;
import com.example.studentapi.model.Student;
import com.example.studentapi.repository.StudentRepository;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class StudentServiceTest {

    private StudentRepository repo;
    private StudentService service;

    @Before
    public void setUp() {
        repo = mock(StudentRepository.class);
        service = new StudentService(repo);
    }

    @Test
    public void testCreateStudentSuccess() {
        StudentDTO dto = new StudentDTO();
        dto.setNombre("Juan");
        dto.setCorreo("juan@example.com");
        dto.setNumeroTelefono("1234567890");
        dto.setIdioma("español");

        when(repo.findByCorreo("juan@example.com")).thenReturn(Optional.empty());
        when(repo.save(any(Student.class))).thenAnswer(inv -> {
            Student s = inv.getArgument(0);
            s.setId(1L);
            return s;
        });

        Student result = service.create(dto);

        assertNotNull(result);
        assertEquals(Long.valueOf(1L), result.getId());
        assertEquals("juan@example.com", result.getCorreo());

        verify(repo, times(1)).save(any(Student.class));
    }

    @Test(expected = BadRequestException.class)
    public void testCreateStudentDuplicateEmail() {
        StudentDTO dto = new StudentDTO();
        dto.setNombre("Luis");
        dto.setCorreo("luis@example.com");
        dto.setNumeroTelefono("1234567890");
        dto.setIdioma("español");

        Student existing = new Student(2L, "Otro", "luis@example.com", "1234567890", "inglés");

        when(repo.findByCorreo("luis@example.com")).thenReturn(Optional.of(existing));

        service.create(dto);
    }

    @Test
    public void testFindByIdSuccess() {
        Student s = new Student(1L, "Ana", "ana@example.com", "1234567890", "francés");

        when(repo.findById(1L)).thenReturn(Optional.of(s));

        Student result = service.findById(1L);

        assertEquals("Ana", result.getNombre());
        assertEquals("ana@example.com", result.getCorreo());
    }

    @Test
    public void testUpdateStudent() {
        Student existing = new Student(1L, "Rodrigo", "rodrigo@example.com", "1234567890", "español");

        StudentDTO dto = new StudentDTO();
        dto.setNombre("Rodrigo Actualizado");
        dto.setCorreo("rodrigo_new@example.com");
        dto.setNumeroTelefono("0987654321");
        dto.setIdioma("inglés");

        when(repo.findById(1L)).thenReturn(Optional.of(existing));
        when(repo.findByCorreo("rodrigo_new@example.com")).thenReturn(Optional.empty());
        when(repo.save(any(Student.class))).thenAnswer(inv -> inv.getArgument(0));

        Student updated = service.update(1L, dto);

        assertEquals("Rodrigo Actualizado", updated.getNombre());
        assertEquals("rodrigo_new@example.com", updated.getCorreo());
        assertEquals("0987654321", updated.getNumeroTelefono());
        assertEquals("inglés", updated.getIdioma());
    }
}
