package core;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PasswordHasherTest {

    @Test
    void generaUnHashBCryptConCoste12() {
        assertTrue(PasswordHasher.hash("Admin123!").startsWith("$2a$12$"));
    }

    @Test
    void verificaUnaContraseñaCorrecta() {
        String hash = PasswordHasher.hash("Admin123!");
        assertTrue(PasswordHasher.verify("Admin123!", hash));
    }

    @Test
    void rechazaUnaContraseñaIncorrecta() {
        String hash = PasswordHasher.hash("Admin123!");
        assertFalse(PasswordHasher.verify("admin123!", hash));
    }

    @Test
    void mismaContraseñaGeneraHashesDistintosPorLaSal() {
        assertNotEquals(PasswordHasher.hash("clave"), PasswordHasher.hash("clave"));
    }

    @Test
    void usuarioInexistenteSiempreFalla() {
        assertFalse(PasswordHasher.verify("Admin123!", null));
    }

    @Test
    void contraseñaNulaFalla() {
        assertFalse(PasswordHasher.verify(null, PasswordHasher.hash("clave")));
    }

    @Test
    void valorGuardadoEnTextoPlanoNoSeAcepta() {
        assertFalse(PasswordHasher.verify("clave", "clave"));
    }
}
