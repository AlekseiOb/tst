import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        ToyStore store = new ToyStore();
        store.addToy(new Toy(1, "Teddy Bear", 10, 30.0));
        store.addToy(new Toy(2, "Lego", 5, 50.0));
        store.addToy(new Toy(3, "RoboCop", 20, 20.0));

        store.drawPrize();
        store.drawPrize();
        store.drawPrize();

        try {
            Toy prizeToy = store.getPrizeToy();
            if (prizeToy != null) {
                System.out.println("Congratulations! You won: " + prizeToy.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Toy {
    private int id;
    private String name;
    private int quantity;
    private double weight;

    public Toy(int id, String name, int quantity, double weight) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Toy{id=" + id + ", name='" + name + "', quantity=" + quantity + ", weight=" + weight + "}";
    }
}

class ToyStore {
    private List<Toy> toys = new ArrayList<Toy>();
    private Queue<Toy> prizeQueue = new LinkedList<Toy>();
    private Random random = new Random();

    public void addToy(Toy toy) {
        toys.add(toy);
    }

    public void updateToyWeight(int id, double newWeight) {
        for (Toy toy : toys) {
            if (toy.getId() == id) {
                toy.setWeight(newWeight);
                break;
            }
        }
    }

    public void drawPrize() {
        double totalWeight = toys.stream().mapToDouble(Toy::getWeight).sum();
        double randomValue = random.nextDouble() * totalWeight;
        double cumulativeWeight = 0.0;

        for (Toy toy : toys) {
            cumulativeWeight += toy.getWeight();
            if (randomValue <= cumulativeWeight && toy.getQuantity() > 0) {
                prizeQueue.add(toy);
                toy.setQuantity(toy.getQuantity() - 1);
                break;
            }
        }
    }

    public Toy getPrizeToy() throws IOException {
        if (prizeQueue.isEmpty()) {
            System.out.println("No prize toys available.");
            return null;
        }

        Toy prizeToy = prizeQueue.poll();
        saveToyToFile(prizeToy);
        return prizeToy;
    }

    private void saveToyToFile(Toy toy) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("winning_toys.txt", true))) {
            writer.write(toy.toString());
            writer.newLine();
        }
    }
}
