package org.example.mybooklibrary.contact;

import lombok.RequiredArgsConstructor;
import org.example.mybooklibrary.config.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactMessageRepository contactRepository;
    private final EmailService emailService;

    // Inject email address where you want to receive the messages
    @Value("${contact.receiver.email}")
    private String receiverEmail;

    public void saveContact(ContactRequest request) {

        ContactMessage message = new ContactMessage();
        message.setName(request.getName());
        message.setEmail(request.getEmail());
        message.setSubject(request.getSubject());
        message.setMessage(request.getMessage());
        contactRepository.save(message);


        String emailBody = "New Contact Message:\n\n"
                + "Name: " + request.getName() + "\n"
                + "Email: " + request.getEmail() + "\n"
                + "Subject: " + request.getSubject() + "\n"
                + "Message: \n" + request.getMessage();

        emailService.sendContactMessageEmail(receiverEmail, "📩 New Contact Message", emailBody);
    }
}
