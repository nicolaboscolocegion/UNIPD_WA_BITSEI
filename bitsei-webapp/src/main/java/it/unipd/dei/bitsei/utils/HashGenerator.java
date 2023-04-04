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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Generates a random hash with SHA-256 algorithm.
 * This class is used to generate a random hash for the reset-password of a user.
 *
 * @author BITSEI GROUP
 * @version 1.00
 * @since 1.00
 */
public class HashGenerator {

    // Generate a random hash
    public static String generateHash() {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hash = digest.digest(java.util.UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8));
        return new String(hash, StandardCharsets.UTF_8);
    }
}