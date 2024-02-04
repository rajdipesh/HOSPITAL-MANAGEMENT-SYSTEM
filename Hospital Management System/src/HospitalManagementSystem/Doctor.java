package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctor {
    //data members
    private Connection connection;
   // private Scanner scanner;

    //create constructor we make parameterised constructor because we assign values from outside
    Doctor(Connection connection) {
        this.connection = connection;
        //this.scanner = scanner;
    }




    //now create methods

    public void viewDoctors(){
        //read operation perform krna hi database se sara patient ka data layenge or usko console pr print kr denge
        String query="SELECT * FROM doctors";
        //we use try catch because when we use statement interface then it throwssql exception
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            //resultset table se jo bhi data aata hi usko hold krta next name ke method se ek pointer set krta hi usko print krate chla jata hi
            ResultSet resultSet=preparedStatement.executeQuery();
            System.out.println("Doctors: ");
            System.out.println("+-----------+-------------------+----------+----------+");
            System.out.println("| Doctor Id | Name              | Specialization      | ");
            System.out.println("+-----------+-------------------+----------+----------+");
            while (resultSet.next()){//next() method ek pointer set krega or line by line hmare data ko print kra dega
                //java local variable
                int id =resultSet.getInt("id");
                String name= resultSet.getString("name");
                String Specialization=resultSet.getString("Specialization");
                System.out.printf("| %-9s | %-17s | %-19s |\n",id, name, Specialization);
                System.out.println("+-----------+-------------------+----------+----------+");
            }
        }catch (SQLException e){//e is reference
            e.printStackTrace();
        }

    }
    public boolean getDoctorById(int id){
        String query="SELECT * FROM doctors WHERE id = ?";
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
