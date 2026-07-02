package br.pucrs.fds.equipe6.tf.domain.service;

import br.pucrs.fds.equipe6.tf.domain.entity.*;
import br.pucrs.fds.equipe6.tf.domain.repository.IContratoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class CalculaValorContratoServiceTest {

    // repositório fake
    static class ContratoRepositorioFalso implements IContratoRepository {
        private final Map<Integer, Contrato> contratos = new HashMap<>();

        void adicionar(Contrato c) { contratos.put(c.getId(), c); }

        @Override public Contrato save(Contrato contrato) { contratos.put(contrato.getId(), contrato); return contrato; }
        @Override public Contrato findById(Integer id) { return contratos.get(id); }
        @Override public List<Contrato> findAll() { return new ArrayList<>(contratos.values()); }
        @Override public List<Contrato> findByJogo(Jogo jogo) { return new ArrayList<>(); }
        @Override public List<Contrato> findByClienteCPF(String cpf) { return new ArrayList<>(); }
        @Override public void deleteById(Integer id) { contratos.remove(id); }
        @Override public boolean existsById(int id) { return contratos.containsKey(id); }
    }

    private ContratoRepositorioFalso contratoRepository;
    private CalculaValorContratoService service;

    @BeforeEach
    void setUp() {
        contratoRepository = new ContratoRepositorioFalso();
        service = new CalculaValorContratoService(contratoRepository);
    }

    private Date dataDe(int ano, int mes, int dia) {
        Calendar cal = Calendar.getInstance();
        cal.set(ano, mes, dia, 12, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    @Test
    void deveRetornarZeroQuandoContratoNaoExiste() {
        assertThat(service.calculaValorById(1)).isZero();
    }

    @Test
    void deveRetornarZeroQuandoContratoNaoTemUsos() {
        Categoria categoria = new Categoria(1, "Ação", 10.0);
        Moeda moeda = new Moeda(1, "Real", "R$", 1.0);
        Jogo jogo = new Jogo(1, "Jogo A", 2024, 0.5, categoria, moeda);
        Cliente cliente = new Cliente(1L, "Lorenzo", "111", "l@t.com", null, "s");
        Contrato contrato = new Contrato(1, new Date(), 30, cliente, jogo);

        contratoRepository.adicionar(contrato);

        assertThat(service.calculaValorById(1)).isZero();
    }

    @Test
    void deveCalcularValorTotalComUmUso() {
        Categoria categoria = new Categoria(1, "Ação", 10.0);
        Moeda moeda = new Moeda(1, "Dólar", "$", 5.0);
        Jogo jogo = new Jogo(1, "Jogo A", 2024, 0.2, categoria, moeda);
        Cliente cliente = new Cliente(1L, "Lorenzo", "111", "l@t.com", null, "s");
        Contrato contrato = new Contrato(1, new Date(), 30, cliente, jogo);

        Date data = dataDe(2026, Calendar.JANUARY, 1);
        Uso uso = new Uso(1, data, data, 10, 12); // 120 minutos
        contrato.addUso(uso);

        contratoRepository.adicionar(contrato);

        // valorBase(10) + valorMinuto(0.2) * 120min = 10 + 24 = 34 ; * taxa(5) = 170
        assertThat(service.calculaValorById(1)).isEqualTo(170.0);
    }

    @Test
    void deveSomarValorDeMultiplosUsos() {
        Categoria categoria = new Categoria(1, "Ação", 10.0);
        Moeda moeda = new Moeda(1, "Real", "R$", 1.0);
        Jogo jogo = new Jogo(1, "Jogo A", 2024, 0.5, categoria, moeda);
        Cliente cliente = new Cliente(1L, "Lorenzo", "111", "l@t.com", null, "s");
        Contrato contrato = new Contrato(1, new Date(), 30, cliente, jogo);

        Date data = dataDe(2026, Calendar.JANUARY, 1);
        Uso uso1 = new Uso(1, data, data, 10, 12); // 120 min -> 10 + 60 = 70
        Uso uso2 = new Uso(2, data, data, 8, 9);   // 60 min -> 10 + 30 = 40

        contrato.addUso(uso1);
        contrato.addUso(uso2);

        contratoRepository.adicionar(contrato);

        assertThat(service.calculaValorById(1)).isEqualTo(110.0);
    }
}
