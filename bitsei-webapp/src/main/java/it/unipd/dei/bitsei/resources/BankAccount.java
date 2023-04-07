package it.unipd.dei.bitsei.resources;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

public class BankAccount extends AbstractResource{

    /**
     * IBAN identification code
     */
    private String iban;
    /**
     * nane if the bank
     */
    private String bankName;
    /**
     * friendly name of the bank
     */
    private String bankAccountFriendlyName;
    /**
     * company id that use the bank account
     */
    private String companyId;

    /**
     * create a bank account by having evry entry
     * @param iban
     * @param bankName
     * @param bankAccountFriendlyName
     * @param companyId
     */
    public BankAccount(String iban, String bankName, String bankAccountFriendlyName, String companyId ){
        this.iban = iban;
        this.bankName= bankName;
        this.bankAccountFriendlyName=bankAccountFriendlyName;
        this.companyId=companyId;
    }

    /**
     * @return the bank name
     */
    public String getBankName() {
        return bankName;
    }
    /**
     * @return the IBAN code
     */
    public String getIban() {
        return iban;
    }
    /**
     * @return the company ID that uses the bank account
     */
    public String getCompanyId() {
        return companyId;
    }
    /**
     * @return return the friendly name of the bank account
     */
    public String getBankAccountFriendlyName() {
        return bankAccountFriendlyName;
    }

    /**
     * crates a JSON with all the data for the bank account
     */
    @Override
    protected void writeJSON(OutputStream out) throws Exception {
        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);


        jg.writeStartObject();

        jg.writeStringField("IBAN", iban);
        jg.writeStringField("bank_name", bankName);
        jg.writeStringField("bankaccount_friendly_name", bankAccountFriendlyName);
        jg.writeStringField("company_id", companyId);

        jg.writeEndObject();
        jg.flush();
    }
    
    
    public static BankAccount fromJSON(InputStream in) throws IOException{
        String jIban = null;
        String jBankName = null;
        String jBankAccountFriendlyName = null;
        String jCompanyId = null;
        

        try {
            final JsonParser jp = JSON_FACTORY.createParser(in);

            while (jp.getCurrentToken() != JsonToken.FIELD_NAME || !"bankAccout".equals(jp.getCurrentName())) {

                // there are no more events
                if (jp.nextToken() == null) {
                    LOGGER.error("Not able to fin a bank account");
                    
                }
            }


            while (jp.nextToken() != JsonToken.END_OBJECT) {

                if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                    switch (jp.getCurrentName()) {
                        case "IBAN" -> {
                            jp.nextToken();
                            jIban = jp.getText();
                        }
                        case "bank_name" -> {
                            jp.nextToken();
                            jBankName = jp.getText();
                        }
                        case "bankaccount_friendly_name" -> {
                            jp.nextToken();
                            jBankAccountFriendlyName = jp.getText();
                        }
                        case "company_id" -> {
                            jp.nextToken();
                            jCompanyId = jp.getText();
                        }
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Unable to parse an User object from JSON.", e);
            throw e;
        }

        return new BankAccount(jIban, jBankName, jBankAccountFriendlyName, jCompanyId);

    }
    
}
