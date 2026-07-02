package br.pucrs.fds.equipe6.tf.drivers.repository;

import br.pucrs.fds.equipe6.tf.domain.entity.Contrato;
import br.pucrs.fds.equipe6.tf.domain.entity.Jogo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContratoRepository extends JpaRepository<Contrato, Integer> {
    List<Contrato> findByJogo(Jogo jogo);
    List<Contrato> findByCliente_CPF(String cpf);
}