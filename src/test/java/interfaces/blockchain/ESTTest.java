package interfaces.blockchain;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ESTTest {
    @Test
    public void testCreateAddress() throws IOException {
        Map<String, Object> address = ESTUtil.createAddress();
        System.out.println(address.get("success"));
        Map<String, String> account = (Map<String, String>) address.get("account");
        System.out.println(account.get("account"));  //e9QEGw1xguc2oW3d2FM3eghirMwYdkHrMu
        System.out.println(account.get("private_key"));//snYjif9q5Q3ZH7wnCRuz5zZRJbfqP

    }

    @Test
    public void testActicte() throws IOException {
        Map<String, Object> activate = ESTUtil.activate("eP1P1bc3PbxU93v7yia5DKkhTyEMEoEwxa", "sniQ9YSE7GXoLihXLxHYD2N9jrcC6", "e9QEGw1xguc2oW3d2FM3eghirMwYdkHrMu", "100", null, null);
        System.out.println(activate);
    }

    @Test
    public void testGetBaclance() throws IOException {
        Map<String, Object> balance = ESTUtil.getBalanceByCode("eLrPeJxmSg4tCXpszoar1ibmpXd5doiEEo", null);
        for (Map.Entry<String, Object> entry : balance.entrySet()) {

            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());

        }
    }

    @Test
    public void testPayment() throws IOException {
        Map<String, Object> payment = ESTUtil.payment("eP1P1bc3PbxU93v7yia5DKkhTyEMEoEwxa", "sniQ9YSE7GXoLihXLxHYD2N9jrcC6", "e9QEGw1xguc2oW3d2FM3eghirMwYdkHrMu", "100");
        for (Map.Entry<String, Object> entry : payment.entrySet()) {

            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());

        }
    }

    @Test
    public void testIssue() throws IOException {
        Map<String, Object> issue = ESTUtil.issue("eLrPeJxmSg4tCXpszoar1ibmpXd5doiEEo", "shfVuWrcersmzvxPPsZaTJQ1ea4os", "efLhYJhn5tZACZiZZLiH6CHoJLEVjbHHCh", "AAAAAAAAAAAAAAAAAAAAAAAAABBBBBB000000001", "50");
        for (Map.Entry<String, Object> entry : issue.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
    }

    @Test
    public void setTrustline() throws IOException {
        Map<String, Object> trust = ESTUtil.setTrustline("e9QEGw1xguc2oW3d2FM3eghirMwYdkHrMu", "snYjif9q5Q3ZH7wnCRuz5zZRJbfqP", "eP1P1bc3PbxU93v7yia5DKkhTyEMEoEwxa", null);
        for (Map.Entry<String, Object> entry : trust.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
    }

    @Test
    public void getTx() throws IOException {
        Map<String, Object> transactions = ESTUtil.getTransactions("e9QEGw1xguc2oW3d2FM3eghirMwYdkHrMu");
        for (Map.Entry<String, Object> entry : transactions.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
    }

    @Test
    public void testBatchProcess() throws IOException {
        Map<String, Object> tran = new HashMap<String, Object>();
        Map<String, String> issue = new HashMap<String, String>();
        issue.put("source_account", "eLrPeJxmSg4tCXpszoar1ibmpXd5doiEEo");
        issue.put("destination_account", "efLhYJhn5tZACZiZZLiH6CHoJLEVjbHHCh");
        issue.put("amount", "2");
        issue.put("code", "AAAAAAAAAAAAAAAAAAAAAAAAABBBBBB000000002");
        issue.put("issuer", "eLrPeJxmSg4tCXpszoar1ibmpXd5doiEEo");
        tran.put("issue", issue);
        tran.put("type", "issue");
        tran.put("private_key", "shfVuWrcersmzvxPPsZaTJQ1ea4os");
        List<Map<String, Object>> tranList = new ArrayList<>();
        tranList.add(tran);
        tranList.add(tran);
        tranList.add(tran);
        Map<String, Object> tran2 = new HashMap<String, Object>();
        tran2.put("transaction_body",tranList);
        Map<String, Object> stringObjectMap = ESTUtil.batchProcess(tran2);
        for (Map.Entry<String, Object> entry : stringObjectMap.entrySet()) {

            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());

        }
    }

}
