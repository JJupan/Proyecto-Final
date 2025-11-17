package com.example.studentapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {

    @NotBlank(message = "Nombre obligatorio")
    @Size(max = 255, message = "Nombre máximo 255 caracteres")
    private String nombre;

    @NotBlank(message = "Correo obligatorio")
    @Email(message = "Correo no válido")
    private String correo;

    @NotBlank(message = "Número de teléfono obligatorio")
    @Pattern(regexp = "\\d{10}", message = "Debe tener exactamente 10 dígitos")
    private String numeroTelefono;

    @NotBlank(message = "Idioma obligatorio")
    @Pattern(
            regexp = "inglés|español|francés",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Idioma debe ser: inglés, español o francés"
    )
    private String idioma;
}
