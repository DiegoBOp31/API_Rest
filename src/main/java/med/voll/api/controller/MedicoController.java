package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.domain.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

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
    public ResponseEntity registrar(@RequestBody @Valid DatosRegistroMedico datos, UriComponentsBuilder uriComponentsBuilder){
        /**
         * Este método maneja la creación (registro) de un nuevo médico desde una solicitud HTTP POST.
         * Devuelve un ResponseEntity con el estado 201 Created, la URI del nuevo recurso y los datos del médico creado.
         */
        var medico = new Medico(datos); // Crea un nuevo objeto Medico usando los datos recibidos en el request.
        repository.save(medico); // Guarda el nuevo médico en la base de datos.
        /**
         * Se construye la URI del recurso recién creado para enviarla en la respuesta.
         * Esto es parte del estándar REST, que recomienda que cuando creas un recurso nuevo, devuelvas la ubicación donde se puede acceder.
         * uriComponentsBuilder permite construir rutas dinámicamente a partir de una plantilla, en este caso "/medicos/{id}".
         * buildAndExpand(medico.getId()) reemplaza {id} por el ID real del médico que acabamos de guardar.
         * toUri() convierte esa ruta en un objeto URI válido que se puede usar en la respuesta.
         */
        var uri = uriComponentsBuilder.path("/medicos/{id}")// Plantilla de la ruta para el nuevo recurso
                .buildAndExpand(medico.getId())// Reemplaza {id} por el ID real del médico
                .toUri();// Convierte la ruta en un objeto URI
        /**
         * Finalmente, construimos y devolvemos un ResponseEntity con:
         * - Código HTTP 201 (Created), que indica que un nuevo recurso fue creado exitosamente.
         * - La URI del nuevo recurso (para que el cliente sepa dónde encontrarlo).
         * - El cuerpo de la respuesta contiene un DTO con los detalles del médico creado
         * (para mostrar al cliente la información del recurso).
         */
        return ResponseEntity
                .created(uri)// Establece el código HTTP 201 Created y la cabecera "Location" con la URI del nuevo recurso
                .body(new DatosDetalleMedico(medico));// En el cuerpo se devuelven los datos del médico en un formato adecuado para el cliente
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
    public ResponseEntity<Page<DatosListaMedico>> listar(@PageableDefault(size=10,sort={"nombre"}) Pageable paginacion){
        /**
         * Esta línea obtiene todos los objetos "Medico" guardados en la base de datos a través del repositorio.
         * Luego convierte esa lista a un "Stream" para poder trabajar con ella.
         * Usamos .map(DatosListaMedico::new) para transformar cada objeto "Medico" en un objeto "DatosListaMedico".
         * El operador ::new llama al constructor del record que recibe un Medico y extrae los datos necesarios.
         * Finalmente, .toList() convierte el Stream nuevamente en una lista (List<DatosListaMedico>) que será devuelta.
         * Esto se hace para no devolver directamente los objetos "Medico", sino solo los datos necesarios
         * en una forma más segura y ordenada.
         */
        var page = repository.findAllByActivoTrue(paginacion).map(DatosListaMedico::new);
        /**
         * Devuelve una respuesta HTTP 200 OK con el contenido de 'page' en el cuerpo.
         * Esto indica que la solicitud fue exitosa y se está devolviendo información.
         */
        return ResponseEntity.ok(page);
        //Estamos devolviendo la página dentro del ResponseEntity
    }

    @Transactional
    @PutMapping
    public ResponseEntity actualizar(@RequestBody @Valid DatosActualizacionMedico datos){
        var medico = repository.getReferenceById(datos.id());
        medico.actualizarInformaciones(datos);
        return ResponseEntity.ok(new DatosDetalleMedico(medico));
    }

    @Transactional
    @DeleteMapping("/{id}")
    //El ResponseEntity es una clase que nos devuelve diferentes códigos http
    public ResponseEntity eliminar(@PathVariable Long id){
        var medico = repository.getReferenceById(id);
        medico.eliminar();
        /**
         * Devuelve una respuesta HTTP con el código 204 No Content.
         * Esto indica que la solicitud fue exitosa, pero no hay datos en el cuerpo de la respuesta.
         * Es útil, por ejemplo, cuando se elimina un recurso correctamente y no se necesita devolver nada.
         */
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    //El ResponseEntity es una clase que nos devuelve diferentes códigos http
    public ResponseEntity detallar(@PathVariable Long id){
        var medico = repository.getReferenceById(id);

        /**
         * Devuelve una respuesta HTTP con el código 204 No Content.
         * Esto indica que la solicitud fue exitosa, pero no hay datos en el cuerpo de la respuesta.
         * Es útil, por ejemplo, cuando se elimina un recurso correctamente y no se necesita devolver nada.
         */
        return ResponseEntity.ok(new DatosDetalleMedico(medico));
    }


}
