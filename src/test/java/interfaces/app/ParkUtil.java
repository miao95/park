package interfaces.app;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.Map;

public class ParkUtil {

	 //private static String serverURL = "https://111.198.29.11:2030"; //正式地址
	 private static String serverURL = "https://localhost:443"; //测试地址

    /**
     * 车辆注册
     * @param carNumber
     * @param nowTime
     * @param issuerAccount
     * @param issuerKey
     * @return
     * @throws IOException
     */
    public static Map<String, Object> registerCar(
    		String carNumber, 
    		String nowTime, 
    		String issuerAccount, 
    		String issuerKey) throws IOException {
        String url = serverURL + "/register/car";
        
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("carNumber", carNumber);
        jsonObject.addProperty("nowTime", nowTime);
        if (issuerAccount != null)
            jsonObject.addProperty("issuerAccount", issuerAccount);
        if (issuerKey != null)
            jsonObject.addProperty("issuerKey", issuerKey);
        
        String result = OkHttpUtil.sendPostRequestWithUrl(url, jsonObject.toString()).body().string();
        return new Gson().fromJson(result, new TypeToken<Map<String, Object>>() {
        }.getType());

    }
    
   /**
    * 停车场注册
    * @param parkinglotNumber
    * @param issuerAccount
    * @param issuerKey
    * @param nowTime
    * @return
    * @throws IOException
    */
    public static Map<String, Object> registerParkinglot(
    		String parkinglotNumber, 
    		String issuerAccount, 
    		String issuerKey, 
    		String nowTime) throws IOException {
        String url = serverURL + "/register/pl";
        
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("parkinglotNumber", parkinglotNumber);
        jsonObject.addProperty("nowTime", nowTime);
        if (issuerAccount != null)
            jsonObject.addProperty("issuerAccount", issuerAccount);
        if (issuerKey != null)
            jsonObject.addProperty("issuerKey", issuerKey);

        String result = OkHttpUtil.sendPostRequestWithUrl(url, jsonObject.toString()).body().string();
        return new Gson().fromJson(result, new TypeToken<Map<String, Object>>() {
        }.getType());

    }
    
    /**
     * 车位发布
     * @param parkinglotNumber
     * @param reserveSpaceAmount
     * @param startDate
     * @param endDate
     * @param week
     * @param startTime
     * @param endTime
     * @param parkinglotAccount
     * @param nowTime
     * @param issuerAccount
     * @param issuerKey
     * @return
     * @throws IOException
     */
    public static Map<String, Object> publishParkingSpace(
    		String parkinglotNumber, 
    		Integer reserveSpaceAmount,
    		String startDate, 
    		String endDate,
    		String week, 
    		String startTime,
    		String endTime,
    		String issuerAccount, 
    		String issuerKey,
    		String parkinglotAccount, 
    		String nowTime,
    		String flag,
    		String building,
    		String floor,
    		String area,
    		String spaceNumber) throws IOException {
        String url = serverURL + "/pl/publish";
        
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("parkinglotNumber", parkinglotNumber);
        jsonObject.addProperty("reserveSpaceAmount", reserveSpaceAmount);
        jsonObject.addProperty("startDate", startDate);
        jsonObject.addProperty("endDate", endDate);
        jsonObject.addProperty("week", week);
        jsonObject.addProperty("startTime", startTime);
        jsonObject.addProperty("endTime", endTime);
        jsonObject.addProperty("nowTime", nowTime);
        jsonObject.addProperty("flag", flag);
        jsonObject.addProperty("building", building);
        jsonObject.addProperty("floor", floor);
        jsonObject.addProperty("area", area);
        jsonObject.addProperty("spaceNumber", spaceNumber);
        if (issuerAccount != null)
            jsonObject.addProperty("issuerAccount", issuerAccount);
        if (issuerKey != null)
            jsonObject.addProperty("issuerKey", issuerKey);
        if (parkinglotAccount != null)
            jsonObject.addProperty("parkinglotAccount", parkinglotAccount);

        String result = OkHttpUtil.sendPostRequestWithUrl(url, jsonObject.toString()).body().string();
        return new Gson().fromJson(result, new TypeToken<Map<String, Object>>() {
        }.getType());

    }
    
    
   /**
    * 查询可预订时段
    * @param parkinglotNumber
    * @param parkinglotAccount
    * @param nowTime
    * @return
    * @throws IOException
    */
    public static Map<String, Object> findReserveTime(
    		String parkinglotNumber, 
    		String parkinglotAccount, 
    		String nowTime,
    		String flag,
    		String building,
    		String floor,
    		String area,
    		String spaceNumber) throws IOException {
        String url = serverURL + "/reserve/query";
        
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("parkinglotNumber", parkinglotNumber);
        jsonObject.addProperty("nowTime", nowTime);
        jsonObject.addProperty("flag", flag);
        jsonObject.addProperty("building", building);
        jsonObject.addProperty("floor", floor);
        jsonObject.addProperty("area", area);
        jsonObject.addProperty("spaceNumber", spaceNumber);
        if(parkinglotAccount != null)
        	jsonObject.addProperty("parkinglotAccount", parkinglotAccount);

        String result = OkHttpUtil.sendPostRequestWithUrl(url, jsonObject.toString()).body().string();
        return new Gson().fromJson(result, new TypeToken<Map<String, Object>>() {
        }.getType());

    }
    
