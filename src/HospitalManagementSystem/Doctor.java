package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Doctor {

    private Connection connection;
    private Scanner sc;

    public Doctor(Connection connect , Scanner scanner){
        this.connection = connect;
        this.sc = scanner;
    }

    public void viewDoctor(){
        String query = "select *from doctors";
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            System.out.println("Doctors: ");
            System.out.println("+--------------+-----------+----------+---------+");
            System.out.println("| Doctor Id  | Name               | Specialization   ");
            System.out.println("+--------------+-----------+----------+---------+");
            while (rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("Name");
                String specialization = rs.getString("specialization");
                System.out.printf("|%-12s|%-20s|%-18s\n",id,name,specialization);
                System.out.println("+--------------+-----------+----------+---------+");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean getDoctorById(int id ){
        String query = "SELECT *FROM doctors WHERE id = ?";
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
