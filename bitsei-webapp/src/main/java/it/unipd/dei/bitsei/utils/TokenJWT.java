/*
 * Copyright 2022-2023 University of Padua, Italy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package it.unipd.dei.bitsei.utils;


import org.jose4j.jwa.AlgorithmConstraints.ConstraintType;
import org.jose4j.jwk.PublicJsonWebKey;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.ErrorCodes;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.jose4j.jwt.consumer.JwtConsumer;
import org.jose4j.jwt.consumer.JwtConsumerBuilder;
import org.jose4j.lang.JoseException;


/**
 * token resource for a JWT implementation
 *
 * @author BITSEI GROUP
 * @version 1.00
 * @since 1.00
 */
public class TokenJWT {
    // 0 = valid, 1 = expired, 2 = not valid
    /**
     * Valid token Status
     */
    public static final int VALID = 0;

    /**
     * EXPIRED token Status
     */
    public static final int EXPIRED = 1;

    /**
     * Not VALID token Status
     */
    public static final int NOT_VALID = 2;

    // expiration time of the token -> 24H
    private static final float EXPIRATION_TIME_MINUTES = 1440;

    // issuer of the token
    private JsonWebSignature token;

    // claims of the token
    private JwtClaims claims;

    // validity of the token
    private int isValid = 2;


    private static RsaJsonWebKey rsaJsonWebKey = null;

    private static final String key =
            "{\r\n    \"p\": \"Dm6kw6vR7S1L5PQxPnuzd0N85Was0AGvn4mRUZDyl_n6f4t0YQM1FCoCeSrErtrOgKr7CsKxOrNk_io63_d2NEbJjX6lKh6qIhAsZnfGMpF9dzwZFv9HOzUBLh4twvbLK436jO2OQ1d43CwHvJx-VrHOI4e1IXbJ1On9-0ZMWTgN\",\r\n    \"kty\": \"RSA\",\r\n    \"q\": \"CncSZobr2QELKXg2-2Dh8ee3eNMBuLM0cNpbTcvbxlUsCAiz6tbbbS0pk51tTAuOQul8Wxd8PpBfYKl3FlYRLCsPCBgwfIAk_IeoeciDSWa3mYsEwJJyghVoEzLbidGkvKtBaBX5IPlkS4qRfC-b8gktSUdOzuwGbWEpZ0PjmKU7\",\r\n    \"d\": \"c--nFlbOCcnkHOEEkaqCnJ0AQuAAMzie3d-PJnpAn7vmIKGMySBd_3qVu8PZ681sfRiHlTUp6Adm1mINEY7KzI8Qa3nVeWhnS1Kt-cnj_J_-Vz7X3FVyhXvUNVx_SG3yvCkDUq3i7xyIfQkLOIb4vbipsr1r154syZtTCLO8ZH1lC-_zlgZndItvhcrBBs6yJ1nRU55MuBO8KBI8cBwxSVmXsa0WFf4-eAHv_PF2pBeHAUT-BMcFSCU6BLFDRssIIMAuKLShFBWm_trsG0z3rmrPiploQcHRUrSoY5nmul1A_ICpxu4nXPjs0j9j5PczMcC4QJm2tipkK1LPOIk2iCk\",\r\n    \"e\": \"AQAB\",\r\n    \"use\": \"sig\",\r\n    \"kid\": \"5eHhhyrXqB1kjy5fAe2Z2j4GViOrnnzv-otgarOnX3E\",\r\n    \"qi\": \"BB-3HWDoqhySKK5ljUXC3v4X0z9COi7KENJSWCZWpAEx4hYJfikCke-XsL1obaSr8uIsHl4XPjDEKJEg6cUbVI7caYZDi-1nLeBGmKQdfMHaZGxptJR1bByKNRLc1ZXPC5mM_2yrKedyDq6pDZ9iVpi4ur7Is0ScPvJiT09MKngd\",\r\n    \"dp\": \"A-fAwgoOhkWJkezaHnROvekPNOwxux-ZrGwHXLECVEmalUThOxcpF8m-XZdLHZ2r33lk8SZ52s6Md5Jp2A0YeVDmXVl1zA38L7d8rEWg5jIkZ01l5KkzVzwik54q9-TcWW7T66qkomhbFgj8Fvep4D_A7jBcvS9tsiWXSpVdN49J\",\r\n    \"alg\": \"RS256\",\r\n    \"dq\": \"Cdy2sXpaztFF9Jm-rt1ZcMAezjWD_MydEfEldEY2Yk0nxpZ0_03l0ZhrAxqrwqttMK-aqrmSM9-Ykp6BhMItQsN9UfKB1wJoWLd7VPFakBTF12QEYXphgSetQdJ-w_2WUtrNM8rpUWLVWr9GZZL0Un7Fd7ZElYomcHPNMezvko27\",\r\n    \"n\": \"lwjnyvGv78GfHTCUXG1uFKC6SdwJxDYYCrLf_KDSCIA5ymS2sy5Rh__8XlAYbJxuCB3dQK1B5n3cCBXg_LT1O-Ync2YkKCZPzOs_5EaZqaTLSB5n5eQjaYwy3413MD4ZUnt7KVzKnnNxA5M0JwgffiRhs3iHtlM5O2wFsxURY7Fu0-ZVzbq-YoEcMyuTywFzMaBddYn1L33RHaKlEE4T61_qbJDWpeQiNm2RUBzzg4jVRcOBui9Q7U_MTQhqMca8hk3s6_0c8U_KAVtM4xJVDX79WynYgFZwF4Ib7kfEHZjsnSpXW2QWavh9qck-w1sHdjej6pnyaSOs7-mQfOhoS_8\"\r\n}";


