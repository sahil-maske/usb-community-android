# Walkthrough: Robust Auth & OEM Battery Optimization

I have implemented a multi-layered solution to solve the Firebase Auth session persistence issue on OEM devices like Vivo.

## 1. Robust Auth Initialization

### [SplashScreen.kt](file:///D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/ui/auth/SplashScreen.kt)
- Replaced the immediate `currentUser` check with an `AuthStateListener` and a 3-second timeout.
- This ensures the app waits for the Firebase SDK to restore the session from its disk cache after a process kill.

## 2. Battery Optimization & Whitelisting Guide

### [AndroidManifest.xml](file:///D:/USBDigitalCommunityPlatform/app/src/main/AndroidManifest.xml)
- Added `REQUEST_IGNORE_BATTERY_OPTIMIZATIONS` permission.

### [BatteryOptimizationUtils.kt](file:///D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/utils/BatteryOptimizationUtils.kt)
- Created utility to detect Vivo devices and generate intents for OEM-specific settings like **Autostart** and **High Background Power Management**.

### [BatteryOptimizationGuideScreen.kt](file:///D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/ui/settings/BatteryOptimizationGuideScreen.kt)
- A professional, iOS-styled guide screen for Vivo users.
- Provides direct links to the exact system settings pages required to whitelist the app.

### Home Screen Integration
- Added a warning banner to both [MemberHomeScreen.kt](file:///D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/ui/home/member/MemberHomeScreen.kt) and [VendorHomeScreen.kt](file:///D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/ui/home/vendor/VendorHomeScreen.kt).
- The banner only appears for Vivo users who haven't yet whitelisted the app.

## Verification Results

### Automated Tests
- Build verification: `:app:compileDebugKotlin` - **SUCCESS**.

### Manual Verification Steps (For User)
1. **Relaunch App**: After deploying, you should see a yellow banner on your Home screen if you're on a Vivo device.
2. **Follow Guide**: Tap the banner to open the guide.
3. **Execute Steps**:
    - Tap **Request Exemption** (Standard Android).
    - Tap **Open Autostart Settings** and enable it for USB Community.
    - Tap **Open Power Management** and select "Allow high background power consumption".
4. **Final Test**: Force stop the app and relaunch. The session should now persist perfectly.

> [!IMPORTANT]
> Vivo's "Smart Control" is very aggressive. The combination of code-level `AuthStateListener` and system-level whitelisting is the industry standard for ensuring persistence on these devices.
