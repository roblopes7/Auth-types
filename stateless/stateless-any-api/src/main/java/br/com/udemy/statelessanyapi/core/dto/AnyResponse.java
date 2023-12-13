package br.com.udemy.statelessanyapi.core.dto;

public record AnyResponse(String status, Integer code, AuthUserResponse authUser) {
}
