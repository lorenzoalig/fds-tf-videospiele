package br.pucrs.fds.equipe6.tf.drivers.repository;

import br.pucrs.fds.equipe6.tf.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByCPF(String cpf);
}