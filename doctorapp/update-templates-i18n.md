# Template Internationalization Update Guide

This guide provides step-by-step instructions for updating all remaining templates with internationalization support.

## Templates Already Updated
- ✅ index.html
- ✅ login.html  
- ✅ register.html
- ✅ doctorregister.html
- ✅ patient-dashboard.html (partially)

## Templates to Update

### 1. Dashboard Templates

#### doctor-dashboard.html
**Location**: `src/main/resources/templates/doctor-dashboard.html`

**Updates needed**:
```html
<!-- Title -->
<title th:text="#{doctor.dashboard.title}">Doctor Dashboard - Healthcare Portal</title>

<!-- Add language selector to navbar -->
<div class="navbar-nav ms-auto">
    <li class="nav-item me-3">
        <div th:replace="~{fragments/language-selector :: language-selector}"></div>
    </li>
    <!-- existing nav items -->
</div>

<!-- Welcome message -->
<h2 th:text="#{doctor.dashboard.welcome}">Welcome to your Doctor Dashboard</h2>

<!-- Section titles -->
<h5 th:text="#{doctor.dashboard.today.appointments}">Today's Appointments</h5>
<h5 th:text="#{doctor.dashboard.pending.appointments}">Pending Appointments</h5>
<h5 th:text="#{doctor.dashboard.total.patients}">Total Patients</h5>
<h5 th:text="#{doctor.dashboard.recent.activities}">Recent Activities</h5>
```

### 2. Appointment Templates

#### appointment.html
**Location**: `src/main/resources/templates/appointment.html`

**Updates needed**:
```html
<!-- Title -->
<title th:text="#{appointment.title}">Appointments - Healthcare Portal</title>

<!-- Add language selector -->
<div style="position: absolute; top: 20px; right: 20px; z-index: 1000;">
    <div th:replace="~{fragments/language-selector :: language-selector}"></div>
</div>

<!-- Form labels -->
<label th:text="#{appointment.date}">Appointment Date</label>
<label th:text="#{appointment.time}">Appointment Time</label>
<label th:text="#{appointment.doctor}">Doctor</label>
<label th:text="#{appointment.notes}">Notes</label>

<!-- Buttons -->
<button th:text="#{appointment.confirm}">Confirm Appointment</button>
<button th:text="#{appointment.cancel}">Cancel Appointment</button>
<button th:text="#{appointment.reschedule}">Reschedule</button>
```

#### myappointments.html
**Location**: `src/main/resources/templates/myappointments.html`

**Updates needed**:
```html
<!-- Title -->
<title th:text="#{appointment.title}">My Appointments - Healthcare Portal</title>

<!-- Add language selector to navbar -->
<div class="navbar-nav ms-auto">
    <li class="nav-item me-3">
        <div th:replace="~{fragments/language-selector :: language-selector}"></div>
    </li>
</div>

<!-- Status badges -->
<span class="badge" th:text="#{appointment.completed}">Completed</span>
<span class="badge" th:text="#{appointment.pending}">Pending</span>
<span class="badge" th:text="#{appointment.cancelled}">Cancelled</span>
```

#### scheduleappointment.html
**Location**: `src/main/resources/templates/scheduleappointment.html`

**Updates needed**:
```html
<!-- Title -->
<title th:text="#{appointment.schedule}">Schedule Appointment - Healthcare Portal</title>

<!-- Add language selector -->
<div style="position: absolute; top: 20px; right: 20px; z-index: 1000;">
    <div th:replace="~{fragments/language-selector :: language-selector}"></div>
</div>

<!-- Form labels -->
<label th:text="#{appointment.date}">Appointment Date</label>
<label th:text="#{appointment.time}">Appointment Time</label>
<label th:text="#{appointment.doctor}">Doctor</label>
<label th:text="#{appointment.notes}">Notes</label>

<!-- Submit button -->
<button th:text="#{appointment.book}">Book Appointment</button>
```

### 3. Medical Records Templates

#### medical-records.html
**Location**: `src/main/resources/templates/medical-records.html`

