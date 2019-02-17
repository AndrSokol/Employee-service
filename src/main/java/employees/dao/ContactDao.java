package employees.dao;

import employees.DBhelper;
import employees.models.Contact;
import employees.models.Employee;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ContactDao {

    static Statement stmt = null;

    public List<Contact> getContactsForEmployee(Long employeeId){
        String query = String.format("Select * from employee_service.contact where employeeId = (%s)", employeeId);

        return executeSelectQuery(query);
    }

    public static List<Contact> executeSelectQuery(String query){
        List<Contact> contactList = new ArrayList<>();

        try {
            System.out.println("Creating statement...");
            stmt = DBhelper.getConnection().createStatement();

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Long contactId = rs.getLong("contactId");
                String value = rs.getString("value");
                String type = rs.getString("type");
                String status = rs.getString("status");
                Long employeeId = rs.getLong("employeeId");

                contactList.add(new Contact(contactId, value, type, status, employeeId));
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
        return contactList;
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

    public String createContact(Contact contact) {
        String query = String.format("insert into contact (value, type, status, employeeId)\n" +
                " values ('%s', '%s', '%s', %s)",
                contact.getValue(),
                contact.getType(),
                contact.getStatus(),
                contact.getEmployeeId()
                );

        return executeUpdateQuery(query) == 1 ? "Contact created sucessfully" : "Something went wrong. See logs";
    }
}
