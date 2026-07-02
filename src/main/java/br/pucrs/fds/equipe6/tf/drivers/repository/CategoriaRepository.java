package br.pucrs.fds.equipe6.tf.drivers.repository;

import br.pucrs.fds.equipe6.tf.domain.entity.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {}