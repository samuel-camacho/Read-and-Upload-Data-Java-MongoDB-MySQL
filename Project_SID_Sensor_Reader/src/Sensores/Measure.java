package Sensores;

public class Measure {
	



private double tValue;
private double lValue;
private String date;





public Measure(double tValue, double lValue, String date) {
	
	this.tValue= tValue;
	this.lValue=lValue;
	this.date=date;
}






public double getTempValue() {
	
	return tValue;
}

public double getLightValue() {
	
	return lValue;
}


public String getDay() {
	
	return date;
}

}
