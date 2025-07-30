package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.controllermedico.DatosActualizacionMedico;
import med.voll.api.medico.DatosListaMedico;
import med.voll.api.medico.DatosRegistroMedico;
import med.voll.api.medico.Medico;
import med.voll.api.medico.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *Esta clase es un controlador REST, maneja peticiones HTTP y
 * devuelve respuestas en formato JSON
 */
@RestController
//Ruta base para todas las peticiones relacionadas con médicos
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository repository;

    //Transactional lo usamos porque vamos a estar registrando datos en una base de datos
    @Transactional//Estamos usando el transactional de spring boot
    //Maneja peticiones HTTP POST para registrar un nuevo recurso (por ejemplo, un médico)
    @PostMapping
    //RequestBody Indica que los datos del cuerpo de la petición HTTP se deben mapear a este objeto Java
    public void registrar(@RequestBody @Valid DatosRegistroMedico datos){
        repository.save(new Medico(datos));
    }

    @GetMapping
    /**
     * El argumento que le pasamos de Pageable tiene que ser de la interfaz springframework.data
     * al igual que el regreso de datos de tipo Page
     */
    /**
     * El PageableDefault es para poner los argumentos de paginación por defecto desde nuestra aplicación
     * y no dejar que lo tenga que escribir el usuario en la url. En este caso le decimos que las
     * páginas serán de 10 elementos y estarán ordenadas alfabéticamente por el nombre
     */
    public Page<DatosListaMedico> listar(@PageableDefault(size=10,sort={"nombre"}) Pageable paginacion){
        /**
         * Esta línea obtiene todos los objetos "Medico" guardados en la base de datos a través del repositorio.
         * Luego convierte esa lista a un "Stream" para poder trabajar con ella.
         * Usamos .map(DatosListaMedico::new) para transformar cada objeto "Medico" en un objeto "DatosListaMedico".
         * El operador ::new llama al constructor del record que recibe un Medico y extrae los datos necesarios.
         * Finalmente, .toList() convierte el Stream nuevamente en una lista (List<DatosListaMedico>) que será devuelta.
         * Esto se hace para no devolver directamente los objetos "Medico", sino solo los datos necesarios
         * en una forma más segura y ordenada.
         */
        return repository.findAll(paginacion).map(DatosListaMedico::new);
    }

    @Transactional
    @PutMapping
    public void actualizar(@RequestBody @Valid DatosActualizacionMedico datos){
        var medico = repository.getReferenceById(datos.id());
        medico.actualizarInformaciones(datos);
    }





}
