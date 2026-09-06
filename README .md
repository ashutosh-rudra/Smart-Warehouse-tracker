# 📦 Smart Warehouse Tracker

A Spring Boot web application for tracking warehouse inventory — stock levels, movement history, and low-stock alerts — using simple JSON files for storage instead of a database.

## Overview

Small warehouses and store rooms often track inventory in spreadsheets, which quickly become error-prone: no audit trail of who took what, no automatic low-stock warnings, and no easy way to search across hundreds of SKUs. **Smart Warehouse Tracker** solves this with a lightweight web app that any single warehouse operator can run locally — no database server to install, just Java and Maven.

## Features

- **Inventory Management (CRUD)** — add, edit, and delete items with SKU, category, location, quantity, unit price, and a per-item low-stock threshold.
- **Search & Filter** — filter the inventory list by name/SKU, category, location, or "low stock only".
- **Stock In / Stock Out** — record every addition or removal of stock with a reason, keeping quantities always in sync.
- **Movement History** — a full audit log of every stock-in/stock-out event, newest first.
- **Low Stock Alerts** — items are automatically flagged once quantity drops to or below their own threshold.
- **Dashboard** — at-a-glance totals: number of SKUs, total units, total inventory value, category count, and current low-stock items.
- **CSV Export** — export the (filtered) inventory list as a CSV file for reporting.
- **File-based persistence** — all data lives in human-readable JSON files (`data/items.json`, `data/movements.json`); no external database required.

## Technologies / Tools Used

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.3 (Spring Web, Spring Validation) |
| View layer | Thymeleaf (server-rendered HTML) |
| Persistence | Flat JSON files, read/written via Jackson |
| CSV export | OpenCSV |
| Build tool | Maven |
| Testing | JUnit 5, Mockito |

## Project Structure

```
smart-warehouse-tracker/
├── src/main/java/com/warehouse/tracker/
│   ├── model/            Item, StockMovement
│   ├── repository/       ItemRepository, MovementRepository (JSON file I/O)
│   ├── service/          ItemService, StockService, CsvExportService
│   ├── controller/       DashboardController, ItemController, StockController
│   ├── exception/        Custom exceptions + GlobalExceptionHandler
│   └── WarehouseTrackerApplication.java
├── src/main/resources/
│   ├── templates/        Thymeleaf pages (dashboard, items, item-form, movements, error)
│   ├── static/css/       Stylesheet
│   └── data/             Seed JSON files (empty arrays)
├── src/test/java/        Unit tests (JUnit 5 + Mockito)
├── pom.xml
├── README.md
└── statement.md
```

## Steps to Install & Run

### Prerequisites
- Java 17 or later
- Maven 3.8+

### Run locally

```bash
# 1. Clone the repository
git clone <your-repo-url>
cd smart-warehouse-tracker

# 2. Build the project
mvn clean install

# 3. Run the application
mvn spring-boot:run
```

The app starts at **http://localhost:8080**

On first run, `data/items.json` and `data/movements.json` are created automatically in the project's working directory (empty arrays) — no setup required.

### Build a runnable JAR instead

```bash
mvn clean package
java -jar target/smart-warehouse-tracker.jar
```

## Instructions for Testing

Unit tests cover the core business rules (low-stock detection, total value calculation, stock-in/stock-out logic including insufficient-stock validation):

```bash
mvn test
```

Manual testing checklist:
1. Open `http://localhost:8080` — dashboard should show zero items initially.
2. Go to **Inventory → Add Item**, create a few items with different categories/locations.
3. Use **+ In** / **− Out** buttons to adjust stock and confirm the dashboard/low-stock badge updates.
4. Check **Movement History** to confirm every stock change was logged.
5. Try removing more stock than available — the app should show a friendly "Insufficient Stock" error page instead of crashing.
6. Use the search/filter bar and the **Export CSV** button on the Inventory page.

## Screenshots

_Add screenshots of the Dashboard, Inventory list, and Item form here after running the app locally._

## Author

Built as part of a VITyarthi "Build Your Own Project" submission.
