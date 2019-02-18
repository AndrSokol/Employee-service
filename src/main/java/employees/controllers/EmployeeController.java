package employees.controllers;

import employees.dao.EmployeeDao;
import employees.models.Employee;
import employees.services.EmployeeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {


    EmployeeService employeeService = new EmployeeService();

    @GetMapping("/employee")
    public List<Employee> getAllEmployees(@RequestParam(name = "status", required = false) String status) {
        return employeeService.retrieveAllEmployees(status);
    }

    @GetMapping("/employee/{id}")
    public Employee getById(@PathVariable Long id) {
        return employeeService.retrieveEmployeeById(id);
    }

    @PostMapping("/employee")
    public String createEmployee(@RequestBody Employee employeeToCreate) {

        Employee newEmployee = new Employee(
                employeeToCreate.getFirstName(),
                employeeToCreate.getLastName(),
                employeeToCreate.getPhone());

        newEmployee.setStatus("A");

        String result = employeeService.createEmployee(newEmployee);

        return result;
    }

    @PutMapping("/employee")
    public Employee updateEmployee(@RequestBody Employee employeeUpdateData){
        Employee employeeToUpdate = employeeService.retrieveEmployeeById(employeeUpdateData.getId());

        if(employeeUpdateData.getFirstName() != null)
            employeeToUpdate.setFirstName(employeeUpdateData.getFirstName());

        if(employeeUpdateData.getLastName() != null)
            employeeToUpdate.setLastName(employeeUpdateData.getLastName());

        if(employeeUpdateData.getPhone() != null)
            employeeToUpdate.setPhone(employeeUpdateData.getPhone());

        employeeService.modifyEmmployee(employeeToUpdate);
        return employeeToUpdate;
    }

    @DeleteMapping("employee/{id}")
    public String deleteById(@PathVariable Long id){
        Employee employeeToDelete = employeeService.retrieveEmployeeById(id);

        if(employeeToDelete != null){
            String res = employeeService.removeEmployee(id);
            return res;
        } else {
            return String.format("404. Employee with id = %s is not present", id);
        }
    }
}
