package com.example.doctorapp.desktop;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import com.example.doctorapp.dto.AppointmentDto;
import com.example.doctorapp.dto.PatientDto;
import com.example.doctorapp.dto.ReportDto;
import com.example.doctorapp.desktop.service.ApiService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ReceptionistApp extends Application {
    
    private ApiService apiService;
    private TableView<AppointmentDto> appointmentTable;
    private ObservableList<AppointmentDto> appointmentData;
    private TableView<PatientDto> patientTable;
    private ObservableList<PatientDto> patientData;
    private Label statusLabel;
    private TabPane mainTabPane;
    
    // Professional medical color scheme
    private static final String PRIMARY_COLOR = "#2c5aa0";      // Professional blue
    private static final String SECONDARY_COLOR = "#1e3a8a";    // Darker blue
    private static final String SUCCESS_COLOR = "#059669";      // Medical green
    private static final String WARNING_COLOR = "#d97706";      // Orange
    private static final String DANGER_COLOR = "#dc2626";       // Red
    private static final String INFO_COLOR = "#0891b2";         // Cyan
    private static final String LIGHT_BG = "#f8fafc";          // Very light gray
    private static final String DARK_BG = "#1e293b";           // Dark slate
    private static final String CARD_BG = "#ffffff";           // Pure white
    private static final String BORDER_COLOR = "#e2e8f0";      // Light border
    
    @Override
    public void start(Stage primaryStage) {
        // Initialize API service directly
        apiService = new ApiService();
        
        // Get screen dimensions
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        
        // Calculate window size (80% of screen size, but not smaller than minimum)
        double windowWidth = Math.max(1200, bounds.getWidth() * 0.8);
        double windowHeight = Math.max(800, bounds.getHeight() * 0.8);
        
        // Center the window on screen
        primaryStage.setX(bounds.getMinX() + (bounds.getWidth() - windowWidth) / 2);
        primaryStage.setY(bounds.getMinY() + (bounds.getHeight() - windowHeight) / 2);
        
        primaryStage.setTitle("Medical Receptionist Dashboard");
        primaryStage.setWidth(windowWidth);
        primaryStage.setHeight(windowHeight);
        primaryStage.setMinWidth(1000);
        primaryStage.setMinHeight(700);
        
        // Enable window decorations for proper window management
        primaryStage.initStyle(StageStyle.DECORATED);
        
        // Handle application close event
        primaryStage.setOnCloseRequest(e -> {
            System.exit(0);
        });
        
        // Create the main layout with gradient background
        BorderPane root = createMainLayout();
        
        Scene scene = new Scene(root);
        try {
            scene.getStylesheets().add(getClass().getResource("/css/modern-styles.css").toExternalForm());
        } catch (Exception e) {
            // CSS file not found, continue without it
            System.out.println("CSS file not found, using default styling");
        }
        
        // Make the layout responsive to window resizing
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            // Handle width changes if needed
        });
        
        scene.heightProperty().addListener((obs, oldVal, newVal) -> {
            // Handle height changes if needed
        });
        
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Add entrance animation
        addEntranceAnimation(mainTabPane);
        
        // Load initial data
        refreshAppointments();
        refreshPatients();
    }
    
    private BorderPane createMainLayout() {
        BorderPane root = new BorderPane();
        
        // Create subtle gradient background
        LinearGradient gradient = new LinearGradient(0, 0, 0, 1, true, null,
            new Stop(0, Color.web("#f1f5f9")),
            new Stop(1, Color.web("#e2e8f0"))
        );
        root.setBackground(new Background(new BackgroundFill(gradient, null, null)));
        
        // Create main content area
        HBox mainContent = createMainContentArea();
        root.setCenter(mainContent);
        
        // Bottom section with status
        HBox bottomSection = createProfessionalBottomSection();
        root.setBottom(bottomSection);
        
        return root;
    }
    
    private HBox createProfessionalTitleBar() {
        HBox titleBar = new HBox();
        titleBar.setPadding(new Insets(12, 20, 12, 20));
        titleBar.setAlignment(Pos.CENTER_LEFT);
        titleBar.setSpacing(15);
        titleBar.setPrefHeight(60);
        
        // Create professional gradient background
        LinearGradient titleGradient = new LinearGradient(0, 0, 1, 0, true, null,
            new Stop(0, Color.web(PRIMARY_COLOR)),
            new Stop(1, Color.web(SECONDARY_COLOR))
        );
        titleBar.setBackground(new Background(new BackgroundFill(titleGradient, null, null)));
        
        // Add subtle shadow effect
        DropShadow shadow = new DropShadow(8, 0, 4, Color.rgb(0, 0, 0, 0.2));
        titleBar.setEffect(shadow);
        
        // Medical cross icon and title
        Label appIcon = new Label("🏥");
        appIcon.setFont(Font.font("System", FontWeight.BOLD, 20));
        appIcon.setTextFill(Color.WHITE);
        
        VBox titleBox = new VBox(2);
        Label title = new Label("Medical Receptionist Dashboard");
        title.setFont(Font.font("System", FontWeight.BOLD, 16));
        title.setTextFill(Color.WHITE);
        
        Label subtitle = new Label("Patient Management System");
        subtitle.setFont(Font.font("System", 12));
        subtitle.setTextFill(Color.rgb(255, 255, 255, 0.8));
        
        titleBox.getChildren().addAll(title, subtitle);
        
        // Spacer to push close button to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Professional close button
        Button closeBtn = createProfessionalButton("✕", DANGER_COLOR);
        closeBtn.setOnAction(e -> System.exit(0));
        closeBtn.setPrefSize(32, 32);
        closeBtn.setStyle("-fx-background-radius: 16; -fx-border-radius: 16;");
        
        titleBar.getChildren().addAll(appIcon, titleBox, spacer, closeBtn);
        
        return titleBar;
    }
    
    private HBox createMainContentArea() {
        HBox mainContent = new HBox(0);
        mainContent.setPadding(new Insets(20));
        
        // Left sidebar for navigation
        VBox sidebar = createSidebar();
        sidebar.setPrefWidth(250);
        sidebar.setMinWidth(200);
        sidebar.setMaxWidth(300);
        
        // Main content area
        VBox contentArea = createContentArea();
        HBox.setHgrow(contentArea, Priority.ALWAYS);
        
        mainContent.getChildren().addAll(sidebar, contentArea);
        
        // Make the layout responsive
        mainContent.widthProperty().addListener((obs, oldVal, newVal) -> {
            // Adjust sidebar width based on window size
            double windowWidth = newVal.doubleValue();
            if (windowWidth < 1200) {
                sidebar.setPrefWidth(200);
            } else {
                sidebar.setPrefWidth(250);
            }
        });
        
        return mainContent;
    }
    
    private VBox createSidebar() {
        VBox sidebar = new VBox(0);
        sidebar.setBackground(new Background(new BackgroundFill(
            Color.web(CARD_BG), 
            new CornerRadii(12, 0, 0, 12, false), 
            Insets.EMPTY
        )));
        
        // Add shadow effect
        DropShadow sidebarShadow = new DropShadow(10, 0, 5, Color.rgb(0, 0, 0, 0.1));
        sidebar.setEffect(sidebarShadow);
        
        // Sidebar header
        VBox sidebarHeader = new VBox(8);
        sidebarHeader.setPadding(new Insets(20, 15, 20, 15));
        sidebarHeader.setBackground(new Background(new BackgroundFill(
            Color.web(PRIMARY_COLOR), 
            new CornerRadii(12, 0, 0, 0, false), 
            Insets.EMPTY
        )));
        
        Label sidebarTitle = new Label("Navigation");
        sidebarTitle.setFont(Font.font("System", FontWeight.BOLD, 14));
        sidebarTitle.setTextFill(Color.WHITE);
        
        Label sidebarSubtitle = new Label("Quick Access");
        sidebarSubtitle.setFont(Font.font("System", 11));
        sidebarSubtitle.setTextFill(Color.rgb(255, 255, 255, 0.8));
        
        sidebarHeader.getChildren().addAll(sidebarTitle, sidebarSubtitle);
        
        // Navigation buttons
        VBox navButtons = new VBox(8);
        navButtons.setPadding(new Insets(20, 15, 20, 15));
        
        Button appointmentsBtn = createNavButton("📅 Appointments", true);
        appointmentsBtn.setOnAction(e -> showAppointmentsTab());
        
        Button patientsBtn = createNavButton("👥 Patients", false);
        patientsBtn.setOnAction(e -> showPatientsTab());
        
        Button billingBtn = createNavButton("💰 Billing", false);
        billingBtn.setOnAction(e -> showBillingTab());
        
        Button reportsBtn = createNavButton("📊 Reports", false);
        reportsBtn.setOnAction(e -> showReportsTab());
        
        navButtons.getChildren().addAll(appointmentsBtn, patientsBtn, billingBtn, reportsBtn);
        
        sidebar.getChildren().addAll(sidebarHeader, navButtons);
        return sidebar;
    }
    
    private Button createNavButton(String text, boolean isSelected) {
        Button button = new Button(text);
        button.setFont(Font.font("System", FontWeight.MEDIUM, 13));
        button.setPadding(new Insets(12, 15, 12, 15));
        button.setPrefWidth(220);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setCursor(javafx.scene.Cursor.HAND);
        
        if (isSelected) {
            button.setBackground(new Background(new BackgroundFill(
                Color.web(PRIMARY_COLOR), 
                new CornerRadii(8), 
                Insets.EMPTY
            )));
            button.setTextFill(Color.WHITE);
            button.setEffect(new DropShadow(4, 0, 2, Color.rgb(0, 0, 0, 0.2)));
        } else {
            button.setBackground(new Background(new BackgroundFill(
                Color.TRANSPARENT, 
                new CornerRadii(8), 
                Insets.EMPTY
            )));
            button.setTextFill(Color.web("#475569"));
            button.setBorder(new Border(new BorderStroke(
                Color.TRANSPARENT, 
                BorderStrokeStyle.SOLID, 
                new CornerRadii(8), 
                new BorderWidths(1)
            )));
        }
        
        // Hover effects
        button.setOnMouseEntered(e -> {
            if (!isSelected) {
                button.setBackground(new Background(new BackgroundFill(
                    Color.web("#f1f5f9"), 
                    new CornerRadii(8), 
                    Insets.EMPTY
                )));
                button.setTextFill(Color.web(PRIMARY_COLOR));
            }
        });
        
        button.setOnMouseExited(e -> {
            if (!isSelected) {
                button.setBackground(new Background(new BackgroundFill(
                    Color.TRANSPARENT, 
                    new CornerRadii(8), 
                    Insets.EMPTY
                )));
                button.setTextFill(Color.web("#475569"));
            }
        });
        
        return button;
    }
    
    private VBox createContentArea() {
        VBox contentArea = new VBox(20);
        contentArea.setPadding(new Insets(0, 0, 0, 20));
        
        // Create main tab pane for different sections
        mainTabPane = new TabPane();
        mainTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        mainTabPane.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        
        // Appointments tab
        Tab appointmentsTab = new Tab("Appointments");
        appointmentsTab.setContent(createAppointmentsContent());
        appointmentsTab.setClosable(false);
        
        // Patients tab
        Tab patientsTab = new Tab("Patients");
        patientsTab.setContent(createPatientsContent());
        patientsTab.setClosable(false);
        
        // Reports tab
        Tab reportsTab = new Tab("Reports");
        reportsTab.setContent(createReportsContent());
        reportsTab.setClosable(false);
        
        mainTabPane.getTabs().addAll(appointmentsTab, patientsTab, reportsTab);
        
        contentArea.getChildren().add(mainTabPane);
        return contentArea;
    }
    
    private VBox createHeaderSection() {
        VBox headerSection = new VBox(10);
        headerSection.setPadding(new Insets(20, 25, 20, 25));
        
        // Create professional card background
        headerSection.setBackground(new Background(new BackgroundFill(
            Color.web(CARD_BG), 
            new CornerRadii(12), 
            Insets.EMPTY
        )));
        
        // Add shadow effect
        DropShadow cardShadow = new DropShadow(8, 0, 4, Color.rgb(0, 0, 0, 0.08));
        headerSection.setEffect(cardShadow);
        
        // Header content with title and search
        VBox headerContent = new VBox(15);
        
        // Title section
        VBox titleBox = new VBox(4);
        Label titleLabel = new Label("Today's Appointments");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        titleLabel.setTextFill(Color.web(DARK_BG));
        
        Label dateLabel = new Label(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy")));
        dateLabel.setFont(Font.font("System", 13));
        dateLabel.setTextFill(Color.web("#64748b"));
        
        titleBox.getChildren().addAll(titleLabel, dateLabel);
        
        // Search and action buttons row
        HBox searchAndActionsRow = new HBox(20);
        searchAndActionsRow.setAlignment(Pos.CENTER_LEFT);
        
        // Search section
        HBox searchSection = new HBox(10);
        searchSection.setAlignment(Pos.CENTER_LEFT);
        
        Label searchLabel = new Label("🔍 Search:");
        searchLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        searchLabel.setTextFill(Color.web(DARK_BG));
        
        TextField searchField = new TextField();
        searchField.setPromptText("Search by patient name, doctor, date, or status...");
        searchField.setPrefWidth(300);
        searchField.setStyle("-fx-background-color: white; -fx-border-color: #d1d5db; -fx-border-radius: 6; -fx-padding: 8 12;");
        
        Button searchButton = createActionButton("Search", INFO_COLOR);
        searchButton.setOnAction(e -> searchAppointments(searchField.getText()));
        
        Button clearSearchButton = createActionButton("Clear", WARNING_COLOR);
        clearSearchButton.setOnAction(e -> {
            searchField.clear();
            refreshAppointments();
        });
        
        searchSection.getChildren().addAll(searchLabel, searchField, searchButton, clearSearchButton);
        
        // Action buttons
        HBox actionButtons = new HBox(12);
        actionButtons.setAlignment(Pos.CENTER_RIGHT);
        
        Button refreshButton = createActionButton("🔄 Refresh", INFO_COLOR);
        refreshButton.setOnAction(e -> refreshAppointments());
        
        Button checkInButton = createActionButton("✅ Check In", SUCCESS_COLOR);
        checkInButton.setOnAction(e -> checkInSelectedAppointment());
        
        Button billingButton = createActionButton("💰 View Billing", WARNING_COLOR);
        billingButton.setOnAction(e -> viewSelectedAppointmentBilling());
        
        actionButtons.getChildren().addAll(refreshButton, checkInButton, billingButton);
        
        // Spacer to push buttons to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Add search section and action buttons to the row
        searchAndActionsRow.getChildren().addAll(searchSection, spacer, actionButtons);
        
        // Add title and search/actions row to header content
        headerContent.getChildren().addAll(titleBox, searchAndActionsRow);
        headerSection.getChildren().add(headerContent);
        
        return headerSection;
    }
    
    private VBox createAppointmentsContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        
        // Header section
        VBox headerSection = createHeaderSection();
        
        // Main content cards
        VBox appointmentsCard = createAppointmentsCard();
        
        content.getChildren().addAll(headerSection, appointmentsCard);
        return content;
    }
    
    private VBox createAppointmentsCard() {
        VBox appointmentsCard = new VBox(15);
        appointmentsCard.setPadding(new Insets(25));
        
        // Create professional card background
        appointmentsCard.setBackground(new Background(new BackgroundFill(
            Color.web(CARD_BG), 
            new CornerRadii(12), 
            Insets.EMPTY
        )));
        
        // Add shadow effect
        DropShadow cardShadow = new DropShadow(8, 0, 4, Color.rgb(0, 0, 0, 0.08));
        appointmentsCard.setEffect(cardShadow);
        
        // Section header
        HBox sectionHeader = new HBox(10);
        sectionHeader.setAlignment(Pos.CENTER_LEFT);
        
        Label sectionIcon = new Label("📋");
        sectionIcon.setFont(Font.font("System", FontWeight.BOLD, 18));
        
        Label sectionTitle = new Label("Appointment List");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        sectionTitle.setTextFill(Color.web(DARK_BG));
        
        sectionHeader.getChildren().addAll(sectionIcon, sectionTitle);
        
        // Create professional table
        appointmentTable = createProfessionalTableView();
        
        appointmentsCard.getChildren().addAll(sectionHeader, appointmentTable);
        return appointmentsCard;
    }
    
    private VBox createPatientsContent() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(20));
        
        // Header section
        VBox headerSection = createPatientsHeaderSection();
        
        // Main content cards
        VBox patientsCard = createPatientsCard();
        
        content.getChildren().addAll(headerSection, patientsCard);
        return content;
    }
    
    private VBox createPatientsHeaderSection() {
        VBox headerSection = new VBox(10);
        headerSection.setPadding(new Insets(20, 25, 20, 25));
        
        // Create professional card background
        headerSection.setBackground(new Background(new BackgroundFill(
            Color.web(CARD_BG), 
            new CornerRadii(12), 
            Insets.EMPTY
        )));
        
        // Add shadow effect
        DropShadow cardShadow = new DropShadow(8, 0, 4, Color.rgb(0, 0, 0, 0.08));
        headerSection.setEffect(cardShadow);
        
        // Header content with title and search
        VBox headerContent = new VBox(15);
        
        // Title section
        VBox titleBox = new VBox(4);
        Label titleLabel = new Label("Patient Management");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        titleLabel.setTextFill(Color.web(DARK_BG));
        
        Label subtitleLabel = new Label("Manage patient records and information");
        subtitleLabel.setFont(Font.font("System", 13));
        subtitleLabel.setTextFill(Color.web("#64748b"));
        
        titleBox.getChildren().addAll(titleLabel, subtitleLabel);
        
        // Search and action buttons row
        HBox searchAndActionsRow = new HBox(20);
        searchAndActionsRow.setAlignment(Pos.CENTER_LEFT);
        
        // Search section
        HBox searchSection = new HBox(10);
        searchSection.setAlignment(Pos.CENTER_LEFT);
        
        Label searchLabel = new Label("🔍 Search:");
        searchLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        searchLabel.setTextFill(Color.web(DARK_BG));
        
        TextField searchField = new TextField();
        searchField.setPromptText("Search by name, email, or phone...");
        searchField.setPrefWidth(300);
        searchField.setStyle("-fx-background-color: white; -fx-border-color: #d1d5db; -fx-border-radius: 6; -fx-padding: 8 12;");
        
        Button searchButton = createActionButton("Search", INFO_COLOR);
        searchButton.setOnAction(e -> searchPatients(searchField.getText()));
        
        Button clearSearchButton = createActionButton("Clear", WARNING_COLOR);
        clearSearchButton.setOnAction(e -> {
            searchField.clear();
            refreshPatients();
        });
        
        searchSection.getChildren().addAll(searchLabel, searchField, searchButton, clearSearchButton);
        
        // Action buttons
        HBox actionButtons = new HBox(12);
        actionButtons.setAlignment(Pos.CENTER_RIGHT);
        
        Button refreshButton = createActionButton("🔄 Refresh", INFO_COLOR);
        refreshButton.setOnAction(e -> refreshPatients());
        
        Button addPatientButton = createActionButton("➕ Add Patient", SUCCESS_COLOR);
        addPatientButton.setOnAction(e -> showAddPatientDialog());
        
        Button editPatientButton = createActionButton("✏️ Edit Patient", WARNING_COLOR);
        editPatientButton.setOnAction(e -> editSelectedPatient());
        
        Button exportButton = createActionButton("📊 Export to Excel", DANGER_COLOR);
        exportButton.setOnAction(e -> exportPatientsToExcel());
        
        actionButtons.getChildren().addAll(refreshButton, addPatientButton, editPatientButton, exportButton);
        
        // Spacer to push buttons to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // Add search section and action buttons to the row
        searchAndActionsRow.getChildren().addAll(searchSection, spacer, actionButtons);
        
        // Add title and search/actions row to header content
        headerContent.getChildren().addAll(titleBox, searchAndActionsRow);
        headerSection.getChildren().add(headerContent);
        
        return headerSection;
    }
    
    private VBox createPatientsCard() {
        VBox patientsCard = new VBox(15);
        patientsCard.setPadding(new Insets(25));
        
        // Create professional card background
        patientsCard.setBackground(new Background(new BackgroundFill(
            Color.web(CARD_BG), 
            new CornerRadii(12), 
            Insets.EMPTY
        )));
        
        // Add shadow effect
        DropShadow cardShadow = new DropShadow(8, 0, 4, Color.rgb(0, 0, 0, 0.08));
        patientsCard.setEffect(cardShadow);
        
        // Section header
        HBox sectionHeader = new HBox(10);
        sectionHeader.setAlignment(Pos.CENTER_LEFT);
        
        Label sectionIcon = new Label("👥");
        sectionIcon.setFont(Font.font("System", FontWeight.BOLD, 18));
        
        Label sectionTitle = new Label("Patient List");
        sectionTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        sectionTitle.setTextFill(Color.web(DARK_BG));
        
        sectionHeader.getChildren().addAll(sectionIcon, sectionTitle);
        
        // Create professional table
        patientTable = createProfessionalPatientTableView();
        
        patientsCard.getChildren().addAll(sectionHeader, patientTable);
        return patientsCard;
    }
    
    private TableView<AppointmentDto> createProfessionalTableView() {
        TableView<AppointmentDto> table = new TableView<>();
        appointmentData = FXCollections.observableArrayList();
        table.setItems(appointmentData);
        
        // Style the table
        table.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        table.setFixedCellSize(55);
        table.setPlaceholder(new Label("No appointments found for today"));
        
        // Patient Name column
        TableColumn<AppointmentDto, String> nameColumn = new TableColumn<>("Patient Name");
        nameColumn.setCellValueFactory(cellData -> {
            AppointmentDto appointment = cellData.getValue();
            String fullName = appointment.getpatientFirstName() + " " + appointment.getpatientLastName();
            return new SimpleStringProperty(fullName);
        });
        nameColumn.setPrefWidth(200);
        styleTableColumn(nameColumn);
        
        // Doctor Name column
        TableColumn<AppointmentDto, String> doctorColumn = new TableColumn<>("Doctor");
        doctorColumn.setCellValueFactory(cellData -> {
            AppointmentDto appointment = cellData.getValue();
            String firstName = appointment.getdoctorFirstName();
            String lastName = appointment.getdoctorLastName();
            
            StringBuilder doctorName = new StringBuilder("Dr. ");
            
            if (firstName != null && !firstName.trim().isEmpty()) {
                doctorName.append(firstName.trim());
            }
            
            if (lastName != null && !lastName.trim().isEmpty()) {
                if (firstName != null && !firstName.trim().isEmpty()) {
                    doctorName.append(" ");
                }
                doctorName.append(lastName.trim());
            }
            
            if ((firstName == null || firstName.trim().isEmpty()) && 
                (lastName == null || lastName.trim().isEmpty())) {
                doctorName.append("[Unknown]");
            }
            
            return new SimpleStringProperty(doctorName.toString());
        });
        doctorColumn.setPrefWidth(180);
        styleTableColumn(doctorColumn);
        
        // Time column
        TableColumn<AppointmentDto, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(cellData -> {
            AppointmentDto appointment = cellData.getValue();
            String timeStr = appointment.getTime() != null ? 
                appointment.getTime().format(DateTimeFormatter.ofPattern("HH:mm")) : "N/A";
            return new SimpleStringProperty(timeStr);
        });
        timeColumn.setPrefWidth(100);
        styleTableColumn(timeColumn);
        
        // Status column
        TableColumn<AppointmentDto, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setPrefWidth(120);
        styleTableColumn(statusColumn);
        
        // Status column with professional colored cells
        statusColumn.setCellFactory(column -> new TableCell<AppointmentDto, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    setAlignment(Pos.CENTER);
                    
                    // Professional status styling
                    switch (status.toUpperCase()) {
                        case "SCHEDULED":
                            setStyle("-fx-background-color: #fef3c7; -fx-text-fill: #92400e; -fx-background-radius: 6; -fx-padding: 4 8; -fx-font-size: 11px; -fx-font-weight: bold;");
                            break;
                        case "CHECKED_IN":
                            setStyle("-fx-background-color: #d1fae5; -fx-text-fill: #065f46; -fx-background-radius: 6; -fx-padding: 4 8; -fx-font-size: 11px; -fx-font-weight: bold;");
                            break;
                        case "COMPLETED":
                            setStyle("-fx-background-color: #dbeafe; -fx-text-fill: #1e40af; -fx-background-radius: 6; -fx-padding: 4 8; -fx-font-size: 11px; -fx-font-weight: bold;");
                            break;
                        case "CANCELLED":
                            setStyle("-fx-background-color: #fee2e2; -fx-text-fill: #991b1b; -fx-background-radius: 6; -fx-padding: 4 8; -fx-font-size: 11px; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("-fx-background-color: #f1f5f9; -fx-text-fill: #475569; -fx-background-radius: 6; -fx-padding: 4 8; -fx-font-size: 11px; -fx-font-weight: bold;");
                    }
                }
            }
        });
        
        // Payment Status column
        TableColumn<AppointmentDto, String> paymentColumn = new TableColumn<>("Payment");
        paymentColumn.setCellValueFactory(cellData -> {
            AppointmentDto appointment = cellData.getValue();
            String paymentStatus = appointment.getPaymentStatus();
            return new SimpleStringProperty(paymentStatus != null ? paymentStatus : "UNPAID");
        });
        paymentColumn.setPrefWidth(100);
        styleTableColumn(paymentColumn);
        
        // Add professional colored cells for payment status
        paymentColumn.setCellFactory(column -> new TableCell<AppointmentDto, String>() {
            @Override
            protected void updateItem(String paymentStatus, boolean empty) {
                super.updateItem(paymentStatus, empty);
                if (empty || paymentStatus == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(paymentStatus);
                    setAlignment(Pos.CENTER);
                    
                    switch (paymentStatus.toUpperCase()) {
                        case "PAID":
                        case "COMPLETED":
                            setStyle("-fx-background-color: #d1fae5; -fx-text-fill: #065f46; -fx-background-radius: 6; -fx-padding: 4 8; -fx-font-size: 11px; -fx-font-weight: bold;");
                            break;
                        case "UNPAID":
                        case "PENDING":
                            setStyle("-fx-background-color: #fef3c7; -fx-text-fill: #92400e; -fx-background-radius: 6; -fx-padding: 4 8; -fx-font-size: 11px; -fx-font-weight: bold;");
                            break;
                        case "FAILED":
                            setStyle("-fx-background-color: #fee2e2; -fx-text-fill: #991b1b; -fx-background-radius: 6; -fx-padding: 4 8; -fx-font-size: 11px; -fx-font-weight: bold;");
                            break;
                        case "REFUNDED":
                            setStyle("-fx-background-color: #dbeafe; -fx-text-fill: #1e40af; -fx-background-radius: 6; -fx-padding: 4 8; -fx-font-size: 11px; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("-fx-background-color: #f1f5f9; -fx-text-fill: #475569; -fx-background-radius: 6; -fx-padding: 4 8; -fx-font-size: 11px; -fx-font-weight: bold;");
                    }
                }
            }
        });
        
        table.getColumns().addAll(nameColumn, doctorColumn, timeColumn, statusColumn, paymentColumn);
        
        // Add professional row styling and double-click functionality
        table.setRowFactory(tv -> {
            TableRow<AppointmentDto> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    AppointmentDto appointment = row.getItem();
                    showProfessionalAppointmentDetails(appointment);
                }
            });
            
            // Add hover effect
            row.setOnMouseEntered(e -> {
                if (!row.isEmpty()) {
                    row.setStyle("-fx-background-color: #f8fafc; -fx-cursor: hand;");
                }
            });
            
            row.setOnMouseExited(e -> {
                if (!row.isEmpty()) {
                    row.setStyle("-fx-background-color: transparent;");
                }
            });
            
            return row;
        });
        
        return table;
    }
    
    private TableView<PatientDto> createProfessionalPatientTableView() {
        TableView<PatientDto> table = new TableView<>();
        patientData = FXCollections.observableArrayList();
        table.setItems(patientData);
        
        // Style the table
        table.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
        table.setFixedCellSize(55);
        table.setPlaceholder(new Label("No patients found"));
        
        // Patient ID column
        TableColumn<PatientDto, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> {
            PatientDto patient = cellData.getValue();
            return new SimpleStringProperty(patient.getId() != null ? patient.getId().toString() : "");
        });
        idColumn.setPrefWidth(60);
        stylePatientTableColumn(idColumn);
        
        // Patient Name column
        TableColumn<PatientDto, String> nameColumn = new TableColumn<>("Full Name");
        nameColumn.setCellValueFactory(cellData -> {
            PatientDto patient = cellData.getValue();
            return new SimpleStringProperty(patient.getFullName());
        });
        nameColumn.setPrefWidth(200);
        stylePatientTableColumn(nameColumn);
        
        // Email column
        TableColumn<PatientDto, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailColumn.setPrefWidth(200);
        stylePatientTableColumn(emailColumn);
        
        // Phone column
        TableColumn<PatientDto, String> phoneColumn = new TableColumn<>("Phone");
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        phoneColumn.setPrefWidth(120);
        stylePatientTableColumn(phoneColumn);
        
        // Age column
        TableColumn<PatientDto, String> ageColumn = new TableColumn<>("Age");
        ageColumn.setCellValueFactory(cellData -> {
            PatientDto patient = cellData.getValue();
            return new SimpleStringProperty(patient.getAge() != null ? patient.getAge().toString() : "");
        });
        ageColumn.setPrefWidth(60);
        stylePatientTableColumn(ageColumn);
        
        // Gender column
        TableColumn<PatientDto, String> genderColumn = new TableColumn<>("Gender");
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        genderColumn.setPrefWidth(80);
        stylePatientTableColumn(genderColumn);
        
        // Address column
        TableColumn<PatientDto, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        addressColumn.setPrefWidth(250);
        stylePatientTableColumn(addressColumn);
        
        table.getColumns().addAll(idColumn, nameColumn, emailColumn, phoneColumn, ageColumn, genderColumn, addressColumn);
        
        // Add professional row styling and double-click functionality
        table.setRowFactory(tv -> {
            TableRow<PatientDto> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    PatientDto patient = row.getItem();
                    showProfessionalPatientDetails(patient);
                }
            });
            
            // Add hover effect
            row.setOnMouseEntered(e -> {
                if (!row.isEmpty()) {
                    row.setStyle("-fx-background-color: #f8fafc; -fx-cursor: hand;");
                }
            });
            
            row.setOnMouseExited(e -> {
                if (!row.isEmpty()) {
                    row.setStyle("-fx-background-color: transparent;");
                }
            });
            
            return row;
        });
        
        return table;
    }
    
    private void styleTableColumn(TableColumn<AppointmentDto, String> column) {
        column.setStyle("-fx-font-weight: bold; -fx-text-fill: #1e293b; -fx-font-size: 12px;");
    }
    
    private void stylePatientTableColumn(TableColumn<PatientDto, String> column) {
        column.setStyle("-fx-font-weight: bold; -fx-text-fill: #1e293b; -fx-font-size: 12px;");
    }
    
    private HBox createProfessionalBottomSection() {
        HBox bottomSection = new HBox();
        bottomSection.setPadding(new Insets(12, 20, 12, 20));
        bottomSection.setAlignment(Pos.CENTER_LEFT);
        bottomSection.setPrefHeight(50);
        
        // Create professional background
        bottomSection.setBackground(new Background(new BackgroundFill(
            Color.web("#f8fafc"), 
            null, 
            null
        )));
        
        // Add subtle border
        bottomSection.setBorder(new Border(new BorderStroke(
            Color.web("#e2e8f0"), 
            BorderStrokeStyle.SOLID, 
            new CornerRadii(0), 
            new BorderWidths(1, 0, 0, 0)
        )));
        
        statusLabel = new Label("Ready");
        statusLabel.setFont(Font.font("System", FontWeight.MEDIUM, 13));
        statusLabel.setTextFill(Color.web("#475569"));
        
        bottomSection.getChildren().add(statusLabel);
        
        return bottomSection;
    }
    
    private Button createProfessionalButton(String text, String color) {
        Button button = new Button(text);
        button.setFont(Font.font("System", FontWeight.BOLD, 13));
        button.setPadding(new Insets(8, 16, 8, 16));
        button.setPrefHeight(36);
        
        // Create professional gradient background
        LinearGradient buttonGradient = new LinearGradient(0, 0, 0, 1, true, null,
            new Stop(0, Color.web(color)),
            new Stop(1, Color.web(color).darker())
        );
        button.setBackground(new Background(new BackgroundFill(buttonGradient, new CornerRadii(6), null)));
        button.setTextFill(Color.WHITE);
        
        // Add subtle shadow effect
        DropShadow buttonShadow = new DropShadow(3, 0, 2, Color.rgb(0, 0, 0, 0.2));
        button.setEffect(buttonShadow);
        
        // Add hover effects
        button.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(150), button);
            scale.setToX(1.02);
            scale.setToY(1.02);
            scale.play();
            
            Glow glow = new Glow(0.2);
            button.setEffect(glow);
        });
        
        button.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(150), button);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
            
            button.setEffect(buttonShadow);
        });
        
        return button;
    }
    
    private Button createActionButton(String text, String color) {
        Button button = new Button(text);
        button.setFont(Font.font("System", FontWeight.MEDIUM, 12));
        button.setPadding(new Insets(8, 14, 8, 14));
        button.setPrefHeight(32);
        
        // Create professional background
        button.setBackground(new Background(new BackgroundFill(
            Color.web(color), 
            new CornerRadii(6), 
            null
        )));
        button.setTextFill(Color.WHITE);
        
        // Add subtle shadow effect
        DropShadow buttonShadow = new DropShadow(2, 0, 2, Color.rgb(0, 0, 0, 0.15));
        button.setEffect(buttonShadow);
        
        // Add hover effects
        button.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(150), button);
            scale.setToX(1.02);
            scale.setToY(1.02);
            scale.play();
            
            Glow glow = new Glow(0.15);
            button.setEffect(glow);
        });
        
        button.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(150), button);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
            
            button.setEffect(buttonShadow);
        });
        
        return button;
    }
    
    private void addEntranceAnimation(TabPane content) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(600), content);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
        
        TranslateTransition slideIn = new TranslateTransition(Duration.millis(600), content);
        slideIn.setFromY(30);
        slideIn.setToY(0);
        slideIn.play();
    }
    
    private void showProfessionalAppointmentDetails(AppointmentDto appointment) {
        Stage detailsStage = new Stage();
        detailsStage.initModality(Modality.APPLICATION_MODAL);
        detailsStage.initStyle(StageStyle.UNDECORATED);
        detailsStage.setTitle("Appointment Details");
        
        VBox content = new VBox(20);
        content.setPadding(new Insets(25));
        content.setAlignment(Pos.CENTER);
        
        // Create professional background
        content.setBackground(new Background(new BackgroundFill(
            Color.web(CARD_BG), 
            new CornerRadii(12), 
            null
        )));
        
        // Add shadow effect
        DropShadow modalShadow = new DropShadow(15, 0, 8, Color.rgb(0, 0, 0, 0.25));
        content.setEffect(modalShadow);
        
        // Header
        HBox headerBox = new HBox(12);
        headerBox.setAlignment(Pos.CENTER);
        
        Label headerIcon = new Label("📋");
        headerIcon.setFont(Font.font("System", FontWeight.BOLD, 24));
        
        Label headerTitle = new Label("Appointment Details");
        headerTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        headerTitle.setTextFill(Color.web(DARK_BG));
        
        headerBox.getChildren().addAll(headerIcon, headerTitle);
        
        // Details grid
        VBox detailsGrid = new VBox(12);
        detailsGrid.setPadding(new Insets(20));
        detailsGrid.setBackground(new Background(new BackgroundFill(
            Color.web("#f8fafc"), 
            new CornerRadii(8), 
            null
        )));
        
        detailsGrid.getChildren().addAll(
            createDetailRow("Patient", appointment.getpatientFirstName() + " " + appointment.getpatientLastName()),
            createDetailRow("Doctor", "Dr. " + appointment.getdoctorFirstName() + " " + appointment.getdoctorLastName()),
            createDetailRow("Date", appointment.getDate().toString()),
            createDetailRow("Time", appointment.getTime() != null ? 
                appointment.getTime().format(DateTimeFormatter.ofPattern("HH:mm")) : "N/A"),
            createDetailRow("Status", appointment.getStatus()),
            createDetailRow("Consultation Fee", "$" + appointment.getconsultationFee()),
            createDetailRow("Payment Status", appointment.getPaymentStatus() != null ? appointment.getPaymentStatus() : "UNPAID")
        );
        
        // Close button
        Button closeBtn = createProfessionalButton("Close", PRIMARY_COLOR);
        closeBtn.setOnAction(e -> detailsStage.close());
        
        content.getChildren().addAll(headerBox, detailsGrid, closeBtn);
        
        Scene scene = new Scene(content, 450, 500);
        detailsStage.setScene(scene);
        detailsStage.showAndWait();
    }
    
    private HBox createDetailRow(String label, String value) {
        HBox row = new HBox(15);
        row.setAlignment(Pos.CENTER_LEFT);
        
        Label labelNode = new Label(label + ":");
        labelNode.setFont(Font.font("System", FontWeight.BOLD, 13));
        labelNode.setTextFill(Color.web("#475569"));
        labelNode.setPrefWidth(120);
        
        Label valueNode = new Label(value);
        valueNode.setFont(Font.font("System", 13));
        valueNode.setTextFill(Color.web("#1e293b"));
        
        row.getChildren().addAll(labelNode, valueNode);
        return row;
    }
    
    private void showProfessionalBillingDialog(AppointmentDto appointment) {
        Stage billingStage = new Stage();
        billingStage.initModality(Modality.APPLICATION_MODAL);
        billingStage.initStyle(StageStyle.UNDECORATED);
        billingStage.setTitle("Billing Details");
        
        VBox content = new VBox(20);
        content.setPadding(new Insets(25));
        content.setAlignment(Pos.CENTER);
        
        // Create professional background
        content.setBackground(new Background(new BackgroundFill(
            Color.web(CARD_BG), 
            new CornerRadii(12), 
            null
        )));
        
        // Add shadow effect
        DropShadow modalShadow = new DropShadow(15, 0, 8, Color.rgb(0, 0, 0, 0.25));
        content.setEffect(modalShadow);
        
        // Header
        HBox headerBox = new HBox(12);
        headerBox.setAlignment(Pos.CENTER);
        
        Label headerIcon = new Label("💰");
        headerIcon.setFont(Font.font("System", FontWeight.BOLD, 24));
        
        Label headerTitle = new Label("Billing Details");
        headerTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        headerTitle.setTextFill(Color.web(DARK_BG));
        
        headerBox.getChildren().addAll(headerIcon, headerTitle);
        
        // Patient info card
        VBox patientCard = createInfoCard("Patient Information", 
            appointment.getpatientFirstName() + " " + appointment.getpatientLastName(),
            "Appointment: " + appointment.getDate() + " at " + 
            (appointment.getTime() != null ? appointment.getTime().format(DateTimeFormatter.ofPattern("HH:mm")) : "N/A"));
        
        // Billing details card
        VBox billingCard = createBillingCard(appointment);
        
        // Payment buttons
        HBox paymentButtons = new HBox(12);
        paymentButtons.setAlignment(Pos.CENTER);
        
        Button processPaymentBtn = createProfessionalButton("💳 Process Payment", SUCCESS_COLOR);
        processPaymentBtn.setOnAction(e -> {
            billingStage.close();
            showProfessionalProcessPaymentDialog(appointment);
        });
        
        // Disable if already paid
        if ("PAID".equals(appointment.getPaymentStatus()) || "COMPLETED".equals(appointment.getPaymentStatus())) {
            processPaymentBtn.setDisable(true);
            processPaymentBtn.setText("✅ Payment Completed");
            processPaymentBtn.setStyle("-fx-background-color: #d1fae5; -fx-text-fill: #065f46;");
        }
        
        Button closeBtn = createProfessionalButton("Close", PRIMARY_COLOR);
        closeBtn.setOnAction(e -> billingStage.close());
        
        paymentButtons.getChildren().addAll(processPaymentBtn, closeBtn);
        
        content.getChildren().addAll(headerBox, patientCard, billingCard, paymentButtons);
        
        Scene scene = new Scene(content, 500, 600);
        billingStage.setScene(scene);
        billingStage.showAndWait();
    }
    
    private VBox createInfoCard(String title, String... details) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(15));
        card.setBackground(new Background(new BackgroundFill(
            Color.web("#f8fafc"), 
            new CornerRadii(8), 
            null
        )));
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        titleLabel.setTextFill(Color.web(DARK_BG));
        
        VBox detailsBox = new VBox(4);
        for (String detail : details) {
            Label detailLabel = new Label(detail);
            detailLabel.setFont(Font.font("System", 12));
            detailLabel.setTextFill(Color.web("#475569"));
            detailsBox.getChildren().add(detailLabel);
        }
        
        card.getChildren().addAll(titleLabel, detailsBox);
        return card;
    }
    
    private VBox createBillingCard(AppointmentDto appointment) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(15));
        card.setBackground(new Background(new BackgroundFill(
            Color.web("#eff6ff"), 
            new CornerRadii(8), 
            null
        )));
        
        Label titleLabel = new Label("💰 Billing Information");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        titleLabel.setTextFill(Color.web(DARK_BG));
        
        BigDecimal amount = appointment.getPaymentAmount() != null ? 
            appointment.getPaymentAmount() : appointment.getconsultationFee();
        
        VBox detailsBox = new VBox(6);
        detailsBox.getChildren().addAll(
            createBillingRow("Consultation Fee", "$" + String.format("%.2f", appointment.getconsultationFee())),
            createBillingRow("Amount", "$" + String.format("%.2f", amount)),
            createBillingRow("Payment Method", appointment.getPaymentMethod() != null ? appointment.getPaymentMethod() : "N/A"),
            createBillingRow("Payment Status", appointment.getPaymentStatus() != null ? appointment.getPaymentStatus() : "UNPAID"),
            createBillingRow("Payment Date", appointment.getPaymentDate() != null ? 
                appointment.getPaymentDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "N/A")
        );
        
        card.getChildren().addAll(titleLabel, detailsBox);
        return card;
    }
    
    private HBox createBillingRow(String label, String value) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);
        
        Label labelNode = new Label(label + ":");
        labelNode.setFont(Font.font("System", FontWeight.BOLD, 12));
        labelNode.setTextFill(Color.web("#475569"));
        labelNode.setPrefWidth(120);
        
        Label valueNode = new Label(value);
        valueNode.setFont(Font.font("System", 12));
        valueNode.setTextFill(Color.web("#1e293b"));
        
        row.getChildren().addAll(labelNode, valueNode);
        return row;
    }
    
    private void showProfessionalProcessPaymentDialog(AppointmentDto appointment) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Process Payment");
        confirmAlert.setHeaderText("💳 Payment Confirmation");
        confirmAlert.setContentText("Mark payment as completed for " + 
            appointment.getpatientFirstName() + " " + appointment.getpatientLastName() + 
            "\n\nAmount: $" + appointment.getconsultationFee() + 
            "\nCurrent Status: " + (appointment.getPaymentStatus() != null ? appointment.getPaymentStatus() : "UNPAID") +
            "\n\nProceed with payment completion?");
        
        // Style the alert
        DialogPane dialogPane = confirmAlert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #ffffff; -fx-border-color: #e2e8f0; -fx-border-radius: 8;");
        
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                statusLabel.setText("Processing payment...");
                
                // Update local appointment object
                appointment.setPaymentAmount(appointment.getconsultationFee());
                appointment.setPaymentMethod("CASH");
                appointment.setPaymentStatus("PAID");
                appointment.setPaymentDate(LocalDate.now());
                appointment.setStatus("COMPLETED");
                
                // Update payment and status in database
                boolean success = apiService.processPayment(
                    appointment.getId(),
                    appointment.getconsultationFee().doubleValue(),
                    "CASH",
                    "PAID",
                    LocalDate.now().toString()
                );
                
                if (success) {
                    appointmentTable.refresh();
                    statusLabel.setText("Payment completed and appointment finished");
                    showProfessionalInfoAlert("Payment Completed", 
                        "Payment has been processed and the appointment is now completed.");
                } else {
                    statusLabel.setText("Payment update failed");
                    showProfessionalErrorAlert("Payment Error", "Failed to update payment in database.");
                }
            } catch (Exception e) {
                statusLabel.setText("Payment update error");
                showProfessionalErrorAlert("Payment Error", "An error occurred while updating payment: " + e.getMessage());
            }
        }
    }
    
    private void showProfessionalInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText("✅ " + title);
        alert.setContentText(message);
        
        // Style the alert
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #f0fdf4; -fx-border-color: #bbf7d0; -fx-border-radius: 8;");
        
        alert.showAndWait();
    }
    
    private void showProfessionalWarningAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText("⚠️ " + title);
        alert.setContentText(message);
        
        // Style the alert
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #fffbeb; -fx-border-color: #fcd34d; -fx-border-radius: 8;");
        
        alert.showAndWait();
    }
    
    private void showProfessionalErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText("❌ " + title);
        alert.setContentText(message);
        
        // Style the alert
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setStyle("-fx-background-color: #fef2f2; -fx-border-color: #fca5a5; -fx-border-radius: 8;");
        
        alert.showAndWait();
    }
    
    private void refreshAppointments() {
        statusLabel.setText("Loading appointments...");
        
        try {
            List<AppointmentDto> appointments = apiService.getTodaysAppointments();
            appointmentData.clear();
            appointmentData.addAll(appointments);
            statusLabel.setText("Loaded " + appointments.size() + " appointments for today");
        } catch (Exception e) {
            showProfessionalErrorAlert("Error loading appointments", e.getMessage());
            statusLabel.setText("Failed to load appointments");
        }
    }
    
    private void checkInSelectedAppointment() {
        AppointmentDto selected = appointmentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showProfessionalWarningAlert("No Selection", "Please select an appointment to check in.");
            return;
        }
        
        if ("CHECKED_IN".equals(selected.getStatus())) {
            showProfessionalWarningAlert("Already Checked In", "This patient has already been checked in.");
            return;
        }
        
        try {
            statusLabel.setText("Checking in patient...");
            boolean success = apiService.checkInAppointment(selected.getId());
            
            if (success) {
                selected.setStatus("CHECKED_IN");
                appointmentTable.refresh();
                statusLabel.setText("Successfully checked in " + selected.getpatientFirstName() + " " + selected.getpatientLastName());
                showProfessionalInfoAlert("Check-in Successful", 
                    selected.getpatientFirstName() + " " + selected.getpatientLastName() + " has been checked in.");
            } else {
                statusLabel.setText("Check-in failed");
                showProfessionalErrorAlert("Check-in Failed", "Unable to check in the patient. Please try again.");
            }
        } catch (Exception e) {
            showProfessionalErrorAlert("Error during check-in", e.getMessage());
            statusLabel.setText("Check-in error occurred");
        }
    }
    
    private void viewSelectedAppointmentBilling() {
        AppointmentDto selected = appointmentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showProfessionalWarningAlert("No Selection", "Please select an appointment to view billing.");
            return;
        }
        
        showProfessionalBillingDialog(selected);
    }
    
    private void showAppointmentsTab() {
        mainTabPane.getSelectionModel().select(0);
        updateNavButtonStates(true, false, false, false);
    }
    
    private void showPatientsTab() {
        mainTabPane.getSelectionModel().select(1);
        updateNavButtonStates(false, true, false, false);
    }
    
    private void updateNavButtonStates(boolean appointments, boolean patients, boolean billing, boolean reports) {
        // This method would update the visual state of navigation buttons
        // For now, we'll just switch tabs
    }
    
    private void refreshPatients() {
        statusLabel.setText("Loading patients...");
        
        try {
            List<PatientDto> patients = apiService.getAllPatients();
            patientData.clear();
            patientData.addAll(patients);
            statusLabel.setText("Loaded " + patients.size() + " patients");
        } catch (Exception e) {
            showProfessionalErrorAlert("Error loading patients", e.getMessage());
            statusLabel.setText("Failed to load patients");
        }
    }
    
    private void searchPatients(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            refreshPatients();
            return;
        }
        
        try {
            List<PatientDto> patients = apiService.searchPatients(searchTerm.trim());
            patientData.clear();
            patientData.addAll(patients);
            statusLabel.setText("Found " + patients.size() + " patients matching '" + searchTerm + "'");
        } catch (Exception e) {
            showProfessionalErrorAlert("Error searching patients", e.getMessage());
            statusLabel.setText("Failed to search patients");
        }
    }
    
    private void searchAppointments(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            refreshAppointments();
            return;
        }
        
        try {
            List<AppointmentDto> appointments = apiService.searchAppointments(searchTerm.trim());
            appointmentData.clear();
            appointmentData.addAll(appointments);
            statusLabel.setText("Found " + appointments.size() + " appointments matching '" + searchTerm + "'");
        } catch (Exception e) {
            showProfessionalErrorAlert("Error searching appointments", e.getMessage());
            statusLabel.setText("Failed to search appointments");
        }
    }
    
    private void showProfessionalPatientDetails(PatientDto patient) {
        Stage detailsStage = new Stage();
        detailsStage.initModality(Modality.APPLICATION_MODAL);
        detailsStage.initStyle(StageStyle.UNDECORATED);
        detailsStage.setTitle("Patient Details");
        
        VBox content = new VBox(20);
        content.setPadding(new Insets(25));
        content.setAlignment(Pos.CENTER);
        
        // Create professional background
        content.setBackground(new Background(new BackgroundFill(
            Color.web(CARD_BG), 
            new CornerRadii(12), 
            null
        )));
        
        // Add shadow effect
        DropShadow modalShadow = new DropShadow(15, 0, 8, Color.rgb(0, 0, 0, 0.25));
        content.setEffect(modalShadow);
        
        // Header
        HBox headerBox = new HBox(12);
        headerBox.setAlignment(Pos.CENTER);
        
        Label headerIcon = new Label("👤");
        headerIcon.setFont(Font.font("System", FontWeight.BOLD, 24));
        
        Label headerTitle = new Label("Patient Details");
        headerTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        headerTitle.setTextFill(Color.web(DARK_BG));
        
        headerBox.getChildren().addAll(headerIcon, headerTitle);
        
        // Details grid
        VBox detailsGrid = new VBox(12);
        detailsGrid.setPadding(new Insets(20));
        detailsGrid.setBackground(new Background(new BackgroundFill(
            Color.web("#f8fafc"), 
            new CornerRadii(8), 
            null
        )));
        
        detailsGrid.getChildren().addAll(
            createDetailRow("ID", patient.getId() != null ? patient.getId().toString() : "N/A"),
            createDetailRow("Full Name", patient.getFullName()),
            createDetailRow("Email", patient.getEmail() != null ? patient.getEmail() : "N/A"),
            createDetailRow("Phone", patient.getPhoneNumber() != null ? patient.getPhoneNumber() : "N/A"),
            createDetailRow("Age", patient.getAge() != null ? patient.getAge().toString() : "N/A"),
            createDetailRow("Gender", patient.getGender() != null ? patient.getGender() : "N/A"),
            createDetailRow("Address", patient.getAddress() != null ? patient.getAddress() : "N/A"),
            createDetailRow("Medical History", patient.getMedicalHistory() != null ? patient.getMedicalHistory() : "N/A")
        );
        
        // Close button
        Button closeBtn = createProfessionalButton("Close", PRIMARY_COLOR);
        closeBtn.setOnAction(e -> detailsStage.close());
        
        content.getChildren().addAll(headerBox, detailsGrid, closeBtn);
        
        Scene scene = new Scene(content, 500, 600);
        detailsStage.setScene(scene);
        detailsStage.showAndWait();
    }
    
    private void showAddPatientDialog() {
        Stage addStage = new Stage();
        addStage.initModality(Modality.APPLICATION_MODAL);
        addStage.initStyle(StageStyle.UNDECORATED);
        addStage.setTitle("Add New Patient");
        
        VBox content = new VBox(20);
        content.setPadding(new Insets(25));
        content.setAlignment(Pos.CENTER);
        
        // Create professional background
        content.setBackground(new Background(new BackgroundFill(
            Color.web(CARD_BG), 
            new CornerRadii(12), 
            null
        )));
        
        // Add shadow effect
        DropShadow modalShadow = new DropShadow(15, 0, 8, Color.rgb(0, 0, 0, 0.25));
        content.setEffect(modalShadow);
        
        // Header
        HBox headerBox = new HBox(12);
        headerBox.setAlignment(Pos.CENTER);
        
        Label headerIcon = new Label("➕");
        headerIcon.setFont(Font.font("System", FontWeight.BOLD, 24));
        
        Label headerTitle = new Label("Add New Patient");
        headerTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        headerTitle.setTextFill(Color.web(DARK_BG));
        
        headerBox.getChildren().addAll(headerIcon, headerTitle);
        
        // Form fields
        VBox formGrid = new VBox(15);
        formGrid.setPadding(new Insets(20));
        formGrid.setBackground(new Background(new BackgroundFill(
            Color.web("#f8fafc"), 
            new CornerRadii(8), 
            null
        )));
        
        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");
        firstNameField.setPrefHeight(35);
        
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");
        lastNameField.setPrefHeight(35);
        
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setPrefHeight(35);
        
        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number");
        phoneField.setPrefHeight(35);
        
        TextField ageField = new TextField();
        ageField.setPromptText("Age");
        ageField.setPrefHeight(35);
        
        ComboBox<String> genderCombo = new ComboBox<>();
        genderCombo.getItems().addAll("Male", "Female", "Other");
        genderCombo.setPromptText("Select Gender");
        genderCombo.setPrefHeight(35);
        
        TextArea addressArea = new TextArea();
        addressArea.setPromptText("Address");
        addressArea.setPrefRowCount(3);
        addressArea.setPrefHeight(80);
        
        TextArea medicalHistoryArea = new TextArea();
        medicalHistoryArea.setPromptText("Medical History");
        medicalHistoryArea.setPrefRowCount(3);
        medicalHistoryArea.setPrefHeight(80);
        
        formGrid.getChildren().addAll(
            createFormRow("First Name:", firstNameField),
            createFormRow("Last Name:", lastNameField),
            createFormRow("Email:", emailField),
            createFormRow("Phone:", phoneField),
            createFormRow("Age:", ageField),
            createFormRow("Gender:", genderCombo),
            createFormRow("Address:", addressArea),
            createFormRow("Medical History:", medicalHistoryArea)
        );
        
        // Buttons
        HBox buttonBox = new HBox(12);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button saveBtn = createProfessionalButton("Save", SUCCESS_COLOR);
        saveBtn.setOnAction(e -> {
            // Validate and save patient
            if (validatePatientForm(firstNameField, lastNameField, emailField, phoneField, ageField)) {
                PatientDto newPatient = new PatientDto();
                newPatient.setFirstName(firstNameField.getText().trim());
                newPatient.setLastName(lastNameField.getText().trim());
                newPatient.setEmail(emailField.getText().trim());
                newPatient.setPhoneNumber(phoneField.getText().trim());
                
                try {
                    newPatient.setAge(Integer.parseInt(ageField.getText().trim()));
                } catch (NumberFormatException ex) {
                    newPatient.setAge(null);
                }
                
                newPatient.setGender(genderCombo.getValue());
                newPatient.setAddress(addressArea.getText().trim());
                newPatient.setMedicalHistory(medicalHistoryArea.getText().trim());
                
                try {
                    boolean success = apiService.createPatient(newPatient);
                if (success) {
                        refreshPatients();
                        addStage.close();
                        showProfessionalInfoAlert("Success", "Patient added successfully!");
                } else {
                        showProfessionalErrorAlert("Error", "Failed to add patient");
                    }
                } catch (Exception ex) {
                    showProfessionalErrorAlert("Error", "Failed to add patient: " + ex.getMessage());
                }
            }
        });
        
        Button cancelBtn = createProfessionalButton("Cancel", DANGER_COLOR);
        cancelBtn.setOnAction(e -> addStage.close());
        
        buttonBox.getChildren().addAll(saveBtn, cancelBtn);
        
        content.getChildren().addAll(headerBox, formGrid, buttonBox);
        
        Scene scene = new Scene(content, 500, 700);
        addStage.setScene(scene);
        addStage.showAndWait();
    }
    
    private VBox createFormRow(String label, Control control) {
        VBox row = new VBox(5);
        
        Label labelNode = new Label(label);
        labelNode.setFont(Font.font("System", FontWeight.BOLD, 12));
        labelNode.setTextFill(Color.web("#475569"));
        
        row.getChildren().addAll(labelNode, control);
        return row;
    }
    
    private boolean validatePatientForm(TextField firstName, TextField lastName, TextField email, 
                                      TextField phone, TextField age) {
        if (firstName.getText().trim().isEmpty()) {
            showProfessionalWarningAlert("Validation Error", "First name is required");
            return false;
        }
        if (lastName.getText().trim().isEmpty()) {
            showProfessionalWarningAlert("Validation Error", "Last name is required");
            return false;
        }
        if (email.getText().trim().isEmpty()) {
            showProfessionalWarningAlert("Validation Error", "Email is required");
            return false;
        }
        if (phone.getText().trim().isEmpty()) {
            showProfessionalWarningAlert("Validation Error", "Phone number is required");
            return false;
        }
        if (age.getText().trim().isEmpty()) {
            showProfessionalWarningAlert("Validation Error", "Age is required");
            return false;
        }
        
        try {
            Integer.parseInt(age.getText().trim());
        } catch (NumberFormatException e) {
            showProfessionalWarningAlert("Validation Error", "Age must be a valid number");
            return false;
        }
        
        return true;
    }
    
    private void editSelectedPatient() {
        PatientDto selected = patientTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showProfessionalWarningAlert("No Selection", "Please select a patient to edit.");
            return;
        }
        
        showEditPatientDialog(selected);
    }
    
    private void showEditPatientDialog(PatientDto patient) {
        // Similar to add patient dialog but with pre-filled fields
        // Implementation would be similar to showAddPatientDialog()
        showProfessionalInfoAlert("Edit Patient", "Edit functionality will be implemented in the next iteration");
    }
    
    // Reports Tab Methods
    private VBox createReportsContent() {
        VBox reportsContent = new VBox(20);
        reportsContent.setPadding(new Insets(20));
        
        // Header section
        VBox headerSection = createReportsHeaderSection();
        
        // Statistics cards section
        VBox statsSection = createReportsStatsSection();
        
        // Charts and detailed reports section
        VBox chartsSection = createReportsChartsSection();
        
        reportsContent.getChildren().addAll(headerSection, statsSection, chartsSection);
        return reportsContent;
    }
    
    private VBox createReportsHeaderSection() {
        VBox headerSection = new VBox(10);
        headerSection.setPadding(new Insets(20, 25, 20, 25));
        
        // Create professional card background
        headerSection.setBackground(new Background(new BackgroundFill(
            Color.web(CARD_BG), 
            new CornerRadii(12), 
            Insets.EMPTY
        )));
        
        // Add shadow effect
        DropShadow cardShadow = new DropShadow(8, 0, 4, Color.rgb(0, 0, 0, 0.08));
        headerSection.setEffect(cardShadow);
        
        // Header content
        HBox headerContent = new HBox(20);
        headerContent.setAlignment(Pos.CENTER_LEFT);
        
        VBox titleBox = new VBox(4);
        Label titleLabel = new Label("Analytics & Reports Dashboard");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        titleLabel.setTextFill(Color.web(DARK_BG));
        
        Label subtitleLabel = new Label("Comprehensive insights into patients, appointments, and revenue");
        subtitleLabel.setFont(Font.font("System", 13));
        subtitleLabel.setTextFill(Color.web("#64748b"));
        
        titleBox.getChildren().addAll(titleLabel, subtitleLabel);
        
        // Action buttons
        HBox actionButtons = new HBox(12);
        actionButtons.setAlignment(Pos.CENTER_RIGHT);
        
        Button currentMonthBtn = createActionButton("📅 Current Month", INFO_COLOR);
        currentMonthBtn.setOnAction(e -> loadCurrentMonthReport());
        
        Button last30DaysBtn = createActionButton("📊 Last 30 Days", SUCCESS_COLOR);
        last30DaysBtn.setOnAction(e -> loadLast30DaysReport());
        
        Button currentYearBtn = createActionButton("📈 Current Year", WARNING_COLOR);
        currentYearBtn.setOnAction(e -> loadCurrentYearReport());
        
        Button exportCurrentMonthBtn = createActionButton("📊 Export Current Month", DANGER_COLOR);
        exportCurrentMonthBtn.setOnAction(e -> exportCurrentMonthReport());
        
        Button exportLast30DaysBtn = createActionButton("📊 Export Last 30 Days", DANGER_COLOR);
        exportLast30DaysBtn.setOnAction(e -> exportLast30DaysReport());
        
        actionButtons.getChildren().addAll(currentMonthBtn, last30DaysBtn, currentYearBtn, exportCurrentMonthBtn, exportLast30DaysBtn);
        
        // Spacer to push buttons to the right
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        headerContent.getChildren().addAll(titleBox, spacer, actionButtons);
        headerSection.getChildren().add(headerContent);
        
        return headerSection;
    }
    
    private VBox createReportsStatsSection() {
        VBox statsSection = new VBox(15);
        
        // Create a grid for statistics cards
        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(15);
        statsGrid.setVgap(15);
        
        // Patient statistics card
        VBox patientStatsCard = createStatsCard("👥 Patient Statistics", "0", "Total Patients", SUCCESS_COLOR);
        GridPane.setConstraints(patientStatsCard, 0, 0);
        
        // Appointment statistics card
        VBox appointmentStatsCard = createStatsCard("📅 Appointment Statistics", "0", "Total Appointments", INFO_COLOR);
        GridPane.setConstraints(appointmentStatsCard, 1, 0);
        
        // Revenue statistics card
        VBox revenueStatsCard = createStatsCard("💰 Revenue Statistics", "$0", "Total Revenue", WARNING_COLOR);
        GridPane.setConstraints(revenueStatsCard, 2, 0);
        
        // Doctor statistics card
        VBox doctorStatsCard = createStatsCard("👨‍⚕️ Doctor Statistics", "0", "Total Doctors", PRIMARY_COLOR);
        GridPane.setConstraints(doctorStatsCard, 3, 0);
        
        statsGrid.getChildren().addAll(patientStatsCard, appointmentStatsCard, revenueStatsCard, doctorStatsCard);
        statsSection.getChildren().add(statsGrid);
        
        // Store references to the stats cards for updating
        statsSection.setUserData(new VBox[]{patientStatsCard, appointmentStatsCard, revenueStatsCard, doctorStatsCard});
        
        return statsSection;
    }
    
    private VBox createStatsCard(String title, String value, String subtitle, String color) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(20));
        card.setBackground(new Background(new BackgroundFill(
            Color.web(CARD_BG), 
            new CornerRadii(12), 
            Insets.EMPTY
        )));
        card.setBorder(new Border(new BorderStroke(
            Color.web(BORDER_COLOR), 
            BorderStrokeStyle.SOLID, 
            new CornerRadii(12), 
            new BorderWidths(1)
        )));
        
        // Add shadow effect
        DropShadow cardShadow = new DropShadow(4, 0, 2, Color.rgb(0, 0, 0, 0.06));
        card.setEffect(cardShadow);
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        titleLabel.setTextFill(Color.web("#475569"));
        
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("System", FontWeight.BOLD, 24));
        valueLabel.setTextFill(Color.web(color));
        
        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.setFont(Font.font("System", 11));
        subtitleLabel.setTextFill(Color.web("#64748b"));
        
        card.getChildren().addAll(titleLabel, valueLabel, subtitleLabel);
        
        // Store references for updating
        card.setUserData(new Object[]{valueLabel, subtitleLabel});
        
        return card;
    }
    
    private VBox createReportsChartsSection() {
        VBox chartsSection = new VBox(15);
        
        // Create a grid for charts
        GridPane chartsGrid = new GridPane();
        chartsGrid.setHgap(15);
        chartsGrid.setVgap(15);
        
        // Patient demographics chart
        VBox patientDemographicsCard = createChartCard("Patient Demographics", "Gender and Age Distribution");
        GridPane.setConstraints(patientDemographicsCard, 0, 0);
        
        // Appointment status chart
        VBox appointmentStatusCard = createChartCard("Appointment Status", "Completed, Pending, Cancelled");
        GridPane.setConstraints(appointmentStatusCard, 1, 0);
        
        // Revenue trends chart
        VBox revenueTrendsCard = createChartCard("Revenue Trends", "Daily and Monthly Revenue");
        GridPane.setConstraints(revenueTrendsCard, 0, 1);
        
        // Payment methods chart
        VBox paymentMethodsCard = createChartCard("Payment Methods", "Revenue by Payment Type");
        GridPane.setConstraints(paymentMethodsCard, 1, 1);
        
        chartsGrid.getChildren().addAll(patientDemographicsCard, appointmentStatusCard, revenueTrendsCard, paymentMethodsCard);
        chartsSection.getChildren().add(chartsGrid);
        
        return chartsSection;
    }
    
    private VBox createChartCard(String title, String subtitle) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(20));
        card.setBackground(new Background(new BackgroundFill(
            Color.web(CARD_BG), 
            new CornerRadii(12), 
            Insets.EMPTY
        )));
        card.setBorder(new Border(new BorderStroke(
            Color.web(BORDER_COLOR), 
            BorderStrokeStyle.SOLID, 
            new CornerRadii(12), 
            new BorderWidths(1)
        )));
        
        // Add shadow effect
        DropShadow cardShadow = new DropShadow(4, 0, 2, Color.rgb(0, 0, 0, 0.06));
        card.setEffect(cardShadow);
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        titleLabel.setTextFill(Color.web(DARK_BG));
        
        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.setFont(Font.font("System", 12));
        subtitleLabel.setTextFill(Color.web("#64748b"));
        
        // Chart content area
        VBox chartContent = new VBox(8);
        chartContent.setAlignment(Pos.CENTER);
        chartContent.setPadding(new Insets(20));
        chartContent.setBackground(new Background(new BackgroundFill(
            Color.web("#f8fafc"), 
            new CornerRadii(8), 
            Insets.EMPTY
        )));
        chartContent.setMinHeight(120);
        
        // Placeholder text
        Label chartText = new Label("No data available");
        chartText.setFont(Font.font("System", 12));
        chartText.setTextFill(Color.web("#64748b"));
        
        chartContent.getChildren().add(chartText);
        
        card.getChildren().addAll(titleLabel, subtitleLabel, chartContent);
        
        // Store reference to chart content for updating
        card.setUserData(chartContent);
        
        return card;
    }
    
    // Report loading methods
    private void loadCurrentMonthReport() {
        try {
            ReportDto report = apiService.getCurrentMonthReport();
            updateReportsUI(report);
            showProfessionalInfoAlert("Success", "Current month report loaded successfully!");
            } catch (Exception e) {
            showProfessionalErrorAlert("Error", "Failed to load current month report: " + e.getMessage());
        }
    }
    
    private void loadLast30DaysReport() {
        try {
            ReportDto report = apiService.getLast30DaysReport();
            updateReportsUI(report);
            showProfessionalInfoAlert("Success", "Last 30 days report loaded successfully!");
        } catch (Exception e) {
            showProfessionalErrorAlert("Error", "Failed to load last 30 days report: " + e.getMessage());
        }
    }
    
    private void loadCurrentYearReport() {
        try {
            ReportDto report = apiService.getCurrentYearReport();
            updateReportsUI(report);
            showProfessionalInfoAlert("Success", "Current year report loaded successfully!");
        } catch (Exception e) {
            showProfessionalErrorAlert("Error", "Failed to load current year report: " + e.getMessage());
        }
    }
    
    private void updateReportsUI(ReportDto report) {
        if (report == null) return;
        
        // Find the reports content area to get the stats cards
        Tab reportsTab = mainTabPane.getTabs().get(2); // Reports tab
        if (reportsTab != null && reportsTab.getContent() instanceof VBox) {
            VBox reportsContent = (VBox) reportsTab.getContent();
            
            // Find the stats section (second child)
            if (reportsContent.getChildren().size() > 1) {
                VBox statsSection = (VBox) reportsContent.getChildren().get(1);
                VBox[] statsCards = (VBox[]) statsSection.getUserData();
                
                if (statsCards != null && statsCards.length >= 4) {
                    // Update patient statistics
                    updateStatsCard(statsCards[0], report.getTotalPatients().toString());
                    
                    // Update appointment statistics
                    updateStatsCard(statsCards[1], report.getTotalAppointments().toString());
                    
                    // Update revenue statistics
                    updateStatsCard(statsCards[2], "$" + report.getTotalRevenue().toString());
                    
                    // Update doctor statistics
                    updateStatsCard(statsCards[3], report.getTotalDoctors().toString());
                }
            }
            
            // Find the charts section (third child)
            if (reportsContent.getChildren().size() > 2) {
                VBox chartsSection = (VBox) reportsContent.getChildren().get(2);
                if (chartsSection.getChildren().size() > 0) {
                    GridPane chartsGrid = (GridPane) chartsSection.getChildren().get(0);
                    
                    // Update patient demographics chart
                    if (chartsGrid.getChildren().size() > 0) {
                        VBox patientDemographicsCard = (VBox) chartsGrid.getChildren().get(0);
                        updatePatientDemographicsChart(patientDemographicsCard, report);
                    }
                    
                    // Update appointment status chart
                    if (chartsGrid.getChildren().size() > 1) {
                        VBox appointmentStatusCard = (VBox) chartsGrid.getChildren().get(1);
                        updateAppointmentStatusChart(appointmentStatusCard, report);
                    }
                    
                    // Update revenue trends chart
                    if (chartsGrid.getChildren().size() > 2) {
                        VBox revenueTrendsCard = (VBox) chartsGrid.getChildren().get(2);
                        updateRevenueTrendsChart(revenueTrendsCard, report);
                    }
                    
                    // Update payment methods chart
                    if (chartsGrid.getChildren().size() > 3) {
                        VBox paymentMethodsCard = (VBox) chartsGrid.getChildren().get(3);
                        updatePaymentMethodsChart(paymentMethodsCard, report);
                    }
                }
            }
        }
        
        System.out.println("Report loaded: " + report.getTotalPatients() + " patients, " + 
                          report.getTotalAppointments() + " appointments, $" + 
                          report.getTotalRevenue() + " revenue");
    }
    
    private void updateStatsCard(VBox card, String newValue) {
        if (card != null && card.getUserData() != null) {
            Object[] data = (Object[]) card.getUserData();
            if (data.length > 0 && data[0] instanceof Label) {
                Label valueLabel = (Label) data[0];
                valueLabel.setText(newValue);
            }
        }
    }
    
    private void updatePatientDemographicsChart(VBox card, ReportDto report) {
        VBox chartContent = (VBox) card.getUserData();
        if (chartContent != null) {
            chartContent.getChildren().clear();
            
            VBox chartData = new VBox(8);
            chartData.setAlignment(Pos.CENTER_LEFT);
            
            // Gender distribution
            if (report.getPatientsByGender() != null && !report.getPatientsByGender().isEmpty()) {
                Label genderTitle = new Label("Gender Distribution:");
                genderTitle.setFont(Font.font("System", FontWeight.BOLD, 12));
                genderTitle.setTextFill(Color.web(DARK_BG));
                chartData.getChildren().add(genderTitle);
                
                for (Map.Entry<String, Long> entry : report.getPatientsByGender().entrySet()) {
                    Label genderLabel = new Label(entry.getKey() + ": " + entry.getValue());
                    genderLabel.setFont(Font.font("System", 11));
                    genderLabel.setTextFill(Color.web("#64748b"));
                    chartData.getChildren().add(genderLabel);
                }
            }
            
            // Age distribution
            if (report.getPatientsByAgeGroup() != null && !report.getPatientsByAgeGroup().isEmpty()) {
                Label ageTitle = new Label("Age Distribution:");
                ageTitle.setFont(Font.font("System", FontWeight.BOLD, 12));
                ageTitle.setTextFill(Color.web(DARK_BG));
                chartData.getChildren().add(ageTitle);
                
                for (Map.Entry<String, Long> entry : report.getPatientsByAgeGroup().entrySet()) {
                    Label ageLabel = new Label(entry.getKey() + ": " + entry.getValue());
                    ageLabel.setFont(Font.font("System", 11));
                    ageLabel.setTextFill(Color.web("#64748b"));
                    chartData.getChildren().add(ageLabel);
                }
            }
            
            if (chartData.getChildren().isEmpty()) {
                Label noDataLabel = new Label("No demographic data available");
                noDataLabel.setFont(Font.font("System", 11));
                noDataLabel.setTextFill(Color.web("#64748b"));
                chartData.getChildren().add(noDataLabel);
            }
            
            chartContent.getChildren().add(chartData);
        }
    }
    
    private void updateAppointmentStatusChart(VBox card, ReportDto report) {
        VBox chartContent = (VBox) card.getUserData();
        if (chartContent != null) {
            chartContent.getChildren().clear();
            
            VBox chartData = new VBox(8);
            chartData.setAlignment(Pos.CENTER_LEFT);
            
            Label title = new Label("Appointment Status:");
            title.setFont(Font.font("System", FontWeight.BOLD, 12));
            title.setTextFill(Color.web(DARK_BG));
            chartData.getChildren().add(title);
            
            Label completedLabel = new Label("Completed: " + report.getCompletedAppointments());
            completedLabel.setFont(Font.font("System", 11));
            completedLabel.setTextFill(Color.web(SUCCESS_COLOR));
            chartData.getChildren().add(completedLabel);
            
            Label pendingLabel = new Label("Pending: " + report.getPendingAppointments());
            pendingLabel.setFont(Font.font("System", 11));
            pendingLabel.setTextFill(Color.web(WARNING_COLOR));
            chartData.getChildren().add(pendingLabel);
            
            Label cancelledLabel = new Label("Cancelled: " + report.getCancelledAppointments());
            cancelledLabel.setFont(Font.font("System", 11));
            cancelledLabel.setTextFill(Color.web(DANGER_COLOR));
            chartData.getChildren().add(cancelledLabel);
            
            chartContent.getChildren().add(chartData);
        }
    }
    
    private void updateRevenueTrendsChart(VBox card, ReportDto report) {
        VBox chartContent = (VBox) card.getUserData();
        if (chartContent != null) {
            chartContent.getChildren().clear();
            
            VBox chartData = new VBox(8);
            chartData.setAlignment(Pos.CENTER_LEFT);
            
            Label title = new Label("Revenue Summary:");
            title.setFont(Font.font("System", FontWeight.BOLD, 12));
            title.setTextFill(Color.web(DARK_BG));
            chartData.getChildren().add(title);
            
            Label totalRevenueLabel = new Label("Total: $" + report.getTotalRevenue());
            totalRevenueLabel.setFont(Font.font("System", 11));
            totalRevenueLabel.setTextFill(Color.web(SUCCESS_COLOR));
            chartData.getChildren().add(totalRevenueLabel);
            
            Label avgRevenueLabel = new Label("Average per day: $" + report.getAverageRevenuePerDay());
            avgRevenueLabel.setFont(Font.font("System", 11));
            avgRevenueLabel.setTextFill(Color.web("#64748b"));
            chartData.getChildren().add(avgRevenueLabel);
            
            Label avgAppointmentLabel = new Label("Average per appointment: $" + report.getAverageAppointmentCost());
            avgAppointmentLabel.setFont(Font.font("System", 11));
            avgAppointmentLabel.setTextFill(Color.web("#64748b"));
            chartData.getChildren().add(avgAppointmentLabel);
            
            chartContent.getChildren().add(chartData);
        }
    }
    
    private void updatePaymentMethodsChart(VBox card, ReportDto report) {
        VBox chartContent = (VBox) card.getUserData();
        if (chartContent != null) {
            chartContent.getChildren().clear();
            
            VBox chartData = new VBox(8);
            chartData.setAlignment(Pos.CENTER_LEFT);
            
            Label title = new Label("Payment Methods:");
            title.setFont(Font.font("System", FontWeight.BOLD, 12));
            title.setTextFill(Color.web(DARK_BG));
            chartData.getChildren().add(title);
            
            if (report.getRevenueByPaymentMethod() != null && !report.getRevenueByPaymentMethod().isEmpty()) {
                for (Map.Entry<String, BigDecimal> entry : report.getRevenueByPaymentMethod().entrySet()) {
                    Label methodLabel = new Label(entry.getKey() + ": $" + entry.getValue());
                    methodLabel.setFont(Font.font("System", 11));
                    methodLabel.setTextFill(Color.web("#64748b"));
                    chartData.getChildren().add(methodLabel);
                }
            } else {
                Label noDataLabel = new Label("No payment method data available");
                noDataLabel.setFont(Font.font("System", 11));
                noDataLabel.setTextFill(Color.web("#64748b"));
                chartData.getChildren().add(noDataLabel);
            }
            
            chartContent.getChildren().add(chartData);
        }
    }
    
    // Tab switching methods
    private void showBillingTab() {
        mainTabPane.getSelectionModel().select(0); // For now, show appointments tab
        showProfessionalInfoAlert("Billing", "Billing functionality will be implemented in the next iteration");
    }
    
    private void showReportsTab() {
        mainTabPane.getSelectionModel().select(2); // Reports tab
        loadCurrentMonthReport(); // Load default report
    }
    
    // Export methods
    private void exportPatientsToExcel() {
        try {
            byte[] excelData = apiService.exportPatientsToExcel();
            saveExcelFile(excelData, "patients.xlsx");
            showProfessionalInfoAlert("Success", "Patients exported to Excel successfully!");
        } catch (Exception e) {
            showProfessionalErrorAlert("Error", "Failed to export patients: " + e.getMessage());
        }
    }
    
    private void exportCurrentMonthReport() {
        try {
            byte[] excelData = apiService.exportCurrentMonthReportToExcel();
            saveExcelFile(excelData, "current-month-report.xlsx");
            showProfessionalInfoAlert("Success", "Current month report exported to Excel successfully!");
        } catch (Exception e) {
            showProfessionalErrorAlert("Error", "Failed to export current month report: " + e.getMessage());
        }
    }
    
    private void exportLast30DaysReport() {
        try {
            byte[] excelData = apiService.exportLast30DaysReportToExcel();
            saveExcelFile(excelData, "last-30-days-report.xlsx");
            showProfessionalInfoAlert("Success", "Last 30 days report exported to Excel successfully!");
        } catch (Exception e) {
            showProfessionalErrorAlert("Error", "Failed to export last 30 days report: " + e.getMessage());
        }
    }
    
    private void saveExcelFile(byte[] excelData, String filename) {
        try {
            // Create a file chooser dialog
            javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
            fileChooser.setTitle("Save Excel File");
            fileChooser.setInitialFileName(filename);
            fileChooser.getExtensionFilters().add(
                new javafx.stage.FileChooser.ExtensionFilter("Excel Files", "*.xlsx")
            );
            
            // Show the save dialog
            File file = fileChooser.showSaveDialog(null);
            
            if (file != null) {
                // Write the Excel data to the selected file
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(excelData);
                }
            }
        } catch (IOException e) {
            showProfessionalErrorAlert("Error", "Failed to save Excel file: " + e.getMessage());
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}