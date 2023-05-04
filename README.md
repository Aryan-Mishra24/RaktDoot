# Raktdoot App

![App Icon](https://link-to-your-app-icon-if-any)

## Overview
Raktdoot is an innovative Android application aimed at connecting blood donors and recipients efficiently. Developed as part of a project at CSVTU, Bhilai, this application is an attempt to leverage technology for social good.

## Features
1. **User Registration and Login:** Users can create a new account using their email and can log in to access the app features.

2. **Donor & Recipient Registration:** Users can register as a blood donor or a recipient with necessary details like blood group, location, etc.

3. **Search Donors:** Recipients can search for available blood donors based on their blood group and location.

4. **Location Services:** The app integrates Google Maps API to provide accurate location services for both donors and recipients.

5. **Request for Blood:** Recipients can send requests to donors directly through the app.

6. **Notification:** The app sends notifications to donors when a new request is made.

## Installation
The app can be installed directly from the Google Play Store. Ensure your Android device is running Android 5.0 (Lollipop) or above.

## Permissions
The app requires the following permissions:
- Internet Access
- Access to Location Services

.

## Project Structure
The project follows the standard Android project structure. It is developed in Java and uses XML for layout designing.

- `MainActivity.java`: This is the main activity of the app where users can register or log in.
- `DonorRegistrationActivity.java`: Handles the registration of blood donors.
- `RecipientRegistrationActivity.java`: Handles the registration of blood recipients.
- `SearchActivity.java`: Enables recipients to search for blood donors.
- `MapActivityX.java`: Handles the integration of Google Maps for location services.
- Here are a couple of suggestions to improve your code:

Error Handling: While you have included some error handling (such as checking if the GPS is enabled), you should also include error handling for Firestore operations and user input validation.

Code Organization: You could further modularize your code for better readability and maintainability. For example, extracting Firebase operations into a separate class or method would make the code cleaner.

Data Validation: You're directly using the user's input to create a new user. You should add checks to validate the input data before using it.

Location Updates: Be careful when dealing with location updates as it can drain the battery quickly. Consider the user's need for location accuracy versus battery life.

Permissions: Always check for necessary permissions before performing operations that require them. In your register class, you have commented out the check for ACCESS_COARSE_LOCATION permission. It's better to check for all necessary permissions at the start of your app.

Avoid Blocking UI: Long-running operations (like network requests) should be performed on a separate thread to avoid blocking the UI. Make sure your Firebase operations aren't blocking the UI thread

## Contributing
We welcome contributions to the Raktdoot app. Please feel free to fork the repository, make changes and create a pull request. If you find any bugs or have suggestions for improvements, please open an issue.

## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details. 

## Contact
For any queries or suggestions, please contact us at [raktdoot@csvtu.ac.in](mailto:raktdoot@csvtu.ac.in)
