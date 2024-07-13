package com.trade.controller;

import com.trade.config.JwtProvider;
import com.trade.modal.TwoFactorOTP;
import com.trade.modal.User;
import com.trade.repository.UserRepository;
import com.trade.response.AuthResponse;
import com.trade.service.CustomeUserDetailService;
import com.trade.service.EmailService;
import com.trade.service.TwoFactorOtpService;
import com.trade.utils.OtpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomeUserDetailService customeUserDetailService;

    @Autowired
    private TwoFactorOtpService twoFactorOtpService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) throws Exception {

        User isEmailExist=userRepository.findByEmail(user.getEmail());
        if(isEmailExist!=null){
            throw new Exception("Email is already used with another account");
        }
        User newUser=new User();
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        newUser.setFullName(user.getFullName());
        User savedUser=userRepository.save(newUser);
        Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt= JwtProvider.generateToken(auth);

        AuthResponse res=new AuthResponse();
        res.setJwt(jwt);
        res.setStatus(true);
        res.setMessage("Register Success");
        return  new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) throws Exception {

        String userName=user.getEmail();
        String password=user.getPassword();

        Authentication auth = authentication(userName,password);
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt= JwtProvider.generateToken(auth);
        User authUser=userRepository.findByEmail(userName);
        if(user.getTwoFactorAuth().isEnabled()){
                AuthResponse res=new AuthResponse();
                res.setMessage("Two factor auth is enabled");
                res.setTwoFactorAuthEnabled(true);
                String otp =OtpUtils.generateOTP();
                TwoFactorOTP oldTwoFactor0TP= twoFactorOtpService.findByUser(authUser.getId());
                if(oldTwoFactor0TP!=null){
                    twoFactorOtpService.deleteTwoFactorOtp(oldTwoFactor0TP);
                }
                TwoFactorOTP newTwoFactorOtp=twoFactorOtpService.createTwoFactorOtp(
                        authUser,otp,jwt
                );
                emailService.sendVerificationOtpEmail(userName,otp);

                res.setSession(newTwoFactorOtp.getId());
                return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
        }

        AuthResponse res=new AuthResponse();
        res.setJwt(jwt);
        res.setStatus(true);
        res.setMessage("Login Success");
        return  new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    private Authentication authentication(String userName, String password) {
        UserDetails userDetails=customeUserDetailService.loadUserByUsername(userName);
        if(userDetails==null){
            throw new BadCredentialsException("Invalid Username");
        }
        if(!password.equals(userDetails.getPassword())){
            throw new BadCredentialsException("Invalid Password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails,password,userDetails.getAuthorities());
    }

    @PostMapping("/two-factor/otp/{otp}")
    public ResponseEntity<AuthResponse> verifySigingOtp(@PathVariable String otp,@RequestParam String id) throws Exception {
        TwoFactorOTP twoFactorOTP=twoFactorOtpService.findById(id);
        if(twoFactorOtpService.verifyTwoFactorOtpI(twoFactorOTP, otp)){
            AuthResponse res=new AuthResponse();
            res.setMessage("Two factor authentication verified");
            res.setTwoFactorAuthEnabled(true);
            res.setJwt(twoFactorOTP.getJwt());
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        throw new Exception("invalid otp");
    }
}
