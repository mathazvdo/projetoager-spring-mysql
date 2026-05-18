package com.dev_spring_jdbc.Projeto.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev_spring_jdbc.Projeto.dao.FuncionarioDAO;
import com.dev_spring_jdbc.Projeto.model.Funcionario;
import com.dev_spring_jdbc.Projeto.service.FuncionarioService;

@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {
	
	@Autowired
	private FuncionarioService service;
	
	@PostMapping
	public void insertFuncionario(@RequestBody Funcionario funcionario) {
		service.insertFuncionario(funcionario);
	}
	
	@PutMapping("/{codigo}")
	public void updateFuncionario(@PathVariable Integer codigo, @RequestBody Funcionario funcionario) {
		funcionario.setCodigo(codigo);
		service.update(funcionario);
	}
	
	@DeleteMapping("/{codigo}")
	public void deleteFuncionario(@PathVariable Integer codigo) {
		service.delete(codigo);
	}
	
	@GetMapping("/{codigo}")
	public Funcionario findById(@PathVariable Integer codigo) {
		return service.findById(codigo);
		
	}
	
	@GetMapping
	public List<Funcionario> findAll() {
		return service.findAll();
	}
	
	@GetMapping("/hello")
	public String hello() {
		return "Ola mundo";
	}
}
