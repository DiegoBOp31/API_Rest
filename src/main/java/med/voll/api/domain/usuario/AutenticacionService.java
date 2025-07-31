package med.voll.api.domain.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AutenticacionService implements UserDetailsService {
    /**
     * Esta clase está anotada con @Service, lo que indica que es un componente de servicio en Spring.
     * Spring detecta esta clase automáticamente durante el escaneo de componentes y la gestiona como un bean.
     */

    /**
     * Se inyecta automáticamente una instancia de UsuarioRepository usando @Autowired.
     * Esto permite acceder a los métodos del repositorio para consultar usuarios en la base de datos.
     */
    @Autowired
    private UsuarioRepository repository;

    /**
     * Este método sobrescribe el método de la interfaz UserDetailsService,
     * que es requerida por Spring Security para el proceso de autenticación.
     * Spring lo usa para buscar los detalles del usuario (UserDetails) con base en su nombre de usuario (username).
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        /**
         * Se busca en la base de datos un usuario cuyo login coincida con el username proporcionado.
         * Este método debe devolver un objeto que implemente la interfaz UserDetails,
         * que contiene la información necesaria para que Spring Security valide las credenciales.
         */
        return repository.findByLogin(username);
    }

}
