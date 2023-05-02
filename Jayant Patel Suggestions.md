These tasks involve a few different aspects of Android development. Here are some general suggestions on how you might approach them:

Fix layouts for multiple devices:

Use ConstraintLayout: ConstraintLayout is a flexible layout manager for your app that allows you to create dynamic and scalable user interfaces for Android. It's designed to adjust to different screen sizes and resolutions, which makes it perfect for this task.
Use "wrap_content", "match_parent", or weights in LinearLayout: These properties allow your views to adjust to different screen sizes.
Use size qualifiers: You can create different layout files for different screen sizes by using size qualifiers (like small, normal, large, xlarge) on your layout resource files.
Test on different emulators: Android Studio allows you to create emulators with different screen sizes and resolutions, so you can test how your app looks on different devices.
Fix night mode colors:

You can define a separate theme for night mode in your styles.xml file. The system will automatically use this theme when the device is in night mode.
Use ?attr: references for colors in your layouts, and define those attributes in your themes. This way, you can define different colors for the same attribute in your day and night themes, and the system will use the correct one based on the current mode.
Check if the app is compatible to be uploaded to Play Store:

Follow Google Play's Developer Program Policies: Make sure your app adheres to all of Google Play's policies. These include content policies, privacy policies, and intellectual property policies.
Use the Android App Bundle (.aab) format: Starting from August 2021, new apps are required to publish with the Android App Bundle on Google Play. This new format allows Google Play to serve optimized APKs for each user’s device configuration, so they download only the code and resources they need to run your app.
Test your app: Make sure your app works correctly and doesn't have any major bugs or crashes.
Provide complete information: When you upload your app to the Play Store, you'll need to provide a lot of information, including an app title, a short and long description, a category, contact information, a privacy policy, and more.