**Updates needed**:
```html
<!-- Title -->
<title th:text="#{medical.records.title}">Medical Records - Healthcare Portal</title>

<!-- Add language selector to navbar -->
<div class="navbar-nav ms-auto">
    <li class="nav-item me-3">
        <div th:replace="~{fragments/language-selector :: language-selector}"></div>
    </li>
</div>

<!-- Section titles -->
<h5 th:text="#{medical.records.patient.info}">Patient Information</h5>
<h5 th:text="#{medical.records.medical.history}">Medical History</h5>
<h5 th:text="#{medical.records.allergies}">Allergies</h5>
<h5 th:text="#{medical.records.medications}">Current Medications</h5>
<h5 th:text="#{medical.records.vital.signs}">Vital Signs</h5>
<h5 th:text="#{medical.records.lab.results}">Lab Results</h5>
<h5 th:text="#{medical.records.imaging}">Imaging Results</h5>

<!-- Buttons -->
<button th:text="#{medical.records.add.record}">Add Medical Record</button>
<button th:text="#{medical.records.edit.record}">Edit Medical Record</button>
```

### 4. Prescription Templates

#### prescriptions.html
**Location**: `src/main/resources/templates/prescriptions.html`

**Updates needed**:
```html
<!-- Title -->
<title th:text="#{prescriptions.title}">Prescriptions - Healthcare Portal</title>

<!-- Add language selector to navbar -->
<div class="navbar-nav ms-auto">
    <li class="nav-item me-3">
        <div th:replace="~{fragments/language-selector :: language-selector}"></div>
    </li>
</div>

<!-- Form labels -->
<label th:text="#{prescriptions.medication}">Medication</label>
<label th:text="#{prescriptions.dosage}">Dosage</label>
<label th:text="#{prescriptions.frequency}">Frequency</label>
<label th:text="#{prescriptions.duration}">Duration</label>
<label th:text="#{prescriptions.instructions}">Instructions</label>
<label th:text="#{prescriptions.prescribed.by}">Prescribed By</label>
<label th:text="#{prescriptions.prescribed.date}">Prescribed Date</label>
<label th:text="#{prescriptions.expiry.date}">Expiry Date</label>

<!-- Buttons -->
<button th:text="#{prescriptions.add}">Add Prescription</button>
<button th:text="#{prescriptions.edit}">Edit Prescription</button>
<button th:text="#{prescriptions.refill}">Refill Prescription</button>
```

#### add-prescription.html
**Location**: `src/main/resources/templates/add-prescription.html`

**Updates needed**:
```html
<!-- Title -->
<title th:text="#{prescriptions.add}">Add Prescription - Healthcare Portal</title>

<!-- Add language selector -->
<div style="position: absolute; top: 20px; right: 20px; z-index: 1000;">
    <div th:replace="~{fragments/language-selector :: language-selector}"></div>
</div>

<!-- Form labels (same as prescriptions.html) -->
<label th:text="#{prescriptions.medication}">Medication</label>
<label th:text="#{prescriptions.dosage}">Dosage</label>
<!-- ... other labels -->

<!-- Submit button -->
<button th:text="#{common.save}">Save</button>
```

#### doctor-prescriptions.html
**Location**: `src/main/resources/templates/doctor-prescriptions.html`

**Updates needed**:
```html
<!-- Title -->
<title th:text="#{prescriptions.title}">Doctor Prescriptions - Healthcare Portal</title>

<!-- Add language selector to navbar -->
<div class="navbar-nav ms-auto">
    <li class="nav-item me-3">
        <div th:replace="~{fragments/language-selector :: language-selector}"></div>
    </li>
</div>

<!-- Same updates as prescriptions.html -->
```

### 5. Billing Templates

#### billing.html
**Location**: `src/main/resources/templates/billing.html`

**Updates needed**:
```html
<!-- Title -->
<title th:text="#{billing.title}">Billing - Healthcare Portal</title>

<!-- Add language selector to navbar -->
<div class="navbar-nav ms-auto">
    <li class="nav-item me-3">
        <div th:replace="~{fragments/language-selector :: language-selector}"></div>
    </li>
</div>

<!-- Table headers -->
<th th:text="#{billing.invoice}">Invoice</th>
<th th:text="#{billing.amount}">Amount</th>
<th th:text="#{billing.date}">Date</th>
<th th:text="#{billing.status}">Status</th>

<!-- Status badges -->
<span th:text="#{billing.paid}">Paid</span>
<span th:text="#{billing.pending}">Pending</span>
<span th:text="#{billing.overdue}">Overdue</span>

<!-- Buttons -->
<button th:text="#{billing.generate.invoice}">Generate Invoice</button>
<button th:text="#{billing.process.payment}">Process Payment</button>
```

