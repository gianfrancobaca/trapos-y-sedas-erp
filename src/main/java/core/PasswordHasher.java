package core;

import at.favre.lib.crypto.bcrypt.BCrypt;

import java.util.UUID;

/**
 * Hash y verificación de contraseñas con BCrypt.
 * Centraliza la librería y el coste para que el resto del sistema no dependa de ella.
 */
public final class PasswordHasher {

    private static final int COSTE = 12;

    /**
     * Hash de un valor aleatorio que nadie conoce, calculado una sola vez con el mismo coste
     * que los hashes reales. Se usa cuando el usuario no existe, para que verificar tarde
     * lo mismo que con un usuario real (evita enumerar usernames midiendo el tiempo).
     */
    private static final String HASH_FALSO = hash(UUID.randomUUID().toString());

    private PasswordHasher() {}

    public static String hash(String password) {
        return BCrypt.withDefaults().hashToString(COSTE, password.toCharArray());
    }

    /**
     * @param hashGuardado hash almacenado, o null si el usuario no existe
     * @return true solo si el usuario existe y la contraseña coincide
     */
    public static boolean verify(String password, String hashGuardado) {
        if (password == null) {
            return false;
        }
        if (hashGuardado == null) {
            BCrypt.verifyer().verify(password.toCharArray(), HASH_FALSO);
            return false;
        }
        return BCrypt.verifyer().verify(password.toCharArray(), hashGuardado).verified;
    }
}