   /**
    * 用户发起预定请求
    * @param parkinglotNumber
    * @param carNumber
    * @param startTime
    * @param endTime
    * @param parkinglotAccount
    * @param parkinglotKey
    * @param lockAccount
    * @param carAccount
    * @param nowTime
    * @return
    * @throws IOException
    */
    public static Map<String, Object> reserveParkingSpace(
    		String parkinglotNumber,
    		String carNumber, 
    		String startTime, 
    		String endTime,
    		String parkinglotAccount,
    		String parkinglotKey, 
    		String lockAccount,
    		String carAccount,
    		String nowTime) throws IOException {
        String url = serverURL + "/reserve/require";
        
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("parkinglotNumber", parkinglotNumber);
        jsonObject.addProperty("carNumber", carNumber);
        jsonObject.addProperty("startTime", startTime);
        jsonObject.addProperty("endTime", endTime);
        jsonObject.addProperty("nowTime", nowTime);
        if (parkinglotAccount != null)
            jsonObject.addProperty("parkinglotAccount", parkinglotAccount);
        if (parkinglotKey != null)
            jsonObject.addProperty("parkinglotKey", parkinglotKey);
        if (lockAccount != null)
            jsonObject.addProperty("lockAccount", lockAccount);
        if (carAccount != null)
            jsonObject.addProperty("carAccount", carAccount);

        String result = OkHttpUtil.sendPostRequestWithUrl(url, jsonObject.toString()).body().string();
        return new Gson().fromJson(result, new TypeToken<Map<String, Object>>() {
        }.getType());

    }
    
   /**
    * 支付成功
    * @param orderNumber
    * @param parkinglotNumber
    * @param carNumber
    * @param parkinglotAccount
    * @param lockAccount
    * @param lockKey
    * @param carAccount
    * @param nowTime
    * @return
    * @throws IOException
    */
    public static Map<String, Object> paySuccess(
    		String orderNumber, 
    		String parkinglotNumber, 
    		String carNumber, 
    		String parkinglotAccount,
    		String lockAccount, 
    		String lockKey, 
    		String carAccount, 
    		String nowTime) throws IOException {
        String url = serverURL + "/pay/success";
        
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("carNumber", carNumber);
        jsonObject.addProperty("nowTime", nowTime);
        jsonObject.addProperty("orderNumber", orderNumber);
        jsonObject.addProperty("parkinglotNumber", parkinglotNumber);
        if (parkinglotAccount != null)
            jsonObject.addProperty("parkinglotAccount", parkinglotAccount);
        if (lockAccount != null)
            jsonObject.addProperty("lockAccount", lockAccount);
        if (lockKey != null)
            jsonObject.addProperty("lockKey", lockKey);
        if (carAccount != null)
            jsonObject.addProperty("carAccount", carAccount);

        String result = OkHttpUtil.sendPostRequestWithUrl(url, jsonObject.toString()).body().string();
        return new Gson().fromJson(result, new TypeToken<Map<String, Object>>() {
        }.getType());

    }
    
    
    /**
     * 支付失败
     * @param orderNumber
     * @param parkinglotNumber
     * @param carNumber
     * @param parkinglotAccount
     * @param lockAccount
     * @param lockKey
     * @param nowTime
     * @return
     * @throws IOException
     */
    public static Map<String, Object> payFail(
    		String orderNumber, 
    		String parkinglotNumber, 
    		String carNumber, 
    		String parkinglotAccount,
    		String lockAccount, 
    		String lockKey, 
    		String nowTime) throws IOException {
        String url = serverURL + "/pay/fail";
        
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("carNumber", carNumber);
        jsonObject.addProperty("nowTime", nowTime);
        jsonObject.addProperty("orderNumber", orderNumber);
        jsonObject.addProperty("parkinglotNumber", parkinglotNumber);
        if (parkinglotAccount != null)
            jsonObject.addProperty("parkinglotAccount", parkinglotAccount);
        if (lockAccount != null)
            jsonObject.addProperty("lockAccount", lockAccount);
        if (lockKey != null)
            jsonObject.addProperty("lockKey", lockKey);

        String result = OkHttpUtil.sendPostRequestWithUrl(url, jsonObject.toString()).body().string();
        return new Gson().fromJson(result, new TypeToken<Map<String, Object>>() {
        }.getType());

    }
    
