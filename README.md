# DailyDigest

An AI-driven entertainment aggregator that automatically generates a daily digest of movies, series, games, and anime. Built with **Spring Boot 3**, this application bridges the gap between massive content libraries and personalized recommendations by leveraging LLMs to curate high-quality summaries and trends.

### Features
- **Automated Curation:** Scheduled tasks to fetch and process data from multiple entertainment sources.
- **AI-Powered Synthesis:** Uses OpenAI/Anthropic APIs to analyze trends and generate human-like summaries.
- **Multi-Category Support:** Segregated logic for Movies, TV Series, Video Games, and Anime.
- **Customizable Preferences:** Users can tune the digest focus (e.g., prioritize specific genres or platforms).
- **RESTful API:** Clean endpoints for retrieving the daily digest via a frontend or external services.

### Tech Stack
- **Language:** Java 21
- **Framework:** Spring Boot 3.x
- **Data Access:** Spring Data JPA / Hibernate
- **Database:** PostgreSQL
- **AI Integration:** Spring AI (or direct RestClient implementation)
- **Scheduling:** Spring Quartz / @Scheduled
- **Testing:** JUnit 5, Mockito, Testcontainers

### How to Run Locally
1. **Clone the repository:**
   ```bash
   git clone https://github.com/Anayo-Anyafulu/DailyDigest.git
   cd DailyDigest
   ```
2. **Setup the database:** Ensure PostgreSQL is running and create a database named `dailydigest`.
3. **Configure Environment Variables:** Create an `application-local.properties` file or set environment variables (see below).
4. **Build and Run:**
   ```bash
   ./mvnw spring-boot:run
   ```

### Environment Variables
| Variable | Description | Default |
| :--- | :--- | :--- |
| `DB_URL` | PostgreSQL connection URL | `jdbc:postgresql://localhost:5432/dailydigest` |
| `DB_USERNAME` | Database username | `postgres` |
| `DB_PASSWORD` | Database password | `password` |
| `AI_API_KEY` | API Key for AI provider (e.g., OpenAI) | *Required* |
| `EXTERNAL_API_KEY` | API Key for data sources (e.g., TMDB/IGDB) | *Required* |

### What I Learned
- **Orchestrating AI in Java:** Implementing structured prompt engineering and handling non-deterministic AI outputs within a rigid Spring Boot type system.
- **Robust Scheduling:** Managing complex retry logic and failure states for automated background jobs.
- **Data Modeling:** Designing a schema that accommodates vastly different entertainment metadata (e.g., "Steam Playtime" vs. "IMDb Rating") while maintaining a unified digest structure.
