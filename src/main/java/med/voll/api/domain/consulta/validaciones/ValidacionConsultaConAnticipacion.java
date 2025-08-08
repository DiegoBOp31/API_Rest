package med.voll.api.domain.consulta.validaciones;

import med.voll.api.domain.ValidacionException;
import med.voll.api.domain.consulta.DatosReservaConsulta;

import java.time.Duration;
import java.time.LocalDateTime;

public class ValidacionConsultaConAnticipacion {

    public void validar(DatosReservaConsulta datos){
        // Obtiene la fecha y hora de la consulta desde el objeto "datos"
        var fechaConsulta = datos.fecha();
        // Obtiene la fecha y hora actual del sistema
        var ahora = LocalDateTime.now();
        // Calcula la diferencia en minutos entre las variables "ahora" y "fechaConsulta"
        var diferenciaEnMinutos = Duration.between(ahora, fechaConsulta).toMinutes();

        // Si la diferencia es menor a 30 minutos lanza la excepción
        if(diferenciaEnMinutos<30){
            throw new ValidacionException("Necesitas por lo menos 30 minutos de anticipación para agendar tu cita");
        }
    }
}
