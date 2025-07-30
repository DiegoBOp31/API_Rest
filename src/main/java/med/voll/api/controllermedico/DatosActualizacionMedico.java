package med.voll.api.controllermedico;

import med.voll.api.direccion.DatosDireccion;

public record DatosActualizacionMedico(
        Long id,
        String nombre,
        String telefono,
        DatosDireccion direccion
) {
}
