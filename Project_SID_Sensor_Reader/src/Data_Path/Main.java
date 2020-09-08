package Data_Path;

import Sensores.ConfigurationFileReader;
import Sensores.Connect_Sensor;

public class Main {

	
public static void main(String[]args) {
	
ConfigurationFileReader build_file= new ConfigurationFileReader();	
Connect_Sensor cs= new Connect_Sensor(build_file);
MongoUpload mp= new MongoUpload(cs);
MongoDownload md= new MongoDownload(build_file);
Thread t= new Thread(mp);
t.start();
Thread t2= new Thread(md);
t2.start();

	}
}