#### doctor-billing.html
**Location**: `src/main/resources/templates/doctor-billing.html`

**Updates needed**:
```html
<!-- Title -->
<title th:text="#{billing.title}">Doctor Billing - Healthcare Portal</title>

<!-- Add language selector to navbar -->
<div class="navbar-nav ms-auto">
    <li class="nav-item me-3">
        <div th:replace="~{fragments/language-selector :: language-selector}"></div>
    </li>
</div>

<!-- Same updates as billing.html -->
```

### 6. Analytics Templates

#### analytics.html
**Location**: `src/main/resources/templates/analytics.html`

**Updates needed**:
```html
<!-- Title -->
<title th:text="#{analytics.title}">Analytics - Healthcare Portal</title>

<!-- Add language selector to navbar -->
<div class="navbar-nav ms-auto">
    <li class="nav-item me-3">
        <div th:replace="~{fragments/language-selector :: language-selector}"></div>
    </li>
</div>

<!-- Section titles -->
<h5 th:text="#{analytics.overview}">Overview</h5>
<h5 th:text="#{analytics.patient.statistics}">Patient Statistics</h5>
<h5 th:text="#{analytics.appointment.statistics}">Appointment Statistics</h5>
<h5 th:text="#{analytics.revenue.statistics}">Revenue Statistics</h5>
<h5 th:text="#{analytics.monthly.report}">Monthly Report</h5>

<!-- Buttons -->
<button th:text="#{analytics.export.data}">Export Data</button>
```

### 7. Health Metrics Templates

#### health-metrics.html
**Location**: `src/main/resources/templates/health-metrics.html`

**Updates needed**:
```html
<!-- Title -->
<title th:text="#{health.metrics.title}">Health Metrics - Healthcare Portal</title>

<!-- Add language selector to navbar -->
<div class="navbar-nav ms-auto">
    <li class="nav-item me-3">
        <div th:replace="~{fragments/language-selector :: language-selector}"></div>
    </li>
</div>

<!-- Metric labels -->
<label th:text="#{health.metrics.blood.pressure}">Blood Pressure</label>
<label th:text="#{health.metrics.heart.rate}">Heart Rate</label>
<label th:text="#{health.metrics.temperature}">Temperature</label>
<label th:text="#{health.metrics.weight}">Weight</label>
<label th:text="#{health.metrics.height}">Height</label>
<label th:text="#{health.metrics.bmi}">BMI</label>

<!-- Buttons -->
<button th:text="#{health.metrics.add.metric}">Add Health Metric</button>
<button th:text="#{health.metrics.track.progress}">Track Progress</button>
```

### 8. Messaging Templates

#### messaging.html
**Location**: `src/main/resources/templates/messaging.html`

**Updates needed**:
```html
<!-- Title -->
<title th:text="#{messaging.title}">Messaging - Healthcare Portal</title>

<!-- Add language selector to navbar -->
<div class="navbar-nav ms-auto">
    <li class="nav-item me-3">
        <div th:replace="~{fragments/language-selector :: language-selector}"></div>
    </li>
</div>

<!-- Navigation -->
<a th:text="#{messaging.inbox}">Inbox</a>
<a th:text="#{messaging.sent}">Sent</a>

<!-- Form labels -->
<label th:text="#{messaging.subject}">Subject</label>
<label th:text="#{messaging.message}">Message</label>

<!-- Buttons -->
<button th:text="#{messaging.send}">Send Message</button>
<button th:text="#{messaging.reply}">Reply</button>
<button th:text="#{messaging.delete}">Delete Message</button>
<button th:text="#{messaging.mark.read}">Mark as Read</button>
<button th:text="#{messaging.mark.unread}">Mark as Unread</button>
```

### 9. Patient Management Templates

#### patient-management.html
**Location**: `src/main/resources/templates/patient-management.html`

