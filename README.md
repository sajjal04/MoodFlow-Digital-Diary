MoodFlow — Digital Diary & Goal Tracker

A Java Swing desktop application for journaling daily thoughts, tracking personal goals, and visualizing productivity — built as a semester project demonstrating Event Handling, Exception Handling, Code Refactoring, Unit Testing, and Git & GitHub.



📋 Project Overview
MoodFlow is a personal productivity tool that lets users:

Write and manage journal entries with mood tracking
Set, track, and complete personal goals with progress bars
View statistics including mood breakdowns and goal completion rates
Search and filter entries and goals
Edit or delete any entry or goal inline
Persist all data to local files — everything survives a restart


✅ SCD Concepts Implemented
1. Event Handling
Every user interaction triggers a registered event listener:

Sidebar navigation buttons switch views and highlight the active menu item
"Save", "Update", and "Delete" buttons submit forms and mutate state
The search field in View Entries filters cards live on button click
The filter dropdown in View Goals re-renders the list immediately
Progress slider in Add Goal updates the percentage label in real time
Mood dropdown in Add Entry updates the mood preview badge instantly

2. Exception Handling
All user input passes through dedicated validation classes before being accepted:

EmptyFieldException — thrown when required fields (date, title, entry, goal name) are left blank
InvalidDateException — thrown when the date format is wrong or the values are out of range (day 1–31, month 1–12, year 2000–2100)
File I/O is wrapped in try-catch blocks throughout FileHandler and GoalFileHandler; errors are reported to the console without crashing the app
All JOptionPane error dialogs display the exact exception message to the user

3. Code Refactoring

Singleton pattern — JournalManager and GoalManager are singletons; all windows share the same in-memory state
Single Responsibility — storage (FileHandler, GoalFileHandler), business logic (*Manager), validation (Validation, GoalValidation), models (JournalEntry, Goal), and GUI are all in separate packages
Theme class — all colours, fonts, mood emojis, and mood colours live in one place so the visual design is changed in a single file
IconFactory — all vector icons are generated at runtime with Java2D; no external image files required
RoundedPanel — reusable custom component used for every card and panel in the app
Edit mode reuses the same AddEntryGUI and GoalGUI forms instead of duplicating code; a constructor overload pre-fills the form and calls updateEntry/updateGoal instead of addEntry/addGoal
Pipe-delimited serialization (toFileLine / fromFileLine) keeps persistence logic inside the model, not scattered across the storage layer

4. Unit Testing (JUnit 5)
49 automated test cases across 7 test classes:
Test ClassWhat it coversJournalManagerTestAdd, delete, update, get, mood counting, streak, singletonGoalManagerTestAdd, delete, update, complete, progress, average, singletonModelTestJournalEntry & Goal serialization round-trips, escaping, edge casesValidationTestAll happy-path and failure branches of journal entry validationGoalValidationTestHappy-path and failure branches of goal validationExceptionTestCustom exception construction, message preservation, checked typeFileOperationsTestSave/load round-trips for both files, empty file, missing file
5. Git & GitHub

Public repository with meaningful commit messages
Consistent development history with logical, incremental commits
This README with setup instructions
All source code, tests, and libraries committed


🗂️ Project Structure
DigitalDiaryProject/
├── src/
│   ├── Main.java                        # Entry point — loads data, launches GUI
│   ├── exceptions/
│   │   ├── EmptyFieldException.java     # Thrown for blank required fields
│   │   └── InvalidDateException.java    # Thrown for bad date format/values
│   ├── gui/
│   │   ├── DashboardGUI.java            # Main window with sidebar navigation
│   │   ├── AddEntryGUI.java             # Add/edit journal entry form
│   │   ├── ViewEntriesGUI.java          # Scrollable card list with search
│   │   ├── GoalGUI.java                 # Add/edit goal form with slider
│   │   ├── ViewGoalsGUI.java            # Goal cards with filter + actions
│   │   ├── StatisticsGUI.java           # Mood breakdown + goal category charts
│   │   ├── Theme.java                   # All colours, fonts, mood helpers
│   │   ├── RoundedPanel.java            # Reusable rounded card component
│   │   └── IconFactory.java             # Runtime vector icons via Java2D
│   ├── manager/
│   │   ├── JournalManager.java          # Singleton — manages journal entries
│   │   └── GoalManager.java             # Singleton — manages goals
│   ├── model/
│   │   ├── JournalEntry.java            # Entry data + pipe serialization
│   │   └── Goal.java                    # Goal data + pipe serialization
│   ├── storage/
│   │   ├── FileHandler.java             # Read/write data/journal.txt
│   │   └── GoalFileHandler.java         # Read/write data/goals.txt
│   ├── validation/
│   │   ├── Validation.java              # Validates journal entry fields
│   │   └── GoalValidation.java          # Validates goal fields
│   └── test/
│       ├── JournalManagerTest.java
│       ├── GoalManagerTest.java
│       ├── ModelTest.java
│       ├── ValidationTest.java
│       ├── GoalValidationTest.java
│       ├── ExceptionTest.java
│       └── FileOperationsTest.java
├── lib/                                 # JUnit 5 JARs (included)
├── data/                                # Auto-created at runtime
│   ├── journal.txt                      # Persisted journal entries
│   └── goals.txt                        # Persisted goals
├── run_tests.sh                         # Run all tests (macOS/Linux)
├── run_tests.bat                        # Run all tests (Windows)
└── README_SETUP.md                      # Detailed IDE setup instructions

🚀 Setup & Running
Prerequisites

Java 17 or newer (JDK 21/25 is fine)
IntelliJ IDEA (recommended) or any Java IDE

Run the Application
From IntelliJ:

Open the project folder in IntelliJ (File → Open)
Trust the project when prompted
Wait for indexing to finish
Open src/Main.java and click the green ▶ Run button

From the command line:
bash# Compile
javac -d build $(find src -name '*.java' -not -path 'src/test/*')

# Run
java -cp build Main
Run the Tests
From IntelliJ:

Right-click the src/test folder → Run 'Tests in test'

From the command line:
macOS / Linux:
bash./run_tests.sh
Windows:
batrun_tests.bat
Expected output:
[ 49 tests found      ]
[ 49 tests successful ]
[  0 tests failed     ]

See README_SETUP.md for detailed IDE library setup if JUnit errors appear.


🧰 Technologies Used
TechnologyPurposeJava 17+Core languageJava SwingGUI frameworkJava2D (AWT Graphics2D)Runtime vector icon generationJUnit 5.10Automated unit testingFile I/O (BufferedReader/Writer)Data persistenceGit & GitHubVersion control

👤 Author
Sajjal Naeem
Semester Project — Software Construction & Development (SCD) Lab
University of Central Punjab, lahore
L1F23BSSE0399
