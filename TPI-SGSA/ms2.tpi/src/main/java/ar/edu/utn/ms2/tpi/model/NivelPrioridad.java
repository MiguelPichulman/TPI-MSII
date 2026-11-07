package ar.edu.utn.ms2.tpi.model;

//---- REFACT 4: ELIMINACION DE PRIMITIVE OBSESSION ----//
// Sustituye los enteros y cadenas sueltas por un Enum fuertemente tipado
// que garantiza valores validos (BAJA, MEDIA, ALTA) en todo el sistema.
public enum NivelPrioridad {
    BAJA(1),
    MEDIA(2),
    ALTA(3);

    private final int valorNumerico;

    NivelPrioridad(int valorNumerico) {
        this.valorNumerico = valorNumerico;
    }

    public int getValorNumerico() {
        return valorNumerico;
    }
}