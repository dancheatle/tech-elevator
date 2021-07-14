package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.view.ConsoleService;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Scanner;

public class TransferService {

    private String API_BASE_URL;
    private RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser currentUser;
    AccountService accountService;
    ConsoleService console;

    BigDecimal zero = new BigDecimal(0.00);

    public TransferService(String API_BASE_URL, AccountService accountService) {
        this.API_BASE_URL = API_BASE_URL;
        this.accountService = accountService;

    }

    public Transfer[] getAllTransfers(AuthenticatedUser currentUser, AccountService accountService) {
        Transfer[] transfers;

        int accountId = accountService.getAccountIdByUserId(currentUser.getUser().getId(), currentUser);
        System.out.println("-------------------------------------------");
        System.out.println("Transfers");
        System.out.println("ID             To/From             Amount");
        System.out.println("-------------------------------------------");

        try {
            transfers = restTemplate.exchange(API_BASE_URL + "/transfers", HttpMethod.GET, makeAuthEntity(currentUser), Transfer[].class).getBody();
            for (Transfer i : transfers) {
                if (i.getAccountFrom() == accountId) {
                    System.out.println(i.getTransferId() + "          To: " + i.getAccountTo() + "            $ " + i.getAmount());
                } else if (i.getAccountTo() == accountId) {
                    System.out.println(i.getTransferId() + "          From: " + i.getAccountFrom() + "          $ " + i.getAmount());
                }
            }
            return transfers;
        } catch (NullPointerException npEx) {
            System.out.println("No transfer found.");
        } catch (RestClientException rcEx) {
            System.out.println("Error: " + rcEx.getMessage());
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
    }
        return null;
    }

    public Transfer getTransferById(AuthenticatedUser currentUser, AccountService accountService, int transferId) {
        try {
            System.out.println("\n" +
                    "Enter transfer ID to view transfer details (0 to cancel): ");
            Scanner scanner = new Scanner(System.in);
            transferId = Integer.parseInt(scanner.nextLine());
            if (transferId != 0) {
                Transfer transfer = restTemplate.exchange(API_BASE_URL + "/transfers/" + transferId, HttpMethod.GET, makeAuthEntity(currentUser), Transfer.class).getBody();
                if (transfer.getAmount() == null) {
                    System.out.println("Please enter a valid transfer ID.");
                    return null;
                } else {
                    System.out.println("------------------------------------------");
                    System.out.println("Transfer Details");
                    System.out.println("------------------------------------------");
                    System.out.println("ID: " + transfer.getTransferId());
                    System.out.println("From: " + accountService.getUsernameByAccountId(transfer.getAccountFrom(), currentUser));
                    System.out.println("To: " + accountService.getUsernameByAccountId(transfer.getAccountTo(), currentUser));
                    System.out.println("Type: " + getTypeDescFromTypeId(transfer.getTransferTypeId(), currentUser));
                    System.out.println("Status: " + getStatusDescFromStatusId(transfer.getTransferStatusId(), currentUser));
                    System.out.println("Amount: $" + transfer.getAmount());
                    return transfer;
                }
            }
        } catch (NumberFormatException numEx) {
            System.out.println("Transfer ID must be formatted as a number.");
        }  catch (NullPointerException npEx) {
        System.out.println("Transfer request cancelled.");
        } catch (RestClientException rcEx) {
            System.out.println("Request invalid.");
        } catch (ResponseStatusException rsEx) {
            System.out.println("Error contacting server.");
        } catch (Exception ex) {
            System.out.println("Error occurred: " + ex.getMessage());
        }
        return null;
    }

    public Transfer createTransfer(AuthenticatedUser currentUser, ConsoleService console, AccountService accountService) {
        try {
            int accountFrom = accountService.getAccountIdByUserId(currentUser.getUser().getId(), currentUser);
            int selectUserTo = console.getUserInputInteger("\nEnter ID of user you want to send TE bucks to (0 to cancel) ");

            if (selectUserTo != 0) {
                int accountTo = accountService.getAccountIdByUserId(selectUserTo, currentUser);

                if (accountFrom == accountTo) {
                    System.out.println("Nice try... but you cannot send money to yourself.");
                    return null;
                }

                if (accountTo == 0) {
                    return null;
                }

                else {
                    double doubleAmount = Double.parseDouble(console.getUserInput("Enter amount to send "));
                    BigDecimal amount = BigDecimal.valueOf(doubleAmount);
                    Transfer transfer = new Transfer();
                    transfer.setTransferTypeId(2);
                    transfer.setTransferStatusId(2);
                    transfer.setAmount(amount);
                    transfer.setAccountTo(accountTo);
                    transfer.setAccountFrom(accountFrom);
                    return transfer;
                }
            }
        }
        catch (NumberFormatException numEx) {
            System.out.println("Please enter valid monetary value as a number.");
        }
        catch (Exception ex) {
            System.out.println("Error occurred: " + ex.getMessage());
        }
        return null;
    }

    public Transfer sendTransfer(Transfer transfer, AuthenticatedUser currentUser, AccountService accountService) {
       try {
        if (accountService.getBalance(currentUser).compareTo(transfer.getAmount()) == -1) {
            System.out.println("Your current balance is " + accountService.getBalance(currentUser) + ".");
            System.out.println("Insufficient funds. Take yo broke ass home!");
            return null;

        }
        if (transfer.getAmount().compareTo(zero) == 0) {
            System.out.println("Transfer amount should be at least $0.01. Please enter a valid transfer amount.");
        }

        else {
                transfer = restTemplate.exchange(API_BASE_URL + "/transfers", HttpMethod.POST, makeTransferEntity(transfer, currentUser), Transfer.class).getBody();
                System.out.println("Transfer was successful.");
                System.out.println("Your new balance is " + accountService.getBalance(currentUser) + ".");
            }
        } catch (NullPointerException npEx) {
           System.out.println("Transfer request cancelled.");
       } catch (RestClientException rcEx) {
           System.out.println("Request invalid.");
       } catch (ResponseStatusException rsEx) {
           System.out.println("Error contacting server.");
       } catch (Exception ex) {
           System.out.println("Error occurred: " + ex.getMessage());
       }
       return transfer;
    }

    public String getStatusDescFromStatusId(int transferStatusId, AuthenticatedUser currentUser) {
        String statusDesc = "";
        try {
            statusDesc = restTemplate.exchange(API_BASE_URL + "/transfers/status/" + transferStatusId, HttpMethod.GET, makeAuthEntity(currentUser), String.class).getBody();
        } catch (RestClientException rcEx){
            System.out.println("Error: " + rcEx.getMessage());
        } catch (Exception ex) {
            System.out.println("Something isn't right.... maybe the status ID is invalid?");
        }
        return statusDesc;
    }

    public String getTypeDescFromTypeId(int transferTypeId, AuthenticatedUser currentUser) {
        String typeDesc = "";
        try {
            typeDesc = restTemplate.exchange(API_BASE_URL + "/transfers/type/" + transferTypeId, HttpMethod.GET, makeAuthEntity(currentUser), String.class).getBody();
        } catch (RestClientException rcEx) {
            System.out.println("Error: " + rcEx.getMessage());
        }catch (Exception ex) {
            System.out.println("Something isn't right.... maybe the type ID is invalid?");
        }
        return typeDesc;
    }

    private HttpEntity makeAuthEntity(AuthenticatedUser currentUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer, AuthenticatedUser currentUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(currentUser.getToken());
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        return entity;
    }
}
