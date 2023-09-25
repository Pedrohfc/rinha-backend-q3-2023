package org.rinha;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import org.rinha.model.Person;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@ApplicationScoped
public class PersonService
{
    @Inject
    private DataSource dataSource;

    public void validatePerson(@Valid Person person)
    {
        LocalDate.parse(person.getNascimento(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public void createPerson(Person person) throws SQLException
    {
        try (Connection conn = dataSource.getConnection())
        {
            try (PreparedStatement stmt = conn.prepareStatement("INSERT INTO person (person_id, nick_name, name, birth_date, stack) values (UUID_TO_BIN(?), ?, ?, ?, ?)"))
            {
                stmt.setString(1, person.getId());
                stmt.setString(2, person.getApelido());
                stmt.setString(3, person.getNome());
                stmt.setString(4, person.getNascimento());
                stmt.setString(5, String.join(";", person.getStack()));

                stmt.execute();
            }
        }

    }
}
