package ar.edu.utn.ms2.tpi.service;

import ar.edu.utn.ms2.tpi.dto.ClasificacionResultado;
import ar.edu.utn.ms2.tpi.dto.RegistroSolicitudComando;
import ar.edu.utn.ms2.tpi.exception.TransicionEstadoInvalidaException;
import ar.edu.utn.ms2.tpi.model.Area;
import ar.edu.utn.ms2.tpi.model.Estudiante;
import ar.edu.utn.ms2.tpi.model.Solicitud;
import ar.edu.utn.ms2.tpi.notification.INotificador;
import ar.edu.utn.ms2.tpi.state.EstadoPendiente;
import ar.edu.utn.ms2.tpi.strategy.*;
import ar.edu.utn.ms2.tpi.validation.SolicitudValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//---- REFACT 1, 2, 3 Y 4 CONSOLIDADOS EN LA CLASE COORDINADORA ----//
public class SolicitudManager {

    private List<Solicitud> dbSolicitudes = new ArrayList<>();
    private int contadorId = 1;

    //---- REFACT 4: INVERSION DE DEPENDENCIAS (DIP) ----//
    // Dependencia de la abstraccion ar.edu.utn.ms2.tpi.notification.INotificador inyectada por constructor
    private final INotificador notificador;

    //---- REFACT 2: EXTRACT CLASS ----//
    // Delegacion de la validacion en clase especializada (SRP)
    private final SolicitudValidator validador;

    //---- REFACT 4: PATRON STRATEGY (MAPA DE ESTRATEGIAS) ----//
    // Registro abierto a extension y cerrado a modificacion (OCP)
    private final Map<String, ClasificadorSolicitudStrategy> estrategias = new HashMap<>();

    public SolicitudManager(INotificador notificador) {
        this.notificador = notificador;
        this.validador = new SolicitudValidator();
        inicializarEstrategias();
    }

    private void inicializarEstrategias() {
        estrategias.put("CERTIFICADO", new ClasificadorCertificadoStrategy());
        estrategias.put("EQUIVALENCIA", new ClasificadorEquivalenciaStrategy());
        estrategias.put("REINSCRIPCION", new ClasificadorReinscripcionStrategy());
    }

    //---- REFACT 1: INTRODUCE PARAMETER OBJECT ----//
    // Recibe un unico objeto ar.edu.utn.ms2.tpi.dto.RegistroSolicitudComando en lugar de 6 primitivos sueltos
    public Solicitud registrarSolicitud(RegistroSolicitudComando comando) {

        //---- REFACT 2: EXTRACT METHOD & VALIDATOR ----//
        // Centraliza chequeos arrojando excepciones descriptivas ante errores
        validador.validar(comando);

        Solicitud sol = new Solicitud();
        sol.id = contadorId++;
        sol.estudiante = new Estudiante(comando.getLegajo(), comando.getNombre(), comando.getEmail());
        sol.tipologia = comando.getTipologia();
        sol.descripcion = comando.getDescripcion();
        sol.fechaCreacion = LocalDate.now();

        //---- REFACT 3: PATRON STATE ----//
        // Se inicializa con el estado concreto inicial
        sol.setEstado(new EstadoPendiente());
        sol.resolucionNotas = "";

        //---- REFACT 4: PATRON STRATEGY ----//
        // Seleccion polimorfica que erradica el if-else encadenado
        ClasificadorSolicitudStrategy estrategia = estrategias.getOrDefault(
                comando.getTipologia().toUpperCase(),
                new ClasificadorGeneralStrategy()
        );
        ClasificacionResultado clasificacion = estrategia.clasificar(comando.getUrgenciaSolicitada());

        sol.areaAsignada = clasificacion.getAreaAsignada();
        sol.prioridad = clasificacion.getPrioridad();

        dbSolicitudes.add(sol);

        //---- REFACT 4: DIP & ADAPTER ----//
        // Notificacion a traves de la abstraccion sin silenciar fallos
        notificador.notificar(comando.getEmail(), "ar.edu.utn.ms2.tpi.model.Solicitud creada #" + sol.id, sol.prioridad);

        return sol;
    }

    //---- REFACT 3: MOVE METHOD Y PATRON STATE ----//
    // El manager no evalua strings ni muta el estado; delega en la ar.edu.utn.ms2.tpi.model.Solicitud
    public boolean actualizarEstado(int idSolicitud, String codigoNuevoEstado, String observacion) {
        Solicitud encontrada = null;
        for (Solicitud s : dbSolicitudes) {
            if (s.id == idSolicitud) {
                encontrada = s;
                break;
            }
        }

        if (encontrada == null) {
            return false;
        }

        try {
            if (codigoNuevoEstado.equalsIgnoreCase("EN_PROCESO")) {
                encontrada.avanzarAEnProceso();
            } else if (codigoNuevoEstado.equalsIgnoreCase("APROBADA")) {
                encontrada.dictaminar(true, observacion);
            } else if (codigoNuevoEstado.equalsIgnoreCase("RECHAZADA")) {
                encontrada.dictaminar(false, observacion);
            } else {
                System.out.println("Estado desconocido: " + codigoNuevoEstado);
                return false;
            }
            return true;
        } catch (TransicionEstadoInvalidaException ex) {
            System.out.println("[REGLA DE ESTADO VIOLADA]: " + ex.getMessage());
            return false;
        }
    }

    //---- REFACT 1: INTRODUCE PARAMETER OBJECT ----//
    // Recibe la entidad ar.edu.utn.ms2.tpi.model.Area estructurada directamente en lugar de 3 Strings sueltos
    public boolean reasignarArea(int idSolicitud, Area nuevaArea) {
        for (Solicitud s : dbSolicitudes) {
            if (s.id == idSolicitud) {
                s.areaAsignada = nuevaArea;
                return true;
            }
        }
        return false;
    }

    public List<Solicitud> obtenerSolicitudesPorEstadoYArea(String codigoEstado, String codigoArea) {
        List<Solicitud> resultado = new ArrayList<>();
        for (Solicitud s : dbSolicitudes) {
            boolean coincideEstado = (codigoEstado == null || codigoEstado.isEmpty())
                    || s.estado.getCodigo().equalsIgnoreCase(codigoEstado);
            boolean coincideArea = (codigoArea == null || codigoArea.isEmpty())
                    || s.areaAsignada.codigo.equalsIgnoreCase(codigoArea);

            if (coincideEstado && coincideArea) {
                resultado.add(s);
            }
        }
        return resultado;
    }

    public List<Solicitud> getTodas() {
        return dbSolicitudes;
    }
}