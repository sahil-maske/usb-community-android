# Walkthrough - Fixing Unresolved Reference 'ScreenWithBottomNav'

I have fixed the build error `Unresolved reference 'ScreenWithBottomNav'` by implementing the missing navigation components that were being used in `NavGraph.kt`.

## Changes Made

### UI Components

#### [BottomNavComponents.kt](file:///D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/ui/home/common/BottomNavComponents.kt)
- Created a new file to house shared navigation components.
- Added `NavItem` data class to represent bottom navigation links.
- Implemented `memberNavItems()` and `vendorNavItems()` to provide role-specific navigation menus.
- Implemented `ScreenWithBottomNav`, a reusable wrapper that adds a `NavigationBar` to any screen.

## Verification Results

### Automated Tests
- Executed `./gradlew :app:compileDebugKotlin`
- **Result**: Build finished successfully.

### Manual Verification
The following screens now have their bottom navigation bars correctly configured in the `NavHost`:
- **Member Home**
- **Vendor Home**
- **Member Profile**

The navigation logic in `NavGraph.kt` is now fully operational with the proper imports and definitions.
