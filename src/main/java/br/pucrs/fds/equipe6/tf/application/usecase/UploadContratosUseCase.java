package br.pucrs.fds.equipe6.tf.application.usecase;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;

import br.pucrs.fds.equipe6.tf.domain.entity.Cliente;
import br.pucrs.fds.equipe6.tf.domain.entity.Contrato;
import br.pucrs.fds.equipe6.tf.domain.entity.FormaPagamento;
import br.pucrs.fds.equipe6.tf.domain.entity.Jogo;
import br.pucrs.fds.equipe6.tf.domain.repository.IClienteRepository;
import br.pucrs.fds.equipe6.tf.domain.repository.IContratoRepository;
import br.pucrs.fds.equipe6.tf.domain.repository.IFormaPagamentoRepository;
import br.pucrs.fds.equipe6.tf.domain.repository.IJogoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class UploadContratosUseCase {

    private final IContratoRepository contratoRepository;
    private final IClienteRepository clienteRepository;
    private final IJogoRepository jogoRepository;
    private final IFormaPagamentoRepository formaPagamentoRepository;

    public UploadContratosUseCase(IContratoRepository contratoRepository,
                                  IClienteRepository clienteRepository,
                                  IJogoRepository jogoRepository,
                                  IFormaPagamentoRepository formaPagamentoRepository) {
        this.contratoRepository = contratoRepository;
        this.clienteRepository = clienteRepository;
        this.jogoRepository = jogoRepository;
        this.formaPagamentoRepository = formaPagamentoRepository;
    }

    public boolean executar(MultipartFile file) {

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream()))) {

            String line;
            boolean first = true;

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            while ((line = reader.readLine()) != null) {

                if (first) {
                    first = false;
                    continue;
                }

                String[] data = line.split(";");

                int id = Integer.parseInt(data[0]);
                java.util.Date dataContrato = sdf.parse(data[1]);
                int periodo = Integer.parseInt(data[2]);

                int codCliente = Integer.parseInt(data[3]);
                int codJogo = Integer.parseInt(data[4]);
                int numFormaPagamento = Integer.parseInt(data[5]);

                if (contratoRepository.existsById(id)) {
                    continue;
                }

                Cliente cliente = clienteRepository.findById((long) codCliente);

                Jogo jogo = jogoRepository.findById(codJogo);

                FormaPagamento formaPagamento = formaPagamentoRepository.findById(numFormaPagamento);

                if (cliente == null || jogo == null || formaPagamento == null) {
                    continue; // ou return false se quiser mais rígido
                }

                Contrato contrato = new Contrato(
                        id,
                        dataContrato,
                        periodo,
                        cliente,
                        jogo,
                        formaPagamento
                );

                contratoRepository.save(contrato);
            }

            return true;

        } catch (Exception e) {
            return false;
        }
    }
}