# Veter96 Web Application

## Overview

This is a Spring Boot MVC web application that recreates the structure and navigation of the original veter96.ru website. The application provides a comprehensive manual for Opel Vectra B automobiles (manufactured since 1995) with detailed information about repair and operation.

## Features

- **Faithful Navigation Structure**: Preserves the original site's navigation with 12 main sections
- **Responsive Design**: Modern, mobile-friendly interface
- **Thymeleaf Templates**: Server-side rendering with reusable components
- **Section-based Organization**: Hierarchical content structure with main sections and subsections
- **Russian Language Support**: Full UTF-8 support for Cyrillic text

## Technology Stack

- **Backend**: Spring Boot 3.2.1
- **Template Engine**: Thymeleaf
- **Frontend**: HTML5, CSS3, JavaScript (ES6)
- **Build Tool**: Maven
- **Java Version**: 17+

## Project Structure

```
veter96-webapp/
├── src/
│   └── main/
│       ├── java/com/veter96/webapp/
│       │   ├── Veter96WebappApplication.java    # Main Spring Boot application
│       │   └── controller/
│       │       └── MainController.java          # Main controller handling all routes
│       └── resources/
│           ├── templates/
│           │   ├── fragments/
│           │   │   └── navigation.html          # Reusable navigation component
│           │   ├── index.html                   # Homepage template
│           │   ├── layout.html                  # Base layout template
│           │   ├── section.html                 # Section page template
│           │   └── subsection.html              # Subsection page template
│           ├── static/
│           │   ├── css/
│           │   │   └── style.css                # Main stylesheet
│           │   └── js/
│           │       └── script.js                # JavaScript functionality
│           └── application.properties           # Spring Boot configuration
└── pom.xml                                      # Maven configuration
```

## Navigation Structure

The application implements the following navigation structure from the original site:

1. **Органы управления и приборы контроля** (Controls and Instruments)
2. **Техническое обслуживание** (Maintenance)
3. **Двигатели** (Engines)
4. **Отопление, вентиляция** (Heating, Ventilation)
5. **Топливная система** (Fuel System)
6. **Системы пуска, зажигания** (Starting, Ignition Systems)
7. **Трансмиссия** (Transmission)
8. **Тормозная система** (Brake System)
9. **Ходовая часть** (Running Gear)
10. **Кузов** (Body)
11. **Электрооборудование** (Electrical Equipment)
12. **Основные неисправности** (Main Malfunctions)

## Route Structure

- `/` - Homepage with welcome information
- `/section/{sectionId}` - Main section pages (e.g., `/section/1`)
- `/section/{sectionId}/{subId}` - Subsection pages (e.g., `/section/1/1`)

## Key Features

### Navigation Component
- Persistent sidebar navigation on all pages
- Active page highlighting
- Responsive design for mobile devices
- Original site structure preservation

### Content Organization
- Hierarchical breadcrumb navigation
- Section-specific content
- Technical information boxes
- Cross-references between sections

### User Experience
- Smooth scrolling navigation
- Print-friendly styles
- Back-to-top functionality
- Mobile-responsive design
- Keyboard shortcuts (Ctrl+P for printing)

## Running the Application

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Installation and Startup

1. **Clone or extract the project**
   ```bash
   cd veter96-webapp
   ```

2. **Run with Maven**
   ```bash
   mvn spring-boot:run
   ```

3. **Access the application**
   - Open your browser and navigate to `http://localhost:8080`
   - The application will be running on port 8080

### Alternative Running Method
If Maven wrapper is not available, you can also run:
```bash
./mvnw spring-boot:run
```

## Configuration

The application can be configured through `application.properties`:

- **Server Port**: `server.port=8080`
- **Thymeleaf Caching**: Disabled for development
- **Static Resources**: Served from `/static/`
- **Character Encoding**: UTF-8 for Russian language support

## Development

### Hot Reload
The application includes Spring Boot DevTools for automatic restart during development:
- Template changes are reflected immediately
- Static resource changes don't require restart
- Java code changes trigger automatic application restart

### Adding New Content
1. **New Sections**: Add cases to the switch statements in `MainController.java`
2. **New Templates**: Create new Thymeleaf templates in `/templates/`
3. **New Styles**: Add CSS rules to `/static/css/style.css`
4. **New JavaScript**: Add functionality to `/static/js/script.js`

## Browser Support

- **Modern Browsers**: Chrome 70+, Firefox 65+, Safari 12+, Edge 79+
- **Mobile**: iOS Safari, Chrome Mobile, Samsung Internet
- **Features**: CSS Grid, Flexbox, ES6 JavaScript

## Accessibility

- Semantic HTML structure
- Keyboard navigation support
- Print stylesheet included
- Russian language declarations
- Screen reader friendly markup

## Original Site Reference

This application recreates the structure of https://veter96.ru/, which was an automotive technical manual website for Opel Vectra B vehicles. The navigation structure and content organization have been preserved while modernizing the user interface and adding responsive design capabilities.

## License

© 2024 Veter96. All rights reserved.

## Contributing

To contribute to this project:
1. Follow the existing code style
2. Test thoroughly on multiple browsers
3. Ensure Russian text encoding is preserved
4. Maintain the original navigation structure
5. Add appropriate documentation for new features