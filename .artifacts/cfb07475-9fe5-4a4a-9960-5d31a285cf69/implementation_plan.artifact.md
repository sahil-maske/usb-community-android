# Implementation Plan: Fix Build Error by Completing Feature Removal

The build error in `MemberHomeScreen.kt` is caused by a missing `BatteryOptimizationUtils.kt` file. This file was previously partially deleted as part of a request to remove Vivo-specific battery optimization features. To fix the build error, we will complete the removal of all references to this feature.

## User Review Required

> [!IMPORTANT]
> This plan assumes that the intended action is to **remove** the Vivo battery optimization feature, as indicated by previous incomplete cleanup efforts found in the project's artifacts.

## Proposed Changes

### UI Components

#### [MODIFY] [MemberHomeScreen.kt](file:///D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/ui/home/member/MemberHomeScreen.kt)
- Remove `isVivo`, `isOptimized`, and `showOptimizationBanner` state variables.
- Remove the `Surface` block that displays the "Vivo Device Detected" warning banner.

#### [MODIFY] [VendorHomeScreen.kt](file:///D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/ui/home/vendor/VendorHomeScreen.kt)
- Remove `isVivo`, `isOptimized`, and `showOptimizationBanner` state variables.
- Remove the `Surface` block that displays the "Vivo Device Detected" warning banner.

#### [DELETE] [BatteryOptimizationGuideScreen.kt](file:///D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/ui/settings/BatteryOptimizationGuideScreen.kt)
- Delete the guide screen file which is no longer needed.

### Navigation

#### [MODIFY] [NavGraph.kt](file:///D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/navigation/NavGraph.kt)
- Remove the `battery_optimization_guide` route.
- Remove the import for `BatteryOptimizationGuideScreen`.

### Manifest

#### [MODIFY] [AndroidManifest.xml](file:///D:/USBDigitalCommunityPlatform/app/src/main/AndroidManifest.xml)
- Remove the `REQUEST_IGNORE_BATTERY_OPTIMIZATIONS` permission.

## Verification Plan

### Automated Tests
- Run `./gradlew :app:compileDebugKotlin` to verify the project builds without errors.

### Manual Verification
- Verify that the Home screens (Member and Vendor) no longer attempt to show a warning banner.
