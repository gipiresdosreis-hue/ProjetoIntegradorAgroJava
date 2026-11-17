
package com.example;

import java.util.Scanner;
import java.util.Locale;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {

        Scanner entrada = new Scanner(System.in).useLocale(Locale.US);

        int opcao = 0;

        double soma = 0;
        double maiorChuva = 0;
        double[] chuva = new double[7];

        int diaMaiorChuva = 0;

        String[] dias = {
                "Segunda-feira",
                "Terça-feira",
                "Quarta-feira",
                "Quinta-feira",
                "Sexta-feira",
                "Sábado",
                "Domingo"
        };

        double[][] umidade = new double[4][4];

        String[] letras = {"A", "B", "C", "D"};

        do {

            System.out.println("\n===== AGROJAVA =====");
            System.out.println("1 - Cadastrar Dados");
            System.out.println("2 - Exibir Mapa do Campo");
            System.out.println("3 - Relatório de Alertas de Irrigação");
            System.out.println("4 - Sair");
            System.out.print("Escolha uma opção: ");

            opcao = entrada.nextInt();
            entrada.nextLine();



            if (opcao == 1) {

                System.out.println("\n===== CADASTRO DE CHUVAS =====");

                System.out.print("Informe a data da semana: ");
                String data = entrada.nextLine();

                soma = 0;

                for (int i = 0; i < 7; i++) {

                    System.out.print("Informe a chuva de " + dias[i] + " (mm): ");
                    chuva[i] = entrada.nextDouble();

                    soma += chuva[i];
                }

                entrada.nextLine();

                maiorChuva = chuva[0];
                diaMaiorChuva = 0;

                for (int i = 1; i < 7; i++) {

                    if (chuva[i] > maiorChuva) {
                        maiorChuva = chuva[i];
                        diaMaiorChuva = i;
                    }
                }

                double media = soma / 7;

                System.out.println("\n===== RESULTADO DA SEMANA =====");
                System.out.println("Data: " + data);
                System.out.printf("Total de chuva: %.2f mm%n", soma);
                System.out.printf("Média semanal: %.2f mm%n", media);
                System.out.println("Dia com maior chuva: " + dias[diaMaiorChuva]);
                System.out.printf("Maior volume: %.2f mm%n", maiorChuva);



                try {

                    File arquivo = new File("historico_chuvas.csv");

                    boolean novoArquivo = !arquivo.exists() || arquivo.length() == 0;

                    BufferedWriter escritor = new BufferedWriter(
                            new FileWriter(arquivo, true)
                    );

                    if (novoArquivo) {
                        escritor.write("data,seg,ter,qua,qui,sex,sab,dom,media,total");
                        escritor.newLine();
                    }

                    escritor.write(data);

                    for (int i = 0; i < 7; i++) {
                        escritor.write("," + String.format(Locale.US, "%.2f", chuva[i]));
                    }

                    escritor.write("," + String.format(Locale.US, "%.2f", media));
                    escritor.write("," + String.format(Locale.US, "%.2f", soma));

                    escritor.newLine();
                    escritor.close();

                    System.out.println("\nDados salvos no histórico com sucesso!");

                } catch (IOException e) {
                    System.out.println("Erro ao salvar o histórico de chuvas.");
                }
            }



            else if (opcao == 2) {

                System.out.println("===== MAPA DE UMIDADE DO SOLO =====");

                for (int linha = 0; linha < 4; linha++) {

                    for (int coluna = 0; coluna < 4; coluna++) {

                        System.out.print("Informe a umidade do talhão "
                                + letras[linha] + (coluna + 1) + " (%): ");

                        umidade[linha][coluna] = entrada.nextDouble();
                    }
                }

                entrada.nextLine();

                // EXIBIR MATRIZ

                System.out.println("===== MAPA DO CAMPO =====");

                for (int linha = 0; linha < 4; linha++) {

                    for (int coluna = 0; coluna < 4; coluna++) {

                        System.out.printf("%.2f%%\t", umidade[linha][coluna]);
                    }

                    System.out.println();
                }


                System.out.println("===== ALERTAS DE IRRIGAÇÃO =====");

                int quantidadeAlertas = 0;

                for (int linha = 0; linha < 4; linha++) {

                    for (int coluna = 0; coluna < 4; coluna++) {

                        if (umidade[linha][coluna] < 30) {

                            System.out.println("Talhão "
                                    + letras[linha] + (coluna + 1)
                                    + " necessita de irrigação.");

                            quantidadeAlertas++;
                        }
                    }
                }

                System.out.println("Total de alertas: " + quantidadeAlertas);



                try {

                    BufferedWriter escritor = new BufferedWriter(
                            new FileWriter("talhoes.csv")
                    );

                    BufferedWriter escritorClassificacao = new BufferedWriter(
                            new FileWriter("talhoes_classificados.csv")
                    );

                    escritor.write("talhao,umidade");
                    escritor.newLine();

                    escritorClassificacao.write("talhao,umidade,status");
                    escritorClassificacao.newLine();

                    for (int linha = 0; linha < 4; linha++) {

                        for (int coluna = 0; coluna < 4; coluna++) {

                            String talhao = letras[linha] + (coluna + 1);

                            double valor = umidade[linha][coluna];

                            String classificacao;

                            if (valor < 20) {
                                classificacao = "Crítico";
                            } else if (valor <= 30) {
                                classificacao = "Atenção";
                            } else {
                                classificacao = "Adequado";
                            }

                            escritor.write(talhao + ","
                                    + String.format(Locale.US, "%.2f", valor));

                            escritor.newLine();

                            escritorClassificacao.write(talhao + ","
                                    + String.format(Locale.US, "%.2f", valor)
                                    + "," + classificacao);

                            escritorClassificacao.newLine();
                        }
                    }

                    escritor.close();
                    escritorClassificacao.close();

                    System.out.println("\nMapa salvo em talhoes.csv!");
                    System.out.println("Classificações salvas em talhoes_classificados.csv!");

                } catch (IOException e) {
                    System.out.println("Erro ao salvar o mapa.");
                }
            }



            else if (opcao == 3) {

                System.out.println("===== HISTÓRICO DE CHUVAS =====");

                File arquivoChuva = new File("historico_chuvas.csv");

                double totalAcumulado = 0;
                double maiorTotal = -1;
                double menorTotal = Double.MAX_VALUE;

                String semanaMaisChuvosa = "";
                String semanaMaisSeca = "";

                int quantidadeSemanas = 0;

                if (arquivoChuva.exists()) {

                    try {

                        BufferedReader leitor = new BufferedReader(
                                new FileReader(arquivoChuva)
                        );

                        String linha;

                        leitor.readLine();

                        while ((linha = leitor.readLine()) != null) {

                            String[] dados = linha.split(",");

                            if (dados.length >= 10) {

                                String data = dados[0];

                                double media = Double.parseDouble(dados[8]);
                                double total = Double.parseDouble(dados[9]);

                                quantidadeSemanas++;

                                totalAcumulado += total;

                                System.out.println("\nSemana: " + data);
                                System.out.printf("Média: %.2f mm%n", media);
                                System.out.printf("Total: %.2f mm%n", total);

                                if (total > maiorTotal) {

                                    maiorTotal = total;
                                    semanaMaisChuvosa = data;
                                }

                                if (total < menorTotal) {

                                    menorTotal = total;
                                    semanaMaisSeca = data;
                                }
                            }
                        }

                        leitor.close();

                    } catch (IOException | NumberFormatException e) {

                        System.out.println("Erro ao consultar o histórico.");
                    }

                } else {

                    System.out.println("Nenhum histórico de chuva encontrado.");
                }

                // DASHBOARD CLIMÁTICO

                System.out.println("===== DASHBOARD CLIMÁTICO =====");

                if (quantidadeSemanas > 0) {

                    double mediaGeral = totalAcumulado / quantidadeSemanas;

                    System.out.println("Semanas cadastradas: " + quantidadeSemanas);

                    System.out.printf("Volume acumulado: %.2f mm%n", totalAcumulado);

                    System.out.println("Semana mais chuvosa: " + semanaMaisChuvosa);

                    System.out.printf("Maior chuva: %.2f mm%n", maiorTotal);

                    System.out.println("Semana mais seca: " + semanaMaisSeca);

                    System.out.printf("Menor chuva: %.2f mm%n", menorTotal);

                    System.out.printf("Média geral: %.2f mm%n", mediaGeral);

                } else {

                    System.out.println("Não existem semanas cadastradas.");
                }



                System.out.println("\n===== RELATÓRIO DA FAZENDA =====");

                File arquivoTalhoes = new File("talhoes.csv");

                if (arquivoTalhoes.exists()) {

                    double somaUmidade = 0;

                    int criticos = 0;
                    int atencao = 0;
                    int adequados = 0;
                    int totalTalhoes = 0;

                    double menorUmidade = Double.MAX_VALUE;
                    double maiorUmidade = -1;

                    String talhaoMaisSeco = "";
                    String talhaoMaisUmido = "";

                    try {

                        BufferedReader leitor = new BufferedReader(
                                new FileReader(arquivoTalhoes)
                        );

                        String linha;

                        leitor.readLine();

                        System.out.println("===== ALERTAS DE IRRIGAÇÃO =====");

                        while ((linha = leitor.readLine()) != null) {

                            String[] dados = linha.split(",");

                            if (dados.length >= 2) {

                                String talhao = dados[0];

                                double valor = Double.parseDouble(dados[1]);

                                totalTalhoes++;

                                somaUmidade += valor;

                                if (valor < 20) {

                                    criticos++;

                                    System.out.println(talhao + " - Crítico");

                                } else if (valor <= 30) {

                                    atencao++;

                                    System.out.println(talhao + " - Atenção");

                                } else {

                                    adequados++;
                                }

                                if (valor < menorUmidade) {

                                    menorUmidade = valor;
                                    talhaoMaisSeco = talhao;
                                }

                                if (valor > maiorUmidade) {

                                    maiorUmidade = valor;
                                    talhaoMaisUmido = talhao;
                                }
                            }
                        }

                        leitor.close();

                        if (totalTalhoes == 16) {

                            int totalAlertas = criticos + atencao;

                            double mediaUmidade = somaUmidade / totalTalhoes;

                            double percentualIrrigacao =
                                    (totalAlertas * 100.0) / totalTalhoes;

                            System.out.println("\nTotal de alertas: " + totalAlertas);

                            System.out.println("\n===== RELATÓRIO GERENCIAL =====");

                            System.out.printf("Média geral de umidade: %.2f%%%n",
                                    mediaUmidade);

                            System.out.println("Talhões críticos: " + criticos);

                            System.out.println("Talhões adequados: " + adequados);

                            System.out.println("Talhões em atenção: " + atencao);

                            System.out.printf("Necessidade de irrigação: %.2f%%%n",
                                    percentualIrrigacao);

                            System.out.println("Talhão mais seco: "
                                    + talhaoMaisSeco + " - " + menorUmidade + "%");

                            System.out.println("Talhão mais úmido: "
                                    + talhaoMaisUmido + " - " + maiorUmidade + "%");



                            try {

                                BufferedWriter escritor = new BufferedWriter(
                                        new FileWriter("relatorio_fazenda.txt")
                                );

                                escritor.write("===== RELATORIO DA FAZENDA =====");
                                escritor.newLine();

                                escritor.write("Data: " + LocalDate.now());
                                escritor.newLine();

                                escritor.write("Media Geral: "
                                        + String.format(Locale.US, "%.2f", mediaUmidade)
                                        + "%");
                                escritor.newLine();

                                escritor.write("Talhoes Criticos: " + criticos);
                                escritor.newLine();

                                escritor.write("Talhoes Adequados: " + adequados);
                                escritor.newLine();

                                escritor.write("Talhoes em Atencao: " + atencao);
                                escritor.newLine();

                                escritor.write("Necessidade de Irrigacao: "
                                        + String.format(Locale.US, "%.2f",
                                        percentualIrrigacao) + "%");
                                escritor.newLine();

                                escritor.write("Talhao mais seco: "
                                        + talhaoMaisSeco + " - " + menorUmidade + "%");
                                escritor.newLine();

                                escritor.write("Talhao mais umido: "
                                        + talhaoMaisUmido + " - " + maiorUmidade + "%");

                                escritor.newLine();

                                escritor.close();

                                System.out.println("\nRelatório exportado com sucesso!");
                                System.out.println("Arquivo: relatorio_fazenda.txt");

                            } catch (IOException e) {

                                System.out.println("Erro ao exportar o relatório.");
                            }

                        } else {

                            System.out.println("O mapa precisa ter os 16 talhões cadastrados.");
                        }

                    } catch (IOException | NumberFormatException e) {

                        System.out.println("Erro ao gerar os relatórios dos talhões.");
                    }

                } else {

                    System.out.println("Cadastre o mapa do campo na opção 2 primeiro.");
                }
            }



            else if (opcao == 4) {

                System.out.println("\nEncerrando o AgroJava...");
                System.out.println("Obrigado por utilizar o sistema!");

            }

            else {

                System.out.println("\nOpção inválida! Tente novamente.");
            }

        } while (opcao != 4);

        entrada.close();
    }
}
