# Problem Statement

## Problem Statement

Small and mid-sized warehouses frequently manage inventory using spreadsheets or paper logs. This approach has several recurring problems: there is no reliable audit trail of stock movements, no automatic warning when an item is close to running out, and searching or filtering across a growing list of SKUs becomes slow and error-prone as the catalog grows. Manual quantity updates are also easy to get wrong, since nothing enforces that stock can't be removed beyond what's available.

**Smart Warehouse Tracker** addresses this by providing a lightweight, self-contained web application that lets a warehouse operator manage inventory, record every stock movement, and get automatic low-stock alerts — without needing to install or administer a database server.

## Scope of the Project

**In scope:**
- CRUD management of warehouse items (SKU, name, category, location, quantity, unit price, per-item low-stock threshold)
- Recording stock-in and stock-out events with a reason, and maintaining a full movement history
- Automatic low-stock flagging based on each item's own threshold
- Search and filtering of inventory by name, SKU, category, location, and stock status
- A dashboard summarizing total SKUs, total units, total inventory value, and active low-stock alerts
- CSV export of the current (filtered) inventory for offline reporting
- File-based (JSON) persistence — no external database dependency

**Out of scope (for this version):**
- Multi-user accounts, authentication, and role-based access control
- Barcode/QR scanning integration
- Multi-warehouse transfer workflows (moving stock between separate warehouse sites)
- Real-time notifications (email/SMS) for low stock — alerts are shown in-app only
- Supplier/purchase-order management

## Target Users

- **Warehouse operators / store-room managers** who need a simple, no-setup way to track what's in stock and get warned before running out.
- **Small business owners** who currently rely on spreadsheets and want a lightweight upgrade without the overhead of enterprise inventory software.
- **Students/evaluators** reviewing this as an academic project demonstrating Java, Spring Boot, and layered application architecture.

## High-Level Features

1. **Inventory Management** — add, edit, delete, search, and filter warehouse items.
2. **Stock Movement Tracking** — stock-in/stock-out actions that update quantities and are logged to an audit trail.
3. **Dashboard & Alerts** — aggregate statistics and automatic low-stock flagging per item.
4. **Reporting** — CSV export of inventory data.
5. **File-Based Storage** — all data persisted as JSON files, keeping the app dependency-free and easy to run.
