import java.util.*;
import java.io.*;

public class GenerateData {
    private static final String[] palabras = {"Dragon", "Empire", "Quest", "Galaxy", "Legends", "Warrior"};
    private static final String[] categorias = {"Acción", "Aventura", "Estrategia", "RPG", "Deportes", "Simulación"};

    public static ArrayList<Game> generar(int N) {
        ArrayList<Game> juegos = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < N; i++) {
            String name = palabras[rand.nextInt(palabras.length)] + palabras[rand.nextInt(palabras.length)];
            String cat = categorias[rand.nextInt(categorias.length)];
            int price = rand.nextInt(70001);
            int quality = rand.nextInt(101);
            juegos.add(new Game(name, cat, price, quality));
        }
        return juegos;
    }

    public static void main(String[] args) throws IOException {
        int[] sizes = {100, 10000, 1000000};
        for (int size : sizes) {
            ArrayList<Game> data = generar(size);
            Dataset ds = new Dataset(data);
            ds.exportToCSV("dataset_" + size + ".csv");
        }
    }
}