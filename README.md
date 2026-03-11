# BlogFlow 📝

Веб-додаток для публікації статей з системою авторизації, коментарями та розділенням ролей.

---

## 🚀 Функціонал

### Для гостей (без реєстрації)
- Перегляд списку статей з пагінацією
- Перегляд повного тексту статті
- Перегляд коментарів до статей
- Реєстрація та вхід на сайт

### Для користувачів (USER)
- Додавання власних статей
- Редагування тільки своїх статей
- Додавання та видалення своїх коментарів
- Редагування профілю (email, телефон, адреса)
- Перегляд тем для написання статей

### Для адміністратора (ADMIN)
- Всі можливості користувача
- Редагування та видалення будь-яких статей
- Додавання, видалення тем для статей
- Управління користувачами (додавання, видалення)
- Доступ до адмін панелі

---

## 🛠 Технології

| Технологія | Версія |
|---|---|
| Java | 17 |
| Spring Boot | 3.4.4 |
| Spring Security | 6 |
| Spring Data JPA | 3.4.4 |
| Hibernate | 6 |
| MySQL | 8 |
| Thymeleaf | 3 |
| Bootstrap | 5.3.3 |
| Lombok | latest |
| Maven | 3 |

---

## 🗄 Структура бази даних

```
custom_user         — користувачі системи
├── id
├── login
├── password (BCrypt)
├── role (ADMIN / USER)
├── email
├── phone
└── address

post                — статті блогу
├── id
├── title
├── anons
├── content (TEXT)
├── views
├── created_at
└── user_id → custom_user

comment             — коментарі до статей
├── id
├── content (TEXT)
├── created_at
├── user_id → custom_user
└── post_id → post

topic               — теми для написання статей
├── id
└── title
```

---

## ⚙️ Як запустити локально

### Що потрібно встановити
- Java 17+
- Maven 3+
- MySQL 8+
- IntelliJ IDEA (рекомендовано)

### Кроки запуску

**1. Клонуй репозиторій:**
```bash
git clone https://github.com/your-username/blogflow.git
cd blogflow
```

**2. Створи базу даних в MySQL:**
```sql
CREATE DATABASE blog_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

**3. Налаштуй підключення до бази даних** в файлі `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/blog_db?serverTimezone=Europe/Kyiv
spring.datasource.username=твій_username
spring.datasource.password=твій_пароль
```

**4. Запусти проект:**
```bash
mvn spring-boot:run
```

або через IntelliJ IDEA — запусти клас `BlogApplication.java`

**5. Відкрий у браузері:**
```
http://localhost:8888
```

### Тестові акаунти (створюються автоматично при першому запуску)

| Логін | Пароль | Роль |
|---|---|---|
| admin | password | ADMIN |
| user | password | USER |

---

## 📁 Структура проекту

```
src/main/java/org/example/blog/
├── config/
│   ├── AppConfig.java          # BCrypt, початкові дані
│   └── SecurityConfig.java     # Spring Security конфігурація
├── controller/
│   ├── MainController.java     # Головна сторінка
│   ├── BlogController.java     # Статті та коментарі
│   ├── UserController.java     # Реєстрація, профіль, адмін
│   └── TopicController.java    # Теми для статей
├── service/
│   ├── PostService.java
│   ├── UserService.java
│   ├── TopicService.java
│   └── CommentService.java
├── repository/
│   ├── PostRepository.java
│   ├── UserRepository.java
│   ├── TopicRepository.java
│   └── CommentRepository.java
├── model/
│   ├── Post.java
│   ├── CustomUser.java
│   ├── UserRole.java
│   ├── Topic.java
│   └── Comment.java
├── security/
│   └── UserDetailsServiceImpl.java
└── exception/
    ├── ResourceNotFoundException.java
    └── GlobalExceptionHandler.java
```

---

## 🔐 Безпека

- Паролі хешуються алгоритмом **BCrypt**
- Доступ до ендпоінтів контролюється через **Spring Security**
- Анотація `@PreAuthorize` захищає адміністративні функції
- Редагування статті доступне тільки автору або адміністратору
- Видалення коментаря доступне тільки автору або адміністратору

---

## 👤 Автор

**Сергій Руденко**

---

## 📄 Ліцензія

Проект створений в навчальних цілях.
