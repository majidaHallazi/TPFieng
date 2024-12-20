package exemple.eurekafeignclient;

import exemple.eurekafeignclient.entities.Client;
import exemple.eurekafeignclient.entities.Voiture;
import exemple.eurekafeignclient.repository.VoitureRepository;
import exemple.eurekafeignclient.service.ClientService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableFeignClients
public class EurekaFeignClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaFeignClientApplication.class, args);
    }

    @Bean
    CommandLineRunner initializerBaseH2(VoitureRepository voitureRepository, ClientService clientService) {
        return args -> {
            Client c1 = clientService.getClient(1);
            Client c2 = clientService.getClient(2);

            System.out.println("****************");
            System.out.println("Client 1 - Id: " + c1.getId() + ", Nom: " + c1.getNom() + ", Age: " + c1.getAge());
            System.out.println("Client 2 - Id: " + c2.getId() + ", Nom: " + c2.getNom() + ", Age: " + c2.getAge());
            System.out.println("****************");

            voitureRepository.save(new Voiture(null, "Toyota", "A25333", "Corolla", c1.getId().intValue(), null));
            voitureRepository.save(new Voiture(null, "Renault", "B63456", "Magane", c2.getId().intValue(), null));
            voitureRepository.save(new Voiture(null, "Peugeot", "A554444", "301", c1.getId().intValue(), null));

            voitureRepository.findAll().forEach(voiture -> {
                voiture.setClient(clientService.getClient(voiture.getId_client()));
                Client client = voiture.getClient();
                System.out.println("Voiture: " + voiture.getMarque() + ", Client: " + client.getNom() + ", Age: " + client.getAge());
            });
        };
    }
}
