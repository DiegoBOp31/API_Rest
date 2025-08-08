package med.voll.api.domain.consulta.validaciones;

import med.voll.api.domain.ValidacionException;
import med.voll.api.domain.consulta.DatosReservaConsulta;
import med.voll.api.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarPacienteActivo implements ValidadorDeConsultas{

    @Autowired
    private PacienteRepository repository;

    public void validar(DatosReservaConsulta datos){
        // Consulta en la base de datos si el paciente con el ID proporcionado está activo
        var pacienteEstaActivo = repository.findActivoById(datos.idPaciente());
        // Si el paciente no está activo, se lanza una excepción para impedir la reserva
        if(!pacienteEstaActivo){
            throw new ValidacionException("No se puede agendar una consulta con un paciente inactivo");
        }
    }
}
