package br.pucrs.fds.equipe6.trab1;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Jogos {

    private List<Jogo> jogos;

    public Jogos() {
        jogos = new ArrayList<Jogo>();
    }

    public List<Jogo> getJogos() {
        return this.jogos;
    }

    public void setJogos(ArrayList<Jogo> jogos) {
        this.jogos = jogos;
    }

    public void addJogo(Jogo j){
        jogos.add(j);
    }

    public Jogo buscaJogoCod(int cod){
                        return jogos
                        .stream()
                        .filter(c -> c.getCod() == cod)
                        .findFirst()
                        .orElse(null);
    }

    public void atualizarSituacaoJogos(Contratos contratos){

        for(Jogo jogo : jogos) {

            // n sobrescreve se foi alterado manualmente
            if (jogo.isSituacaoManual()) continue;

            if(jogo.estaRemovido(contratos)) {
                jogo.setSituacao(Situacao.REMOVIDO);
            }

            else if(jogo.estaObsoleto(contratos)) {
                jogo.setSituacao(Situacao.OBSOLETO);
            }

            else if(jogo.estaContratado(contratos)) {
                jogo.setSituacao(Situacao.CONTRATADO);
            }
            else {
                jogo.setSituacao(Situacao.DISPONIVEL);
            }
        }
    }

    public List<Jogo> consultaJogos(String s) {

        Situacao situacao = Situacao.buscaPorNome(s);

        return jogos.stream()
                    .filter(j -> j.getSituacao() == situacao)
                    .toList();
    }
}
