package com.example.mssscredentials.services;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

@Service
public class Sha256Hash {
    public String hash(String input) {
        return DigestUtils.sha256Hex(input);
    }
}
