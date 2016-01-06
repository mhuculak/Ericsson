import java.io.*;
public class TestEricsson {
    public static void main(String []argv) {
	EricssonCorporation ericsson = EricssonCorporation.getInstance();
	ericsson.computePay("Employee 1", 7.5, 30);
	ericsson.computePay("Employee 2", 8.2, 47);
        ericsson.computePay("Employee 3", 10, 73);
	//	ericsson.computePay("Employee test", 10, 45);
    }
}
