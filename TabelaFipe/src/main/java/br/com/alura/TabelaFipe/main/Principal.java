package br.com.alura.TabelaFipe.main;

import br.com.alura.TabelaFipe.model.Dados;
import br.com.alura.TabelaFipe.model.Veiculo;
import br.com.alura.TabelaFipe.service.ConsumoApi;
import br.com.alura.TabelaFipe.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private final String URL_Base = "https://fipe.parallelum.com.br/api/v2/";
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    public void exibeMenu() {
        var menu = """
            *** OPÇÕES ***
            Carro
            Moto
            Caminhão
            
            Digite uma das opções para consulta:
            
            """;

        System.out.println(menu);
        var opcao = leitura.nextLine();

        String endereco;

        if (opcao.toLowerCase().contains("car")){
            endereco = URL_Base + "cars/brands/";
        } else if (opcao.toLowerCase().contains("mo")) {
            endereco = URL_Base + "motorcycles/brands/";
        } else {
            endereco = URL_Base + "trucks/brands/";
        }

        var json = consumoApi.obterDados(endereco);

        var marcas = conversor.obterLista(json, Dados.class);
        marcas.stream()
                .sorted(Comparator.comparing(Dados::name))
                .forEach(System.out::println);

        System.out.println("Informe o código da marca para consulta: ");
        var codigoMarca = leitura.nextLine();

        endereco = endereco + codigoMarca + "/models/";
        System.out.println(endereco);

        json = consumoApi.obterDados(endereco);
        var modeloLista = conversor.obterLista(json, Dados.class);

        System.out.println("--------------------  Modelos desta marca  --------------------");
        modeloLista.stream()
                .sorted(Comparator.comparing(Dados::name))
                .forEach(System.out::println);



        //mostrando todos os modelos específicos
        System.out.println("--------------------  Informe o nome do modelo para consulta --------------------");
        var nomeModelo = leitura.nextLine();

        List<Dados> modelosFiltrados = modeloLista.stream()
                .filter(m -> m.name().toLowerCase().contains(nomeModelo.toLowerCase()))
                .collect(Collectors.toList());

        System.out.println("--------------------  Modelos Filtrados  --------------------");
        modelosFiltrados.stream().forEach(System.out::println);


        //Mostrando os valores dos modelos e anos
        System.out.println("--------------------  Informe o código do modelo para buscar valores  --------------------");
        var codigoModelo = leitura.nextLine();

        endereco = endereco + codigoModelo + "/years/";
        json = consumoApi.obterDados(endereco);
        var anosLista = conversor.obterLista(json, Dados.class);

        List<Veiculo> veiculos = new ArrayList<>();

        for (int i = 0; i < anosLista.size(); i++) {
            var enderecoAnos = endereco + anosLista.get(i).code();
            json = consumoApi.obterDados(enderecoAnos);
            Veiculo veiculo = conversor.obterDados(json, Veiculo.class);
            veiculos.add(veiculo);
        }

        System.out.println("-------------------- Todos os anos desse modelo --------------------");
        veiculos.stream()
                .sorted(Comparator.comparing(Veiculo::valor))
                .forEach(System.out::println);


    }
}
