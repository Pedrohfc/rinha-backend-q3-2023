package org.rinha;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import org.rinha.model.Person;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@ApplicationScoped
public class PersonService
{

    public static final String PERSON_FIELDS = "person_id, nick_name, name, birth_date, stack";

    public static final String INSERT_PERSON = "INSERT INTO person (" + PERSON_FIELDS + ") values (UUID_TO_BIN(?), ?, ?, ?, ?)";

    public static final String SELECT_PERSON = "SELECT " + PERSON_FIELDS + " FROM person WHERE person_id = UUID_TO_BIN(?)";

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
            try (PreparedStatement stmt = conn.prepareStatement(INSERT_PERSON))
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

    public Person findPerson(String personId) throws SQLException
    {
        try (Connection conn = dataSource.getConnection())
        {
            try (PreparedStatement stmt = conn.prepareStatement(SELECT_PERSON))
            {
                stmt.setString(1, personId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next())
                {
                    Person person = new Person();
                    person.setId(personId);
                    person.setNome(rs.getString("name"));
                    person.setApelido(rs.getString("nick_name"));
                    person.setNascimento(rs.getString("birth_date"));
                    person.setStack(List.of(rs.getString("stack").split(";")));
                    return person;
                }
            }
        }
        return null;
    }
}
