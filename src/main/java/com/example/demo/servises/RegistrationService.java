package com.example.demo.servises;



import com.example.demo.models.Person;
import com.example.demo.repositories.PersonRepo;
import com.example.demo.util.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RegistrationService {

    private final PersonRepo repo;
    private final PasswordEncoder passwordEncoder;



    @SneakyThrows
    @Transactional
    public void registrationNewPerson(Person person){          ///Тут мы кодируем пришедший к нам пароль


        String encodedPassword = passwordEncoder.encode(person.getPassword());      ///и уже Person с зашифрованным паролем
        person.setPassword(encodedPassword);                                        ///улетает на сервер
        person.setPersonRole(UserRole.ROLE_USER);                                   ///
        person.setLast_active(new Timestamp(System.currentTimeMillis()));
        person.setAccount_created(new Date(System.currentTimeMillis()));
        person.setNotBanned(true);

        repo.save(person);                                                          ///
    }


    public boolean isExist(String email){ return repo.findPersonByEmail(email).isPresent();} // проверяем на совпадение




}
