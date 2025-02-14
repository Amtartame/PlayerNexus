
# PlayerNexus 🎮✨

Welcome to **PlayerNexus** – the ultimate Minecraft plugin for flexible player data management! 🚀  
Easily configure your own dynamic data schema, choose your favorite persistent storage, and supercharge your server with blazing fast caching. ⚡️

---

## Table of Contents 📚
- [Overview](#overview)
- [Features](#features)
- [Tech Stack & Dependencies](#tech-stack--dependencies)
- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
- [Troubleshooting](#troubleshooting)
- [Contributing](#contributing)
- [License](#license)

---

## Overview 🌟

**PlayerNexus** is a cutting-edge plugin that allows you to:
- **Customize Your Data Model**: Define your own schema (e.g., `money`, `experience`, etc.) directly in the config file.
- **Persistent Storage Options**:  
  - **YAML** file for local storage 🗃️  
  - **MySQL** database with a dynamically built table – see your data in a neat table format directly in your DB! 🐬
- **Caching Options**:  
  - **Local (In-Memory)** for lightning-fast access 🚀  
  - **Redis** for distributed caching and improved performance across networks 🌐

Every time a player joins, their data is automatically initialized if it doesn't exist, ensuring a seamless experience from the very first login! 😊

---

## Features ✨

- **Dynamic Data Model**  
  Customize your schema by simply editing `config.yml`!  
  **Example:**  
  ```yaml
  model:
    player:
      - money
      - experience
      - level
  ```
  
- **Dual Storage Mechanism**  
  - **Persistent Storage:**  
    - **YAML**: Simple file-based storage for small servers 📄  
    - **MySQL**: Robust, scalable storage with dynamic table creation based on your schema 🛠️  
  - **Caching:**  
    - **Local Memory**: Fast in-server caching ⚡  
    - **Redis**: External caching for high-performance networks 🔥

- **Automatic Data Initialization**  
  No data? No problem! PlayerNexus creates default entries for every new player. 👶

- **In-Game Commands**  
  Use `/pdata` to check and update your stats:
  - `/pdata` – Display all your data 📊  
  - `/pdata money` – Check your money 💰  
  - `/pdata money 500` – Set your money to 500 💵

- **Real-Time Table Updates in MySQL**  
  See your data structure come to life in your database with dynamically created columns that match your model. 📝

---

## Tech Stack & Dependencies 🛠️

- **Java 17+** – The powerhouse behind the plugin. ☕
- **Spigot API (Minecraft 1.21)** – Seamless integration with your Minecraft server. 🧱
- **Jedis** – For Redis caching support. 🔄
- **MySQL Connector/J** – For MySQL database connectivity. 🐬
- **YAML** – For configuration and local storage using Bukkit's built-in support. 📄

### Maven Dependencies Example
```xml
<dependencies>
  <!-- Spigot API -->
  <dependency>
    <groupId>org.spigotmc</groupId>
    <artifactId>spigot-api</artifactId>
    <version>1.21-R0.1-SNAPSHOT</version>
    <scope>provided</scope>
  </dependency>
  
  <!-- Jedis for Redis -->
  <dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
    <version>4.3.1</version>
  </dependency>
  
  <!-- MySQL Connector -->
  <dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.32</version>
  </dependency>
</dependencies>
```

---

## Installation 🚀

1. **Download/Build the Plugin**  
   - Clone the repo or download the latest release.
   - Build the JAR using Maven (or your preferred build tool).

2. **Deploy the JAR**  
   - Place `PlayerNexus.jar` in your server’s `plugins/` folder.

3. **Start Your Server**  
   - Launch your Minecraft server and watch PlayerNexus generate the default configuration files if they don't already exist. 🔥

---

## Configuration ⚙️

All configuration is done via `config.yml`. Customize your persistent storage, caching method, and data model!

```yaml
storage:
  persistent: yml         # Options: yml, mysql
  persistent_yaml:
    path: plugins/PlayerNexus/data.yml
  persistent_mysql:
    host: localhost
    port: 3306
    database: playerdata
    username: user
    password: pass

  cache: local            # Options: local, redis
  cache_redis:
    host: localhost
    port: 6379

model:
  player:
    - money
    - experience
    - level
```

### Highlights:
- **Persistent Storage Options:**  
  Select between **YAML** (local file) and **MySQL** (dynamic table creation based on your schema).  
- **Caching Options:**  
  Choose **local** for in-memory caching or **Redis** for network-wide caching.

---

## Usage 📜

- **Player Data Command:**  
  Type `/pdata` in-game to see your stats. Use additional parameters to get or set values.  
  - `/pdata` – Displays all your stats.
  - `/pdata money` – Shows the current value of `money`.
  - `/pdata money 500` – Sets your `money` value to 500.

- **Automatic Initialization:**  
  New players get default data values (e.g., `0` for all fields) on join. No extra setup needed!

---

## Troubleshooting ⚠️

- **MySQL Issues:**  
  - Ensure your MySQL server is running and credentials in `config.yml` are correct.
  - Verify that the user has privileges to create/alter tables.
  
- **Redis Issues:**  
  - Confirm that your Redis server is running on the specified host and port.
  - Check firewall settings if you're experiencing connection problems.

- **General:**  
  - Always check your server console for detailed error messages.
  - Verify that your `config.yml` is properly formatted in YAML.

---

## Contributing 🤝

We welcome contributions from the community!  
Feel free to open an issue or submit a pull request on GitHub. Let’s make Minecraft servers even more awesome together! 🌟

---

## License 📄

This project is licensed under the **MIT License**. See the [LICENSE](LICENSE) file for details.

---

Thank you for using **PlayerNexus**! Enjoy a smoother, more customizable experience managing player data on your Minecraft server. Happy crafting! 🎉🛠️
