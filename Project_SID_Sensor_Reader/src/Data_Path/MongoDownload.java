package Data_Path;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.bson.Document;
import org.bson.conversions.Bson;


import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import Sensores.ConfigurationFileReader;
import Sensores.Measure;


public class MongoDownload implements Runnable{
	
private MongoClient mc;
private ConfigurationFileReader build_file;
private MongoDatabase db;
private MongoCollection<org.bson.Document> collection;
private Connection sql_connector;
private FindIterable<Document> list;
private boolean transf_complete=false;
private ArrayList<ServerAddress> seeds = new ArrayList<ServerAddress>();





	public MongoDownload(ConfigurationFileReader build_file) {
		
		this.build_file=build_file;
		connectToCluster();
		this.db=mc.getDatabase(build_file.getMongoDBName());
		System.out.println("entrou db");
		this.collection=db.getCollection(build_file.collectionName());
		System.out.println("entrou coleccao");
		connectToSQL();

	}

@Override
public void run() {
	while(true) {
		try {
			System.out.println("O Processo de Obtenção de dados Mongo Vai Iniciar!");
			getMeasures();
			System.out.println("Inicio de Espera de 20 segundos até Recomeçar!");
			Thread.sleep(build_file.getSleepTime());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}	

public void connectToCluster() {
	seeds.add(new ServerAddress("SamuelCamacho", 27000));
	seeds.add(new ServerAddress("SamuelCamacho", 27001));
	seeds.add(new ServerAddress("SamuelCamacho", 27002));
	this.mc=new MongoClient(seeds);
	System.out.println("entrou no mongo");

}
	
	
public void connectToSQL() {
System.out.println("jdbc:mysql://localhost:3306/"+build_file.getSQLDB());
	try {
		Class.forName("com.mysql.jdbc.Driver");
		this.sql_connector = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+build_file.getSQLDB(),build_file.getSQLUser(),"grupo6");
	} catch (ClassNotFoundException  | SQLException e) {
		e.printStackTrace();
		e.printStackTrace();
		}
	}
	
	
public void getMeasures() {
Bson filter= Filters.eq("migrado", "0");	
list= collection.find(filter);
int n=0;
for(Document doc: list){
	Measure m= treatDoc(doc);
	sendMeasures(m);
	     if(transf_complete) {
	    	Bson id= Filters.eq("_id",doc.getObjectId("_id"));
	    		Bson migrated= Updates.set("migrado", "1");
	    			collection.findOneAndUpdate(id, migrated);
	    				System.out.println(n + " - Mais um Doc Updated");
	    				n++;	        
	    		}
	    else {
	    	System.out.println("Transferência não efectuada, Nao foi feito Update aos Dados");
	    	}
	  }
}
	   
	
	
public void sendMeasures(Measure m) {
transf_complete=false;
String query="";
PreparedStatement ps;
	if(m!=null) {
		try {
		
				query= "INSERT INTO medicoes_temperatura (`IDMedicao`, `Valor_Medicao_Temperatura`, `DataHoraMedicao`) values(null," +m.getTempValue()+ ",'" + m.getDay() + "')";	
					ps = sql_connector.prepareStatement(query);
						ps.execute(query);
		
			
			
				query= "INSERT INTO medicoes_luminosidade (`IDMedicao`, `Valor_Medicao_Luminosidade`, `DataHoraMedicao`) values(null," +m.getLightValue()+ ",'" + m.getDay() + "')";	
					ps = sql_connector.prepareStatement(query);
						ps.execute(query);
			
			} catch (SQLException e) {
	e.printStackTrace();
		}
transf_complete=true;
System.out.println( " - One Measure Sented To MYSQL - " + "Valor Temp: " + m.getTempValue() + " Valor Luz: " + m.getLightValue() + " / Data: " + m.getDay());
	}
}



public Measure treatDoc(Document doc) {

Measure m=null;

String dateTime= doc.getString("dat").concat(" "+doc.getString("tim"));
m= new Measure(Double.parseDouble(doc.getString("tmp")), Double.parseDouble(doc.getString("cell")), dateTime);

	
return m;
	}
}
