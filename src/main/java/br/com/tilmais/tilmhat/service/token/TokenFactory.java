package br.com.tilmais.tilmhat.service.token;

public class TokenFactory {

    public static TokenEncoder getEncoder(String nameOfListGrantedAuthority, String keySecret) {
        return new JwtEncoder(nameOfListGrantedAuthority, keySecret);
    }

    public static TokenDecoder getDecoder(String jwt, String keySecret, String nameOfListGrantedAuthority) {
        return new JwtDecoder(jwt, keySecret, nameOfListGrantedAuthority);
    }
}
