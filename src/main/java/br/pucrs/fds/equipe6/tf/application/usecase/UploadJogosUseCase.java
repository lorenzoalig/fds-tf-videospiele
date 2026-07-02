package br.pucrs.fds.equipe6.tf.application.usecase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import br.pucrs.fds.equipe6.tf.domain.entity.Categoria;
import br.pucrs.fds.equipe6.tf.domain.entity.Jogo;
import br.pucrs.fds.equipe6.tf.domain.entity.Moeda;
import br.pucrs.fds.equipe6.tf.domain.repository.ICategoriaRepository;
import br.pucrs.fds.equipe6.tf.domain.repository.IJogoRepository;
import br.pucrs.fds.equipe6.tf.domain.repository.IMoedaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class UploadJogosUseCase {

    private final IJogoRepository jogoRepository;
    private final ICategoriaRepository categoriaRepository;
    private final IMoedaRepository moedaRepository;

    public UploadJogosUseCase(IJogoRepository jogoRepository,
                              ICategoriaRepository categoriaRepository,
                              IMoedaRepository moedaRepository) {
        this.jogoRepository = jogoRepository;
        this.categoriaRepository = categoriaRepository;
        this.moedaRepository = moedaRepository;
    }

    public boolean executar(MultipartFile file) {

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream()))) {

            String line;
            boolean first = true;

            while ((line = reader.readLine()) != null) {

                // pula header
                if (first) {
                    first = false;
                    continue;
                }

                String[] data = line.split(";");

                int cod = Integer.parseInt(data[0]);
                String nome = data[1];
                int ano = Integer.parseInt(data[2]);
                double valorMinuto = Double.parseDouble(data[3]);
                String nomeCategoria = data[4];
                int codMoeda = Integer.parseInt(data[5]);

                if (jogoRepository.existsById(cod)) {
                    continue;
                }

                Categoria categoria = categoriaRepository
                        .findAll()
                        .stream()
                        .filter(c -> c.getNome().equalsIgnoreCase(nomeCategoria))
                        .findFirst()
                        .orElse(null);

                Moeda moeda = moedaRepository.findById(codMoeda);

                if (categoria == null || moeda == null) {
                    continue; // ou pode dar false se quiser validar mais forte
                }

                Jogo jogo = new Jogo(
                        cod,
                        nome,
                        ano,
                        valorMinuto,
                        categoria,
                        moeda
                );

                jogoRepository.save(jogo);
            }

            return true;

        } catch (IOException e) {
            return false;
        }
    }
}