package br.com.udemy.statefulanyapi.core.dto;

public record AnyResponse(String status, Integer code, AuthUserResponse authResponse) {
}
