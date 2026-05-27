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

import com.dev_spring_jdbc.Projeto.exception.SuccessResponse;
import com.dev_spring_jdbc.Projeto.model.Funcionario;
import com.dev_spring_jdbc.Projeto.service.FuncionarioService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@CrossOrigin("*")
@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {
    
	@Autowired
	private FuncionarioService service;
  

	@PostMapping
	public ResponseEntity<SuccessResponse> insertFuncionario(
			@RequestBody Funcionario funcionario) {

		service.inserir(funcionario);

		SuccessResponse response =
				new SuccessResponse("Funcionário cadastrado com sucesso.");

		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(response);
	}
    
	@PutMapping("/{codigo}")
	public ResponseEntity <SuccessResponse> updateFuncionario(@PathVariable Integer codigo, @RequestBody Funcionario funcionario) {
		funcionario.setCodigo(codigo);
		service.atualizar(funcionario);
		SuccessResponse response =
				new SuccessResponse("Funcionário atualizado com sucesso.");

		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(response);
        
	}
    
	@DeleteMapping("/{codigo}")
	public void deleteFuncionario(@PathVariable Integer codigo) {
		service.deletar(codigo);
	}
    
	@GetMapping("/{codigo}")
	public Funcionario findById(@PathVariable Integer codigo) {
		return service.buscarPorId(codigo);
        
	}
    
	@GetMapping
	public List<Funcionario> findAll() {
		return service.listarTodos();
	}
    
}
