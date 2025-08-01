package med.voll.api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
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

    public String getSubject(String tokenJWT){
        /**
         * Este método recibe un token JWT como parámetro y devuelve el "subject",
         * que normalmente representa el identificador del usuario (como su email o ID).
         * Si el token es inválido o ha expirado, lanza una excepción.
         */
        try {
            System.out.println("CLAVE USADA PARA VERIFICAR: " + secret);
            System.out.println("TOKEN A VERIFICAR: " + tokenJWT);
            /**
             * Crea un algoritmo de verificación usando HMAC256 con la clave secreta definida en la variable 'secret'.
             * Este algoritmo se usó también para firmar el token originalmente.
             */
            var algoritmo = Algorithm.HMAC256(secret);
            /**
             * Verifica el token JWT usando el algoritmo.
             * También valida que el token haya sido emitido por "API Voll.med".
             */
            return JWT.require(algoritmo)// Establece el algoritmo con el que se firmó el token
                    .withIssuer("API Voll.med")// Valida que el emisor del token sea el correcto
                    .build()// Construye el verificador
                    .verify(tokenJWT)// Verifica el token JWT recibido
                    .getSubject();// Extrae y devuelve el "subject" del token (el usuario)
        } catch (JWTVerificationException exception){
            throw new RuntimeException("Token JWT invalido o expirado");
        }
    }

}
