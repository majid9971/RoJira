# =============================================================
# Multi-stage Dockerfile – Java Basic Calculator Application
# =============================================================

# ── Stage 1: Build ────────────────────────────────────────────
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copy source files
COPY src/main/java/ ./src/

# Compile Java sources
RUN mkdir -p out && \
    javac -d out src/Product.java src/ProductRepository.java src/ProductService.java src/ProductServer.java src/Calculator.java src/Main.java

# ── Stage 2: Runtime ──────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine AS runtime

# Create a non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Copy compiled classes from builder stage
COPY --from=builder /app/out ./

# Set ownership
RUN chown -R appuser:appgroup /app

USER appuser

EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
  CMD wget -qO- http://localhost:8080/health || exit 1

ENTRYPOINT ["java", "Main"]
