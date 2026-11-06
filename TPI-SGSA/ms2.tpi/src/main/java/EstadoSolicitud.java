public class EstadoSolicitud {
    public String codigo; // "PENDIENTE", "EN_PROCESO", "APROBADA", "RECHAZADA"
    public String descripcion;

    public EstadoSolicitud(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }
}