package med.voll.api.domain.consulta.validaciones;

import med.voll.api.domain.ValidacionException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DatosReservaConsulta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarPacienteSinOtraConsultaEnElMismoDia implements ValidadorDeConsultas{

    @Autowired
    private ConsultaRepository repository;

    public void validar(DatosReservaConsulta datos){
        // Se define el primer horario del día para consultas: 7:00 AM
        var primerHorario = datos.fecha().withHour(7);
        // Se define el último horario del día para consultas: 6:00 PM
        var ultimoHorario = datos.fecha().withHour(18);

        /**
         * Se verifica si el paciente ya tiene una consulta programada en ese rango de horas
         * La consulta se busca por ID del paciente y entre el primer y último horario del día
         */
        var pacienteTieneOtraConsultaEnElDia = repository.existsByPacienteIdAndFechaBetween(
                datos.idPaciente(),
                primerHorario,
                ultimoHorario
        );

        // Si el paciente ya tiene otra consulta para ese día, se lanza una excepción de validación
        if(pacienteTieneOtraConsultaEnElDia){
            throw new ValidacionException("El paciente ya tiene una consulta reservada para ese día");
        }
    }
}
