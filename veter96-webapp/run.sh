#!/bin/bash

# Veter96 Web Application Startup Script

echo "=========================================="
echo "Veter96 Web Application"
echo "=========================================="
echo ""

# Check if Java is installed
if ! command -v java &> /dev/null
then
    echo "❌ Java is not installed. Please install Java 17 or higher."
    exit 1
fi

echo "✅ Java found: $(java -version 2>&1 | head -n 1)"

# Check if Maven is installed
if ! command -v mvn &> /dev/null
then
    echo "❌ Maven is not installed."
    echo "Please install Maven 3.6 or higher, or use the Maven wrapper (./mvnw) if available."
    echo ""
    echo "Installation instructions:"
    echo "- Ubuntu/Debian: sudo apt install maven"
    echo "- CentOS/RHEL: sudo yum install maven"
    echo "- macOS: brew install maven"
    echo "- Windows: Download from https://maven.apache.org/"
    exit 1
fi

echo "✅ Maven found: $(mvn -version | head -n 1)"
echo ""

# Start the application
echo "🚀 Starting Veter96 Web Application..."
echo "📁 Working directory: $(pwd)"
echo "🌐 Application will be available at: http://localhost:8080"
echo ""
echo "Press Ctrl+C to stop the application"
echo "=========================================="

# Run the Spring Boot application
mvn spring-boot:run