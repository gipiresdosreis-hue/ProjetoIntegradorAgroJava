package com.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner entrada = new Scanner(System.in);

        int opcao = 0;
        double soma = 0;
        double maiorChuva = 0;
        double[] chuva = new double[7];

        int diaMaiorChuva = 0;

        String[] dias = {
                "Segunda",
                "Terça",
                "Quarta",
                "Quinta",
                "Sexta",
                "Sábado",
                "Domingo"
        };


        double[][] umidade = new double[4][4];

        do {

            System.out.println("Menu Consolidação de Safra");
            System.out.println("1 - Cadastrar Dados");
            System.out.println("2 - Exibir Mapa do Campo");
            System.out.println("3 - Relatório de Alertas de Irrigação");
            System.out.println("4 - Encerrar sistema");
            System.out.print("Digite uma das opções: ");

            opcao = entrada.nextInt();
            entrada.nextLine();


            if (opcao == 1) {

                soma = 0;
                maiorChuva = 0;

                for (int i = 0; i < 7; i++) {

                    System.out.print("Informe o volume de chuva de "
                            + dias[i] + " (mm): ");

                    chuva[i] = entrada.nextDouble();

                    soma += chuva[i];

                    if (i == 0 || chuva[i] > maiorChuva) {
                        maiorChuva = chuva[i];
                        diaMaiorChuva = i;
                    }
                }

                double media = soma / 7;

                System.out.println("RESULTADO");

                System.out.printf(
                        "Média semanal de chuva: %.2f mm%n",
                        media
                );

                System.out.println(
                        "Dia com maior índice de chuva: "
                                + dias[diaMaiorChuva]
                );

                System.out.printf(
                        "Maior volume registrado: %.2f mm%n",
                        maiorChuva
                );

            }


            else if (opcao == 2) {

                System.out.println("MAPA DO CAMPO");


                for (int linha = 0; linha < 4; linha++) {

                    for (int coluna = 0; coluna < 4; coluna++) {

                        System.out.print(
                                "Informe a umidade do talhão ["
                                        + linha + "][" + coluna + "] (%): "
                        );

                        umidade[linha][coluna] =
                                entrada.nextDouble();
                    }
                }


                System.out.println("MATRIZ DE UMIDADE");

                for (int linha = 0; linha < 4; linha++) {

                    for (int coluna = 0; coluna < 4; coluna++) {

                        System.out.printf(
                                "%6.1f%% ",
                                umidade[linha][coluna]
                        );
                    }

                    System.out.println();
                }


                System.out.println(
                        "ALERTAS DE IRRIGAÇÃO"
                );

                boolean existeAlerta = false;

                for (int linha = 0; linha < 4; linha++) {

                    for (int coluna = 0; coluna < 4; coluna++) {

                        if (umidade[linha][coluna] < 30) {

                            System.out.println(
                                    "Talhão ["
                                            + linha + "][" + coluna
                                            + "] necessita de irrigação. "
                                            + "Umidade: "
                                            + umidade[linha][coluna]
                                            + "%"
                            );

                            existeAlerta = true;
                        }
                    }
                }

                if (!existeAlerta) {

                    System.out.println(
                            "Nenhum talhão necessita de irrigação."
                    );
                }

            }


            else if (opcao == 3) {

                System.out.println(
                        "RELATÓRIO DE ALERTAS DE IRRIGAÇÃO"
                );


                System.out.println("Volume de chuva da semana:");

                for (int i = 0; i < 7; i++) {

                    System.out.printf(
                            "%s: %.2f mm%n",
                            dias[i],
                            chuva[i]
                    );
                }


                System.out.printf(
                        "Média semanal: %.2f mm%n",
                        soma / 7
                );


                System.out.println(
                        "Talhões que necessitam de irrigação:"
                );

                boolean existeAlerta = false;

                for (int linha = 0; linha < 4; linha++) {

                    for (int coluna = 0; coluna < 4; coluna++) {

                        if (umidade[linha][coluna] < 30) {

                            System.out.printf(
                                    "Talhão [%d][%d] - Umidade: %.1f%%%n",
                                    linha,
                                    coluna,
                                    umidade[linha][coluna]
                            );

                            existeAlerta = true;
                        }
                    }
                }

                if (!existeAlerta) {

                    System.out.println(
                            "Nenhum talhão está com umidade abaixo de 30%."
                    );
                }

            }


            else if (opcao == 4) {

                System.out.println(
                        "Sistema encerrado."
                );

            }


            else {

                System.out.println(
                        "Opção inválida!"
                );
            }

        } while (opcao != 4);

        entrada.close();
    }
}


