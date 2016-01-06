import java.io.*;
public class EricssonCorporation {
    private static double overtime_threshold = 40;  // overtime payed when hours exceed 40 per week
    private static double overtime_rate = 0.5;      // overtime payed at 50% extra on top of regular salary
    private static double minimum_salary = 8.0;       
    private static double maximum_hours = 60.0;
    private static EricssonCorporation instance = null;
    public static EricssonCorporation getInstance() {
	if (instance == null) {
	    instance = new EricssonCorporation();
	}
	return instance;
    }
    //
    // Compute Employee payment from salary and hours worked.
    // Returns: false - if there is an error in the input data
    //          true - if the payment was successfully calculated
    //
    public static boolean computePay(String name, double salary, double hours) {
	if (salary < minimum_salary) {
	    System.out.format("%s - ERROR: salary of $%.2f is below the minimum allowed of $%.2f\n",name,salary,minimum_salary);
	    return false;
	}
	if (hours < 0) {
	    System.out.println(name + " - ERROR: hours worked of " + hours + " cannot be negative");
	    return false;
	}
	if (hours > maximum_hours) {
	    System.out.println(name + " - ERROR: hours worked of " + hours + " exceeds the maximum limit of " + maximum_hours );
	    return false;
	}
	double overtime_hours = 0;
	if (hours > overtime_threshold) {
	    overtime_hours = hours - overtime_threshold;
	}
	double pay = salary*(hours + overtime_hours*overtime_rate);
	System.out.format("%s pay is $%.2f for a salary of $%.2f per hour and work of %f hours\n", name, pay, salary, hours);
	return true;
    }
}
