/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import java.time.LocalDateTime;

/**
 *
 * @author Aluno
 */
public class Mensagem
{
    private Cliente cli_origem;
    private Cliente cli_destino;
    private String mensagem;
    private LocalDateTime  dt;
    public Mensagem(Cliente cli_origem, Cliente cli_destino, String mensagem)
    {
        this.cli_origem = cli_origem;
        this.cli_destino = cli_destino;
        this.mensagem = mensagem;
        this.dt = LocalDateTime.now();
    }

    public Cliente getCli_origem()
    {
        return cli_origem;
    }

    public void setCli_origem(Cliente cli_origem)
    {
        this.cli_origem = cli_origem;
    }

    public Cliente getCli_destino()
    {
        return cli_destino;
    }

    public void setCli_destino(Cliente cli_destino)
    {
        this.cli_destino = cli_destino;
    }

    public String getMensagem()
    {
        return mensagem;
    }

    public void setMensagem(String mensagem)
    {
        this.mensagem = mensagem;
    }

    @Override
    public String toString()
    {
        return "\n"+dt.toString()+"\n"+cli_origem + " disse:" + cli_destino;
    }
}