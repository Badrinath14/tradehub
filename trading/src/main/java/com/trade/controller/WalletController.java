package com.trade.controller;

import com.trade.modal.Order;
import com.trade.modal.User;
import com.trade.modal.Wallet;
import com.trade.modal.WalletTransaction;
import com.trade.service.UserService;
import com.trade.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @Autowired
    private UserService userService;

    @GetMapping("/api/wallet")
    public ResponseEntity<Wallet> getUserWallet(@RequestHeader("Authorization") String jwt) throws Exception {
        User user=userService.findUserProfileByJwt(jwt);
        Wallet wallet=walletService.getUserWallet(user);
        return  new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }

    @PutMapping("/api/wallet/${walletId}/transfer")
    public ResponseEntity<Wallet> walletToWalletTransfer(@RequestHeader("Authorization") String jwt, @PathVariable Long walletId, @RequestBody WalletTransaction req) throws Exception{
        User senderUser=userService.findUserProfileByJwt (jwt);
        Wallet receiverWallet=walletService.findWalletById(walletId);
        Wallet wallet=walletService.walletToWalletTransfer (
                senderUser, receiverWallet,
                req.getAmount());
        return new ResponseEntity<>(wallet,HttpStatus.ACCEPTED);
    }

    @PutMapping("/api/wallet/order/{orderId}/pay")
    public ResponseEntity<Wallet> payOrderPayment( @RequestHeader("Authorization") String jwt, @PathVariable Long orderId
    ) throws Exception {
        User user=userService.findUserProfileByJwt(jwt);
        Order order = OrderService.getOrderById(orderId);
        Wallet wallet=walletService.payorderPayment(order, user);
        return new ResponseEntity<>(wallet, HttpStatus.ACCEPTED);
    }

}
