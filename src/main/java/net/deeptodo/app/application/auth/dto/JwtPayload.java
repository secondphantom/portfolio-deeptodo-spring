package net.deeptodo.app.application.auth.dto;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public record JwtPayload(
        Long userId
) {

    public static Map<String, Object> covertToClaims(Object obj) {
        Map<String, Object> map = new HashMap<>();
        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true); // private 필드 접근 허용
            try {
                map.put(field.getName(), field.get(obj));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return map;
    }

    public static JwtPayload of(Long userId) {
        return new JwtPayload(userId);
    }
}
