package net.deeptodo.app.api.project.dto;

import lombok.Builder;
import net.deeptodo.app.api.project.dto.request.QueryOrder;
import net.deeptodo.app.api.project.exception.ProjectErrorCode;
import net.deeptodo.app.common.exception.BadRequestException;

public record GetProjectsByQueryDto(
        Integer page,
        QueryOrder order,
        Boolean enabled,
        String search

) {
    @Builder
    public GetProjectsByQueryDto(Integer page, String order, Boolean enabled,String search) {
        this(page, parseOrder(order), enabled,search);
    }

    private static QueryOrder parseOrder(String order) {
        return switch (order) {
            case "recent" -> QueryOrder.RECENT;
            case "old" -> QueryOrder.OLD;
            case "create" -> QueryOrder.CREATE;
            default ->
                    throw new BadRequestException(ProjectErrorCode.getErrorCode(ProjectErrorCode.BAD_REQUEST_INVALID_INPUT));
        };
    }
}
