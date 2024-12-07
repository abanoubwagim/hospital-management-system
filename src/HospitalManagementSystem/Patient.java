package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {

    private Connection connection;
    private Scanner sc;

    public Patient(Connection connect , Scanner scanner){
        this.connection = connect;
        this.sc = scanner;
    }

    public void addPatient(){
        System.out.print("Enter Patient Name : ");
        String name = sc.next();
        System.out.print("Enter Patient Age : ");
        int age = sc.nextInt();
        System.out.print("Enter Patient Gender : ");
        String gender = sc.next();


        try {
            String query = "INSERT INTO patients(name,age,gender)VALUES (?,?,?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,name);
            statement.setInt(2,age);
            statement.setString(3,gender);

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0 ){
                System.out.println("Patient Added Successfully");
            }else {
                System.out.println("Failed to add Patient");
            }

        }catch (SQLException e){
            System.out.println(e.getMessage());
        }

    }
    public void viewPatient(){
        String query = "select *from patients";
        try {
                PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            System.out.println("Patients: ");
            System.out.println("+--------------+-----------+----------+---------+--------------+");
            System.out.println("| Patient Id | Name               | Age      | Gender     |");
            System.out.println("+--------------+-----------+----------+---------+--------------+");
            while (rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("Name");
                int age = rs.getInt("age");
                String gender = rs.getString("gender");
                System.out.printf("|%-12s|%-20s|%-10s|%-12s\n",id,name,age,gender);
                System.out.println("+--------------+-----------+----------+---------+--------------+");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public boolean getPatientById(int id ){
        String query = "SELECT *FROM patients WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()){
                return true;
            }else {
                return false;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }


}
