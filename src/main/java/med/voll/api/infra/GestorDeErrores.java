package med.voll.api.infra;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GestorDeErrores {
    /**
     * @RestControllerAdvice se utiliza para manejar excepciones de manera global en una aplicación REST.
     * Permite capturar y procesar errores que ocurren en los controladores
     * sin tener que escribir el mismo código de manejo de errores en cada clase.
     */

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity gestionarError404(){
        /**
         * Este método se encarga de manejar específicamente las excepciones del tipo EntityNotFoundException.
         * Cuando se lanza esta excepción en cualquier parte de los controladores REST,
         * este método será llamado automáticamente gracias a la anotación @ExceptionHandler.
         * Retorna una respuesta HTTP 404 (Not Found) sin cuerpo.
         * Es útil para centralizar el manejo de errores y evitar repetir código en cada controlador.
         */
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity gestionarError400(MethodArgumentNotValidException ex){
        /**
         * Este método maneja excepciones del tipo MethodArgumentNotValidException,
         * que ocurren cuando fallan las validaciones en los parámetros de entrada de una petición (por ejemplo, @NotNull, @Size, etc.)
         * La anotación @ExceptionHandler indica que este método debe ejecutarse automáticamente cuando se lanza esta excepción.
         */
        // Se obtienen todos los errores de validación a nivel de campo ("el campo 'nombre' no puede estar vacío")
        var errores = ex.getFieldErrors();

        // Se construye una respuesta con estado 400 (Bad Request) y se convierte cada error a un objeto DatosErrorValidacion.
        return ResponseEntity.badRequest()
                .body(
                        errores.stream()
                                .map(DatosErrorValidacion::new)// Se transforma cada FieldError en un DatosErrorValidacion
                                .toList());// Se convierte el stream a una lista que se envía como cuerpo de la respuesta
    }

    public record DatosErrorValidacion(
            String campo,
            String mensaje
    ) {
        /**
         * Clase record que representa un error de validación individual.
         * Contiene el nombre del campo con error y el mensaje asociado (por ejemplo, "nombre", "no puede estar vacío").
         */
        // Constructor que convierte directamente un objeto FieldError en un DatosErrorValidacion
        public DatosErrorValidacion(FieldError error){
            this(
                    error.getField(), error.getDefaultMessage()
            );
        }
    }
}
