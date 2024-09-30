# Project Setup Instructions

After cloning the project, follow these steps to ensure compatibility with your version of Android Studio:

## Step 1: Update the `libs.versions.toml` file

1. Navigate to the `libs.versions.toml` file located in the **Gradle Scripts** section.
2. Under the `[versions]` section, locate the line for `agp` (Android Gradle Plugin).
3. Modify the version of AGP to match the version supported by your Android Studio.

   For example:
   ```toml
   [versions]
   agp = "8.1.0"  # Change to the version supported by your Android Studio
   ```

4. **Save the file**.

## Step 2: Sync Gradle

1. After updating the AGP version, click on the **Sync Now** button in Android Studio to synchronize the project with the new Gradle configuration.

## Notes:
- **Warnings**: After updating the AGP version, you may still see some **warnings** during the build process. However, these warnings are expected and will not prevent the project from building successfully.
- Ensure the AGP version aligns with the version of Android Studio you're using. You can find the compatible versions in the official [Android Studio & AGP compatibility chart](https://developer.android.com/studio/releases/gradle-plugin).
- If there are other dependency versions that need updating, modify them in the same `libs.versions.toml` file.
