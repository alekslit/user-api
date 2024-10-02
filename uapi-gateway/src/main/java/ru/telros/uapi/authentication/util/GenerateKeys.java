package ru.telros.uapi.authentication.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

// Вспомогательный класс для генерации секретных ключей:
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GenerateKeys {
    public static void main(String[] args) {
        System.out.printf("SecretAccessKey: %s \nSecretRefreshKey: %s", generateKey(), generateKey());
    }

    private static String generateKey() {
        return Encoders.BASE64.encode(Jwts.SIG.HS512.key().build().getEncoded());
    }
}