package br.com.fiap.gastrosphere.utils;

import br.com.fiap.gastrosphere.exceptions.ResourceNotFoundException;

import java.util.UUID;

import static java.util.regex.Pattern.matches;

public class GastroSphereUtils {
    private static final String REGEX_UUID = "^[0-9a-f]{8}-[0-9a-f]{4}-[4][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$";

    public static void uuidValidator(UUID id) {
        if (!matches(REGEX_UUID, id.toString())) {
            throw new ResourceNotFoundException("ID de usuário inválido");
        }
    }

}
