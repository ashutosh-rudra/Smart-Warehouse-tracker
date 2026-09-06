import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SmartWarehouseTracker {

    static Scanner sc = new Scanner(System.in);

    static ArrayList<Item> items = new ArrayList<>();
    static ArrayList<StockMovement> movements = new ArrayList<>();

    static final String ITEM_FILE = "items.dat";
    static final String MOVEMENT_FILE = "movements.dat";

    static int nextId = 1;

    static DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    // ================= ITEM CLASS =================

    static class Item implements Serializable {
        int id;
        String sku;
        String name;
        String category;
        String location;
        int quantity;
        int lowStockThreshold;
        double unitPrice;
        LocalDateTime createdAt;

        Item(int id, String sku, String name, String category,
             String location, int quantity,
             int lowStockThreshold, double unitPrice) {

            this.id = id;
            this.sku = sku;
            this.name = name;
            this.category = category;
            this.location = location;
            this.quantity = quantity;
            this.lowStockThreshold = lowStockThreshold;
            this.unitPrice = unitPrice;
            this.createdAt = LocalDateTime.now();
        }

        boolean isLowStock() {
            return quantity <= lowStockThreshold;
        }

        double getTotalValue() {
            return quantity * unitPrice;
        }
    }

    // ================= STOCK MOVEMENT CLASS =================

    static class StockMovement implements Serializable {
        int itemId;
        String type;
        int quantity;
        String reason;
        LocalDateTime timestamp;

        StockMovement(int itemId, String type,
                      int quantity, String reason) {

            this.itemId = itemId;
            this.type = type;
            this.quantity = quantity;
            this.reason = reason;
            this.timestamp = LocalDateTime.now();
        }
    }

    // ================= MAIN =================

    public static void main(String[] args) {

        loadData();

        System.out.println("==========================================");
        System.out.println("       SMART WAREHOUSE TRACKER");
        System.out.println("       Command Line Interface");
        System.out.println("==========================================");

        while (true) {

            showMenu();

            int choice = readInt("Enter your choice: ");

            switch (choice) {

                case 1:
                    addItem();
                    break;

                case 2:
                    viewItems();
                    break;

                case 3:
                    updateItem();
                    break;

                case 4:
                    deleteItem();
                    break;

                case 5:
                    stockIn();
                    break;

                case 6:
                    stockOut();
                    break;

                case 7:
                    searchItem();
                    break;

                case 8:
                    showLowStockItems();
                    break;

                case 9:
                    showMovementHistory();
                    break;

                case 10:
                    dashboard();
                    break;

                case 11:
                    saveData();
                    System.out.println("\nThank you for using Smart Warehouse Tracker!");
                    System.exit(0);

                default:
                    System.out.println("\nInvalid choice! Please try again.");
            }
        }
    }

    // ================= MENU =================

    static void showMenu() {

        System.out.println("\n------------------------------------------");
        System.out.println("              MAIN MENU");
        System.out.println("------------------------------------------");

        System.out.println("1. Add Inventory Item");
        System.out.println("2. View All Inventory");
        System.out.println("3. Update Inventory Item");
        System.out.println("4. Delete Inventory Item");
        System.out.println("5. Stock In");
        System.out.println("6. Stock Out");
        System.out.println("7. Search Inventory");
        System.out.println("8. Low Stock Alerts");
        System.out.println("9. Movement History");
        System.out.println("10. Dashboard");
        System.out.println("11. Exit");

        System.out.println("------------------------------------------");
    }

    // ================= ADD ITEM =================

    static void addItem() {

        System.out.println("\n========== ADD INVENTORY ITEM ==========");

        String sku = readString("Enter SKU: ");
        String name = readString("Enter item name: ");
        String category = readString("Enter category: ");
        String location = readString("Enter warehouse location: ");

        int quantity = readNonNegativeInt("Enter quantity: ");

        int threshold =
                readNonNegativeInt("Enter low-stock threshold: ");

        double price =
                readNonNegativeDouble("Enter unit price: ");

        // Check duplicate SKU
        for (Item item : items) {
            if (item.sku.equalsIgnoreCase(sku)) {
                System.out.println("Error: SKU already exists.");
                return;
            }
        }

        Item item = new Item(
                nextId++,
                sku,
                name,
                category,
                location,
                quantity,
                threshold,
                price
        );

        items.add(item);

        saveData();

        System.out.println("\nItem added successfully!");
        System.out.println("Generated Item ID: " + item.id);
    }

    // ================= VIEW ITEMS =================

    static void viewItems() {

        System.out.println("\n================ INVENTORY ================");

        if (items.isEmpty()) {
            System.out.println("No inventory items found.");
            return;
        }

        printItemHeader();

        for (Item item : items) {
            printItem(item);
        }

        System.out.println("============================================");
    }

    static void printItemHeader() {

        System.out.printf(
                "%-4s %-12s %-20s %-15s %-12s %-8s %-10s %-10s%n",
                "ID", "SKU", "NAME", "CATEGORY",
                "LOCATION", "QTY", "THRESHOLD", "PRICE"
        );

        System.out.println(
                "--------------------------------------------------------------------------------"
        );
    }

    static void printItem(Item item) {

        System.out.printf(
                "%-4d %-12s %-20s %-15s %-12s %-8d %-10d %-10.2f",
                item.id,
                item.sku,
                item.name,
                item.category,
                item.location,
                item.quantity,
                item.lowStockThreshold,
                item.unitPrice
        );

        if (item.isLowStock()) {
            System.out.print("  <-- LOW STOCK");
        }

        System.out.println();
    }

    // ================= FIND ITEM =================

    static Item findItem(int id) {

        for (Item item : items) {

            if (item.id == id) {
                return item;
            }
        }

        return null;
    }

    // ================= UPDATE ITEM =================

    static void updateItem() {

        System.out.println("\n========== UPDATE ITEM ==========");

        int id = readInt("Enter Item ID: ");

        Item item = findItem(id);

        if (item == null) {
            System.out.println("Item not found.");
            return;
        }

        System.out.println("\nCurrent Details:");

        System.out.println("SKU       : " + item.sku);
        System.out.println("Name      : " + item.name);
        System.out.println("Category  : " + item.category);
        System.out.println("Location  : " + item.location);
        System.out.println("Quantity  : " + item.quantity);
        System.out.println("Threshold : " + item.lowStockThreshold);
        System.out.println("Price     : " + item.unitPrice);

        System.out.println("\nEnter new details:");

        item.sku = readString("Enter new SKU: ");
        item.name = readString("Enter new name: ");
        item.category = readString("Enter new category: ");
        item.location = readString("Enter new location: ");

        item.quantity =
                readNonNegativeInt("Enter new quantity: ");

        item.lowStockThreshold =
                readNonNegativeInt("Enter new threshold: ");

        item.unitPrice =
                readNonNegativeDouble("Enter new unit price: ");

        saveData();

        System.out.println("\nItem updated successfully!");
    }

    // ================= DELETE ITEM =================

    static void deleteItem() {

        System.out.println("\n========== DELETE ITEM ==========");

        int id = readInt("Enter Item ID: ");

        Item item = findItem(id);

        if (item == null) {
            System.out.println("Item not found.");
            return;
        }

        System.out.println("Item: " + item.name);
        System.out.println("SKU : " + item.sku);

        String confirmation =
                readString("Are you sure you want to delete? (yes/no): ");

        if (confirmation.equalsIgnoreCase("yes")) {

            items.remove(item);

            saveData();

            System.out.println("Item deleted successfully.");

        } else {

            System.out.println("Delete operation cancelled.");
        }
    }

    // ================= STOCK IN =================

    static void stockIn() {

        System.out.println("\n========== STOCK IN ==========");

        int id = readInt("Enter Item ID: ");

        Item item = findItem(id);

        if (item == null) {
            System.out.println("Item not found.");
            return;
        }

        int quantity =
                readPositiveInt("Enter quantity to add: ");

        String reason =
                readString("Enter reason: ");

        item.quantity += quantity;

        StockMovement movement =
                new StockMovement(
                        item.id,
                        "STOCK IN",
                        quantity,
                        reason
                );

        movements.add(movement);

        saveData();

        System.out.println("\nStock added successfully.");
        System.out.println("Item       : " + item.name);
        System.out.println("Added      : " + quantity);
        System.out.println("New Stock  : " + item.quantity);
    }

    // ================= STOCK OUT =================

    static void stockOut() {

        System.out.println("\n========== STOCK OUT ==========");

        int id = readInt("Enter Item ID: ");

        Item item = findItem(id);

        if (item == null) {
            System.out.println("Item not found.");
            return;
        }

        int quantity =
                readPositiveInt("Enter quantity to remove: ");

        // Prevent negative stock
        if (quantity > item.quantity) {

            System.out.println(
                    "\nERROR: Insufficient stock!"
            );

            System.out.println(
                    "Available stock: " + item.quantity
            );

            return;
        }

        String reason =
                readString("Enter reason: ");

        item.quantity -= quantity;

        StockMovement movement =
                new StockMovement(
                        item.id,
                        "STOCK OUT",
                        quantity,
                        reason
                );

        movements.add(movement);

        saveData();

        System.out.println("\nStock removed successfully.");
        System.out.println("Item       : " + item.name);
        System.out.println("Removed    : " + quantity);
        System.out.println("Remaining  : " + item.quantity);

        if (item.isLowStock()) {

            System.out.println(
                    "WARNING: Item is now LOW STOCK!"
            );
        }
    }

    // ================= SEARCH =================

    static void searchItem() {

        System.out.println("\n========== SEARCH INVENTORY ==========");

        String keyword =
                readString("Enter SKU, name, category or location: ");

        boolean found = false;

        for (Item item : items) {

            if (
                    item.sku.toLowerCase().contains(keyword.toLowerCase())
                            ||
                    item.name.toLowerCase().contains(keyword.toLowerCase())
                            ||
                    item.category.toLowerCase().contains(keyword.toLowerCase())
                            ||
                    item.location.toLowerCase().contains(keyword.toLowerCase())
            ) {

                if (!found) {
                    printItemHeader();
                }

                printItem(item);

                found = true;
            }
        }

        if (!found) {
            System.out.println("No matching item found.");
        }
    }

    // ================= LOW STOCK =================

    static void showLowStockItems() {

        System.out.println("\n========== LOW STOCK ALERTS ==========");

        boolean found = false;

        for (Item item : items) {

            if (item.isLowStock()) {

                System.out.println(
                        "ID: " + item.id
                                + " | SKU: " + item.sku
                                + " | Item: " + item.name
                                + " | Stock: " + item.quantity
                                + " | Threshold: "
                                + item.lowStockThreshold
                );

                found = true;
            }
        }

        if (!found) {
            System.out.println("No low-stock items.");
        }
    }

    // ================= MOVEMENT HISTORY =================

    static void showMovementHistory() {

        System.out.println("\n========== MOVEMENT HISTORY ==========");

        if (movements.isEmpty()) {

            System.out.println(
                    "No stock movements recorded."
            );

            return;
        }

        System.out.printf(
                "%-8s %-12s %-20s %-10s %-25s%n",
                "Item ID",
                "TYPE",
                "ITEM",
                "QTY",
                "DATE/TIME"
        );

        System.out.println(
                "--------------------------------------------------------------------------"
        );

        for (StockMovement movement : movements) {

            Item item = findItem(movement.itemId);

            String itemName =
                    item != null ? item.name : "Deleted Item";

            System.out.printf(
                    "%-8d %-12s %-20s %-10d %-25s%n",
                    movement.itemId,
                    movement.type,
                    itemName,
                    movement.quantity,
                    movement.timestamp.format(formatter)
            );

            System.out.println(
                    "   Reason: " + movement.reason
            );
        }
    }

    // ================= DASHBOARD =================

    static void dashboard() {

        System.out.println("\n============== DASHBOARD ==============");

        int skuCount = items.size();

        int totalUnits = 0;

        int lowStockCount = 0;

        double totalValue = 0;

        for (Item item : items) {

            totalUnits += item.quantity;

            totalValue += item.getTotalValue();

            if (item.isLowStock()) {
                lowStockCount++;
            }
        }

        System.out.println("Total SKUs       : " + skuCount);
        System.out.println("Total Units      : " + totalUnits);
        System.out.printf(
                "Total Inventory Value : ₹%.2f%n",
                totalValue
        );
        System.out.println(
                "Low Stock Items  : " + lowStockCount
        );
        System.out.println(
                "Total Movements  : " + movements.size()
        );

        System.out.println("----------------------------------------");

        if (lowStockCount > 0) {

            System.out.println("LOW STOCK ALERT:");

            for (Item item : items) {

                if (item.isLowStock()) {

                    System.out.println(
                            "- " + item.name
                                    + " | Stock: "
                                    + item.quantity
                                    + " | Threshold: "
                                    + item.lowStockThreshold
                    );
                }
            }

        } else {

            System.out.println(
                    "All inventory levels are healthy."
            );
        }

        System.out.println("========================================");
    }

    // ================= INPUT METHODS =================

    static String readString(String message) {

        while (true) {

            System.out.print(message);

            String input = sc.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            }

            System.out.println(
                    "Input cannot be empty."
            );
        }
    }

    static int readInt(String message) {

        while (true) {

            try {

                System.out.print(message);

                return Integer.parseInt(
                        sc.nextLine().trim()
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid integer."
                );
            }
        }
    }

    static int readNonNegativeInt(String message) {

        while (true) {

            int value = readInt(message);

            if (value >= 0) {
                return value;
            }

            System.out.println(
                    "Value cannot be negative."
            );
        }
    }

    static int readPositiveInt(String message) {

        while (true) {

            int value = readInt(message);

            if (value > 0) {
                return value;
            }

            System.out.println(
                    "Value must be greater than zero."
            );
        }
    }

    static double readNonNegativeDouble(String message) {

        while (true) {

            try {

                System.out.print(message);

                double value =
                        Double.parseDouble(
                                sc.nextLine().trim()
                        );

                if (value >= 0) {
                    return value;
                }

                System.out.println(
                        "Value cannot be negative."
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid number."
                );
            }
        }
    }

    // ================= FILE STORAGE =================

    static void saveData() {

        try {

            ObjectOutputStream itemOutput =
                    new ObjectOutputStream(
                            new FileOutputStream(ITEM_FILE)
                    );

            itemOutput.writeObject(items);
            itemOutput.close();

            ObjectOutputStream movementOutput =
                    new ObjectOutputStream(
                            new FileOutputStream(MOVEMENT_FILE)
                    );

            movementOutput.writeObject(movements);
            movementOutput.close();

        } catch (IOException e) {

            System.out.println(
                    "Error saving data: "
                            + e.getMessage()
            );
        }
    }

    // ================= LOAD DATA =================

    @SuppressWarnings("unchecked")
    static void loadData() {

        try {

            File itemFile = new File(ITEM_FILE);

            if (itemFile.exists()) {

                ObjectInputStream itemInput =
                        new ObjectInputStream(
                                new FileInputStream(itemFile)
                        );

                items =
                        (ArrayList<Item>)
                                itemInput.readObject();

                itemInput.close();

                // Set next ID
                for (Item item : items) {

                    if (item.id >= nextId) {
                        nextId = item.id + 1;
                    }
                }
            }

        } catch (IOException | ClassNotFoundException e) {

            System.out.println(
                    "Could not load inventory data."
            );
        }

        try {

            File movementFile =
                    new File(MOVEMENT_FILE);

            if (movementFile.exists()) {

                ObjectInputStream movementInput =
                        new ObjectInputStream(
                                new FileInputStream(
                                        movementFile
                                )
                        );

                movements =
                        (ArrayList<StockMovement>)
                                movementInput.readObject();

                movementInput.close();
            }

        } catch (IOException | ClassNotFoundException e) {

            System.out.println(
                    "Could not load movement data."
            );
        }
    }
}
