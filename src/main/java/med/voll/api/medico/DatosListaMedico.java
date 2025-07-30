package med.voll.api.medico;

public record DatosListaMedico(
        String nombre,
        String email,
        String documento,
        Especialidad especialidad
) {
    public DatosListaMedico(Medico medico) {
        /**
         *Constructor adicional que recibe un objeto Medico completo y llama al constructor principal del record
         * (recordando que los record crean de forma automática un constructor con todos los datos)
         *usando sus getters. Esto nos permite crear un DatosListaMedico fácilmente a partir de un Medico,
         *y usarlo con expresiones como .map(DatosListaMedico::new) en streams.
         */
        this(
                medico.getNombre(),
                medico.getEmail(),
                medico.getDocumento(),
                medico.getEspecialidad()
        );
    }
}
