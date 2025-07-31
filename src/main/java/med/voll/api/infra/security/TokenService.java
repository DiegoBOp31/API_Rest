package med.voll.api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import med.voll.api.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    /**
     * Con esto indicamos de donde obtenemos el secret, que en este caso lo obtenemos del archivo
     * de application.propeties
     */
    @Value("${api.security.token.secret}")
    private String secret;

    public String generarToken(Usuario usuario){
        try {
            //El HMAC256 es un algoritmo de generación de tokens que recibe un parámetro
            //para firmar los tokens desde este servicio
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.create()
                    //aquí indicamos qué servidor es el que está firmando el token
                    .withIssuer("API Voll.med")
                    //Quién es el que se está logeando
                    .withSubject(usuario.getLogin())
                    //Implementamos la fecha de expiración del token
                    .withExpiresAt(fechaExpiracion())
                    //Aquí pasamos el algoritmo para firmar el token
                    .sign(algoritmo);
        } catch (JWTCreationException exception){
            throw new RuntimeException("Error al generar el token JWT", exception);
        }
    }

    private Instant fechaExpiracion() {
        /**
         * Este método calcula una fecha y hora de expiración en formato Instant, que representa un momento exacto en el tiempo (timestamp global).
         * Primero obtiene la fecha y hora actual del sistema con LocalDateTime.now(), que no tiene zona horaria asociada.
         * Luego le suma 2 horas con plusHours(2), es decir, establece que el token u operación expirará dentro de 2 horas.
         * Finalmente, convierte ese LocalDateTime a un Instant usando ZoneOffset.of("-06:00"), que indica que estamos usando la zona horaria UTC-6,
         * correspondiente a la Ciudad de México
         * Instant se usa comúnmente en APIs que manejan tiempos exactos, como la expiración de JWTs u otros tokens.
         */
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-06:00"));
    }
}
