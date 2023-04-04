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
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
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
 */
public class TokenJWT {
    
    

    public static final int VALID = 0;
    public static final int EXPIRED = 1;
    public static final int NOT_VALID = 2;

    private static final float EXPIRATION_TIME_MINUTES = 1440; //24H

    private JsonWebSignature token;
    private JwtClaims claims;
    private int isValid =2;

    private static RsaJsonWebKey rsaJsonWebKey = null ;
    

    /**
     * recreates the token given the string of it
     * it also controls if is valid
     * @throws JoseException
     * @throws MalformedClaimException
     */
    public TokenJWT(String T) throws JoseException, MalformedClaimException{
        if(rsaJsonWebKey==null){
            rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
            rsaJsonWebKey.setKeyId("k1");
        }

        //prepers all the fields of the token that we are expeting
        JwtConsumer jwtConsumer = new JwtConsumerBuilder()
            .setRequireExpirationTime() 
            .setExpectedIssuer("bitsei_webapp") 
            .setAllowedClockSkewInSeconds(10)
            .setExpectedAudience("user") 
            .setVerificationKey(rsaJsonWebKey.getKey()) 
            .setJwsAlgorithmConstraints( 
                    ConstraintType.PERMIT, AlgorithmIdentifiers.RSA_USING_SHA256) 
            
            .build(); // create the JwtConsumer instance
            
            
            try
            {
                //  Validate the JWT and process it to the Claims
                this.claims = jwtConsumer.processToClaims(T);

                this.isValid = VALID;
                

            }
            catch (InvalidJwtException e)
            {
                
                
                
                if (e.hasExpired())
                {
                    isValid = EXPIRED;
                }
        
                
                if (e.hasErrorCode(ErrorCodes.AUDIENCE_INVALID))
                {
                    isValid = NOT_VALID;
                }

                
            }
            
    
    }

    /**
     * create a token given the email and the password
     * @param email user email
     * @param password password of the user, for now is just having two different constructor
     * @throws JoseException 
     */
    public TokenJWT(String email, String password) throws JoseException{
        if(rsaJsonWebKey==null){
            rsaJsonWebKey = RsaJwkGenerator.generateJwk(2048);
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

        
        
        //create the token
        token = new JsonWebSignature();
        token.setPayload(claims.toJson());


        //set the private key of the token
        token.setKey(rsaJsonWebKey.getPrivateKey());

        token.setKeyIdHeaderValue(rsaJsonWebKey.getKeyId());

        token.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        isValid = VALID;
        
    }

    public String getTokenString() throws JoseException {
        return "Bearer ".concat(token.getCompactSerialization());
    }

    public String getEmail() {
        return claims.getClaimValueAsString("email");
    }

    /**
     * 
     * @return the exiration date in Unix timestamp, if the claim is malformed will return 0
     */
    public long getExpireDate(){
        try{
            return claims.getExpirationTime().getValueInMillis()/1000;
        }catch(Exception  e){
            return 0;
        }

    }

     /**
     * 
     * @return the issiued date in Unix timestamp, if the claim is malformed will return 0
     */
    public long getCreationDate() {
        try{
            return claims.getIssuedAt().getValueInMillis()/1000;
        }catch(Exception  e){
            return 0;
        }
    }

    /**
     * 
     * @return returns one of this 3 things: VALID EXPIRED NOT_VALID
     */
    public int getIsValid() {
        return  isValid;
    }

    

}
