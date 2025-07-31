package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.domain.usuario.DatosAutenticacion;
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
    private AuthenticationManager manager;

    @PostMapping
    //Recibimos un dto de DatosAutenticacion
    public ResponseEntity iniciarSesion(@RequestBody @Valid DatosAutenticacion datos){
        //Después transformamos nuestro dto en un dto propio de SpringSecurity
        var token = new UsernamePasswordAuthenticationToken(datos.login(), datos.contrasenia());
        //Para que después el autenticator manager pueda reconocerlo aquí, y llama a nuestro AutenticacionService
        var autenticacion = manager.authenticate(token);

        return ResponseEntity.ok().build();
    }
}
