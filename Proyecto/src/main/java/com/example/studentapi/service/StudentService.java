package com.example.studentapi.service;

import com.example.studentapi.dto.StudentDTO;
import com.example.studentapi.exception.BadRequestException;
import com.example.studentapi.exception.ResourceNotFoundException;
import com.example.studentapi.model.Student;
import com.example.studentapi.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository repo;
    public StudentService(StudentRepository repo){ this.repo = repo; }

    public List<Student> findAll(){ return repo.findAll(); }

    public Student findById(Long id){
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado"));
    }

    public Student create(StudentDTO dto){

        Optional<Student> existing = repo.findByCorreo(dto.getCorreo());
        if(existing.isPresent()) throw new BadRequestException("Correo ya existe");
        Student s = new Student();
        s.setNombre(dto.getNombre());
        s.setCorreo(dto.getCorreo());
        s.setNumeroTelefono(dto.getNumeroTelefono());
        s.setIdioma(dto.getIdioma().toLowerCase());
        return repo.save(s);
    }

    public Student update(Long id, StudentDTO dto){
        Student s = findById(id);

        if(!s.getCorreo().equalsIgnoreCase(dto.getCorreo())){
            repo.findByCorreo(dto.getCorreo()).ifPresent(x -> { throw new BadRequestException("Correo ya existe");});
        }
        s.setNombre(dto.getNombre());
        s.setCorreo(dto.getCorreo());
        s.setNumeroTelefono(dto.getNumeroTelefono());
        s.setIdioma(dto.getIdioma().toLowerCase());
        return repo.save(s);
    }


    public Student patch(Long id, Map<String, Object> updates){
        Student s = findById(id);
        if(updates.containsKey("nombre")){
            s.setNombre((String) updates.get("nombre"));
        }
        if(updates.containsKey("correo")){
            String correo = (String) updates.get("correo");
            repo.findByCorreo(correo).ifPresent(ex -> {
                if(!ex.getId().equals(id)) throw new BadRequestException("Correo ya existe");
            });
            s.setCorreo(correo);
        }
        if(updates.containsKey("numero_telefono")){
            s.setNumeroTelefono((String) updates.get("numero_telefono"));
        }
        if(updates.containsKey("idioma")){
            s.setIdioma(((String) updates.get("idioma")).toLowerCase());
        }

        return repo.save(s);
    }

    public void delete(Long id){
        if(!repo.existsById(id)) throw new ResourceNotFoundException("Estudiante no encontrado");
        repo.deleteById(id);
    }
}
