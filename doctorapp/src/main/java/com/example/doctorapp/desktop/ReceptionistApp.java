package com.example.doctorapp.desktop;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.example.doctorapp.dto.AppointmentDto;
import com.example.doctorapp.desktop.service.ApiService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public class ReceptionistApp extends Application {
    
    private ApiService apiService;
    private TableView<AppointmentDto> appointmentTable;
    private ObservableList<AppointmentDto> appointmentData;
    private Label statusLabel;
    private TabPane mainTabPane;
    
    @Override
    public void start(Stage primaryStage) {
        // Initialize API service directly
        apiService = new ApiService();
        
        primaryStage.setTitle("Receptionist Dashboard - Doctor App");
        primaryStage.setWidth(1200);
        primaryStage.setHeight(700);
        
        // Handle application close event
        primaryStage.setOnCloseRequest(e -> {
            System.exit(0);
        });
        
        // Create the main layout
        BorderPane root = new BorderPane();
        
        // Create tab pane for different sections
        mainTabPane = new TabPane();
        mainTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        // Appointments tab
        Tab appointmentsTab = new Tab("Appointments");
        appointmentsTab.setContent(createAppointmentsContent());
        
        mainTabPane.getTabs().addAll(appointmentsTab);
        
        root.setCenter(mainTabPane);
        
        // Bottom section with status
        HBox bottomSection = createBottomSection();
        root.setBottom(bottomSection);
        
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        // Load initial data
        refreshAppointments();
    }
    
    private VBox createAppointmentsContent() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(15));
        
        // Top section with title and controls
        VBox topSection = createAppointmentsTopSection();
        
        // Center section with appointments table
        VBox centerSection = createAppointmentsCenterSection();
        
        content.getChildren().addAll(topSection, centerSection);
        return content;
    }
    
    private VBox createAppointmentsTopSection() {
        VBox topSection = new VBox(10);
        topSection.setPadding(new Insets(15));
        topSection.setStyle("-fx-background-color: #f8f9fa;");
        
        Label titleLabel = new Label("Today\'s Appointments - " + LocalDate.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")));
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        
        HBox buttonBox = new HBox(10);
        Button refreshButton = new Button("Refresh Appointments");
        refreshButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold;");
        refreshButton.setOnAction(e -> refreshAppointments());
        
        Button checkInSelectedButton = new Button("Check In Selected");
        checkInSelectedButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-weight: bold;");
        checkInSelectedButton.setOnAction(e -> checkInSelectedAppointment());
        
        Button viewBillingButton = new Button("View Billing");
        viewBillingButton.setStyle("-fx-background-color: #17a2b8; -fx-text-fill: white; -fx-font-weight: bold;");
        viewBillingButton.setOnAction(e -> viewSelectedAppointmentBilling());
        
        buttonBox.getChildren().addAll(refreshButton, checkInSelectedButton, viewBillingButton);
        topSection.getChildren().addAll(titleLabel, buttonBox);
        
        return topSection;
    }
    
    private VBox createAppointmentsCenterSection() {
        VBox centerSection = new VBox(10);
        
        // Create table
        appointmentTable = new TableView<>();
        appointmentData = FXCollections.observableArrayList();
        appointmentTable.setItems(appointmentData);
        
        // Patient Name column
        TableColumn<AppointmentDto, String> nameColumn = new TableColumn<>("Patient Name");
        nameColumn.setCellValueFactory(cellData -> {
            AppointmentDto appointment = cellData.getValue();
            String fullName = appointment.getpatientFirstName() + " " + appointment.getpatientLastName();
            return new SimpleStringProperty(fullName);
        });
        nameColumn.setPrefWidth(180);
        
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
        
        // Time column
        TableColumn<AppointmentDto, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(cellData -> {
            AppointmentDto appointment = cellData.getValue();
            String timeStr = appointment.getTime() != null ? 
                appointment.getTime().format(DateTimeFormatter.ofPattern("HH:mm")) : "N/A";
            return new SimpleStringProperty(timeStr);
        });
        timeColumn.setPrefWidth(100);
        
        // Status column
        TableColumn<AppointmentDto, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusColumn.setPrefWidth(120);
        
        // Status column with colored cells
        statusColumn.setCellFactory(column -> new TableCell<AppointmentDto, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    switch (status.toUpperCase()) {
                        case "SCHEDULED":
                            setStyle("-fx-background-color: #fff3cd; -fx-text-fill: #856404;");
                            break;
                        case "CHECKED_IN":
                            setStyle("-fx-background-color: #d4edda; -fx-text-fill: #155724;");
                            break;
                        case "COMPLETED":
                            setStyle("-fx-background-color: #d1ecf1; -fx-text-fill: #0c5460;");
                            break;
                        case "CANCELLED":
                            setStyle("-fx-background-color: #f8d7da; -fx-text-fill: #721c24;");
                            break;
                        default:
                            setStyle("");
                    }
                }
            }
        });
        
        // Payment Status column - Updated to use appointment data
        TableColumn<AppointmentDto, String> paymentColumn = new TableColumn<>("Payment");
        paymentColumn.setCellValueFactory(cellData -> {
            AppointmentDto appointment = cellData.getValue();
            String paymentStatus = appointment.getPaymentStatus();
            return new SimpleStringProperty(paymentStatus != null ? paymentStatus : "UNPAID");
        });
        paymentColumn.setPrefWidth(100);
        
        // Add colored cells for payment status
        paymentColumn.setCellFactory(column -> new TableCell<AppointmentDto, String>() {
            @Override
            protected void updateItem(String paymentStatus, boolean empty) {
                super.updateItem(paymentStatus, empty);
                if (empty || paymentStatus == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(paymentStatus);
                    switch (paymentStatus.toUpperCase()) {
                        case "PAID":
                        case "COMPLETED":
                            setStyle("-fx-background-color: #d4edda; -fx-text-fill: #155724;");
                            break;
                        case "UNPAID":
                        case "PENDING":
                            setStyle("-fx-background-color: #fff3cd; -fx-text-fill: #856404;");
                            break;
                        case "FAILED":
                            setStyle("-fx-background-color: #f8d7da; -fx-text-fill: #721c24;");
                            break;
                        case "REFUNDED":
                            setStyle("-fx-background-color: #d1ecf1; -fx-text-fill: #0c5460;");
                            break;
                        default:
                            setStyle("");
                    }
                }
            }
        });
        
        appointmentTable.getColumns().addAll(nameColumn, doctorColumn, timeColumn, statusColumn, paymentColumn);
        appointmentTable.setRowFactory(tv -> {
            TableRow<AppointmentDto> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    AppointmentDto appointment = row.getItem();
                    showAppointmentDetails(appointment);
                }
            });
            return row;
        });
        
        centerSection.getChildren().add(appointmentTable);
        return centerSection;
    }
    
    private HBox createBottomSection() {
        HBox bottomSection = new HBox();
        bottomSection.setPadding(new Insets(10));
        bottomSection.setStyle("-fx-background-color: #e9ecef;");
        
        statusLabel = new Label("Ready");
        statusLabel.setStyle("-fx-font-weight: bold;");
        bottomSection.getChildren().add(statusLabel);
        
        return bottomSection;
    }
    
    private void refreshAppointments() {
        statusLabel.setText("Loading appointments...");
        
        try {
            List<AppointmentDto> appointments = apiService.getTodaysAppointments();
            appointmentData.clear();
            appointmentData.addAll(appointments);
            statusLabel.setText("Loaded " + appointments.size() + " appointments for today");
        } catch (Exception e) {
            showErrorAlert("Error loading appointments", e.getMessage());
            statusLabel.setText("Failed to load appointments");
        }
    }
    
    private void checkInSelectedAppointment() {
        AppointmentDto selected = appointmentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarningAlert("No Selection", "Please select an appointment to check in.");
            return;
        }
        
        if ("CHECKED_IN".equals(selected.getStatus())) {
            showWarningAlert("Already Checked In", "This patient has already been checked in.");
            return;
        }
        
        try {
            statusLabel.setText("Checking in patient...");
            boolean success = apiService.checkInAppointment(selected.getId());
            
            if (success) {
                selected.setStatus("CHECKED_IN");
                appointmentTable.refresh();
                statusLabel.setText("Successfully checked in " + selected.getpatientFirstName() + " " + selected.getpatientLastName());
                showInfoAlert("Check-in Successful", 
                    selected.getpatientFirstName() + " " + selected.getpatientLastName() + " has been checked in.");
            } else {
                statusLabel.setText("Check-in failed");
                showErrorAlert("Check-in Failed", "Unable to check in the patient. Please try again.");
            }
        } catch (Exception e) {
            showErrorAlert("Error during check-in", e.getMessage());
            statusLabel.setText("Check-in error occurred");
        }
    }
    
    private void viewSelectedAppointmentBilling() {
        AppointmentDto selected = appointmentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showWarningAlert("No Selection", "Please select an appointment to view billing.");
            return;
        }
        
        showBillingDialog(selected);
    }
    
    private void showBillingDialog(AppointmentDto appointment) {
        Stage billingStage = new Stage();
        billingStage.initModality(Modality.APPLICATION_MODAL);
        billingStage.setTitle("Billing Details - " + appointment.getpatientFirstName() + " " + appointment.getpatientLastName());
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        
        // Patient info
        Label patientInfo = new Label("Patient: " + appointment.getpatientFirstName() + " " + appointment.getpatientLastName());
        patientInfo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        Label appointmentInfo = new Label("Appointment: " + appointment.getDate() + " at " + appointment.getTime());
        
        // Billing details using appointment data
        VBox billingDetails = new VBox(5);
        billingDetails.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 10;");
        
        Label billingTitle = new Label("Billing Details:");
        billingTitle.setStyle("-fx-font-weight: bold;");
        
        // Use appointment data instead of separate billing entity
        BigDecimal amount = appointment.getPaymentAmount() != null ? 
            appointment.getPaymentAmount() : appointment.getconsultationFee();
        
        billingDetails.getChildren().addAll(
            billingTitle,
            new Label("Consultation Fee: " + String.format("%.2f", appointment.getconsultationFee())),
            new Label("Amount: " + String.format("%.2f", amount)),
            new Label("Payment Method: " + (appointment.getPaymentMethod() != null ? appointment.getPaymentMethod() : "N/A")),
            new Label("Payment Status: " + (appointment.getPaymentStatus() != null ? appointment.getPaymentStatus() : "UNPAID")),
            new Label("Payment Date: " + (appointment.getPaymentDate() != null ? 
                appointment.getPaymentDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "N/A"))
        );
        
        // Payment section
        HBox paymentButtons = new HBox(10);
        paymentButtons.setAlignment(Pos.CENTER);
        
        Button processPaymentBtn = new Button("Process Payment");
        processPaymentBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-weight: bold;");
        processPaymentBtn.setOnAction(e -> {
            billingStage.close();
            showProcessPaymentDialog(appointment);
        });
        
        // Disable if already paid
        if ("PAID".equals(appointment.getPaymentStatus()) || "COMPLETED".equals(appointment.getPaymentStatus())) {
            processPaymentBtn.setDisable(true);
            processPaymentBtn.setText("Payment Completed");
        }
        
        Button closeBtn = new Button("Close");
        closeBtn.setOnAction(e -> billingStage.close());
        
        paymentButtons.getChildren().addAll(processPaymentBtn, closeBtn);
        
        content.getChildren().addAll(patientInfo, appointmentInfo, billingDetails, paymentButtons);
        
        Scene scene = new Scene(content, 450, 400);
        billingStage.setScene(scene);
        billingStage.showAndWait();
    }
    
    private void showProcessPaymentDialog(AppointmentDto appointment) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Mark Payment as Completed");
        confirmAlert.setHeaderText("Payment for " + appointment.getpatientFirstName() + " " + appointment.getpatientLastName());
        
        StringBuilder content = new StringBuilder();
        content.append("Consultation Fee: ").append(appointment.getconsultationFee()).append("\n");
        content.append("Current Payment Status: ").append(appointment.getPaymentStatus() != null ? appointment.getPaymentStatus() : "UNPAID").append("\n\n");
        content.append("Mark this payment as completed?");
        
        confirmAlert.setContentText(content.toString());
        
        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                statusLabel.setText("Processing payment...");
                
                // Update local appointment object to show as paid and completed
                appointment.setPaymentAmount(appointment.getconsultationFee());
                appointment.setPaymentMethod("CASH"); // Default method
                appointment.setPaymentStatus("PAID");
                appointment.setPaymentDate(LocalDate.now());
                appointment.setStatus("COMPLETED"); // Mark appointment as completed
                
                // Update payment and status in database using the API service
                boolean success = apiService.processPayment(
                    appointment.getId(),
                    appointment.getconsultationFee().doubleValue(),
                    "CASH", // Default payment method
                    "PAID",
                    LocalDate.now().toString()
                );
                
                if (success) {
                    appointmentTable.refresh();
                    statusLabel.setText("Payment completed and appointment marked as finished");
                    showInfoAlert("Payment Completed", 
                        "Payment has been processed and the appointment is now completed.");
                } else {
                    statusLabel.setText("Payment update failed");
                    showErrorAlert("Payment Error", "Failed to update payment in database.");
                }
            } catch (Exception e) {
                statusLabel.setText("Payment update error");
                showErrorAlert("Payment Error", "An error occurred while updating payment: " + e.getMessage());
            }
        }
    }
    
    private void showAppointmentDetails(AppointmentDto appointment) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Appointment Details");
        alert.setHeaderText(null);
        
        StringBuilder details = new StringBuilder();
        details.append("Patient: ").append(appointment.getpatientFirstName()).append(" ").append(appointment.getpatientLastName()).append("\n");
        details.append("Doctor: ").append(appointment.getdoctorFirstName()).append(" ").append(appointment.getdoctorLastName()).append("\n");
        details.append("Date: ").append(appointment.getDate()).append("\n");
        details.append("Time: ").append(appointment.getTime()).append("\n");
        details.append("Status: ").append(appointment.getStatus()).append("\n");
        details.append("Consultation Fee: ").append(appointment.getconsultationFee()).append("\n");
        details.append("Payment Status: ").append(appointment.getPaymentStatus() != null ? appointment.getPaymentStatus() : "UNPAID").append("\n");
        
        if (appointment.getPaymentAmount() != null) {
            details.append("Payment Amount: ").append(appointment.getPaymentAmount()).append("\n");
        }
        if (appointment.getPaymentMethod() != null) {
            details.append("Payment Method: ").append(appointment.getPaymentMethod()).append("\n");
        }
        if (appointment.getPaymentDate() != null) {
            details.append("Payment Date: ").append(appointment.getPaymentDate()).append("\n");
        }
        
        alert.setContentText(details.toString());
        alert.showAndWait();
    }
    
    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showWarningAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}