package ec.com.company.core.estudio.model;

import jakarta.validation.constraints.NotNull;

public record Destinatario(@NotNull
                           String nombre,
                           @NotNull
                           String cargo,
                           @NotNull
                           String tratamiento,
                           @NotNull
                           String ciudad) {
}
