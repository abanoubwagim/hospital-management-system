package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {

    private static final String driverName = "com.mysql.cj.jdbc.Driver";
    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String userName = "root";
    private static final String password = "";


    public static void main(String[] args) {

        try {
            Class.forName(driverName);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try(
                Connection connection = DriverManager.getConnection(url,userName,password);
                Scanner sc = new Scanner(System.in);
            ){
                Patient patient = new Patient(connection ,sc);
                Doctor doctor = new Doctor(connection,sc);
                while (true){
                    System.out.println();
                    System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                    System.out.println("1. Add Patient");
                    System.out.println("2. View Patients");
                    System.out.println("3. View Doctors");
                    System.out.println("4. Book Appointment");
                    System.out.println("5. Exit");
                    System.out.println("Enter your choice: ");
                    int choice = sc.nextInt();

                    switch (choice){
                        case 1:
                            patient.addPatient();
                            break;
                        case 2:
                            patient.viewPatient();
                            break;
                        case 3:
                            doctor.viewDoctor();
                            break;
                        case 4:
                            bookAppointment(patient,doctor,connection,sc);
                            break;
                        case 5:
                            return;
                        default:
                            System.out.println("Enter valid choice");
                            break;
                    }
                }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    private static void bookAppointment(Patient patient, Doctor doctor ,Connection connection , Scanner scanner){
        System.out.println("Enter Patient Id : ");
        int patientId = scanner.nextInt();
        System.out.println("Enter Doctor Id :");
        int doctorId = scanner.nextInt();
        System.out.println("Enter appointment data (YYYY-MM-DD)");
        String appointmentDate = scanner.next();

        if (patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)){

            if (checkDoctorAvailability(doctorId,appointmentDate,connection)){
                String appointmentQuery = "INSERT INTO appointment(patient_id,doctor_id,appointment_data) VALUES (?,?,?)";
                try(
                        PreparedStatement statement = connection.prepareStatement(appointmentQuery);
                        )
                {
                        statement.setInt(1,patientId);
                        statement.setInt(2,doctorId);
                        statement.setString(3,appointmentDate);

                        int rowsAffected = statement.executeUpdate();
                        if (rowsAffected > 0){
                            System.out.println("Appointment Booked ");
                        }else {
                            System.out.println("Failed to book Appointment");
                        }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }else {
                System.out.println("Doctor not available on this date");
            }
        }else {
            System.out.println("Either doctor or patient doesn't exist");
        }
    }

    private static boolean checkDoctorAvailability(int doctorId, String appointmentDate,Connection connection) {

        String query = "SELECT COUNT(*) FROM appointment WHERE doctor_id = ? AND appointment_data = ?";
        try (
                PreparedStatement statement = connection.prepareStatement(query);
                )
        {
            statement.setInt(1,doctorId);
            statement.setString(2,appointmentDate);
            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                int count = rs.getInt(1);
                if (count == 0 ){
                    return true;
                }else {
                    return false;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

            return false;
    }
}
