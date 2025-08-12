package med.voll.api.domain.medico;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private EntityManager em;

    @Test
    /**
     * Esta notación sirve para nombrar de mejor forma nuestros test, para poder diferenciarlos correctamente unos
     * de otros y ser más explícitos de lo que hace cada uno.
     */
    @DisplayName("Deberia devolver null cuando el medico buscado existe pero no esta disponible en esa fecha")
    void elegirMedicoAleatorioDisponibleEnLaFechaEscenario1() {
        /**
         * Esto es para forzar que siempre se cumpla la condición de que el test se hará en un día y una hora aceptada
         * por el sistema para intentar crear una cita (siempre en lunes a las 10 am)
         */
        var lunesSiguienteALas10 = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY)).atTime(10,0);

        var medicoLibre = medicoRepository.elegirMedicoAleatorioDisponibleEnLaFecha(Especialidad.CARDIOLOGIA, lunesSiguienteALas10);

        assertThat(medicoLibre).isNull();
    }
}