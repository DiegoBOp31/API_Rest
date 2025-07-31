package med.voll.api.infra.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


// Esta clase está anotada con @Configuration, lo que indica que contiene configuraciones para la aplicación.
// La anotación @EnableWebSecurity activa la configuración personalizada de seguridad de Spring Security.
@Configuration
@EnableWebSecurity
public class SecurityConfigurations {

    /**
     * Este método define un bean de tipo SecurityFilterChain, que es el encargado de configurar
     * la cadena de filtros de seguridad que maneja las peticiones HTTP.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /**
         * Desactiva la protección contra CSRF (Cross-Site Request Forgery)
         */
        return http.csrf(csrf -> csrf.disable())
                /**
                 * Configura la política de manejo de sesiones para que sea "STATELESS",
                 * es decir, que no se almacene ninguna sesión del usuario en el servidor.
                 * Esto es ideal para APIs que usan tokens como JWT.
                 */
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // build() finaliza la configuración y construye el SecurityFilterChain.
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        /**
         * Define un bean de tipo AuthenticationManager, que es el componente principal
         * encargado de manejar la autenticación en Spring Security.
         * Se obtiene desde la configuración automática de Spring Security.
         * Esto es útil cuando necesitas autenticar usuarios manualmente (por ejemplo, en un servicio que genera JWTs).
         */
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        /**
         * Este método define un bean de tipo PasswordEncoder, que es la interfaz estándar
         * que usa Spring Security para codificar (hashear) y verificar contraseñas de forma segura.
         *
         * Al devolver una instancia de BCryptPasswordEncoder, le estás diciendo a Spring
         * que use el algoritmo BCrypt para codificar las contraseñas.
         *
         * Al declarar este bean, puedes inyectarlo en cualquier parte de tu aplicación usando @Autowired,
         * y Spring lo usará automáticamente cuando se necesite un PasswordEncoder, como en la autenticación de usuarios.
         */
        return new BCryptPasswordEncoder();
    }
}
