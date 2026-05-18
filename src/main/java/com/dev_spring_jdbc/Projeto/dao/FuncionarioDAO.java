package com.dev_spring_jdbc.Projeto.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.dev_spring_jdbc.Projeto.model.Funcionario;

@Repository
public class FuncionarioDAO {

	@Autowired
	private DataSource dataSource;

	public void insert(Funcionario funcionario) {
		System.out.println("CODIGO: " + funcionario.getCodigo());
		StringBuilder stringBuilder = new StringBuilder();
		boolean possuiCodigo = funcionario.getCodigo() != null;
		if (possuiCodigo) {
			stringBuilder.append("INSERT INTO funcionario ").append("(codigo, nome, cpf, cargo, birth_date) ")
					.append("VALUES (?, ?, ?, ?, ?)");
		} else {
			stringBuilder.append("INSERT INTO funcionario ").append("(nome, cpf, cargo, birth_date) ")
					.append("VALUES (?, ?, ?, ?)");
		}
		try (Connection conn = dataSource.getConnection();
				PreparedStatement st = conn.prepareStatement(stringBuilder.toString())) {
			int index = 1;
			if (possuiCodigo) {
				st.setInt(index++, funcionario.getCodigo());
			}
			st.setString(index++, funcionario.getNome());
			st.setString(index++, funcionario.getCpf());
			st.setString(index++, funcionario.getCargo());
			st.setDate(index, java.sql.Date.valueOf(funcionario.getBirthDate()));

			st.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao inserir funcionário: " + e.getMessage(), e);
		}
	}

	public void update(Funcionario funcionario) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("UPDATE funcionario SET nome = ?, cpf = ?, cargo = ?, birth_date = ? ")
				.append("WHERE codigo = ?");
		try (Connection conn = dataSource.getConnection();
				PreparedStatement st = conn.prepareStatement(stringBuilder.toString())) {
			st.setString(1, funcionario.getNome());
			st.setString(2, funcionario.getCpf());
			st.setString(3, funcionario.getCargo());
			st.setDate(4, java.sql.Date.valueOf(funcionario.getBirthDate()));
			st.setInt(5, funcionario.getCodigo());
			st.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException("Erro ao atualizar funcionário: " + e.getMessage(), e);
		}
	}

	public void delete(Integer codigo) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("DELETE FROM funcionario WHERE codigo = ?");
		try (Connection conn = dataSource.getConnection();
				PreparedStatement st = conn.prepareStatement(stringBuilder.toString())) {
			st.setInt(1, codigo);
			st.executeUpdate();
		} catch (Exception e) {
			throw new RuntimeException("Erro ao deletar funcionário: " + e.getMessage(), e);
		}
	}

	public Funcionario findById(Integer codigo) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("SELECT codigo, nome, cpf, cargo, birth_date ").append("FROM funcionario ")
				.append("WHERE codigo = ?");
		try (Connection conn = dataSource.getConnection();
				PreparedStatement st = conn.prepareStatement(stringBuilder.toString())) {
			st.setInt(1, codigo);
			try (ResultSet rs = st.executeQuery()) {
				if (rs.next()) {
					Funcionario funcionario = new Funcionario();
					funcionario.setCodigo(rs.getInt("codigo"));
					funcionario.setNome(rs.getString("nome"));
					funcionario.setCpf(rs.getString("cpf"));
					funcionario.setCargo(rs.getString("cargo"));
					funcionario.setBirthDate(rs.getDate("birth_date").toLocalDate());
					return funcionario;
				} else {
					return null; // Retorna null se o funcionário não for encontrado
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Erro ao buscar funcionário por ID: " + e.getMessage(), e);
		}
	}

	public Funcionario findByCpf(String cpf) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("SELECT codigo, nome, cpf, cargo, birth_date ").append("FROM funcionario ")
				.append("WHERE cpf = ?");
		try (Connection conn = dataSource.getConnection();
				PreparedStatement st = conn.prepareStatement(stringBuilder.toString())) {
			st.setString(1, cpf);
			try (ResultSet rs = st.executeQuery()) {
				if (rs.next()) {
					Funcionario funcionario = new Funcionario();
					funcionario.setCodigo(rs.getInt("codigo"));
					funcionario.setNome(rs.getString("nome"));
					funcionario.setCpf(rs.getString("cpf"));
					funcionario.setCargo(rs.getString("cargo"));
					funcionario.setBirthDate(rs.getDate("birth_date").toLocalDate());
					return funcionario;
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao buscar funcionário por CPF: " + e.getMessage(), e);
		}
		return null; // Retorna null se o funcionário não for encontrado
	}

	public List<Funcionario> findAll() {
		List<Funcionario> funcionariosList = new ArrayList<>();
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("SELECT codigo, nome, cpf, cargo, birth_date ").append("FROM funcionario");
		try (Connection conn = dataSource.getConnection();
				PreparedStatement st = conn.prepareStatement(stringBuilder.toString());
				ResultSet rs = st.executeQuery()) {
			while (rs.next()) {
				Funcionario funcionario = new Funcionario();
				funcionario.setCodigo(rs.getInt("codigo"));
				funcionario.setNome(rs.getString("nome"));
				funcionario.setCpf(rs.getString("cpf"));
				funcionario.setCargo(rs.getString("cargo"));
				funcionario.setBirthDate(rs.getDate("birth_date").toLocalDate());
				funcionariosList.add(funcionario);
			}
		} catch (Exception e) {
			throw new RuntimeException("Erro ao buscar funcionários: " + e.getMessage(), e);
		}
		return funcionariosList;
	}
}
