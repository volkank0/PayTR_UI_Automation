# PayTR_UI_Automation

This project is designed to test the functionality of on a website and verify processes.

---

## Features

- **Selenium WebDriver** for web automation.
- Test execution using **JUnit 5**.
- Driver management with **WebDriver Manager**.
- Read test data from Excel files using **Apache POI**.
- Detailed logging with **Log4J**.

---

## Requirements

- **Java 8** or later
- **Apache Maven 3.x**
- **Google Chrome** and **ChromeDriver**

---

## Installation

### 1. Clone the Repository

```bash
git clone https://https://github.com/volkank0/PayTR_UI_Automation
cd PayTR_UI_Automation
```
---
### 2. Install Maven Dependencies

```bash
mvn clean install
```
---

## Usage
### Run All Tests

```bash
mvn test
```
---

### Run a Specific Test
```bash
mvn -Dtest=HomePageTest test
mvn -Dtest=PayWithLinksTest test

```
