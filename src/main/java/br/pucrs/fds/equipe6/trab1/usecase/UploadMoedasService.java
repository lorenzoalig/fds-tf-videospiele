package br.pucrs.fds.equipe6.trab1.usecase;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.pucrs.fds.equipe6.trab1.Moeda;
import br.pucrs.fds.equipe6.trab1.repository.MoedaRepository;

@Service
public class UploadMoedasService {

    private final MoedaRepository moedaRepository;

    public UploadMoedasService(MoedaRepository moedaRepository) {
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
                String simbolo = data[2];
                double taxaParaReal = Double.parseDouble(data[3]);

                // evita duplicados
                if (moedaRepository.existsById(cod)) {
                    continue;
                }

                Moeda moeda = new Moeda(cod, nome, simbolo, taxaParaReal);

                moedaRepository.save(moeda);
            }

            return true;

        } catch (Exception e) {
            return false;
        }
    }
}