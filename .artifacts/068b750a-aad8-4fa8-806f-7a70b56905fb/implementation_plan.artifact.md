# Implementation Plan: Remove Vivo Device Detection & Battery Optimization Guide

The user has requested to completely remove the Vivo-specific detection and the battery optimization guidance UI added in the previous iteration.

## Proposed Changes

### [Cleanup]

#### [DELETE] [BatteryOptimizationUtils.kt](file:///D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/utils/BatteryOptimizationUtils.kt)
- Remove the utility class used for device detection and settings intents.

#### [DELETE] [BatteryOptimizationGuideScreen.kt](file:///D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/ui/settings/BatteryOptimizationGuideScreen.kt)
- Remove the guide screen UI.

### [UI & Navigation]

#### [MODIFY] [NavGraph.kt](file:///D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/navigation/NavGraph.kt)
- Remove the `battery_optimization_guide` route and the import for `BatteryOptimizationGuideScreen`.

#### [MODIFY] [MemberHomeScreen.kt](file:///D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/ui/home/member/MemberHomeScreen.kt)
- Remove the `showOptimizationBanner` logic, the banner UI, and Vivo-related imports/state.

#### [MODIFY] [VendorHomeScreen.kt](file:///D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/ui/home/vendor/VendorHomeScreen.kt)
- Remove the `showOptimizationBanner` logic, the banner UI, and Vivo-related imports/state.

### [Project Configuration]

#### [MODIFY] [AndroidManifest.xml](file:///D:/USBDigitalCommunityPlatform/app/src/main/AndroidManifest.xml)
- Remove the `REQUEST_IGNORE_BATTERY_OPTIMIZATIONS` permission.

## Verification Plan

### Automated Tests
- Run `:app:compileDebugKotlin` to ensure all references to the deleted files are removed.

### Manual Verification
- Verify that the home screens (Member and Vendor) no longer show any banners or warnings related to Vivo devices.
