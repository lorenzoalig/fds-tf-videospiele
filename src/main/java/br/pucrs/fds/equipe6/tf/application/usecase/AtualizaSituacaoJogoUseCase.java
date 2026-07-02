package br.pucrs.fds.equipe6.tf.application.usecase;


import br.pucrs.fds.equipe6.tf.domain.entity.Jogo;
import br.pucrs.fds.equipe6.tf.domain.entity.Situacao;

import br.pucrs.fds.equipe6.tf.domain.repository.IJogoRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AtualizaSituacaoJogoUseCase {

    private final IJogoRepository jogoRepository;

    public AtualizaSituacaoJogoUseCase(IJogoRepository jogoRepository) {
        this.jogoRepository = jogoRepository;
    }

    public Optional<Jogo> executar(int codigo, String status) {
        Jogo jogo = jogoRepository.findById(codigo);
        if (jogo == null) return Optional.empty();

        Situacao situacao = Situacao.buscaPorNome(status);
        if (situacao == null) return Optional.empty();

        jogo.setSituacao(situacao);
        jogo.setSituacaoManual(situacao == Situacao.BLOQUEADO);

        return Optional.of(jogoRepository.save(jogo));
    }
}