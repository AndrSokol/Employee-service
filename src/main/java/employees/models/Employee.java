package employees.models;

import lombok.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    private String firstName;
    private String lastName;
    private String phone;
    private Long id;
    private String status;
    private List<Contact> contacts;

    public Employee(String firstName, String lastName, String phone){
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public Employee(String firstName, String lastName, String phone, Long id, String status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.id = id;
        this.status = status;
    }
}
