package edu.buaa.sem.interfaces.app;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

/**
 * 建议测试时按照下列顺序一个接口一个接口测试，因为每一个接口都有可能用到其上面接口的数据
 * @author miao
 *
 */
public class ParkTest {
	@Test
    public void testRegisterParkinglot() throws IOException {
        Map<String, Object> map = ParkUtil.registerParkinglot("19002080345", "eP1P1bc3PbxU93v7yia5DKkhTyEMEoEwxa", "sniQ9YSE7GXoLihXLxHYD2N9jrcC6", "2018-1-21 20:00");
        System.out.println(map.get("result"));
        System.out.println(map.get("parkinglotAccount"));  
        System.out.println(map.get("parkinglotKey"));

    }
	
	@Test
    public void testPublishParkingSpace() throws IOException {
        Map<String, Object> map = ParkUtil.publishParkingSpace("19002080345", 1, "2018-5-5", "2018-5-10", "2345", "08:00", "18:00","eP1P1bc3PbxU93v7yia5DKkhTyEMEoEwxa",
        		"sniQ9YSE7GXoLihXLxHYD2N9jrcC6", "eL85P6tPrnScZZ5F5S5uncxMzH5oqWjRAY", "2018-4-30 20:15", "D", "08", "05", "1", "001");
        System.out.println(map.get("result"));
    }
	
    @Test
    public void testRegisterCar() throws IOException {
        Map<String, Object> map = ParkUtil.registerCar("京A88888", "2018-1-21 17:00", "eP1P1bc3PbxU93v7yia5DKkhTyEMEoEwxa", "sniQ9YSE7GXoLihXLxHYD2N9jrcC6");
        System.out.println(map.get("result"));
        System.out.println(map.get("carAccount"));  
        System.out.println(map.get("carKey"));

    }
    
    @Test
    public void testFindReserveTime() throws IOException {
        Map<String, Object> map = ParkUtil.findReserveTime("19002080345", "eL85P6tPrnScZZ5F5S5uncxMzH5oqWjRAY", "2018-1-21 15:43", "D", "01", "05", "1", "001");
        System.out.println(map.get("result"));
        System.out.println(map.get("parkinglotNumber"));  
        System.out.println(map.get("accessStartTime"));
        System.out.println(map.get("accessEndTime"));
    }
    
    @Test
    public void testReserveParkingSpace() throws IOException {
        Map<String, Object> map = ParkUtil.reserveParkingSpace("19002080345", "京A88888", "2018-1-23 09:00", "2018-1-23 10:00", "eL85P6tPrnScZZ5F5S5uncxMzH5oqWjRAY", "spmjAzrQcy6nNZnRzZ8SiZF4iPT6x","eKRgFC5ZwGLFSUKfo5T37qZxvvLK5u6SnP", "es9u3JBfPZGESaUHDmq8sPy9E8KKqAjFbs", "2018-01-21 18:00");
        System.out.println(map.get("result"));
        System.out.println(map.get("reason"));  
        System.out.println(map.get("orderNumber"));
    }

	@Test
    public void testPaySuccess() throws IOException {
        Map<String, Object> map = ParkUtil.paySuccess("OR京A88888190020803452018123090020181231000352", "19002080345", "京A88888", "eL85P6tPrnScZZ5F5S5uncxMzH5oqWjRAY", "eKRgFC5ZwGLFSUKfo5T37qZxvvLK5u6SnP","ssao7VPdtyvhSyRPhQnjr9RzGgv6h", "es9u3JBfPZGESaUHDmq8sPy9E8KKqAjFbs", "2018-01-21 20:00");
        System.out.println(map.get("result"));
    }
    
    
	@Test
    public void testPayFail() throws IOException {
        Map<String, Object> map = ParkUtil.payFail("OR京A88888190020803452018123090020181231000352", "19002080345", "京A88888", "eL85P6tPrnScZZ5F5S5uncxMzH5oqWjRAY", "eKRgFC5ZwGLFSUKfo5T37qZxvvLK5u6SnP","ssao7VPdtyvhSyRPhQnjr9RzGgv6h", "2018-1-21 20:00");
        System.out.println(map.get("result"));
    }

    @Test
    public void testInRequire() throws IOException {
        Map<String, Object> map = ParkUtil.inRequire("OR京A88888190020803452018123090020181231000352", "19002080345", "京A88888", "eL85P6tPrnScZZ5F5S5uncxMzH5oqWjRAY", "es9u3JBfPZGESaUHDmq8sPy9E8KKqAjFbs", "2018-1-21 20:00");
        System.out.println(map.get("result"));
    }
    
    @Test
    public void testOutRequire() throws IOException {
        Map<String, Object> map = ParkUtil.outRequire("OR京A88888190020803452018123090020181231000352", "19002080345", "京A88888", 300, "eL85P6tPrnScZZ5F5S5uncxMzH5oqWjRAY", "spmjAzrQcy6nNZnRzZ8SiZF4iPT6x", "es9u3JBfPZGESaUHDmq8sPy9E8KKqAjFbs", "shQedwxfzhkHVyveAk8mbEZTcnat6", "eGEkpiS2YDMT7BF9eZC2fHRa6x3MroVtWD", "2018-1-21 20:00");
        System.out.println(map.get("result"));
        System.out.println(map.get("omitOrderId"));
    }
    
    @Test
    public void testPayAgain() throws IOException {
        Map<String, Object> map = ParkUtil.payAgain("OR京A88888190020803452018123090020181231000352", "京A88888", 300, "es9u3JBfPZGESaUHDmq8sPy9E8KKqAjFbs", "shQedwxfzhkHVyveAk8mbEZTcnat6", "eL85P6tPrnScZZ5F5S5uncxMzH5oqWjRAY", "2018-1-21 20:00");
        System.out.println(map.get("result"));
    }

}
