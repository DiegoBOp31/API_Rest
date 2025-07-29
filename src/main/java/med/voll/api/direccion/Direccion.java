package med.voll.api.direccion;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter//Crea todos los getters en tiempo de compilación
@NoArgsConstructor//Crea un constructor sin argumentos
@AllArgsConstructor//Crea un constructor con todos los argumentos

@Embeddable
public class Direccion {
    private String calle;
    private String numero;
    private String complemento;
    private String barrio;
    private String ciudad;
    private String codigo_postal;
    private String estado;
}
