package main.dtos;


import lombok.Data;

@Data
public class TokenDto {

    private String accessToken;
    private String tokenType;

    public TokenDto(String accessToken, String tokenType) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
    }


}