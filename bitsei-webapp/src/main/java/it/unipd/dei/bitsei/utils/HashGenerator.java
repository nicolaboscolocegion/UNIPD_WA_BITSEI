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

import java.util.UUID;

/**
 * HashGenerator Generates a random UUID.
 * This class is used to generate a random UUID for the reset-password of a user.
 *
 * @author BITSEI GROUP
 * @version 1.00
 * @since 1.00
 */
public class HashGenerator {

    /**
     * Empty constructor.
     */
    public HashGenerator(){
        // empty constructor
    }

    /** Generate a random UUID.
     *
     * @return a random UUID.
     */
    public static String generateHash() {
        return UUID.randomUUID().toString();
    }

    public static void main(String[] args) {
        System.out.println(generateHash());
    }
}