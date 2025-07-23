# SynchroScore

SynchroScore is a comprehensive scoring and meet management application for synchronized swimming competitions. It provides tools for organizing meets, managing participants, scoring routines and figures, and generating detailed reports, all through an intuitive graphical user interface.

## Project Overview

SynchroScore streamlines the process of running synchronized swimming meets, from setup to scoring and reporting. Designed for clubs and officials, it supports both figures and routine events, automates scoring calculations, and produces printable results and labels.

## Key Features

- **Meet Management:** Create, configure, and manage meets, teams, swimmers, and events.
- **Scoring System:** Enter and calculate scores for figures and routines, with support for multiple levels and scoring rules.
- **Reporting:** Generate detailed reports, score sheets, labels, and results using integrated JasperReports templates.
- **Graphical User Interface:** User-friendly GUI for all operations, including meet setup, scoring entry, and report generation.
- **Data Import/Export:** Supports CSV data for score sheets and test data.
- **Robust Architecture:** Modular design with managers, controllers, and domain models for maintainability and extensibility.

## Installation

### Prerequisites

- **Java Development Kit (JDK) 8 or higher**
- **Apache Maven**

### Steps

1. Clone or download the repository.
2. Ensure Java and Maven are installed on your system.
3. Build the project using Maven:

   ```bash
   mvn clean package
   ```

   See [`pom.xml`](SynchroScore/pom.xml) for dependencies and build configuration.

4. Optionally, use [`launch4j.xml`](SynchroScore/launch4j.xml) to package the application as a Windows executable.

### Building a Windows Executable (EXE) with Launch4j

SynchroScore uses the [launch4j-maven-plugin](https://github.com/lukaszlenart/launch4j-maven-plugin) to package the application as a native Windows executable. This wraps the Maven-built jar-with-dependencies into a `.exe` file for easier distribution and launching on Windows.

**Requirements:**
- Windows operating system (for running the generated `.exe`)
- Icon file (`synchro.ico`) and splash image (`splash.bmp`) must exist in the project root

**Steps:**
1. Ensure all prerequisites above are met.
2. Run Maven to build the executable:
   ```bash
   mvn clean package
   ```
3. After the build completes, the Windows executable will be located at:
   ```
   target/SynchroScore-<version>.exe
   ```
   (Replace `<version>` with the current version, e.g., `1.32.2-SNAPSHOT`.)

**Configuration Reference:**
The launch4j integration is defined in [`pom.xml`](SynchroScore/pom.xml:102-147) under the `<plugin>` section for `com.akathist.maven.plugins.launch4j`. It wraps the jar-with-dependencies and sets the icon and splash files.

For further customization, see the plugin configuration in [`pom.xml`](SynchroScore/pom.xml:102-147).

## Usage

1. After building, run the application:

   ```bash
   java -jar target/SynchroScore-<version>.jar
   ```

2. For detailed usage instructions, refer to [`doc/Instructions.pdf`](SynchroScore/doc/Instructions.pdf).

## Directory Structure

- `src/org/aquastarz/score/manager/` — Meet, routine, swimmer, and team management
- `src/org/aquastarz/score/controller/` — Application controllers
- `src/org/aquastarz/score/gui/` — GUI components
- `src/org/aquastarz/score/report/` — Reporting and JasperReports templates
- `doc/` — User and testing documentation

## Notes for Users and Contributors

- All scoring logic and meet management are handled within the application; no external database setup is required.
- Contributions are welcome. Please ensure code changes are tested and documented.
- For troubleshooting or advanced configuration, consult the documentation in the `doc/` directory.

## License

See [`LICENSE.txt`](SynchroScore/LICENSE.txt) for licensing information.