    /**
     * recreates the token given the string of it is also controls if is valid
     *
     * @param T string of the token
     * @throws JoseException           if the token is not valid
     * @throws MalformedClaimException if the token is not valid
     */
    public TokenJWT(String T) throws JoseException, MalformedClaimException {
        if (rsaJsonWebKey == null) {
            rsaJsonWebKey = (RsaJsonWebKey) PublicJsonWebKey.Factory.newPublicJwk(key);
            rsaJsonWebKey.setKeyId("k1");
        }

        //prepare all the fields of the token that we are exception
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
                .setRequireExpirationTime()
                .setExpectedIssuer("bitsei_webapp")
                .setAllowedClockSkewInSeconds(10)
                .setExpectedAudience("user")
                .setVerificationKey(rsaJsonWebKey.getKey())
                .setJwsAlgorithmConstraints(
                        ConstraintType.PERMIT, AlgorithmIdentifiers.RSA_USING_SHA256
                )
                .build(); // create the JwtConsumer instance


        try {
            //  Validate the JWT and process it to the Claims
            this.claims = jwtConsumer.processToClaims(T);
            this.isValid = VALID;
        } catch (InvalidJwtException e) {
            if (e.hasExpired()) {
                isValid = EXPIRED;
            }
            if (e.hasErrorCode(ErrorCodes.AUDIENCE_INVALID)) {

                isValid = NOT_VALID;
            }

        }
    }

    /**
     * create a token given the email and the password
     *
     * @param email    user email
     * @param password password of the user, for now is just having two different constructor
     * @param owner_id id of the owner of the token
     * @throws JoseException if the token is not valid
     */
    public TokenJWT(String email, String password, int owner_id) throws JoseException {
        if (rsaJsonWebKey == null) {
            rsaJsonWebKey = (RsaJsonWebKey) PublicJsonWebKey.Factory.newPublicJwk(key);
            rsaJsonWebKey.setKeyId("k1");
        }


        //creates a set of claims
        claims = new JwtClaims();
        claims.setIssuer("bitsei_webapp");
        claims.setAudience("user");
        claims.setExpirationTimeMinutesInTheFuture(EXPIRATION_TIME_MINUTES);
        claims.setGeneratedJwtId();
        claims.setIssuedAtToNow();
        claims.setNotBeforeMinutesInThePast(2);
        claims.setClaim("email", email);
        claims.setClaim("owner_id", owner_id);


        //create the token
        token = new JsonWebSignature();
        token.setPayload(claims.toJson());


        //set the private key of the token
        token.setKey(rsaJsonWebKey.getPrivateKey());
        token.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());
        token.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        isValid = VALID;

    }

    /**
     * get the string of the token with the "Bearer" prefix
     *
     * @return the string of the token
     * @throws JoseException if the token is not valid
     */
    public String getTokenString() throws JoseException {
        return "Bearer ".concat(token.getCompactSerialization());
    }

    /**
     * get the email of the user from the token
     *
     * @return the email of the user, if the claim is malformed will return null
     */
    public String getEmail() {
        return claims.getClaimValueAsString("email");
    }

    /**
     * get the owner id of the user from the token
     *
     * @return the owner id of the user, if the claim is malformed will return null
     */
    public String getOwnerID() {
        return claims.getClaimValueAsString("owner_id");
    }

    /**
     * get the expiration date of the token in Unix timestamp
     *
     * @return the expiration date in Unix timestamp, if the claim is malformed will return 0
     */
    public long getExpireDate() {
        try {
            return claims.getExpirationTime().getValueInMillis() / 1000;
        } catch (Exception e) {
            return 0;
        }

    }

    /**
     * get creation date of the token in Unix timestamp
     *
     * @return the issued date in Unix timestamp, if the claim is malformed will return 0
     */
    public long getCreationDate() {
        try {
            return claims.getIssuedAt().getValueInMillis() / 1000;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * get the validity of the token
     *
     * @return returns one of this 3 things: VALID EXPIRED NOT_VALID
     */
    public int getIsValid() {
        return isValid;
    }
}
