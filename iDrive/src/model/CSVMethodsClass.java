package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Part;

public class CSVMethodsClass {
	private String file;

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}
	
	private void readCSV(Connection connection){
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("C:/iDriveCSV/"+file));
			
			//String headerLine = br.readLine(); used to skip the first row.
			String line = br.readLine();
			
			while (line != null){
				
				String columns[] = line.split(",");
				
				if(columns.length == 6){
					if(validateName(columns[0]) == true && validateName(columns[1]) == true && validateEmail(columns[2]) == true
							&& validateID(columns[4]) == true && validatePosition(columns[5])==true){
						System.out.println("\nFirst Name: " + columns[0]);
						System.out.println("Last Name: " + columns[1]);
						System.out.println("Email: " + columns[2]);
						System.out.println("Department: " + columns[3]);
						System.out.println("Employee ID: " + Integer.parseInt(columns[4]));
						System.out.println("Position: " + columns[5]);
					} 
				} else if (columns.length == 7){
					if(validateName(columns[0]) == true && validateName(columns[1]) == true && validateEmail(columns[2]) == true
							&& validateID(columns[4]) == true && validatePosition(columns[5])==true && validateAction(columns[6]) == true){
						System.out.println("\nFirst Name: " + columns[0]);
						System.out.println("Last Name: " + columns[1]);
						System.out.println("Email: " + columns[2]);
						System.out.println("Department: " + columns[3]);
						System.out.println("Employee ID: " + Integer.parseInt(columns[4]));
						System.out.println("Position: " + columns[5]);
						System.out.println("Action: " + columns[6]);
						boolean exists = duplicate(connection, Integer.parseInt(columns[4]));
						executeQuery(connection, columns, exists);
					}
				} else {
					System.out.println(columns.length);
				}
				line = br.readLine();
				
				
		}
			
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static boolean validateName(String text){
		String regex = "^[\\p{L} .'-]+$";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(text);
		return matcher.find();
	}
	
	private static boolean validatePosition(String text){
		if(text.equals("Employee")){
			return true;
		}else if(text.equals("Manager")){
			return true;
		}else if(text.equals("Admin")){
			return true;
		}else{
			return false;
		}
	}
	
	private static boolean validateEmail(String text){
		String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);
		return matcher.matches();
	}
	
	private static boolean validateID(String text){
		if(text.matches("[0-9]+") && text.length() == 9){
			return true;
		} else {
			return false;
		}
	}
	
	private static boolean validateAction(String text){
		if(text.length() == 1){
			if(text.toUpperCase().equals("A") || text.toUpperCase().equals("D") || text.toUpperCase().equals("U")){
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	private void checkDirectory(){
		File file = new File("C:\\iDriveCSV");
		
		if(!file.exists()){
			if(file.mkdir()){
				System.out.println("Directory Created!");
			} else {
				System.out.println("Failed to create Directory!");
			}
		} else {
			System.out.println("Directory already exists!");
		}
	}
	
	private void createFile(Part filePart) throws IOException{
		
		OutputStream out = null;
		InputStream fileContent = null;
		
		try{
			out = new FileOutputStream(new File("C:/iDriveCSV/"+file));
			fileContent = filePart.getInputStream();
			
			int read = 0;
		        final byte[] bytes = new byte[1024];

		        while ((read = fileContent.read(bytes)) != -1) {
		            out.write(bytes, 0, read);
		        }
		        
		        System.out.println("New file " + file + " created at " + "C:/iDriveCSV/"+file);
		        
		} catch (FileNotFoundException fne){
			System.out.println("File was not found!");
		} catch (IOException io){
			System.out.println("IO Exception!");
		} finally {
			 if (out != null) {
		            out.close();
		        }
		        if (fileContent != null) {
		            fileContent.close();
		        }
		}
	}
	
	private boolean duplicate(Connection connection, int empID){
		try{
			String duplicateChecker = "SELECT * FROM employee WHERE employeeID = ?";
			PreparedStatement pstmt = connection.prepareStatement(duplicateChecker);
			pstmt.setInt(1, empID);
			ResultSet rs = 	pstmt.executeQuery();
			if(rs.next()){
				return true;
			}else{
				return false;
			}
		}catch(SQLException sqle){
			System.out.println(sqle);
			return false;
		}
		
	}
	
	private void executeQuery(Connection connection, String columns[], boolean result){
		try{

			int accType = 0;
			
			if(columns[5].equals("Manager")){
				accType =2;
			}else if(columns[5].equals("Admin")){
				accType=3;
			}else{
				accType = 1;
			}

			String query="";
			if(columns[6].equals("A") && result == false){
				query ="INSERT INTO employee (employeeID, firstName, lastName, departmentName, email, pass, accountTypeID) values (?,?,?,?,?,?,?)";
				PreparedStatement pstmt = connection.prepareStatement(query);
				pstmt.setInt(1, Integer.parseInt(columns[4])); //empId
				pstmt.setString(2, columns[0]); //firstname
				pstmt.setString(3, columns[1]); //lastname
				pstmt.setString(4, columns[3]); //department
				pstmt.setString(5,columns[2]); //email
				pstmt.setString(6, columns[4]+columns[1]);//pass (default is employee num+lastname)
				pstmt.setInt(7, accType); //accountTypeID
				pstmt.executeUpdate();
			}else if(columns[6].equals("D") && result == true){
				query="DELETE FROM employee WHERE employeeID = ?";
				PreparedStatement pstmt = connection.prepareStatement(query);
				pstmt.setInt(1, Integer.parseInt(columns[4])); //empId
				pstmt.executeUpdate();
			}else if(columns[6].equals("U") && result == true){
				query="UPDATE employee SET employeeID = ? , firstName = ? , lastName = ? , departmentName = ? , email = ? , accountTypeID = ? WHERE employeeID = ?";
				PreparedStatement pstmt = connection.prepareStatement(query);
				pstmt.setInt(1, Integer.parseInt(columns[4])); //empId
				pstmt.setString(2, columns[0]); //firstname
				pstmt.setString(3, columns[1]); //lastname
				pstmt.setString(4, columns[3]); //department
				pstmt.setString(5,columns[2]); //email
				pstmt.setInt(6, accType); //accountTypeID
				pstmt.setInt(7,Integer.parseInt(columns[4]));
				pstmt.executeUpdate();
			}else{
//				query="SELECT * FROM employee WHERE employeeID = ?";
//				PreparedStatement pstmt = connection.prepareStatement(query);
//				pstmt.setInt(1, Integer.parseInt(columns[4])); //empId
//				pstmt.executeUpdate();
			}
		}catch(SQLException sqle){
			System.out.println(sqle);
		}
	}
	
	public void process(Part filePart, Connection connection) throws IOException{
		checkDirectory();
		createFile(filePart);
		readCSV(connection);
	}
}
