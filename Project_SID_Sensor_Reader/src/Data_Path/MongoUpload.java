package Data_Path;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import Sensores.Connect_Sensor;

public class MongoUpload implements Runnable {

	
private Connect_Sensor cs;
private MongoClient mc;
private MongoDatabase db;
private MongoCollection<org.bson.Document> collection;
private ArrayList<ServerAddress> seeds = new ArrayList<ServerAddress>();



	public MongoUpload(Connect_Sensor cs) {
		
		this.cs=cs;
		connectToCluster();
		this.db=mc.getDatabase("lab_sensores");
		this.collection= db.getCollection("medicoes");
		
	}	
	
	
	
	
	@Override
	public void run() {
		while(true) {
			addMeasureToCollection(cs.sendMeasure());
		}}
	
	
	
	public void connectToCluster() {
		seeds.add(new ServerAddress("SamuelCamacho", 27000));
		seeds.add(new ServerAddress("SamuelCamacho", 27001));
		seeds.add(new ServerAddress("SamuelCamacho", 27002));
		this.mc=new MongoClient(seeds);
		System.out.println("entrou no mongo");

	}
	
	public String currentDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date= new Date();
		String s=dateFormat.format(date);
		return s;
	}
	
	
	public void addMeasureToCollection(MqttMessage msg) {
		
		org.bson.Document doc= org.bson.Document.parse(msg.toString());
		String []v=currentDate().split(" ");
		String d=v[0];
		String h=v[1];
		doc.append("migrado", "0");
		doc.replace("dat",d);
		doc.replace("tim",h);
		System.out.println(doc.toString()+ " vai assim po mongo");
		collection.insertOne(doc);
		
		}
	


}
