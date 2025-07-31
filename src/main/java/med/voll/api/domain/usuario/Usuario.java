package med.voll.api.domain.usuario;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "usuarios")
@Entity(name = "Usuario")
@Getter//Crea todos los getters en tiempo de compilación
@NoArgsConstructor//Crea un constructor sin argumentos
@AllArgsConstructor//Crea un constructor con todos los argumentos
@EqualsAndHashCode(of = "id")//El sistema identifica que dos objetos son iguales si tienen el mismo id

public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String login;
    private String contrasenia;
}
