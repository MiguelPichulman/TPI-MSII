package ar.edu.utn.ms2.tpi.model;

import ar.edu.utn.ms2.tpi.state.EstadoSolicitud;

import java.time.LocalDate;

//---- REFACT 3: MOVE METHOD & REFACTORIZACION DE DOMINIO ----//
// ar.edu.utn.ms2.tpi.model.Solicitud deja de ser una estructura pasiva y asume el control de sus
// transiciones mediante delegacion en el patron State.
public class Solicitud {
    public int id;
    public Estudiante estudiante;
    public String tipologia;
    public String descripcion;

    //---- REFACT 3: Estado polimorfico
    public EstadoSolicitud estado;

    public Area areaAsignada;

    //---- REFACT 4: Tipado fuerte con Enum
    public NivelPrioridad prioridad;

    public LocalDate fechaCreacion;
    public String resolucionNotas;

    //---- REFACT 3: MOVE METHOD ----//
    // La entidad delega la transicion a su estado actual
    public void avanzarAEnProceso() {
        this.estado.pasarAEnProceso(this);
    }

    //---- REFACT 3: MOVE METHOD ----//
    // La entidad delega el dictamen a su estado actual
    public void dictaminar(boolean aprobada, String observacion) {
        this.estado.dictaminar(this, aprobada, observacion);
    }

    public void setEstado(EstadoSolicitud nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public void setResolucionNotas(String notas) {
        this.resolucionNotas = notas;
    }
}