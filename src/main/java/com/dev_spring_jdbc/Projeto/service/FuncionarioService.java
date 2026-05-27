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

    public void inserir(Funcionario funcionario) {

        validarCamposObrigatorios(funcionario);
        validarCpf(funcionario, false);

        definirCodigoSeNecessario(funcionario);

        dao.insert(funcionario);
    }

    public void atualizar(Funcionario funcionario) {

        Funcionario funcionarioBanco = buscarFuncionarioOuErro(funcionario.getCodigo());

        validarCamposObrigatorios(funcionario);
        validarCpf(funcionario, true);

        funcionario.setCodigo(funcionarioBanco.getCodigo());

        dao.update(funcionario);
    }

    public void deletar(Integer codigo) {

        buscarFuncionarioOuErro(codigo);

        dao.delete(codigo);
    }

    public Funcionario buscarPorId(Integer codigo) {
        return buscarFuncionarioOuErro(codigo);
    }

    public Funcionario buscarPorCpf(String cpf) {
        return dao.buscarPorCpf(cpf);
    }

    public List<Funcionario> listarTodos() {
        return dao.findAll();
    }

    private void definirCodigoSeNecessario(Funcionario funcionario) {
        if (funcionario.getCodigo() == null) {
            funcionario.setCodigo(dao.buscarMenorCodigoDisponivel());
        } else {
            validarCodigoDisponivel(funcionario);
        }
    }

    private void validarCodigoDisponivel(Funcionario funcionario) {
        Funcionario existente = dao.buscarPorCodigo(funcionario.getCodigo());

        if (existente != null) {
            throw new BusinessException(
                    String.format("Código já cadastrado: %d", funcionario.getCodigo())
            );
        }
    }

    private Funcionario buscarFuncionarioOuErro(Integer codigo) {

        if (codigo == null) {
            throw new BusinessException("Código do funcionário é obrigatório.");
        }

        Funcionario funcionario = dao.buscarPorCodigo(codigo);

        if (funcionario == null) {
            throw new BusinessException(
                    String.format("Funcionário com código %d não encontrado.", codigo)
            );
        }

        return funcionario;
    }

    private void validarCpf(Funcionario funcionario, boolean update) {

        validarCampoObrigatorio(funcionario.getCpf(), "CPF");

        Funcionario existente = dao.buscarPorCpf(funcionario.getCpf());

        if (existente != null) {

            boolean cpfDeOutroFuncionario =
                    funcionario.getCodigo() == null ||
                    !existente.getCodigo().equals(funcionario.getCodigo());

            if (!update || cpfDeOutroFuncionario) {
                throw new BusinessException(
                        String.format("CPF já cadastrado: %s", funcionario.getCpf())
                );
            }
        }
    }

    private void validarCamposObrigatorios(Funcionario funcionario) {

        validarCampoObrigatorio(funcionario.getNome(), "Nome");
        validarCampoObrigatorio(funcionario.getCargo(), "Cargo");

        if (funcionario.getDataNascimento() == null) {
            throw new BusinessException("Data de nascimento é obrigatória.");
        }
    }

    private void validarCampoObrigatorio(String valor, String campo) {

        if (valor == null || valor.isBlank()) {
            throw new BusinessException(
                    String.format("O campo %s é obrigatório.", campo)
            );
        }
    }
}