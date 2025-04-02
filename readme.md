# StockMate - Inventory Management System

![Java](https://img.shields.io/badge/Java-17-blue)
![Swing](https://img.shields.io/badge/GUI-Swing-orange)
![JSON](https://img.shields.io/badge/Data-JSON-lightgrey)

StockMate is a comprehensive inventory management system designed for small to medium-sized businesses. It provides features for product management, supplier tracking, customer management, order processing, and user administration.

## Features

### Core Modules
- **Product Management**
    - Add/edit/delete products
    - Track stock quantities
    - Set alert thresholds for low stock
    - Categorize products

- **Supplier Management**
    - Maintain supplier records
    - Track contact information
    - View supplier-product relationships

- **Customer Management**
    - Store customer details
    - Track contact information
    - Manage customer records

- **Order Processing**
    - Create new orders
    - Track order history
    - Manage order status
    - Calculate order totals

- **User Management**
    - Role-based access control (Admin, Manager, User)
    - Secure authentication
    - User profile management

### Technical Features
- JSON-based data persistence
- Modern Swing GUI
- Role-based interface customization
- Responsive design
- Input validation

## Installation

### Prerequisites
- Java 17 or later
- Maven (for building)

### Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/stockmate.git

2. Navigate to the project directory:
   ```bash
   cd stockmate

3. Build the project:
   ```bash
    mvn clean package

4. Run the application:
   ```bash
   java -jar target/stockmate.jar
   
## Usage
1. Login :
    - Use the default credentials to login:
    - Different roles have different access levels:
        - Admin : Full access to all modules
        - User : Basic access to view products and orders

2. Navigation :
    - Admin see all tabs (Product, Supplier, Customer, Order, User)
    - User see only Product and Order tabs

3. Data Management :
    - Use the tables to view records
    - Use the form to add records
    - Select a row in the table to edit or delete a record

## Data Storage
All data is stored in JSON files in the src/data/ directory:
- products.json
- suppliers.json
- customers.json
- customer_order.json
- users.json

## Development
### Dependencies
- Google Gson (v2.8.8)
- Java Swing
- 
### Build
The project uses Maven for dependency management. To build:
```bash
mvn clean package
```

## Screenshots

### Login
![Login](screenshots/login.png)

### Admin view

1. Product :

![Product](screenshots/product.png)

3. Supplier:

![Supplier](screenshots/supplier.png)

4. Order:

![Order](screenshots/order.png)

4. User:

![User](screenshots/user.png)

### User view
![Customer](screenshots/customers.png)