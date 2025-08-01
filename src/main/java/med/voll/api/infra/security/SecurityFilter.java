package med.voll.api.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Esta clase define un filtro de seguridad personalizado que se ejecutará
 * una vez por cada petición HTTP que reciba el backend.
 * Aquí podemos inspeccionar la petición y realizar lógica relacionada con autenticación (como leer el token JWT).
 */
@Component//Algo generico que necesitamos que spring lo cargue
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private TokenService tokenService;

    /**
     * Este método se ejecuta automáticamente cada vez que llega una petición al backend.
     * Aquí es donde se aplica la lógica principal del filtro.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, // Representa la solicitud HTTP entrante
                                    HttpServletResponse response, // Representa la respuesta HTTP que se enviará
                                    FilterChain filterChain) // Es la cadena de filtros que se deben ejecutar después de este
            throws ServletException, IOException {
        System.out.println("Filtro llamado alv");
        // Se llama al método recuperarToken para obtener el token JWT desde el encabezado "Authorization" de la solicitud.
        var tokenJWT = recuperarToken(request);
        System.out.println("TOKEN DEVUELTO: "+ tokenJWT);
        if(tokenJWT != null){
            System.out.println("Entramos el if del doFiltrtInternal");
            //Obtenemo el usuario que se logeo a partir del token
            var subject = tokenService.getSubject(tokenJWT);
            System.out.println("USUARIO: "+subject);
            //Obtengo el usuario de la base de datos
            var usuario = repository.findByLogin(subject);
            //Creo una autenticacion que se la enviamos a setAutentication
            var authentication = new UsernamePasswordAuthenticationToken(usuario,null,usuario.getAuthorities());
            /**
             * Guarda la información del usuario autenticado en el contexto de seguridad de Spring.
             * Esto es necesario para que las siguientes peticiones dentro del mismo request
             * reconozcan al usuario como autenticado y puedan aplicar reglas de autorización.
             */
            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("Usuario Logeado");
        }
        //Sino viene ningún token, entonces encárgate tú de revisar si el usuario está logeado
        /**
         * Llama al siguiente filtro en la cadena (puede ser otro filtro o el controlador si este es el último).
         * Es obligatorio para que la petición siga su curso. Si no llamas a esto, la petición se detiene aquí.
         */
        filterChain.doFilter(request,response);
    }

    // Este método intenta recuperar el token JWT desde el encabezado Authorization de la solicitud.
    private String recuperarToken(HttpServletRequest request) {
        System.out.println("Recuperando token");
        // Obtiene el valor del header "Authorization" (donde típicamente se envía el token JWT).
        var authorizationHeader = request.getHeader("Authorization");
        System.out.println("Token recuperado");
        // Si el encabezado no está presente, lanza una excepción (esto lo puedes personalizar).
        if (authorizationHeader != null){
            System.out.println("Sí hay token");
            // Devuelve el token, quitando la palabra "Bearer" al inicio.
            return authorizationHeader.replace("Bearer","").trim();
            /**
             * Tuve que agregar el trim() al final porque por alguna razón al generar o verificar el token,
             * se le agregaban espacios. Lo que provocaba que se muriera el programa principalmente al hacer la
             * verificación
              */
        }
        System.out.println("Esto no pasa");
        return null;
    }
}
