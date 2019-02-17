package employees.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Contact {
    private Long contactId;
    private String value;
    private String type;
    private String status;
    private Long employeeId;

    public Contact(String value, String type) {
        this.value = value;
        this.type = type;
    }
}
