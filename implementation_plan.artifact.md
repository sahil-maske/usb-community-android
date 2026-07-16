# Implementation Plan - Remove Redundant Bottom Navigation Bar

The application currently has two bottom navigation bars appearing on certain screens (like Member Home and Vendor Home). One is a standard Material 3 `NavigationBar` provided by `ScreenWithBottomNav` in `NavGraph.kt`, and the other is a custom floating `ReusableBottomNav` provided by `MainActivity.kt`. The user wants to keep the custom one and remove the redundant one.

## User Review Required

> [!IMPORTANT]
> I am removing the `ScreenWithBottomNav` wrapper from `NavGraph.kt`. This will eliminate the double bottom bar issue. The custom floating bottom bar in `MainActivity` will remain as the primary navigation.

## Proposed Changes

### Navigation

#### [MODIFY] [NavGraph.kt](file:///D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/navigation/NavGraph.kt)
- Remove `ScreenWithBottomNav` wrapper from `member_home`, `vendor_home`, and `MemberRoutes.PROFILE` routes.
- Remove unused imports related to `ScreenWithBottomNav`, `memberNavItems`, and `vendorNavItems`.

### UI Components

#### [MODIFY] [BottomNavComponents.kt](file:///D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/ui/home/common/BottomNavComponents.kt)
- Remove the `ScreenWithBottomNav` composable as it is no longer used and was causing the double bottom bar issue.
- Keep `NavItem`, `memberNavItems`, and `vendorNavItems` for now in case they are needed for the "real" bottom bar later.

## Verification Plan

### Automated Tests
- Build the project to ensure no compilation errors after removing the wrapper.

### Manual Verification
- Navigate to the Member Home and Vendor Home screens.
- Verify that only one bottom navigation bar ( the custom floating one) is visible.
- Verify that navigation still works correctly between Home and Profile.
