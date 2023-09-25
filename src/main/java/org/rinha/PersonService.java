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

/* * Copyright (c) - UOL Inc,
 * Todos os direitos reservados
 *
 * Este arquivo e uma propriedade confidencial do Universo Online Inc.
 * Nenhuma parte do mesmo pode ser copiada, reproduzida, impressa ou
 * transmitida por qualquer meio sem autorização expressa e por escrito
 * de um representante legal do Universo Online Inc.
 *
 * All rights reserved
 *
 * This file is a confidential property of Universo Online Inc.
 * No part of this file may be reproduced or copied in any form or by
 * any means without written permission from an authorized person from
 * Universo Online Inc.
 *
 * Histórico de revisões
 * Autor Data Motivo
 * --------------------------------- ---------- -----------------------
 * phcassiano 22/09/2023 Criacao inicial
 */
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
