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


 package it.unipd.dei.bitsei.dao;

import org.apache.logging.log4j.message.StringFormattedMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserAuthDAO extends AbstractDAO<Boolean> {

    private static final String STATEMENT = "SELECT name, FROM bitsei_schema.Owner WHERE password='pass' AND user='usr'";

    private final String username;
    private final String password;

    public UserAuthDAO(final Connection con, final String username, final String password){
        super(con);
        this.username=username;
        this.password=password; 
    }

    @Override
    protected void doAccess() throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'doAccess'");
    }
}
