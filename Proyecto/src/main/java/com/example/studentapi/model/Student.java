package com.example.studentapi.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Student {
    private Long id;
    private String nombre;
    private String correo;
    private String numeroTelefono;
    private String idioma;
}
