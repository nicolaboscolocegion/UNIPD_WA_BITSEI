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
package it.unipd.dei.bitsei.resources;


/**
 * Contains constants for the actions performed by the application.
 *
 * @author BITSEI GROUP
 * @version 1.00
 * @since 1.00
 */
public final class Actions {

    /**
     * The list of the users
     */
    public static final String LIST_USER = "LIST_USER";


    /**
     * login of the user
     */
    public static final String LOGIN = "LOGIN";


    /**
     * The rest password
     */
    public static final String REST_PASSWORD = "REST_PASSWORD";

    /**
     * The change password
     */
    public static final String CHANGE_PASSWORD = "CHANGE_PASSWORD";


    public static final String CREATE_COMPANY = "CREATE_COMPANY";
    public static final String UPDATE_COMPANY = "UPDATE_COMPANY";

    public static final String UPDATE_COMPANY = "UPDATE_COMPANY";

    public static final String LIST_COMPANIES = "LIST_COMPANIES";

    public static final String GET_COMPANIES = "GET_COMPANIES";

    public static final String GET_COMPANY_IMAGE = "GET_COMPANY_IMAGE";
    /**
     * This class can be neither instantiated nor sub-classed.
     */
    private Actions() {
        throw new AssertionError(String.format("No instances of %s allowed.", Actions.class.getName()));
    }
}
