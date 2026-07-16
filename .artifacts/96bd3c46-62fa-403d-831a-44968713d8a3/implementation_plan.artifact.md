# Fix Unresolved Reference 'ScreenWithBottomNav'

The project is failing to build because `ScreenWithBottomNav`, `memberNavItems`, and `vendorNavItems` are missing. These are used in `NavGraph.kt` to provide a consistent bottom navigation experience for Member and Vendor roles.

## User Review Required

> [!IMPORTANT]
> I will be creating a new file `BottomNavComponents.kt` in `com.dev.usbdigitalcommunityplatform.ui.home.common` to define these missing components. This will include the `ScreenWithBottomNav` wrapper, the navigation item data structure, and the functions to provide role-specific items.

## Proposed Changes

### [Component Name]

#### [NEW] [BottomNavComponents.kt](file:///D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/ui/home/common/BottomNavComponents.kt)
- Define `NavItem` data class.
- Implement `memberNavItems()` and `vendorNavItems()` functions.
- Implement `ScreenWithBottomNav` Composable using `Scaffold` and `NavigationBar`.

## Verification Plan

### Automated Tests
- Run `./gradlew :app:compileDebugKotlin` to ensure the project builds successfully.

### Manual Verification
- Once built, the user can run the app to verify that the bottom navigation appears on the Home and Profile screens for both Member and Vendor roles.
