# Multi-Language Support Implementation

This document describes the multi-language (internationalization) implementation for the Doctor Appointment Application.

## Supported Languages

The application now supports three languages:

1. **English** (en) - Default language
2. **French** (fr) - Français
3. **Arabic** (ar) - العربية (with RTL support)

## Implementation Overview

### 1. Configuration Files

#### Locale Configuration
- **File**: `src/main/java/com/example/doctorapp/config/LocaleConfig.java`
- **Purpose**: Configures Spring Boot's internationalization support
- **Features**:
  - Session-based locale resolver
  - Language change interceptor
  - UTF-8 encoded message source

#### Message Properties Files
- **English**: `src/main/resources/messages.properties` (default)
- **French**: `src/main/resources/messages_fr.properties`
- **Arabic**: `src/main/resources/messages_ar.properties`

### 2. Language Selector Component

#### Location
- **File**: `src/main/resources/templates/fragments/language-selector.html`
- **Usage**: Reusable Thymeleaf fragment

#### Features
- Dropdown menu with language options
- Visual indication of current language
- Automatic RTL/LTR direction switching
- Mobile-responsive design

#### Integration
```html
<div th:replace="~{fragments/language-selector :: language-selector}"></div>
```

### 3. RTL Support for Arabic

#### CSS File
- **Location**: `src/main/resources/static/css/rtl.css`
- **Features**:
  - Complete RTL layout support
  - Form element alignment
  - Navigation and menu adjustments
  - Responsive design considerations

#### Automatic Loading
- RTL CSS is automatically loaded when Arabic is selected
- Automatically removed when switching to other languages

### 4. Controller Support

#### Language Controller
- **File**: `src/main/java/com/example/doctorapp/controller/LanguageController.java`
- **Endpoints**:
  - `GET /change-language?lang={language}` - Switch language
  - `GET /test-i18n` - Test page for verification

## Usage Instructions

### For Users

1. **Language Selection**: Click the language dropdown in the top-right corner
2. **Language Switching**: Select your preferred language from the dropdown
3. **RTL Support**: Arabic automatically enables right-to-left layout

### For Developers

#### Adding New Messages

1. **Add to English file** (`messages.properties`):
```properties
new.message.key=English text here
```

2. **Add to French file** (`messages_fr.properties`):
```properties
new.message.key=Texte français ici
```

3. **Add to Arabic file** (`messages_ar.properties`):
```properties
new.message.key=النص العربي هنا
```

#### Using Messages in Templates

```html
<!-- Simple text -->
<span th:text="#{message.key}">Default text</span>

<!-- With parameters -->
<span th:text="#{message.with.param(${variable})}">Default text</span>

<!-- In attributes -->
<input th:placeholder="#{input.placeholder}" />
```

#### Adding Language Selector to New Pages

```html
<!-- Include in the head or body -->
<div th:replace="~{fragments/language-selector :: language-selector}"></div>
```

## Testing

### Test Page
- **URL**: `/test-i18n`
- **Purpose**: Verify all messages are properly translated
- **Features**:
  - Shows current language information
  - Displays all message keys and values
  - Tests RTL layout for Arabic

### Manual Testing Steps

1. **Start the application**
2. **Navigate to the home page**
3. **Test language switching**:
   - Click the language dropdown
   - Select each language
   - Verify text changes
   - Check RTL layout for Arabic
4. **Test persistence**: Refresh the page to ensure language preference is maintained
5. **Test navigation**: Navigate between pages to ensure language preference persists

## Technical Details

### Locale Resolution
- **Method**: Session-based locale resolver
- **Parameter**: `lang` (e.g., `?lang=fr`)
- **Default**: English (`en`)

### Character Encoding
- **Encoding**: UTF-8 throughout the application
- **Support**: Full Unicode support for all languages

### Browser Compatibility
- **Modern browsers**: Full support
- **RTL**: Supported in all modern browsers
- **Fallback**: Graceful degradation for older browsers

## File Structure

```
src/main/
├── java/com/example/doctorapp/
│   ├── config/
│   │   └── LocaleConfig.java
│   └── controller/
│       └── LanguageController.java
├── resources/
│   ├── messages.properties
│   ├── messages_fr.properties
│   ├── messages_ar.properties
│   ├── static/css/
│   │   └── rtl.css
│   └── templates/
│       ├── fragments/
│       │   └── language-selector.html
│       ├── index.html
│       ├── login.html
│       └── test-i18n.html
```

## Best Practices

### Message Keys
- Use descriptive, hierarchical keys (e.g., `user.doctor.title`)
- Keep keys consistent across all language files
- Use lowercase with dots for separation

### Translation Guidelines
- Maintain consistent terminology across languages
- Consider cultural differences in phrasing
- Test with native speakers when possible

### RTL Considerations
- Always test Arabic layout thoroughly
- Ensure proper text alignment and flow
- Consider icon and image positioning

## Troubleshooting

### Common Issues

1. **Messages not displaying**:
   - Check message key spelling
   - Verify message exists in all language files
   - Check Thymeleaf syntax

2. **RTL not working**:
   - Verify RTL CSS is loading
   - Check browser console for errors
   - Ensure proper HTML structure

3. **Language not persisting**:
   - Check session configuration
   - Verify locale resolver setup
   - Test with different browsers

### Debug Information
- Use `/test-i18n` page to verify translations
- Check browser developer tools for CSS loading
- Monitor network requests for language changes

## Future Enhancements

### Potential Improvements
1. **Database-driven translations** for dynamic content
2. **User preference storage** in user profiles
3. **Automatic language detection** based on browser settings
4. **Translation management interface** for administrators
5. **Pluralization support** for different languages
6. **Date and number formatting** per locale

### Additional Languages
To add more languages:
1. Create new `messages_{language_code}.properties` file
2. Add language option to language selector
3. Update language controller if needed
4. Test thoroughly with native speakers
