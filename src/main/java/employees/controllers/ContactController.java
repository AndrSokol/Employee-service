package employees.controllers;

import employees.dao.ContactDao;
import employees.models.Contact;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ContactController {

    ContactDao contactDao = new ContactDao();

    @GetMapping("employee/{id}/contact")
    public List<Contact> getContact(@PathVariable Long id){
        return contactDao.getContactsForEmployee(id);
    }

    @PostMapping("employee/{employeeId}/contact")
    public String createContact(@PathVariable Long employeeId, @RequestBody Contact contactToCreate){
        Contact contact = new Contact(
                contactToCreate.getValue(),
                contactToCreate.getType()
        );

        contact.setEmployeeId(employeeId);
        contact.setStatus("A");

        return contactDao.createContact(contact);
    }

}
