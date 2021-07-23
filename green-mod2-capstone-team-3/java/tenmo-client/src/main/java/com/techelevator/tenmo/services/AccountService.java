package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import org.apiguardian.api.API;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.BigInteger;

public class AccountService {

    private String API_BASE_URL;
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser currentUser;

    public AccountService(String API_BASE_URL) {
        this.API_BASE_URL = API_BASE_URL;
    }

    public BigDecimal getBalance(AuthenticatedUser currentUser) {
        BigDecimal balance = new BigDecimal(0.00);
        try {
            balance = restTemplate.exchange(API_BASE_URL + "/account/" + currentUser.getUser().getId() + "/balance", HttpMethod.GET, makeAuthEntity(currentUser), BigDecimal.class).getBody();
        }catch(RestClientException rcEx){
            System.out.println("Error: " + rcEx.getMessage());
        }
        return balance;
    }

    public User[] listAllUsers(AuthenticatedUser currentUser) {
        User[] users = null;
        try {
            users = restTemplate.exchange(API_BASE_URL + "/users", HttpMethod.GET, makeAuthEntity(currentUser), User[].class).getBody();
            System.out.println("-------------------------------------------");
            System.out.println("Users");
            System.out.println("ID             Name");
            System.out.println("-------------------------------------------");
        } catch (RestClientException | NullPointerException rcEx) {
            System.out.println("Error: " + rcEx.getMessage());
        }
        try {
            for (User i : users) {
                if (!i.getId().equals(currentUser.getUser().getId())) {
                    System.out.println(i.getId() + "          " + i.getUsername());
                }
            }
        } catch (NullPointerException npEx) {
            System.out.println("Error: " + npEx.getMessage());
            return users;
        }
        return null;
    }

    public int getAccountIdByUserId(int userId, AuthenticatedUser currentUser) {
        int accountId = 0;
        try {
            accountId = restTemplate.exchange(API_BASE_URL + "/account/" + userId, HttpMethod.GET, makeAuthEntity(currentUser), Integer.class).getBody();
        } catch (Exception ex){
            System.out.println("User ID is invalid.");
        }
        return accountId;
    }

    public String getUsernameByAccountId(int accountId, AuthenticatedUser currentUser){
        String username = "";
        try {
            username = restTemplate.exchange(API_BASE_URL + "/account/" + accountId + "/username", HttpMethod.GET, makeAuthEntity(currentUser), String.class).getBody();
        } catch (Exception ex){
            System.out.println("Account ID is invalid.");
        }
        return username;
    }

    private HttpEntity makeAuthEntity(AuthenticatedUser currentUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }
}
