import enums.ActionLetter;
import model.*;
import util.UniversalArray;
import util.UniversalArrayImpl;

import java.util.Scanner;

public class AppRunner {

    private final UniversalArray<Product> products = new UniversalArrayImpl<>();

    private final CoinAcceptor coinAcceptor;
    private final Cashaccceptor cashaccceptor;

    private static boolean isExit = false;

    private AppRunner() {

        products.addAll(new Product[]{
                new Water(ActionLetter.B, 20),
                new CocaCola(ActionLetter.C, 50),
                new Soda(ActionLetter.D, 30),
                new Snickers(ActionLetter.E, 80),
                new Mars(ActionLetter.F, 80),
                new Pistachios(ActionLetter.G, 130)
        });
        coinAcceptor = new CoinAcceptor(100);
        cashaccceptor = new Cashaccceptor(100);
    }

    public static void run() {
        AppRunner app = new AppRunner();
        while (!isExit) {
            app.startSimulation();
        }
    }

    private void startSimulation() {
        print("В автомате доступны:");
        showProducts(products);

        print("Монет на сумму: " + coinAcceptor.getAmount());
        print("Купюр на сумму: " + cashaccceptor.getAmount());


        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        allowProducts.addAll(getAllowedProducts().toArray());
        chooseAction(allowProducts);

    }

    private UniversalArray<Product> getAllowedProducts() {
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        for (int i = 0; i < products.size(); i++) {
            if (coinAcceptor.getAmount() >= products.get(i).getPrice() || cashaccceptor.getAmount() >= products.get(i).getPrice()) {

                allowProducts.add(products.get(i));
            }
        }
        return allowProducts;
    }

    private void chooseAction(UniversalArray<Product> products) {
        showActions(products);
        print("Выберите действие ('h' для выхода):");
        String action = fromConsole().trim();
        if (action.equalsIgnoreCase("h")) {
            isExit = true;
            return;
        }
        print("Выберите товар для покупки:");
        String productCode = fromConsole().trim();
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getActionLetter().getValue().equalsIgnoreCase(productCode)) {
                choosePaymentMethod(products.get(i));
                return;
            }
        }
        print("Товар не найден.");
    }
    private void choosePaymentMethod(Product product) {
        print("Выберите метод оплаты: (1 - монеты, 2 - наличные, 3 - купюры)");
        int paymentMethod = Integer.parseInt(fromConsole().trim());
        switch (paymentMethod) {
            case 1:
                handleCoinPayment(product);
                break;
            case 2:
                handleCashPayment(product);
                break;
           
            default:
                print("Недопустимый метод оплаты.");
                break;
        }
    }

    private void handleCoinPayment(Product product) {
        if (coinAcceptor.getAmount() >= product.getPrice()) {
            coinAcceptor.setAmount(coinAcceptor.getAmount() - product.getPrice());
            print("Вы купили " + product.getName());
        } else {
            print("Недостаточно монет для оплаты.");
        }
    }

    private void handleCashPayment(Product product) {
        if (cashaccceptor.getAmount() >= product.getPrice()) {
            cashaccceptor.setAmount(cashaccceptor.getAmount() - product.getPrice());
            print("Вы купили " + product.getName());
        } else {
            print("Недостаточно наличных для оплаты.");
        }
    }




    private void showActions(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(String.format(" %s - %s", products.get(i).getActionLetter().getValue(), products.get(i).getName()));
        }
    }

    private String fromConsole() {
        return new Scanner(System.in).nextLine();
    }

    private void showProducts(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(products.get(i).toString());
        }
    }

    private void print(String msg) {
        System.out.println(msg);
    }
}
