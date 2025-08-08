package med.voll.api.domain.consulta.validaciones;

import med.voll.api.domain.ValidacionException;
import med.voll.api.domain.consulta.DatosReservaConsulta;

import java.time.DayOfWeek;

public class ValidacionFueraHorarioConsultas {

    public void validar(DatosReservaConsulta datos){
        // Obtener la fecha y hora de la cita desde los datos de la reserva
        var fechaConsulta = datos.fecha();
        // Verificar si la cita está programada en domingo
        var domingo = fechaConsulta.getDayOfWeek().equals(DayOfWeek.SUNDAY);
        // Verificar si la cita es antes de que abra la clínica (antes de las 7:00 AM)
        var horarioAntesDeAperturaClinica = fechaConsulta.getHour() < 7;
        // Verificar si la cita es después de que cierre la clínica (después de las 6:00 PM)
        var horarioDespuesDeCierreClinica = fechaConsulta.getHour() > 18;

        // Si la cita es en domingo o fuera del horario laboral, lanzar una excepción
        if(domingo || horarioAntesDeAperturaClinica || horarioDespuesDeCierreClinica){
            throw new ValidacionException("Horario seleccionado fuera del horario de atención de la clinica.");
        }

    }
}
