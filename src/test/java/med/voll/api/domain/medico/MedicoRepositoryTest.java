package med.voll.api.domain.medico;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Usamos esta notación cuando hacemos tests que necesitan de la capa de persistencia, como los repository
 */
@DataJpaTest
/**
 * Usa la misma base de datos que nosotros estamos usando (mysql) para hacer el test, la ventaja es que es más fiel a
 * nuestra aplicación, la desventaja es que es más lento el test.
 */
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
/**
 * Con este lo estamos diciendo que use el contexto test de las properties para realizar el test, en este caso, el
 * archivo nuevo que creamos. También tenemos que crear una nueva base de datos con el nombre que pusimos en nuestra
 * primer línea del application-test, en nuestro caso fue "vollmed_api_test".
 */
@ActiveProfiles("test")
class MedicoRepositoryTest {

    @Test
    void elegirMedicoAleatorioDisponibleEnLaFecha() {

    }
}