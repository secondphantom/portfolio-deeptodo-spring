package net.deeptodo.app.repository.project.dto;

public record ProjectIdAndVersionAndEnabledDto(
        Long id,
        Integer version,
        boolean enabled
) {
}
