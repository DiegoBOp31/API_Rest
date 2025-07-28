package med.voll.api.controller;

import med.voll.api.medico.DatosRegistroMedico;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *Esta clase es un controlador REST, maneja peticiones HTTP y
 * devuelve respuestas en formato JSON
 */
@RestController
//Ruta base para todas las peticiones relacionadas con médicos
@RequestMapping("/medicos")
public class MedicoController {

    //Maneja peticiones HTTP POST para registrar un nuevo recurso (por ejemplo, un médico)
    @PostMapping
    //RequestBody Indica que los datos del cuerpo de la petición HTTP se deben mapear a este objeto Java
    public void registrar(@RequestBody DatosRegistroMedico json){
        System.out.println(json);
    }
}