    /**
     * 车辆入场
     * @param orderNumber
     * @param parkinglotNumber
     * @param carNumber
     * @param parkinglotAccount
     * @param carAccount
     * @param nowTime
     * @return
     * @throws IOException
     */
    public static Map<String, Object> inRequire(
    		String orderNumber, 
    		String parkinglotNumber, 
    		String carNumber, 
    		String parkinglotAccount,
    		String carAccount, 
    		String nowTime) throws IOException {
        String url = serverURL + "/in/require";
        
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("carNumber", carNumber);
        jsonObject.addProperty("nowTime", nowTime);
        jsonObject.addProperty("orderNumber", orderNumber);
        jsonObject.addProperty("parkinglotNumber", parkinglotNumber);
        if (parkinglotAccount != null)
            jsonObject.addProperty("parkinglotAccount", parkinglotAccount);
        if (carAccount != null)
            jsonObject.addProperty("carAccount", carAccount);

        String result = OkHttpUtil.sendPostRequestWithUrl(url, jsonObject.toString()).body().string();
        return new Gson().fromJson(result, new TypeToken<Map<String, Object>>() {
        }.getType());

    }
    
    
   /**
    * 车辆离场
    * @param orderNumber
    * @param parkinglotNumber
    * @param carNumber
    * @param prePayment
    * @param parkinglotAccount
    * @param parkinglotKey
    * @param carAccount
    * @param carKey
    * @param recycleAccount
    * @param nowTime
    * @return
    * @throws IOException
    */
    public static Map<String, Object> outRequire(
    		String orderNumber, 
    		String parkinglotNumber, 
    		String carNumber,
    		int prePayment,
    		String parkinglotAccount,
    		String parkinglotKey,
    		String carAccount,
    		String carKey,
    		String recycleAccount,
    		String nowTime) throws IOException {
        String url = serverURL + "/out/require";
        
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("carNumber", carNumber);
        jsonObject.addProperty("nowTime", nowTime);
        jsonObject.addProperty("orderNumber", orderNumber);
        jsonObject.addProperty("parkinglotNumber", parkinglotNumber);
        jsonObject.addProperty("prePayment", prePayment);
        if (parkinglotAccount != null)
            jsonObject.addProperty("parkinglotAccount", parkinglotAccount);
        if (parkinglotKey != null)
            jsonObject.addProperty("parkinglotKey", parkinglotKey);
        if (carAccount != null)
            jsonObject.addProperty("carAccount", carAccount);
        if (carKey != null)
            jsonObject.addProperty("carKey", carKey);
        if (recycleAccount != null)
            jsonObject.addProperty("recycleAccount", recycleAccount);

        String result = OkHttpUtil.sendPostRequestWithUrl(url, jsonObject.toString()).body().string();
        return new Gson().fromJson(result, new TypeToken<Map<String, Object>>() {
        }.getType());

    }
    
   /**
    * 二次支付
    * @param orderNumber
    * @param carNumber
    * @param payment
    * @param carAccount
    * @param carKey
    * @param parkinglotAccount
    * @param nowTime
    * @return
    * @throws IOException
    */
    public static Map<String, Object> payAgain(
    		String orderNumber, 
    		String carNumber, 
    		Integer payment, 
    		String carAccount,
    		String carKey,
    		String parkinglotAccount,
    		String nowTime) throws IOException {
        String url = serverURL + "/pay/again";
        
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("carNumber", carNumber);
        jsonObject.addProperty("nowTime", nowTime);
        jsonObject.addProperty("orderNumber", orderNumber);
        jsonObject.addProperty("payment", payment);
        if (parkinglotAccount != null)
            jsonObject.addProperty("parkinglotAccount", parkinglotAccount);
        if (carAccount != null)
            jsonObject.addProperty("carAccount", carAccount);
        if (carKey != null)
            jsonObject.addProperty("carKey", carKey);

        String result = OkHttpUtil.sendPostRequestWithUrl(url, jsonObject.toString()).body().string();
        return new Gson().fromJson(result, new TypeToken<Map<String, Object>>() {
        }.getType());

    }
    
    

   

}
