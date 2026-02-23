# 🏛️ Custody Securities Application

[![Spring Boot](https://img.shields.io/badge/Spring--Boot-3.5.5-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-25-007396?style=for-the-badge&logo=java&logoColor=white)](https://www.oracle.com/java/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=for-the-badge)](https://opensource.org/licenses/MIT)

> **A high-performance settlement and inventory management engine for modern digital custody operations.**

---

## 🌟 Overview

The **Custody Securities Application** is a robust, enterprise-grade solution designed to handle the complexities of modern securities settlement. Built with a focus on compliance and efficiency, it streamlines the ingestion of global financial messaging standards and automates position life-cycle management.

At its core, the application serves as a bridge between complex ISO 20022 messaging and real-time ledger accuracy, providing a single source of truth for security holdings, transactions, and corporate actions.

---

## 🔥 Key Features

### 📨 ISO 20022 Message Ingestion
Full support for industry-standard financial messaging, including **sese.023** (Settlement Instructions), enabling seamless integration with global clearinghouses and CSDs.

### ⚖️ Real-Time Inventory Management
Dynamic tracking of securities positions. View real-time holdings across multiple clients and asset classes with sub-second latency.

### 🔄 Instruction Workflow
Advanced status tracking for settlement instructions—from "Pending" and "Matched" to "Settled"—ensuring full transparency throughout the transaction lifecycle.

### 📊 Corporate Action Processing
Automated handling of mandatory and voluntary corporate actions (e.g., bonus issues), ensuring inventory is updated accurately without manual intervention.

### 🛠️ Interactive UI
A sleek, responsive frontend that provides clear visibility into transaction audit trails and current inventory status.

---

## 💻 Tech Stack

- **Backend:** [Spring Boot 3.5.5](https://spring.io/)
- **Language:** [Java 25](https://openjdk.java.net/)
- **Data Layer:** [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- **Database:** [H2](https://www.h2database.com/html/main.html) (In-memory for lightning-fast processing)
- **Messaging:** [Jakarta XML Binding (JAXB)](https://eclipse-ee4j.github.io/jaxb-ri/) for ISO 20022 parsing
- **Utilities:** [Lombok](https://projectlombok.org/) for clean, boilerplate-free code

---

## 🚀 Getting Started

### Prerequisites

- **JDK 25** or higher
- **Maven 3.8+**

### Quick Start

1. **Clone the repository:**
   ```bash
   git clone https://github.com/[your-username]/custody-securities-app.git
   cd custody-securities-app
   ```

2. **Build the application:**
   ```bash
   mvn clean install
   ```

3. **Launch the app:**
   ```bash
   ./start-custody-app.bat
   ```
   *The application will be available at `http://localhost:8080`.*

---

## 📂 Project Structure

```text
├── src/main/java          # Core logic, controllers, and domain models
├── src/main/resources     # Static assets, UI build, and configuration
├── samples                # ISO 20022 sample message files (sese.023)
├── launch-app.bat         # Automated launch script
└── pom.xml                # Maven project configuration
```

---

## 🛡️ Security & Compliance

This application is designed with **straight-through processing (STP)** principles in mind, minimizing human error and ensuring that audit trails for every transaction are immutable and easily accessible.

---

## 🤝 Contributing

We welcome contributions! Please feel free to submit a Pull Request or open an issue for any bugs or feature requests.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

Developed with ❤️ for the future of Financial Infrastructure.
