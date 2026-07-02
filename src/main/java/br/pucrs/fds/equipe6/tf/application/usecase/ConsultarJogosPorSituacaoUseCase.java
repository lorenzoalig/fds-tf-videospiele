package br.pucrs.fds.equipe6.tf.application.usecase;


import br.pucrs.fds.equipe6.tf.domain.entity.Jogo;
import br.pucrs.fds.equipe6.tf.domain.entity.Situacao;
import br.pucrs.fds.equipe6.tf.domain.repository.IJogoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultarJogosPorSituacaoUseCase {

    private final IJogoRepository jogoRepository;
    private final AtualizaSituacaoJogosUseCase atualizaSituacaoJogosUseCase;

    public ConsultarJogosPorSituacaoUseCase(IJogoRepository jogoRepository,
                                             AtualizaSituacaoJogosUseCase atualizaSituacaoJogosUseCase) {
        this.jogoRepository = jogoRepository;
        this.atualizaSituacaoJogosUseCase = atualizaSituacaoJogosUseCase;
    }

    public List<Jogo> executar(String nomeSituacao) {
        atualizaSituacaoJogosUseCase.executar();

        Situacao situacao = Situacao.buscaPorNome(nomeSituacao);

        return jogoRepository.findAll()
                .stream()
                .filter(j -> j.getSituacao() == situacao)
                .toList();
    }
}