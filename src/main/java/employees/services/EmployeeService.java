package employees.services;

import employees.dao.ContactDao;
import employees.dao.EmployeeDao;
import employees.models.Contact;
import employees.models.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    EmployeeDao employeeDao;

    @Autowired
    ContactDao contactDao;

    public List<Employee> retrieveAllEmployees(String status) {
        List<Employee> employeeList = employeeDao.getAllEmployees(status);

        List<String> employeeIds = employeeList.stream()
                .map(e -> e.getId().toString()).collect(Collectors.toList());

        List<Contact> contactList = contactDao.getAllContacts(employeeIds);

        employeeList.forEach(e -> e.setContacts(getContactsById(contactList, e.getId())));

//        TODO: Why id doesn't work?
//        employeeList.stream()
//                .map(e -> e.setContacts(getContactsById(contactList, e.getId()))
//                ).collect(Collectors.toList());

        return employeeList;
    }

    private List<Contact> getContactsById(List<Contact> contactList, Long id) {
        return contactList.stream()
                .filter(c -> c.getEmployeeId().equals(id))
                .collect(Collectors.toList());
    }

    public Employee retrieveEmployeeById(Long id) {
        Employee employee = employeeDao.findEmployeeById(id);
        if (employee == null){
            return null;
        }
        employee.setContacts(contactDao.getContactsForEmployee(id));

        return employee;
    }

    public String createEmployee(Employee newEmployee) {
        return employeeDao.addEmployee(newEmployee);
    }

    public void modifyEmmployee(Employee employeeToUpdate) {
        employeeDao.updateEmployee(employeeToUpdate);
    }

    public String removeEmployee(Long id) {
        return employeeDao.deleteEmployee(id);
    }
}
