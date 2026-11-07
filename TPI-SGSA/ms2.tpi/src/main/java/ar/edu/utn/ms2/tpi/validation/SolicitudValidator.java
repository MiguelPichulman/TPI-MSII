package ar.edu.utn.ms2.tpi.validation;

import ar.edu.utn.ms2.tpi.dto.RegistroSolicitudComando;
import ar.edu.utn.ms2.tpi.exception.ValidacionSolicitudException;

//---- REFACT 2: EXTRACT CLASS & EXTRACT METHOD ----//
// Se extrae la logica de validacion fuera de ar.edu.utn.ms2.tpi.service.SolicitudManager hacia una clase con
// responsabilidad unica (SRP), dividida en metodos pequenos y atomicos (Clean Code).
public class SolicitudValidator {

    public void validar(RegistroSolicitudComando comando) {
        validarLegajo(comando.getLegajo());
        validarEmail(comando.getEmail());
        validarDescripcion(comando.getDescripcion());
    }

    //---- REFACT 2: EXTRACT METHOD ----//
    // Validacion atomica de legajo
    private void validarLegajo(String legajo) {
        if (legajo == null || legajo.trim().isEmpty() || legajo.length() < 4) {
            throw new ValidacionSolicitudException("Legajo invalido: debe contener al menos 4 caracteres.");
        }
    }

    //---- REFACT 2: EXTRACT METHOD ----//
    // Validacion atomica de formato basico de email
    private void validarEmail(String email) {
        if (email == null || !email.contains("@") || !email.contains(".")) {
            throw new ValidacionSolicitudException("Formato de correo electronico incorrecto.");
        }
    }

    //---- REFACT 2: EXTRACT METHOD ----//
    // Validacion atomica de longitud de descripcion
    private void validarDescripcion(String descripcion) {
        if (descripcion == null || descripcion.trim().length() < 10) {
            throw new ValidacionSolicitudException("La descripcion es insuficiente: debe tener al menos 10 caracteres.");
        }
    }
}