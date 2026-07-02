package br.pucrs.fds.equipe6.tf.drivers.repository;

import br.pucrs.fds.equipe6.tf.domain.entity.Jogo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JogoRepository extends JpaRepository<Jogo, Integer> {

}