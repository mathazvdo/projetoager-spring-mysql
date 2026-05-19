package com.dev_spring_jdbc.Projeto.model;

import java.time.LocalDate;

public class Funcionario {

	private Integer codigo;
	private String nome;
	private String cpf;
	private String cargo;
	private LocalDate dataNascimento;

	public Funcionario() {
	}

	public Funcionario(Integer codigo, String nome, String cpf, String cargo, LocalDate dataNascimento) {
		this.codigo = codigo;
		this.nome = nome;
		this.cpf = cpf;
		this.cargo = cargo;
		this.dataNascimento = dataNascimento;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	@Override
	public String toString() {
		return "Funcionario{" + "codigo=" + codigo + ", nome='" + nome + '\'' + ", cpf='" + cpf + '\'' + ", cargo='"
				+ cargo + '\'' + ", dataNascimento=" + dataNascimento + '}';
	}

}
