package br.pucrs.fds.equipe6.tf.drivers.persistence;

import br.pucrs.fds.equipe6.tf.domain.entity.Moeda;
import br.pucrs.fds.equipe6.tf.domain.repository.IMoedaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Primary
public class MoedaJpaImplRepository implements IMoedaRepository {
    private IMoedaJpaItfRepo repository;

    @Autowired
    public MoedaJpaImplRepository(IMoedaJpaItfRepo repository) {
        this.repository = repository;
    }

    @Override
    public Moeda save(Moeda moeda) {
        return repository.save(moeda);
    }

    @Override
    public Moeda findById(Integer id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Moeda> findAll() {
        List<Moeda> moedas = new ArrayList<>();
        repository.findAll().forEach(moedas::add);
        return moedas;
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(int cod) {
        return repository.existsById(cod);
    }
}