**Updates needed**:
```html
<!-- Title -->
<title th:text="#{patient.management.title}">Patient Management - Healthcare Portal</title>

<!-- Add language selector to navbar -->
<div class="navbar-nav ms-auto">
    <li class="nav-item me-3">
        <div th:replace="~{fragments/language-selector :: language-selector}"></div>
    </li>
</div>

<!-- Buttons -->
<button th:text="#{patient.management.add.patient}">Add Patient</button>
<button th:text="#{patient.management.edit.patient}">Edit Patient</button>
<button th:text="#{patient.management.delete.patient}">Delete Patient</button>
<button th:text="#{patient.management.search.patient}">Search Patient</button>

<!-- Section titles -->
<h5 th:text="#{patient.management.patient.list}">Patient List</h5>
<h5 th:text="#{patient.management.contact.info}">Contact Information</h5>
<h5 th:text="#{patient.management.emergency.contact}">Emergency Contact</h5>
```

#### patient-profile.html
**Location**: `src/main/resources/templates/patient-profile.html`

**Updates needed**:
```html
<!-- Title -->
<title th:text="#{patient.profile.title}">Patient Profile - Healthcare Portal</title>

<!-- Add language selector to navbar -->
<div class="navbar-nav ms-auto">
    <li class="nav-item me-3">
        <div th:replace="~{fragments/language-selector :: language-selector}"></div>
    </li>
</div>

<!-- Section titles -->
<h5 th:text="#{patient.profile.personal.info}">Personal Information</h5>
<h5 th:text="#{patient.profile.contact.info}">Contact Information</h5>
<h5 th:text="#{patient.profile.emergency.contact}">Emergency Contact</h5>
<h5 th:text="#{patient.profile.insurance.info}">Insurance Information</h5>
<h5 th:text="#{patient.profile.medical.history}">Medical History</h5>

<!-- Buttons -->
<button th:text="#{patient.profile.update.profile}">Update Profile</button>
<button th:text="#{patient.profile.save.changes}">Save Changes</button>
```

### 10. Doctor Appointments Template

#### doctor-appointments.html
**Location**: `src/main/resources/templates/doctor-appointments.html`

**Updates needed**:
```html
<!-- Title -->
<title th:text="#{appointment.title}">Doctor Appointments - Healthcare Portal</title>

<!-- Add language selector to navbar -->
<div class="navbar-nav ms-auto">
    <li class="nav-item me-3">
        <div th:replace="~{fragments/language-selector :: language-selector}"></div>
    </li>
</div>

<!-- Same updates as appointment.html -->
```

## Common Updates for All Templates

### 1. Add Language Selector
For templates with navbar:
```html
<div class="navbar-nav ms-auto">
    <li class="nav-item me-3">
        <div th:replace="~{fragments/language-selector :: language-selector}"></div>
    </li>
    <!-- existing nav items -->
</div>
```

For templates without navbar:
```html
<div style="position: absolute; top: 20px; right: 20px; z-index: 1000;">
    <div th:replace="~{fragments/language-selector :: language-selector}"></div>
</div>
```

### 2. Update Common Elements
```html
<!-- Back buttons -->
<a href="/" class="back-btn" th:text="#{common.back}">← Back to Home</a>

<!-- Submit buttons -->
<button type="submit" th:text="#{common.save}">Save</button>
<button type="submit" th:text="#{common.submit}">Submit</button>

<!-- Action buttons -->
<button th:text="#{common.edit}">Edit</button>
<button th:text="#{common.delete}">Delete</button>
<button th:text="#{common.view}">View</button>
<button th:text="#{common.add}">Add</button>

<!-- Search and filter -->
<input th:placeholder="#{common.search}" placeholder="Search..." />
<button th:text="#{common.filter}">Filter</button>
<button th:text="#{common.clear}">Clear</button>
```

### 3. Fix Backdrop Filter Issues
For all templates with backdrop-filter CSS:
```css
-webkit-backdrop-filter: blur(10px);
backdrop-filter: blur(10px);
```

## Testing Checklist

After updating each template:

1. ✅ Check that the language selector appears
2. ✅ Test language switching (English, French, Arabic)
3. ✅ Verify RTL layout for Arabic
4. ✅ Check that all text is properly translated
5. ✅ Test form submissions and navigation
6. ✅ Verify responsive design in all languages

## Notes

- All message keys are already defined in the properties files
- The language selector fragment is reusable across all templates
- RTL CSS is automatically loaded for Arabic
- UTF-8 encoding is already configured
- Session-based locale resolution is implemented

This guide covers all the major templates in the application. Follow the same pattern for any additional templates that may be added in the future.
