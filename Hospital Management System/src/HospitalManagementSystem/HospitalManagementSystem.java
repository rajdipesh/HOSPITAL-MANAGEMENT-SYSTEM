package HospitalManagementSystem;

import com.mysql.cj.jdbc.Driver;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "Dipesh@4321";

    public static void main(String[] args) {
        //drivers load krenge jo ki database ke sath connect krne ke liye necessary hi
        try {
            //use class instance lo load all drivers
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Scanner scanner = new Scanner(System.in);

        try {
            //connection bnayenge database ke sath with connection interface instance ke sath
            Connection connection = DriverManager.getConnection(url, username, password);
            //dono classes ka object bnayenge
            Patients patients = new Patients(connection, scanner);
            Doctor doctor = new Doctor(connection);
            while (true){
            System.out.println("HOSPITAL MANAGEMENT SYSTEM");
            System.out.println("1. Add Patient");
            System.out.println("2. View Patient");
            System.out.println("3. View Doctor");
            System.out.println("4. Book Appointment");
            System.out.println("5. Exit");
            System.out.println("Enter Your Choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    //Add Patient
                    patients.AddPatients();
                    System.out.println();
                    break;
                case 2:
                    //View Patient
                    patients.viewPatients();
                    System.out.println();
                    break;
                case 3:
                    //View Doctor
                    doctor.viewDoctors();
                    System.out.println();
                    break;
                case 4:
                    //Book Appointment
                    bookAppointment(patients, doctor, connection, scanner);
                    System.out.println();
                    break;
                case 5:
                    System.out.println("THANK YOU! FOR USING HOSPITAL MANAGEMENT SYSTEM!");
                    return;
                default:
                    System.out.println("Plz Enter valid choice!!!");
                    break;
            }
        }
    }catch(SQLException e){
        e.printStackTrace();
    }

}
    //appointment book krne liye ek new method/function bnayenge
    /*connection-database se connect krega or scanner user se patient id lenge or fir denkhenge ki
    kya doctor ke id se match krenge kya wo us date ko present hi ki  nahi*/
    public static void bookAppointment(Patients patients,Doctor doctor,Connection connection,Scanner scanner){
        System.out.println("Enter Patient Id: ");
        int patientId=scanner.nextInt();
        System.out.println("Enter Doctor Id: ");
        int doctorId=scanner.nextInt();
        System.out.println("Enter Appointment date (YYYY-MM-DD): ");
        String appointmentDate =scanner.next();
        //to check patient and doctor is both available at same time or not
        if (patients.getPatientById(patientId) && doctor.getDoctorById(doctorId)){
             /*agr available hi to check krenge kya us date pr available hi ki nahi to ek boolean function bna denge kya doctor
            us date ko abavailable hi ki nhi*/
            if (checkDoctorAvailability(doctorId,appointmentDate,connection)){
                //or agr available hi to appointment book krenge to query bnayenge kyoki ab database se connect hoga kyoki database ke nader data ko input krne wale hi to check availability
                 String appointmentQuery="INSERT INTO appointments(patients_id, doctors_id,appointment_date) VALUES(?, ?, ?)";
                 try {
                     PreparedStatement preparedStatement=connection.prepareStatement(appointmentQuery);
                     //set values to placeholders
                     preparedStatement.setInt(1,patientId);
                     preparedStatement.setInt(2,doctorId);
                     preparedStatement.setString(3,appointmentDate);
                     //ek rowseffected bnayenge kya koi row effect huyi hi nhi or execute kr denge update nam ke method se jo preparedStatement me pra hua hi
                     int rowsAffected=preparedStatement.executeUpdate();
                     //now check
                     if (rowsAffected>0){
                         System.out.println("Appointment Booked!");
                     }else {
                         System.out.println("Failed to Book Appointment!!");
                     }
                 }catch (SQLException e){
                     e.printStackTrace();
                 }

            }else {
                System.out.println("Doctor not Available on this Date!!!");
            }

        }else {
            System.out.println("Either doctor or Patient Does not Exit!!!");
        }
    }
    //now create method of checkDoctorAvailability
    public static boolean checkDoctorAvailability(int doctorId,String appointmentDate,Connection connection){
        //count(*) ye no. of rows ko btayega hme pura data nhi nikalna hi
        String query = "SELECT COUNT(*) FROM appointments WHERE doctors_id = ? AND appointment_date = ?";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1,doctorId);
            preparedStatement.setString(2,appointmentDate);
            //now execute query using reasultset object
            ResultSet resultSet=preparedStatement.executeQuery();
            //next()-it ckeck all rows by setting pointer
            if (resultSet.next()){
                int count =resultSet.getInt(1);
                if (count==0){
                    return true;
                }else {
                    return false;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
       return false;

    }
}
