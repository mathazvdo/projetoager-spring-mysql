package com.dev_spring_jdbc.Projeto.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dev_spring_jdbc.Projeto.dao.FuncionarioDAO;
import com.dev_spring_jdbc.Projeto.exception.BusinessException;
import com.dev_spring_jdbc.Projeto.model.Funcionario;

@Service
public class FuncionarioService {

	@Autowired
	private FuncionarioDAO dao;

	public void insertFuncionario(Funcionario funcionario) {
		if (funcionario.getCodigo() == null) {

			funcionario.setCodigo(
					dao.buscarProximoCodigoDisponivel()
			);
		}
		
		validarCodigo(funcionario);
		validarCpf(funcionario);
		dao.insert(funcionario);
	}

	public void update(Funcionario funcionario) {
		validarCpfUpdate(funcionario);
		dao.update(funcionario);
	}

	public void delete(Integer codigo) {
		dao.delete(codigo);
	}

	private void validarCpf(Funcionario funcionario) {
		Funcionario funcionarioExistente = dao.findByCpf(funcionario.getCpf());
		if (funcionarioExistente != null) {
			throw new BusinessException("CPF já existe para outro funcionário: " + funcionario.getCpf());
		}
	}

	private void validarCpfUpdate(Funcionario funcionario) {
		Funcionario funcionarioBanco = dao.findByCpf(funcionario.getCpf());
		if (funcionarioBanco != null && !funcionarioBanco.getCodigo().equals(funcionario.getCodigo())) {
			throw new BusinessException("CPF já existe para outro funcionário: " + funcionario.getCpf());
		}
	}

	private void validarCodigo(Funcionario funcionario) {

		if (funcionario.getCodigo() != null) {

			Funcionario funcionarioExistente = dao.findById(funcionario.getCodigo());

			if (funcionarioExistente != null) {

				throw new BusinessException("Código já existe: " + funcionario.getCodigo());
			}
		}
	}

	public Funcionario findById(Integer codigo) {
		return dao.findById(codigo);
	}

	public List<Funcionario> findAll() {
		return dao.findAll();
	}
}
