# 📰 DailyDigest - AI-Powered Entertainment News Aggregator

An AI-driven Spring Boot 3 application that automatically generates daily entertainment news briefs covering gaming, movies, and TV series. The system aggregates data from multiple real-world APIs, uses AI to summarize and analyze content, and produces beautiful, dynamically generated HTML newsletters.

![Java](https://img.shields.io/badge/Java-21-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-green)
![Spring AI](https://img.shields.io/badge/Spring%20AI-1.0.0--M5-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)

---

## 🚀 Features

- **Multi-Source Data Aggregation**: Collects from TMDb (movies & TV), RAWG (gaming), and NewsAPI
- **AI-Powered Summarization**: Uses Ollama (Mistral 7B) via Spring AI to generate professional summaries
- **Beautiful HTML Newsletters**: Magazine-style responsive templates with modern design
- **Automated Scheduling**: Daily digest generation at configurable times
- **REST API**: Clean endpoints for manual triggering and retrieval
- **Database Persistence**: Stores all generated digests with H2 (dev) and PostgreSQL (prod)
- **Reactive HTTP Clients**: Non-blocking API calls with WebClient
- **OpenAPI Documentation**: Interactive Swagger UI

---

## 📋 Prerequisites

- **Java 21** (JDK 21.0.5 or later)
- **Maven 3.9+**
- **Ollama** (for local AI)
- **API Keys**:
  - NewsAPI: Already configured
  - RAWG: Already configured
  - TMDb: Get from [https://www.themoviedb.org/settings/api](https://www.themoviedb.org/settings/api)

---

## 🛠️ Installation & Setup

### 1. Clone & Navigate

```bash
cd /home/junior/Documents/Projects/DailyDigest
```

### 2. Install & Configure Ollama

```bash
# Install Ollama (if not already installed)
curl https://ollama.ai/install.sh | sh

# Start Ollama server
ollama serve

# In another terminal, pull the Mistral model
ollama pull mistral:7b
```

### 3. Configure TMDb API Key

Edit `src/main/resources/application.yml`:

```yaml
api:
  tmdb:
    key: YOUR_TMDB_API_KEY_HERE
```

Or set as environment variable:

```bash
export TMDB_API_KEY=your_tmdb_key_here
```

### 4. Build the Project

```bash
# Using Java 21
bash -c 'source ~/.sdkman/bin/sdkman-init.sh && mvn clean package'
```

---

## 🎯 Running the Application

### Start the Application

```bash
bash -c 'source ~/.sdkman/bin/sdkman-init.sh && mvn spring-boot:run'
```

The application will start on **http://localhost:8080**

---

## 📡 API Endpoints

### Generate a Digest

```bash
curl -X POST http://localhost:8080/api/v1/digest/generate
```

### View Latest Digest (JSON)

```bash
curl http://localhost:8080/api/v1/digest/latest | jq
```

### View Latest Digest (HTML in Browser)

```
http://localhost:8080/api/v1/digest/latest/html
```

### Get Digest by Date

```bash
curl http://localhost:8080/api/v1/digest/date/2025-12-14 | jq
```

### View HTML by Date

```
http://localhost:8080/api/v1/digest/date/2025-12-14/html
```

---

## 🗄️ Database Access

### H2 Console (Development)

URL: **http://localhost:8080/h2-console**

- **JDBC URL**: `jdbc:h2:mem:dailydigest`
- **Username**: `sa`
- **Password**: _(leave empty)_

### View Data

```sql
-- View all digests
SELECT * FROM DIGESTS ORDER BY DATE DESC;

-- View digest sections
SELECT * FROM DIGEST_SECTIONS WHERE DIGEST_ID = 1;

-- Check digest status
SELECT ID, DATE, TITLE, STATUS, CREATED_AT 
FROM DIGESTS 
WHERE STATUS = 'COMPLETED';
```

---

## 📖 API Documentation

Interactive Swagger UI: **http://localhost:8080/swagger-ui.html**

OpenAPI JSON: **http://localhost:8080/api-docs**

---

## ⏰ Scheduled Jobs

The digest automatically generates daily at **8:00 AM** (configured via `digest.scheduling.cron` in `application.yml`).

To disable scheduling:

```yaml
digest:
  scheduling:
    enabled: false
```

To change schedule (e.g., every 5 minutes for testing):

```yaml
digest:
  scheduling:
    cron: "0 */5 * * * *"
```

---

## 🏗️ Architecture

```
┌─────────────────┐
│   Scheduler     │
│   (Daily 8 AM)  │
└────────┬────────┘
         │
         ▼
┌─────────────────────────────────────────┐
│      DailyDigestService                 │
│  (Orchestrates digest generation)       │
└────┬──────┬──────┬──────┬──────┬────────┘
     │      │      │      │      │
     ▼      ▼      ▼      ▼      ▼
┌────────┐ ┌────┐ ┌────┐ ┌─────┐ ┌──────────┐
│NewsAPI │ │TMDb│ │RAWG│ │ AI  │ │  HTML    │
│ Client │ │    │ │    │ │Ollam│ │Rendering │
└────────┘ └────┘ └────┘ └─────┘ └──────────┘
     │      │      │        │         │
     └──────┴──────┴────────┴─────────┘
                   │
                   ▼
            ┌─────────────┐
            │  Database   │
            │  (H2/PG SQL)│
            └─────────────┘
```

---

## 📁 Project Structure

```
src/main/java/junioranyafulu/DailyDigest/
├── client/                  # API Clients
│   ├── NewsApiClient.java
│   ├── TMDbClient.java
│   ├── RAWGClient.java
│   └── OllamaClient.java
├── controller/              # REST Controllers
│   └── DailyDigestController.java
├── domain/                  # JPA Entities
│   ├── Digest.java
│   ├── DigestSection.java
│   ├── DigestStatus.java
│   └── SectionType.java
├── dto/                     # Data Transfer Objects
│   ├── NewsAPI DTOs
│   ├── tmdb/
│   └── rawg/
├── repository/              # Spring Data JPA
│   ├── DigestRepository.java
│   └── DigestSectionRepository.java
├── scheduler/               # Scheduled Jobs
│   └── DigestScheduler.java
├── service/                 # Business Logic
│   ├── DailyDigestService.java
│   └── HtmlRenderingService.java
└── DailyDigestApplication.java

src/main/resources/
├── application.yml          # Configuration
└── templates/
    └── digest-template.html # Thymeleaf template
```

---

## 🎨 HTML Newsletter Preview

The generated newsletter features:

- **Gradient header** with date
- **AI-generated summary** in highlighted box
- **Gaming section** with game cards (title, rating, genres)
- **Movies section** with movie cards (title, rating, overview)
- **TV shows section** with show cards
- **"Worth Your Time" recommendations** in vibrant gradient box
- **Responsive design** (mobile-friendly)

---

## 🔧 Configuration

### Application Properties (`application.yml`)

```yaml
spring:
  datasource:
    url: jdbc:h2:mem:dailydigest  # H2 for development
  ai:
    ollama:
      base-url: http://localhost:11434
      chat:
        options:
          model: mistral:7b
          temperature: 0.7

api:
  newsapi:
    key: 7ae1cf97e29d441d9de83ae6b6546ae4
  tmdb:
    key: ${TMDB_API_KEY}
  rawg:
    key: 3ec94fe6cc424d38ad4883d8302cbdac

digest:
  scheduling:
    enabled: true
    cron: "0 0 8 * * *"  # Daily at 8 AM
```

---

## 🐳 Docker Deployment (Optional)

```bash
# Build Docker image
docker build -t dailydigest:latest .

# Run with docker-compose
docker-compose up -d
```

---

## 🧪 Testing

### Manual Test Flow

1. **Start Ollama**:
   ```bash
   ollama serve
   ```

2. **Run Application**:
   ```bash
   mvn spring-boot:run
   ```

3. **Generate Digest**:
   ```bash
   curl -X POST http://localhost:8080/api/v1/digest/generate
   ```

4. **View in Browser**:
   - Open: `http://localhost:8080/api/v1/digest/latest/html`

5. **Check Database**:
   - H2 Console: `http://localhost:8080/h2-console`
   - Run: `SELECT * FROM DIGESTS;`

---

## 🚨 Troubleshooting

### Issue: Ollama Connection Failed

**Solution**: Make sure Ollama is running:
```bash
ollama serve
```

### Issue: TMDb API Errors

**Solution**: Verify API key is set:
```bash
echo $TMDB_API_KEY
```

### Issue: Build Fails with Java Error

**Solution**: Ensure Java 21 is active:
```bash
java --version  # Should show Java 21
```

---

## 🛣️ Roadmap

- [ ] User personalization (favorite genres, platforms)
- [ ] Email delivery integration
- [ ] PDF export
- [ ] Trending analytics dashboard
- [ ] Redis caching
- [ ] Multi-language support

---

## 📄 License

MIT License - feel free to use and modify!

---

## 👤 Author

**Junior Anyafulu**
- Email: chibuikeanyafulu@gmail.com

---

## 🙏 Acknowledgments

- **TMDb** - Movie and TV data
- **RAWG** - Gaming data
- **NewsAPI** - Entertainment news
- **Spring AI** - AI abstraction layer
- **Ollama** - Local AI inference

---

## 📸 Screenshots

_Screenshots will be added after first successful run_

---

**Built with ❤️ using Spring Boot 3 and Spring AI**
