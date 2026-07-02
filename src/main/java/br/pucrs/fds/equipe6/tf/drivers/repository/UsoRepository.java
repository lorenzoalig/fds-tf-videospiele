package br.pucrs.fds.equipe6.tf.drivers.repository;

import java.util.List;
import br.pucrs.fds.equipe6.tf.domain.entity.Uso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsoRepository extends JpaRepository<Uso, Integer> {

    List<Uso> findByContrato_Id(int contratoId);
}