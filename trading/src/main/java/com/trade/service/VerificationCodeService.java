package com.trade.service;

import com.trade.domain.VerificationType;
import com.trade.modal.User;
import com.trade.modal.VerificationCode;
import org.springframework.stereotype.Service;

@Service
public interface VerificationCodeService {
    VerificationCode sendVerificationCode(User user, VerificationType verificationType);
    VerificationCode getVerificationCodeById(Long id) throws Exception; // no usages
    VerificationCode getVerificationCodeByUser (Long userId); // no usages
    void deleteVerificationCodeById(VerificationCode verificationCode); // no usages

}
