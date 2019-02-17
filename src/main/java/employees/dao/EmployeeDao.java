package employees.dao;

import employees.DBhelper;
import employees.models.Employee;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDao {

    static Statement stmt = null;

    public String addEmployee(Employee newEmployee) {
        String sql = String.format("insert into employee_service.employee (firstName, lastName, phone, status)\n" +
                        "values ('%s', '%s', '%s', '%s')",
                newEmployee.getFirstName(),
                newEmployee.getLastName(),
                newEmployee.getPhone(),
                newEmployee.getStatus());

        return executeUpdateQuery(sql) == 1 ? "User created sucessfully" : "Something went wrong. See logs";
    }

    public String removeEmployee(Long id) {
        String sql = String.format("update employee_service.employee set status = 'D' where id = %s", id);

        return executeUpdateQuery(sql)  == 1 ? "User " + id + " deleted sucessfully" : "Something went wrong. See logs";
    }

    public Employee findEmployeeById(Long employeeId) {
        String query = "Select * from employee_service.employee where id = " + employeeId;;

        Employee employee = executeSelectQuery(query).get(0);

        // TODO: Create Service class for building employees?
        employee.setContacts(new ContactDao().getContactsForEmployee(employeeId));

        return employee;
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

        try {
            System.out.println("Creating statement...");
            stmt = DBhelper.getConnection().createStatement();

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
            stmt.close();
            DBhelper.closeConnection();

        } catch (Exception se) {
            se.printStackTrace();
        }
        finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }
            if (DBhelper.getConnection() != null)
                DBhelper.closeConnection();
        }
        return employeeList;
    }

    public static int executeUpdateQuery(String sqlQuery){
        int res = 0;

        try {
            System.out.println("Creating statement...");
            stmt = DBhelper.getConnection().createStatement();

            res = stmt.executeUpdate(sqlQuery);
            System.out.println("Inserted records into the table... " + res);

            stmt.close();
            DBhelper.closeConnection();

        } catch (Exception se) {
            se.printStackTrace();
        } finally {
            try {
                if (stmt != null)
                    stmt.close();
            } catch (SQLException se2) {
            }
            if (DBhelper.getConnection() != null)
                DBhelper.closeConnection();
        }

        return res;
    }
}
