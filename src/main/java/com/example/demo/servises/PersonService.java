package com.example.demo.servises;


import com.example.demo.models.Person;
import com.example.demo.repositories.PersonRepo;
import com.example.demo.security.PersonDetails;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PersonService implements UserDetailsService {

    private final JdbcTemplate jdbcTemplate;
    private final PersonRepo repo;


    public boolean isAuth(){
        int currentId = getCurrentUserID();
        if ( currentId != -1){
            return true;
        }
        return false;
    }           //проверка на авторизацию пользователя в системе

    private int getCurrentUserID() {
        return 0;
    }

    public Person getCurrentUser(){
        int currentId = getCurrentUserID();
        if ( currentId != -1){
            return repo.findById(currentId).get();
        }
        return null;
    }    //получение текущего пользователя



    @Transactional
    public void UpdatePerson(Person person){
        jdbcTemplate.update("update Person set first_name=?, last_name=? where email = ?",
                person.getFirst_name(),
                person.getLast_name(),
                person.getEmail()
        );
    }

    @Transactional
    public void save(Person person){ repo.save(person); };



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Person> user = repo.findPersonByEmail(email); //пытаемся получить из БД человека с Таким Email-ом

        if(user.isEmpty()) {
            log.info("Попытка войти в систему под не зарегистрированной почтой {}", email);
            throw new UsernameNotFoundException("Пользователь с такой почтой не зарегистрирован");
        }
        return new PersonDetails(user.get());
    }

}
