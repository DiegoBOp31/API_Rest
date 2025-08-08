package med.voll.api.domain.consulta.validaciones;

import med.voll.api.domain.ValidacionException;
import med.voll.api.domain.consulta.DatosReservaConsulta;
import med.voll.api.domain.medico.MedicoRepository;

public class ValidarMedicoActivo {

    private MedicoRepository repository;

    public void validar(DatosReservaConsulta datos){
        // Si no se especifica un médico (idMedico es null), no se realiza ninguna validación y se sale del método
        if(datos.idMedico()==null){
            return;
        }
        // Consulta en el repositorio si el médico con el ID dado está activo
        var medicoEstaActivo = repository.findActivoById(datos.idMedico());

        // Si el médico no está activo, lanza una excepción de validación
        if(!medicoEstaActivo){
            throw new ValidacionException("No puedes agendar una consulta con un médico inactivo");
        }
    }
}
