import java.util.*;
import java.io.*;

public class Main {

    static ArrayList<Musica> lista = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        carregarMusicas();
        carregarAvaliacoes();

        int op;

        do {
            System.out.println("\n===== TRACKLIST =====");
            System.out.println("1. Cadastrar música");
            System.out.println("2. Avaliar música");
            System.out.println("3. Listar músicas");
            System.out.println("4. Busca sequencial");
            System.out.println("5. Busca binária");
            System.out.println("6. Filtrar por gênero");
            System.out.println("7. Ranking");
            System.out.println("8. Exibir avaliações");
            System.out.println("9. Sair");

            op = sc.nextInt();
            sc.nextLine();

            switch (op) {
                case 1 -> cadastrar();
                case 2 -> avaliar();
                case 3 -> listar();
                case 4 -> buscaSequencial();
                case 5 -> buscaBinaria();
                case 6 -> filtrarGenero();
                case 7 -> ranking();
                case 8 -> exibirAvaliacoes();
                case 9 -> {
                    estatisticas();
                    System.out.println("Encerrando...");
                }
            }

        } while (op != 9);
    }

    // ================= FUNÇÕES =================

    static void cadastrar() {
        System.out.print("Título: ");
        String t = sc.nextLine();

        System.out.print("Artista: ");
        String a = sc.nextLine();

        System.out.print("Gênero: ");
        String g = sc.nextLine();

        System.out.print("Ano: ");
        int ano = sc.nextInt();
        sc.nextLine();

        lista.add(new Musica(t, a, g, ano));
    }

    static void avaliar() {
        System.out.print("Título da música: ");
        String t = sc.nextLine();

        Musica m = buscaSeqObj(t);

        if (m != null) {
            System.out.print("Usuário: ");
            String nome = sc.nextLine();

            System.out.print("Nota: ");
            double nota = sc.nextDouble();
            sc.nextLine();

            System.out.print("Comentário: ");
            String c = sc.nextLine();

            m.adicionarAvaliacao(new Avaliacao(nome, nota, c));
        } else {
            System.out.println("Não encontrada!");
        }
    }

    static void listar() {
        for (Musica m : lista) {
            System.out.println(m);
        }
    }

    static void buscaSequencial() {
        System.out.print("Título: ");
        String t = sc.nextLine();

        int comp = 0;

        for (Musica m : lista) {
            comp++;
            if (m.getTitulo().equalsIgnoreCase(t)) {
                System.out.println("Encontrada: " + m);
                System.out.println("Comparações: " + comp);
                return;
            }
        }

        System.out.println("Não encontrada!");
        System.out.println("Comparações: " + comp);
    }

    static Musica buscaSeqObj(String t) {
        for (Musica m : lista) {
            if (m.getTitulo().equalsIgnoreCase(t)) {
                return m;
            }
        }
        return null;
    }

    static void ordenar() {
        for (int i = 0; i < lista.size(); i++) {
            for (int j = 0; j < lista.size() - 1; j++) {
                if (lista.get(j).getTitulo()
                        .compareToIgnoreCase(lista.get(j + 1).getTitulo()) > 0) {

                    Musica temp = lista.get(j);
                    lista.set(j, lista.get(j + 1));
                    lista.set(j + 1, temp);
                }
            }
        }
    }

    static void buscaBinaria() {
        ordenar();

        System.out.print("Título: ");
        String t = sc.nextLine();

        int ini = 0, fim = lista.size() - 1, comp = 0;

        while (ini <= fim) {
            int meio = (ini + fim) / 2;
            comp++;

            int res = lista.get(meio).getTitulo()
                    .compareToIgnoreCase(t);

            if (res == 0) {
                System.out.println("Encontrada: " + lista.get(meio));
                System.out.println("Comparações: " + comp);
                return;
            } else if (res < 0) {
                ini = meio + 1;
            } else {
                fim = meio - 1;
            }
        }

        System.out.println("Não encontrada!");
        System.out.println("Comparações: " + comp);
    }

    static void filtrarGenero() {
        System.out.print("Gênero: ");
        String g = sc.nextLine();

        for (Musica m : lista) {
            if (m.getGenero().equalsIgnoreCase(g)) {
                System.out.println(m);
            }
        }
    }

    static void ranking() {
        ArrayList<Musica> copia = new ArrayList<>(lista);

        copia.sort((a, b) -> Double.compare(b.calcularMedia(), a.calcularMedia()));

        int limite = Math.min(10, copia.size());

        for (int i = 0; i < limite; i++) {
            System.out.println((i + 1) + "º " + copia.get(i));
        }
    }

    static void exibirAvaliacoes() {
        System.out.print("Título: ");
        String t = sc.nextLine();

        Musica m = buscaSeqObj(t);

        if (m != null) {
            System.out.println("Média: " + m.calcularMedia());

            for (Avaliacao a : m.getAvaliacoes()) {
                System.out.println(a);
            }
        }
    }

    static void estatisticas() {
        int totalMusicas = lista.size();
        int totalAvaliacoes = 0;
        double soma = 0;

        for (Musica m : lista) {
            totalAvaliacoes += m.getAvaliacoes().size();
            soma += m.calcularMedia();
        }

        double mediaGeral = totalMusicas > 0 ? soma / totalMusicas : 0;

        System.out.println("Total de músicas: " + totalMusicas);
        System.out.println("Total de avaliações: " + totalAvaliacoes);
        System.out.println("Média geral: " + mediaGeral);
    }

    // ================= ARQUIVOS =================

    static void carregarMusicas() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("musicas.txt"));
            String linha;

            while ((linha = br.readLine()) != null) {
                String[] p = linha.split(";");

                lista.add(new Musica(
                        p[0], p[1], p[2], Integer.parseInt(p[3])
                ));
            }

            br.close();
        } catch (IOException e) {
            System.out.println("Erro ao carregar músicas!");
        }
    }

    static void carregarAvaliacoes() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("avaliacoes.txt"));
            String linha;

            while ((linha = br.readLine()) != null) {
                String[] p = linha.split(";");

                Musica m = buscaSeqObj(p[0]);

                if (m != null) {
                    m.adicionarAvaliacao(new Avaliacao(
                            p[1],
                            Double.parseDouble(p[2]),
                            p[3]
                    ));
                }
            }

            br.close();
        } catch (IOException e) {
            System.out.println("Erro ao carregar avaliações!");
        }
    }
}