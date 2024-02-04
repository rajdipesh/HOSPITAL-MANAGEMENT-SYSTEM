package HospitalManagementSystem;

import com.mysql.cj.x.protobuf.MysqlxPrepare;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patients {
    //data members
    private Connection connection;
    private Scanner scanner;

    //create constructor we make parameterised constructor because we assign values from outside
    Patients(Connection connection,Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }


        //now create methods
       public void AddPatients(){
            System.out.print("Enter Patient Name: ");
            String name= scanner.next();
            System.out.print("Enter Patient Age: ");
            int age=scanner.nextInt();
           System.out.print("Enter Patient Gender: ");
           String gender =scanner.next();

           /*now we use try catch because now we work with database & when we make connection interface it gives exception i.e
           is called sql exception */

           try {
                /*WE use placeholder that is prepared statement jisse hm bad me value ko replace kr skte user ke mutabik*/
               String query= "INSERT INTO patients(name, age, Gender) VALUES(?, ?, ?)";
               PreparedStatement preparedStatement = connection.prepareStatement(query);
               preparedStatement.setString(1,name);
               preparedStatement.setInt(2,age);
               preparedStatement.setString(3,gender);
               int affectedRows =preparedStatement.executeUpdate();//it return int values ki hmare database ke ander particular query se kitne rows effect huye hi
               if (affectedRows>0){
                   System.out.println("Patient Added Successfully!!");
               }else {
                   System.out.println("Failed To Add Patient!!");
               }
           }catch (SQLException e){
               e.printStackTrace();
           }

        }
        public void viewPatients(){
        //read operation perform krna hi database se sara patient ka data layenge or usko console pr print kr denge
            String query="SELECT * FROM patients";
            //we use try catch because when we use statement interface then it throwssql exception
            try {
                PreparedStatement preparedStatement=connection.prepareStatement(query);
                //resultset table se jo bhi data aata hi usko hold krta next name ke method se ek pointer set krta hi usko print krate chla jata hi
                ResultSet resultSet=preparedStatement.executeQuery();
                System.out.println("Patients: ");
                System.out.println("+------------+-------------------+----------+------------+");
                System.out.println("| Patient Id | Name              | age      | Gender     | ");
                System.out.println("+------------+-------------------+----------+------------+");
                while (resultSet.next()){//next() method ek pointer set krega or line by line hmare data ko print kra dega
                    //java local variable
                    int id =resultSet.getInt("id");
                    String name= resultSet.getString("name");
                    int age =resultSet.getInt("age");
                    String Gender=resultSet.getString("gender");
                    System.out.printf("|%-12s|%-19s|%-10s|%-12s\n", id, name, age, Gender);
                    System.out.println("+------------+-------------------+----------+------------+");
                }
            }catch (SQLException e){//e is reference
                e.printStackTrace();
            }

        }
        public boolean getPatientById(int id){
         String query="SELECT * FROM patients WHERE id = ?";
         try{
             PreparedStatement preparedStatement=connection.prepareStatement(query);
             preparedStatement.setInt(1,id);
             ResultSet resultSet=preparedStatement.executeQuery();
             if (resultSet.next()){
                 return true;
             }else {
                 return false;
             }
         }catch (SQLException e){
             e.printStackTrace();
         }
         return false;
        }
    }



