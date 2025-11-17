package com.example.studentapi.repository;

import com.example.studentapi.model.Student;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Getter
@Setter
@ToString
@Repository
public class StudentRepository {
    private final ConcurrentHashMap<Long, Student> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(1);

    public List<Student> findAll() { return new ArrayList<>(storage.values()); }
    public Optional<Student> findById(Long id) { return Optional.ofNullable(storage.get(id)); }
    public Optional<Student> findByCorreo(String correo) {
        return storage.values().stream()
                .filter(s -> s.getCorreo().equalsIgnoreCase(correo))
                .findFirst();
    }
    public Student save(Student s) {
        if (s.getId() == null) {
            s.setId(idGen.getAndIncrement());
        }
        storage.put(s.getId(), s);
        return s;
    }
    public void deleteById(Long id) { storage.remove(id); }
    public boolean existsById(Long id) { return storage.containsKey(id); }
}
