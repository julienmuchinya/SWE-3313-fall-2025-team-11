# SWE-3313-fall-2025-team-11

The team project is 50% of your semester grade. This project attempts to simulate the software development process a small team might follow to produce a software solution for a client (instructor). The project has seven graded components, one of which is graded by your teammates. The entire project is 50% of your semester grade.

## Team 11 - Jeff's Roughnecks

- Repository: https://github.com/julienmuchinya/SWE-3313-fall-2025-team-11

### Team members

- Lai, Isaac - ilai@students.kennesaw.edu
- Moyers, Matthew - mmoyers2@students.kennesaw.edu
- Muchinya, Julien - jmuchiny@students.kennesaw.edu
- Parker, Javontae - MISSING
- Parrish, Cooper - cparri31@students.kennesaw.edu

## Implementation

Language: Java 24

Framework: Spring Boot (Maven)

Storage: SQLite (local file store.db)

## Quick start (Windows PowerShell)

1. Build:

```powershell
mvn -v; mvn clean package -DskipTests
```

2. Run:

```powershell
mvn spring-boot:run
```

The application will start on http://localhost:8080 and exposes a simple REST API:

- GET /api/artworks -> list available artworks
- POST /api/artworks -> create artwork (JSON body)
- GET /api/artworks/{id} -> get artwork
- POST /api/artworks/{id}/purchase -> mark artwork sold

Notes:

- The project uses a local SQLite file `store.db` (created at runtime). Ensure write permissions in the repo folder.
- This is a minimal scaffold to satisfy course requirements (Spring + SQLite). Expand with front-end, auth, tests as next steps.
