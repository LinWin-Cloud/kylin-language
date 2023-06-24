public class Main {
    public static void main(String[] args) {
        try {
            main.mainApp.main(args);
        }catch (Exception exception) {
            System.out.println("ERR: "+exception.getMessage());
        }
    }
}
