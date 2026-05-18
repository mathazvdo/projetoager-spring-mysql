package com.dev_spring_jdbc.Projeto.model;

import java.time.LocalDate;

public class Funcionario {

	private Integer codigo;
	private String nome;
	private String cpf;
	private String cargo;
	private LocalDate birthDate;

	public Funcionario() {
	}

	public Funcionario(Integer codigo, String nome, String cpf, String cargo, LocalDate birthDate) {
		this.codigo = codigo;
		this.nome = nome;
		this.cpf = cpf;
		this.cargo = cargo;
		this.birthDate = birthDate;
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

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}
	
	
}
