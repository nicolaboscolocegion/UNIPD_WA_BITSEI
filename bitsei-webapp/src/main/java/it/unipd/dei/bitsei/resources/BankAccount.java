package it.unipd.dei.bitsei.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

/**
 * The BankAccount resource.
 *
 * @author Nicola Boscolo Cegion (nicola.boscolocegion.1@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class BankAccount extends AbstractResource {


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
    private int companyId;

    /**
     * database ID of the bankAccount, if -1 the bank account is not in the database
     */
    private int bankAccount_id;

    /**
     * create a bank account by having every entry
     *
     * @param iban                    iban code of the bank account
     * @param bankName                name of the bank
     * @param bankAccountFriendlyName friendly name of the bank account
     * @param companyId               company ID that uses the bank account
     * @param bankAccount_id          database ID of the bank account
     */
    public BankAccount(int bankAccount_id, String iban, String bankName, String bankAccountFriendlyName, int companyId) {
        this.bankAccount_id = bankAccount_id;
        this.iban = iban;
        this.bankName = bankName;
        this.bankAccountFriendlyName = bankAccountFriendlyName;
        this.companyId = companyId;
    }

    /**
     * gets bank account name
     *
     * @return the bank name
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * gets the IBAN code
     *
     * @return the IBAN code
     */
    public String getIban() {
        return iban;
    }

    /**
     * gets the company ID that uses the bank account
     *
     * @return the company ID that uses the bank account
     */
    public int getCompanyId() {
        return companyId;
    }

    /**
     * gets the friendly name of the bank account
     *
     * @return return the friendly name of the bank account
     */
    public String getBankAccountFriendlyName() {
        return bankAccountFriendlyName;
    }

    /**
     * gets the bank account ID
     *
     * @return bank account ID
     */
    public int getBankAccountID() {
        return bankAccount_id;
    }

    /**
     * Writes the JSON representation of this object to the given output stream.
     *
     * @param out the output stream
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void writeJSON(OutputStream out) throws Exception {
        final JsonGenerator jg = JSON_FACTORY.createGenerator(out);


        jg.writeStartObject();

        jg.writeNumberField("bankaccount_id", bankAccount_id);
        jg.writeStringField("IBAN", iban);
        jg.writeStringField("bank_name", bankName);
        jg.writeStringField("bankaccount_friendly_name", bankAccountFriendlyName);
        jg.writeNumberField("company_id", companyId);

        jg.writeEndObject();
        jg.flush();
    }

    /**
     * fetch JSONs with all the data for the bank account
     *
     * @param in The input stream
     * @return new BankAccount entity
     * @throws IOException if an I/O error occurs
     */
    public static BankAccount fromJSON(InputStream in) throws IOException {
        String jIban = null;
        String jBankName = null;
        String jBankAccountFriendlyName = null;
        int jbankaccount_id = -1;
        int jCompanyId = -1;


        try {
            final JsonParser jp = JSON_FACTORY.createParser(in);
            /*
            while (jp.getCurrentToken() != JsonToken.FIELD_NAME || !"bankAccout".equals(jp.getCurrentName())) {

                // there are no more events
                if (jp.nextToken() == null) {
                    LOGGER.error("Not able to find a bank account");
                    
                }
            }
            */

            while (jp.nextToken() != JsonToken.END_OBJECT) {

                if (jp.getCurrentToken() == JsonToken.FIELD_NAME) {
                    switch (jp.getCurrentName()) {
                        case "bankaccount_id" -> {
                            jp.nextToken();
                            jbankaccount_id = jp.getNumberValue().intValue();
                        }
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
                            jCompanyId = jp.getNumberValue().intValue();
                        }
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Unable to parse an User object from JSON.", e);
            throw e;
        }

        return new BankAccount(jbankaccount_id, jIban, jBankName, jBankAccountFriendlyName, jCompanyId);

    }

}
