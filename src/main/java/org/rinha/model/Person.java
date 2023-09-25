package org.rinha.model;

import jakarta.validation.constraints.*;

import java.util.List;

public class Person
{
    @NotBlank()
    @Size(max = 32)
    private String apelido;

    @NotBlank
    @Size(max = 100)
    private String nome;

    @NotBlank
    private String nascimento;

    private List<@NotBlank @Size(max = 32) String> stack;

    private String id;

    @Override
    public String toString()
    {
        return "Person{" +
                "apelido='" + apelido + '\'' +
                ", nome='" + nome + '\'' +
                ", nascimento='" + nascimento + '\'' +
                ", stack=" + stack +
                '}';
    }

    public String getApelido()
    {
        return apelido;
    }

    public void setApelido(String apelido)
    {
        this.apelido = apelido;
    }

    public String getNome()
    {
        return nome;
    }

    public void setNome(String nome)
    {
        this.nome = nome;
    }

    public String getNascimento()
    {
        return nascimento;
    }

    public void setNascimento(String nascimento)
    {
        this.nascimento = nascimento;
    }

    public List<String> getStack()
    {
        return stack;
    }

    public void setStack(List<String> stack)
    {
        this.stack = stack;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }
}
