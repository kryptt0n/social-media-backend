package com.example.mainservice.services.hashing;

import org.apache.commons.codec.digest.DigestUtils;

public class Sha256HashingService implements HashingService {
    @Override
    public String hash(String input) {
        return DigestUtils.sha256Hex(input);
    }
}
