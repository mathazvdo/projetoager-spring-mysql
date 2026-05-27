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

import com.dev_spring_jdbc.Projeto.exception.DatabaseException;
import com.dev_spring_jdbc.Projeto.model.Funcionario;

@Repository
public class FuncionarioDAO {

    @Autowired
    private DataSource dataSource;

    private static final String SQL_BASE_SELECT =
            "SELECT codigo, nome, cpf, cargo, dataNascimento FROM funcionario";

    private static final String SQL_ATUALIZAR_FUNCIONARIO =
            "UPDATE funcionario SET nome = ?, cpf = ?, cargo = ?, dataNascimento = ? WHERE codigo = ?";

    private static final String SQL_DELETAR_FUNCIONARIO =
            "DELETE FROM funcionario WHERE codigo = ?";

    private static final String SQL_LISTAR_FUNCIONARIOS =
            "SELECT codigo, nome, cpf, cargo, dataNascimento FROM funcionario";

    private static final String SQL_BUSCAR_POR_CPF =
            SQL_BASE_SELECT + " WHERE cpf = ?";

    private static final String SQL_BUSCAR_POR_CODIGO =
            SQL_BASE_SELECT + " WHERE codigo = ?";

    public void insert(Funcionario funcionario) {

        boolean possuiCodigo = funcionario.getCodigo() != null;

        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO funcionario (");

        if (possuiCodigo) {
            sql.append("codigo, ");
        }

        sql.append("nome, cpf, cargo, dataNascimento) VALUES (");

        if (possuiCodigo) {
            sql.append("?, ");
        }

        sql.append("?, ?, ?, ?)");

        try (Connection conn = dataSource.getConnection();
             PreparedStatement st = conn.prepareStatement(sql.toString())) {

            int i = 1;

            if (possuiCodigo) {
                st.setInt(i++, funcionario.getCodigo());
            }

            st.setString(i++, funcionario.getNome());
            st.setString(i++, funcionario.getCpf());
            st.setString(i++, funcionario.getCargo());
            st.setDate(i, java.sql.Date.valueOf(funcionario.getDataNascimento()));

            st.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException(
                    String.format("Erro ao inserir funcionário: %s", e.getMessage()), e
            );
        }
    }

    public void update(Funcionario funcionario) {

        try (Connection conn = dataSource.getConnection();
             PreparedStatement st = conn.prepareStatement(SQL_ATUALIZAR_FUNCIONARIO)) {

            int i = 1;

            st.setString(i++, funcionario.getNome());
            st.setString(i++, funcionario.getCpf());
            st.setString(i++, funcionario.getCargo());
            st.setDate(i++, java.sql.Date.valueOf(funcionario.getDataNascimento()));
            st.setInt(i, funcionario.getCodigo());

            st.executeUpdate();

        } catch (SQLException e) {
            throw tratarErroBanco("atualizar", e);
        }
    }

    public void delete(Integer codigo) {

        try (Connection conn = dataSource.getConnection();
             PreparedStatement st = conn.prepareStatement(SQL_DELETAR_FUNCIONARIO)) {

            st.setInt(1, codigo);
            st.executeUpdate();

        } catch (SQLException e) {
            throw tratarErroBanco("deletar", e);
        }
    }

    public Funcionario buscarPorCpf(String cpf) {
        return buscarFuncionario(SQL_BUSCAR_POR_CPF, cpf);
    }

    public Funcionario buscarPorCodigo(Integer codigo) {
        return buscarFuncionario(SQL_BUSCAR_POR_CODIGO, codigo);
    }

    public List<Funcionario> findAll() {

        List<Funcionario> lista = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement st = conn.prepareStatement(SQL_LISTAR_FUNCIONARIOS);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                lista.add(montarFuncionario(rs));
            }

        } catch (SQLException e) {
            throw tratarErroBanco("listar", e);
        }

        return lista;
    }

    private Funcionario buscarFuncionario(String sql, Object valor) {

        try (Connection conn = dataSource.getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {

            st.setObject(1, valor);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return montarFuncionario(rs);
                }
            }

        } catch (SQLException e) {
            throw tratarErroBanco("buscar funcionário", e);
        }

        return null;
    }

    private Funcionario montarFuncionario(ResultSet rs) throws SQLException {

        Funcionario funcionario = new Funcionario();

        funcionario.setCodigo(rs.getInt("codigo"));
        funcionario.setNome(rs.getString("nome"));
        funcionario.setCpf(rs.getString("cpf"));
        funcionario.setCargo(rs.getString("cargo"));
        funcionario.setDataNascimento(rs.getDate("dataNascimento").toLocalDate());

        return funcionario;
    }

    private DatabaseException tratarErroBanco(String operacao, Exception e) {
        return new DatabaseException(
                String.format("Erro ao %s funcionário: %s", operacao, e.getMessage()),
                e
        );
    }
    
    public Integer buscarMenorCodigoDisponivel() {

        String sql =
            "SELECT MIN(t1.codigo + 1) AS proximo " +
            "FROM funcionario t1 " +
            "LEFT JOIN funcionario t2 ON t1.codigo + 1 = t2.codigo " +
            "WHERE t2.codigo IS NULL";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            if (rs.next()) {
                int codigo = rs.getInt("proximo");
                return rs.wasNull() ? 1 : codigo;
            }

        } catch (SQLException e) {
            throw new DatabaseException("Erro ao buscar menor código disponível", e);
        }

        return 1;
    }
}