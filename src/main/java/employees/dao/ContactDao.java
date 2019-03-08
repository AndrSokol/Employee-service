package employees.dao;

import employees.DBhelper;
import employees.models.Contact;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ContactDao {

    public List<Contact> getContactsForEmployee(Long employeeId) {

        String selectContactByEmployeeId = "Select c.* from employee_service.contact as c join employee_service.employee e\n" +
                " on c.employeeId = e.id\n" +
                " where c.employeeId in (%s) and e.status = 'A'";

        String query = String.format(selectContactByEmployeeId, employeeId);

        return executeSelectQuery(query);
    }

    public static List<Contact> executeSelectQuery(String query) {
        List<Contact> contactList = new ArrayList<>();

        try (Connection conn = DBhelper.getConnection();
             Statement stmt = conn.createStatement()) {
            log.info(String.format("Execute SQL query: %s", query));

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

        } catch (Exception se) {
            se.printStackTrace();
        }
        return contactList;
    }

    public static int executeUpdateQuery(String sqlQuery) {
        int res = 0;

        log.info(String.format("Execute insert query: %s", sqlQuery));

        try (Connection conn = DBhelper.getConnection();
             Statement stmt = conn.createStatement()) {

            res = stmt.executeUpdate(sqlQuery);
            log.info(String.format("Inserted records into the table: %s", res));

        } catch (Exception se) {
            se.printStackTrace();
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

    public List<Contact> getAllContacts(List<String> employeeIds) {

        String idsStr = employeeIds.stream().collect(Collectors.joining(", ", "", ""));

        String query = String.format("Select * from employee_service.contact where employeeId in (%s)", idsStr);

        return executeSelectQuery(query);
    }
}
