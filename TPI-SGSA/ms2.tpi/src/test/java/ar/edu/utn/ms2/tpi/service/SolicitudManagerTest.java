package ar.edu.utn.ms2.tpi.service;

import ar.edu.utn.ms2.tpi.dto.RegistroSolicitudComando;
import ar.edu.utn.ms2.tpi.exception.TransicionEstadoInvalidaException;
import ar.edu.utn.ms2.tpi.exception.ValidacionSolicitudException;
import ar.edu.utn.ms2.tpi.mock.NotificadorMock;
import ar.edu.utn.ms2.tpi.model.Area;
import ar.edu.utn.ms2.tpi.model.NivelPrioridad;
import ar.edu.utn.ms2.tpi.model.Solicitud;
import ar.edu.utn.ms2.tpi.service.SolicitudManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SolicitudManagerTest {

    private SolicitudManager manager;
    private NotificadorMock notificadorMock;

    @BeforeEach
    public void setUp() {
        // Arrange comun: Aislamiento completo con el doble de prueba
        notificadorMock = new NotificadorMock();
        manager = new SolicitudManager(notificadorMock);
    }

    // =========================================================================
    // PRUEBAS DE REGISTRO Y REGLAS DE ESTRATEGIA (STRATEGY + OCP)
    // =========================================================================

    @Test
    @DisplayName("Debe registrar solicitud de CERTIFICADO asignando ALUMNOS y prioridad BAJA")
    public void testRegistrarSolicitudCertificado() {
        // Arrange
        RegistroSolicitudComando cmd = new RegistroSolicitudComando(
                "7723", "Lucia Fernandez", "lfernandez@alumnos.utn.edu.ar",
                "CERTIFICADO", "Certificado de alumno regular para tramite de pasaje", 1);

        // Act
        Solicitud sol = manager.registrarSolicitud(cmd);

        // Assert (Patron AAA)
        assertNotNull(sol, "La solicitud creada no debe ser nula");
        assertEquals(1, sol.id, "El primer id debe ser 1");
        assertEquals("ALUMNOS", sol.areaAsignada.codigo);
        assertEquals(NivelPrioridad.BAJA, sol.prioridad);
        assertEquals("PENDIENTE", sol.estado.getCodigo());

        // Verificacion sobre el Mock (Aislamiento y auditoria de llamadas)
        assertEquals(1, notificadorMock.getLlamadas(), "Debe emitirse exactamente 1 notificacion");
        assertEquals("lfernandez@alumnos.utn.edu.ar", notificadorMock.getUltimoDestinatario());
        assertEquals(NivelPrioridad.BAJA, notificadorMock.getUltimaPrioridad());
    }

    @Test
    @DisplayName("Debe registrar solicitud de EQUIVALENCIA urgente con prioridad ALTA y Secretaria Academica")
    public void testRegistrarSolicitudEquivalenciaUrgente() {
        // Arrange
        RegistroSolicitudComando cmd = new RegistroSolicitudComando(
                "9841", "Martin Gomez", "mgomez@alumnos.utn.edu.ar",
                "EQUIVALENCIA", "Equivalencia completa de asignaturas afines", 2);

        // Act
        Solicitud sol = manager.registrarSolicitud(cmd);

        // Assert
        assertEquals("ACADEMICA", sol.areaAsignada.codigo);
        assertEquals(NivelPrioridad.ALTA, sol.prioridad);
        assertEquals(1, notificadorMock.getLlamadas());
    }

    @Test
    @DisplayName("Debe registrar solicitud con tipologia desconocida usando la estrategia general fallback")
    public void testRegistrarSolicitudTipologiaDesconocidaFallback() {
        // Arrange
        RegistroSolicitudComando cmd = new RegistroSolicitudComando(
                "5511", "Carlos Sanchez", "csanchez@alumnos.utn.edu.ar",
                "PASANTIA_EXTERNA", "Tramite especial no catalogado de pasantia", 1);

        // Act
        Solicitud sol = manager.registrarSolicitud(cmd);

        // Assert
        assertEquals("MESA_GENERAL", sol.areaAsignada.codigo);
        assertEquals(NivelPrioridad.BAJA, sol.prioridad);
    }

    // =========================================================================
    // PRUEBAS DE CASOS DE BORDE Y VALIDACIONES (EXTRACT CLASS & METHOD)
    // =========================================================================

    @Test
    @DisplayName("Debe lanzar ValidacionSolicitudException si el legajo tiene menos de 4 caracteres")
    public void testValidacionLegajoCorto() {
        // Arrange
        RegistroSolicitudComando cmdInvalido = new RegistroSolicitudComando(
                "12", "Estudiante Prueba", "correo@alumnos.utn.edu.ar",
                "CERTIFICADO", "Descripcion suficientemente larga para pasar", 1);

        // Act & Assert
        ValidacionSolicitudException ex = assertThrows(ValidacionSolicitudException.class, () -> {
            manager.registrarSolicitud(cmdInvalido);
        });
        assertTrue(ex.getMessage().contains("Legajo invalido"));
        assertEquals(0, notificadorMock.getLlamadas(), "No debe notificarse si falla la validacion");
    }

    @Test
    @DisplayName("Debe lanzar ValidacionSolicitudException si el correo electronico no es valido")
    public void testValidacionEmailInvalido() {
        // Arrange
        RegistroSolicitudComando cmdInvalido = new RegistroSolicitudComando(
                "1234", "Estudiante Prueba", "correo_sin_arroba.com",
                "CERTIFICADO", "Descripcion suficientemente larga para pasar", 1);

        // Act & Assert
        assertThrows(ValidacionSolicitudException.class, () -> {
            manager.registrarSolicitud(cmdInvalido);
        });
    }

    @Test
    @DisplayName("Debe lanzar ValidacionSolicitudException si la descripcion tiene menos de 10 caracteres")
    public void testValidacionDescripcionInsuficiente() {
        // Arrange
        RegistroSolicitudComando cmdInvalido = new RegistroSolicitudComando(
                "1234", "Estudiante Prueba", "valido@alumnos.utn.edu.ar",
                "CERTIFICADO", "Corta", 1);

        // Act & Assert
        assertThrows(ValidacionSolicitudException.class, () -> {
            manager.registrarSolicitud(cmdInvalido);
        });
    }

    // =========================================================================
    // PRUEBAS DE CICLO DE VIDA Y TRANSICION (PATRON STATE)
    // =========================================================================

    @Test
    @DisplayName("Debe permitir transicion valida: PENDIENTE -> EN_PROCESO -> APROBADA")
    public void testCicloDeVidaExitosoAprobada() {
        // Arrange
        RegistroSolicitudComando cmd = new RegistroSolicitudComando(
                "9841", "Martin Gomez", "mgomez@alumnos.utn.edu.ar",
                "EQUIVALENCIA", "Solicitud formal de equivalencia academica", 1);
        Solicitud sol = manager.registrarSolicitud(cmd);

        // Act 1: Pasar a proceso
        boolean okProceso = manager.actualizarEstado(sol.id, "EN_PROCESO", "Evaluando antecedentes");
        assertTrue(okProceso);
        assertEquals("EN_PROCESO", sol.estado.getCodigo());

        // Act 2: Aprobar
        boolean okAprobada = manager.actualizarEstado(sol.id, "APROBADA", "Equivalencia 100% otorgada");
        assertTrue(okAprobada);
        assertEquals("APROBADA", sol.estado.getCodigo());
        assertEquals("Equivalencia 100% otorgada", sol.resolucionNotas);
    }

    @Test
    @DisplayName("Debe rechazar transicion ilegal de PENDIENTE directo a APROBADA")
    public void testTransicionIlegalPendienteHaciaAprobada() {
        // Arrange
        RegistroSolicitudComando cmd = new RegistroSolicitudComando(
                "7723", "Lucia Fernandez", "lfernandez@alumnos.utn.edu.ar",
                "CERTIFICADO", "Solicitud para probar salto indebido de estado", 1);
        Solicitud sol = manager.registrarSolicitud(cmd);

        // Act: Intento directo hacia APROBADA sin pasar por EN_PROCESO
        boolean resultado = manager.actualizarEstado(sol.id, "APROBADA", "Salto directo");

        // Assert: El manager captura la TransicionEstadoInvalidaException y retorna false
        assertFalse(resultado, "No debe permitir aprobar directamente si esta PENDIENTE");
        assertEquals("PENDIENTE", sol.estado.getCodigo(), "El estado debe permanecer en PENDIENTE");
    }

    // =========================================================================
    // PRUEBAS DE OPERACIONES DE REASIGNACION Y CONSULTA
    // =========================================================================

    @Test
    @DisplayName("Debe reasignar el Area correctamente recibiendo el Parameter Object estructurado")
    public void testReasignarArea() {
        // Arrange
        RegistroSolicitudComando cmd = new RegistroSolicitudComando(
                "6512", "Esteban Quito", "equito@alumnos.utn.edu.ar",
                "REINSCRIPCION", "Reincorporacion regular por cese laboral", 1);
        Solicitud sol = manager.registrarSolicitud(cmd);

        Area areaDespacho = new Area("DESPACHO", "Despacho General", "despacho@frd.utn.edu.ar");

        // Act
        boolean reasignado = manager.reasignarArea(sol.id, areaDespacho);

        // Assert
        assertTrue(reasignado);
        assertEquals("DESPACHO", sol.areaAsignada.codigo);
        assertEquals("Despacho General", sol.areaAsignada.nombre);
    }

    @Test
    @DisplayName("Debe filtrar solicitudes por estado y area correctamente")
    public void testObtenerSolicitudesPorEstadoYArea() {
        // Arrange
        manager.registrarSolicitud(new RegistroSolicitudComando(
                "1001", "Alumno Uno", "uno@alumnos.utn.edu.ar", "CERTIFICADO", "Constancia regular 1", 1));
        manager.registrarSolicitud(new RegistroSolicitudComando(
                "1002", "Alumno Dos", "dos@alumnos.utn.edu.ar", "CERTIFICADO", "Constancia regular 2", 1));
        Solicitud sol3 = manager.registrarSolicitud(new RegistroSolicitudComando(
                "1003", "Alumno Tres", "tres@alumnos.utn.edu.ar", "EQUIVALENCIA", "Pedido de pase de sede", 1));

        manager.actualizarEstado(sol3.id, "EN_PROCESO", "En curso");

        // Act
        List<Solicitud> pendientesAlumnos = manager.obtenerSolicitudesPorEstadoYArea("PENDIENTE", "ALUMNOS");
        List<Solicitud> enProcesoAcademica = manager.obtenerSolicitudesPorEstadoYArea("EN_PROCESO", "ACADEMICA");

        // Assert
        assertEquals(2, pendientesAlumnos.size(), "Deben haber 2 solicitudes de Alumnos pendientes");
        assertEquals(1, enProcesoAcademica.size(), "Debe haber 1 solicitud en proceso en Academica");
    }
}