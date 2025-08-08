package med.voll.api.domain.consulta.validaciones;

import med.voll.api.domain.ValidacionException;
import med.voll.api.domain.consulta.ConsultaRepository;
import med.voll.api.domain.consulta.DatosReservaConsulta;

public class ValidarMedicoConOtraConsultaEnElMismoHorario {

    private ConsultaRepository repository;

    public void validar(DatosReservaConsulta datos){

        /**
         * Llama al repositorio para verificar si existe una consulta en la misma fecha y hora
         * para el médico especificado. Devuelve true si ya hay una consulta.
         */
        var medicoTieneOtraConsultaEnElMismoHorario = repository.existsByMedicoIdAndFecha(datos.idMedico(),datos.fecha());

        if(medicoTieneOtraConsultaEnElMismoHorario){
            throw new ValidacionException("El médico ya tiene una consulta en esa misma fecha y hora");
        }
    }
}
