package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.domain.usuario.DatosAutenticacion;
import med.voll.api.domain.usuario.Usuario;
import med.voll.api.infra.security.DatosTokenJWT;
import med.voll.api.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacionController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager manager;

    @PostMapping
    //Recibimos un dto de DatosAutenticacion
    public ResponseEntity iniciarSesion(@RequestBody @Valid DatosAutenticacion datos){
        //Después transformamos nuestro dto en un dto propio de SpringSecurity
        var authenticationToken = new UsernamePasswordAuthenticationToken(datos.login(), datos.contrasenia());
        //Para que después el autenticator manager pueda reconocerlo aquí, y llama a nuestro AutenticacionService
        var autenticacion = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.generarToken((Usuario) autenticacion.getPrincipal());
        /**
         * Esta línea construye una respuesta HTTP 200 OK y como cuerpo devuelve un token JWT generado por el servicio tokenService.
         * Se llama al método generarToken(), pasando como argumento el usuario autenticado,
         * que se obtiene de autenticacion.getPrincipal() y se convierte (cast) a tipo Usuario.
         * Esto funciona porque durante el proceso de autenticación, Spring Security guarda los datos del usuario autenticado
         * dentro del objeto Authentication, y getPrincipal() devuelve el objeto UserDetails (en este caso, tu clase Usuario).
         */
        return ResponseEntity.ok(new DatosTokenJWT(tokenJWT));
    }

}
