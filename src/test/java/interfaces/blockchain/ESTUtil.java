package interfaces.blockchain;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ESTUtil {

    private static String serverURL = "https://dapi2.ecosysnet.com/v2/";

    /**
     * 生成钱包地址
     *
     * @return
     */
    public static Map<String, Object> createAddress() throws IOException {
        String url = serverURL + "new";
        String result = OkHttpUtil.sendGetRequestWithUrl(url).body().string();
        return new Gson().fromJson(result, new TypeToken<Map<String, Object>>() {
        }.getType());
    }

    /**
     * 激活
     *
     * @param sourceAccount         钱包地址
     * @param sourceSecret          钱包私钥
     * @param destinationAccount    目标地址
     * @param amount                发送数量
     * @param issuerAaccount        将要信任的发行商 可选
     * @param destinationPrivateKey 目标地址的私钥  可选
     * @return
     * @throws IOException
     */
    public static Map<String, Object> activate(String sourceAccount, String sourceSecret, String destinationAccount, String amount, String issuerAaccount, String destinationPrivateKey) throws IOException {
        String url = serverURL + "activate/" + sourceAccount + "/" + destinationAccount;
        if (amount != null)
            url += "?amount=" + amount;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("source_private_key", sourceSecret);
        jsonObject.addProperty("issuer_account", sourceSecret);
        if (issuerAaccount != null)
            jsonObject.addProperty("issuer_account", issuerAaccount);
        if (destinationPrivateKey != null)
            jsonObject.addProperty("destination_private_key", destinationPrivateKey);

        String result = OkHttpUtil.sendPostRequestWithUrl(url, jsonObject.toString()).body().string();
        return new Gson().fromJson(result, new TypeToken<Map<String, Object>>() {
        }.getType());

    }


    /**
     * 获取账户余额
     * 根据资产编号查资产
     *
     * @param account
     * @return
     */
    public static Map<String, Object> getBalanceByCode(String account,String code) throws IOException {
        String url = serverURL + "assets/" + account;
        if (code != null)
            url += "?code=" + code;
        String result = OkHttpUtil.sendGetRequestWithUrl(url).body().string();
        return new Gson().fromJson(result, new TypeToken<Map<String, Object>>() {
        }.getType());
    }
    
    /**
     * 获取账户余额
     *
     * @param account
     * @return
     */
    public static Map<String, Object> getBalance(String account) throws IOException {
        String url = serverURL + "assets/" + account;
        String result = OkHttpUtil.sendGetRequestWithUrl(url).body().string();
        return new Gson().fromJson(result, new TypeToken<Map<String, Object>>() {
        }.getType());
    }

    /**
     * 简单支付 发送非EST
     *
     * @param sourceAccount      钱包公钥
     * @param sourceSecret       钱包私钥
     * @param destinationAccount 目标地址
     * @param assetCode          资产代码
     * @param assetIssuer        资产发行商
     * @param amount             发送数量
     * @return Transaction的hash
     * @throws IOException
     */
    public static Map<String, Object> payment(String sourceAccount, String sourceSecret, String destinationAccount, String assetCode, String assetIssuer, String amount) throws IOException {
        String url = serverURL + "transfer/" + sourceAccount + "?validated=true";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("private_key", sourceSecret);
        JsonObject transfer = new JsonObject();
        transfer.addProperty("destination_account", destinationAccount);
        transfer.addProperty("code", assetCode);
        if (assetIssuer != null)
            transfer.addProperty("issuer", assetIssuer);
        transfer.addProperty("amount", amount);
        jsonObject.add("transfer", transfer);
        String result = OkHttpUtil.sendPostRequestWithUrl(url, jsonObject.toString()).body().string();
        return new Gson().fromJson(result, new TypeToken<Map<String, Object>>() {
        }.getType());
    }

    /**
     * 简单支付，发送EST
     *
     * @param sourceAccount      钱包公钥
     * @param sourceSecret       钱包私钥
     * @param destinationAccount 目标地址
     * @param amount             发送数量
     * @return Transaction的hash
     */
    public static Map<String, Object> payment(String sourceAccount, String sourceSecret, String destinationAccount, String amount) throws IOException {
        return payment(sourceAccount, sourceSecret, destinationAccount, "EST", null, amount);
    }

    /**
     * 发行
     *
     * @param sourceAccount      钱包公钥
     * @param sourceSecret       钱包私钥
     * @param destinationAccount 目标地址
     * @param assetCode          资产代码
     * @param amount             发行数量
     * @return Transaction的hash
     * @throws IOException
     */
    public static Map<String, Object> issue(String sourceAccount, String sourceSecret, String destinationAccount, String assetCode, String amount) throws IOException {
        String url = serverURL + "issue/" + sourceAccount;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("private_key", sourceSecret);
        JsonObject issue = new JsonObject();
        issue.addProperty("destination_account", destinationAccount);
        issue.addProperty("code", assetCode);
        issue.addProperty("amount", amount);
        issue.addProperty("issuer", sourceAccount);
        jsonObject.add("issue", issue);
        String result = OkHttpUtil.sendPostRequestWithUrl(url, jsonObject.toString()).body().string();
        return new Gson().fromJson(result, new TypeToken<Map<String, Object>>() {
        }.getType());
    }

    /**
     * 设置信任
     *
     * @param sourceAccount 源公钥
     * @param sourceSecret  源私钥
     * @param trustAccount  将要信任的发行商
     * @param assetCode     将要信任的资产编码
     * @return
     * @throws IOException
     */
    public static Map<String, Object> setTrustline(String sourceAccount, String sourceSecret, String trustAccount, String assetCode) throws IOException {
        String url = serverURL + "trust/" + sourceAccount;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("private_key", sourceSecret);
        JsonObject trust = new JsonObject();
        trust.addProperty("issuer", trustAccount);
        if (assetCode != null)
            trust.addProperty("code", assetCode);
        jsonObject.add("trust", trust);
        String result = OkHttpUtil.sendPostRequestWithUrl(url, jsonObject.toString()).body().string();
        return new Gson().fromJson(result, new TypeToken<Map<String, Object>>() {
        }.getType());
    }

    /**
     * 获取交易历史
     *
     * @param account
     * @return
     * @throws IOException
     */
    public static Map<String, Object> getTransactions(String account) throws IOException {
        String url = serverURL + "transactions/" + account;
        String result = OkHttpUtil.sendGetRequestWithUrl(url).body().string();
        return new Gson().fromJson(result, new TypeToken<Map<String, Object>>() {
        }.getType());

    }

    /**
     * 批量交易
     * @param transactionList
     * @return
     * @throws IOException
     */
    public static Map<String, Object> batchProcess(Map<String,Object> transactionList) throws IOException {
        String url = serverURL + "batch_process";

        String json = new Gson().toJson(transactionList);
        String result = OkHttpUtil.sendPostRequestWithUrl(url, json).body().string();
        return new Gson().fromJson(result, new TypeToken<Map<String, Object>>() {
        }.getType());
    }


}
