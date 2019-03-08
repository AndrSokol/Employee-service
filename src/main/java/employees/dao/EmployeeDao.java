package employees.dao;

import employees.DBhelper;
import employees.models.Employee;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Component
public class EmployeeDao {

    public String addEmployee(Employee newEmployee) {
        String sql = String.format("insert into employee_service.employee (firstName, lastName, phone, status)\n" +
                        "values ('%s', '%s', '%s', '%s')",
                newEmployee.getFirstName(),
                newEmployee.getLastName(),
                newEmployee.getPhone(),
                newEmployee.getStatus());

        return executeUpdateQuery(sql) == 1 ? "User created sucessfully" : "Something went wrong. See logs";
    }

    public String deleteEmployee(Long id) {
        String sql = String.format("update employee_service.employee set status = 'D' where id = %s", id);

        return executeUpdateQuery(sql)  == 1 ? "User " + id + " deleted sucessfully" : "Something went wrong. See logs";
    }

    public Employee findEmployeeById(Long employeeId) {
        String query = "Select * from employee_service.employee where status = 'A' and id = " + employeeId;
        List<Employee> employeeList = executeSelectQuery(query);
        return employeeList.size() != 0 ? executeSelectQuery(query).get(0) : null;
    }

    public List<Employee> getAllEmployees(String status) {
        String statusClause = "'A'";

        if("deleted".equals(status)) {
            statusClause = "'D'";
        } else if("all".equals(status)){
            statusClause = "'A', 'D'";
        }

        String query = String.format("Select * from employee_service.employee where status in (%s)", statusClause);

        return executeSelectQuery(query);
    }

    public String updateEmployee(Employee employeeToUpdate) {
        String sql = String.format(
                "update employee_service.employee set firstName = '%s', lastName = '%s', phone = '%s'" +
                        " where id = %s",
                employeeToUpdate.getFirstName(),
                employeeToUpdate.getLastName(),
                employeeToUpdate.getPhone(),
                employeeToUpdate.getId());

        return executeUpdateQuery(sql) == 1 ? "User created sucessfully" : "Something went wrong. See logs";
    }

    public static List<Employee> executeSelectQuery(String query){
        List<Employee> employeeList = new ArrayList<>();

        try (Connection conn = DBhelper.getConnection();
             Statement stmt = conn.createStatement()
                ) {

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String phone = rs.getString("phone");
                Long id = rs.getLong("id");
                String status = rs.getString("status");

                employeeList.add(new Employee(firstName, lastName, phone, id, status));
            }

            rs.close();

        } catch (Exception se) {
            se.printStackTrace();
        }
        return employeeList;
    }

    public static int executeUpdateQuery(String sqlQuery){
        int res = 0;

        try (Connection conn = DBhelper.getConnection();
             Statement stmt = conn.createStatement() ) {

            res = stmt.executeUpdate(sqlQuery);
            System.out.println("Inserted records into the table... " + res);

        } catch (Exception se) {
            se.printStackTrace();
        }

        return res;
    }
}
