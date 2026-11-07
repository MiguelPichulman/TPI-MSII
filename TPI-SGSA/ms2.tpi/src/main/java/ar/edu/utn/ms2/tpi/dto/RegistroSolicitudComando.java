package ar.edu.utn.ms2.tpi.dto;

//---- REFACT 1: INTRODUCE PARAMETER OBJECT ----//
// Se crea esta clase para encapsular los 6 parametros primitivos que viajaban juntos
// en el registro, resolviendo el smell Long Parameter List y Primitive Obsession.
public class RegistroSolicitudComando {
    private String legajo;
    private String nombre;
    private String email;
    private String tipologia;
    private String descripcion;
    private int urgenciaSolicitada;

    public RegistroSolicitudComando(String legajo, String nombre, String email,
                                    String tipologia, String descripcion, int urgenciaSolicitada) {
        this.legajo = legajo;
        this.nombre = nombre;
        this.email = email;
        this.tipologia = tipologia;
        this.descripcion = descripcion;
        this.urgenciaSolicitada = urgenciaSolicitada;
    }

    public String getLegajo() {
        return legajo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getTipologia() {
        return tipologia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getUrgenciaSolicitada() {
        return urgenciaSolicitada;
    }
}