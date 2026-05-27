package com.dev_spring_jdbc.Projeto.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dev_spring_jdbc.Projeto.dao.FuncionarioDAO;
import com.dev_spring_jdbc.Projeto.exception.BusinessException;
import com.dev_spring_jdbc.Projeto.model.Funcionario;

@ExtendWith(MockitoExtension.class)
class FuncionarioServiceTest {

	@Mock
	private FuncionarioDAO dao;

	@InjectMocks
	private FuncionarioService service;

	private Funcionario funcionario;

	@BeforeEach
	void setup() {
		funcionario = new Funcionario();
		funcionario.setNome("João");
		funcionario.setCpf("12345678900");
		funcionario.setCargo("Analista");
		funcionario.setDataNascimento(LocalDate.of(1990, 1, 1));
	}

	@Test
	void deveInserirFuncionarioComSucesso() {

		when(dao.buscarPorCpf("12345678900")).thenReturn(null);
		when(dao.buscarMenorCodigoDisponivel()).thenReturn(1);

		assertDoesNotThrow(() -> service.inserir(funcionario));

		verify(dao, times(1)).insert(funcionario);
	}

	@Test
	void deveLancarErroQuandoCpfJaExiste() {

		Funcionario existente = new Funcionario();
		existente.setCodigo(1);
		existente.setCpf("12345678900");

		when(dao.buscarPorCpf("12345678900")).thenReturn(existente);

		BusinessException ex = assertThrows(
				BusinessException.class,
				() -> service.inserir(funcionario)
		);

		assertEquals("CPF já cadastrado: 12345678900", ex.getMessage());

		verify(dao, never()).insert(any());
	}

	@Test
	void deveAtualizarFuncionarioComSucesso() {

		funcionario.setCodigo(1);

		when(dao.buscarPorCodigo(1)).thenReturn(funcionario);
		when(dao.buscarPorCpf("12345678900")).thenReturn(null);

		assertDoesNotThrow(() -> service.atualizar(funcionario));

		verify(dao, times(1)).update(funcionario);
	}

	@Test
	void deveLancarErroQuandoFuncionarioNaoExiste() {

		when(dao.buscarPorCodigo(99)).thenReturn(null);

		BusinessException ex = assertThrows(
				BusinessException.class,
				() -> service.buscarPorId(99)
		);

		assertTrue(ex.getMessage().contains("não encontrado"));
	}
}