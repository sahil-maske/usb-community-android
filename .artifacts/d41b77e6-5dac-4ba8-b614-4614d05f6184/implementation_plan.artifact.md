# Fix Unresolved Reference 'CAScreen' and Navigation Sync

The project is currently failing to build because `CAScreen` and `LegalHomeScreen` are referenced in `NavGraph.kt` but are either not implemented or imported from incorrect locations. Additionally, the route constants used in `NavGraph.kt` do not match those defined in `MemberHomeScreen.kt`.

## User Review Required

> [!IMPORTANT]
> I will be implementing placeholder "Coming Soon" versions of `CAScreen` and `LegalHomeScreen` to allow the project to build. You can later replace these with your actual business logic.

> [!NOTE]
> I will update the `MemberRoutes` constants to be consistent between `MemberHomeScreen.kt` and `NavGraph.kt`.

## Proposed Changes

### [Component] Navigation & Routes

#### [MODIFY] [MemberHomeScreen.kt](file:///D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/ui/home/member/MemberHomeScreen.kt)
- Update `MemberRoutes` to include `CA_FINANCE` and `LEGAL_HELP`.
- Update `quickActions` to use these new constants.

#### [MODIFY] [NavGraph.kt](file:///D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/navigation/NavGraph.kt)
- Fix imports for `CAScreen` and `LegalHomeScreen`.
- Ensure routes match the updated `MemberRoutes`.

### [Component] UI Screens

#### [MODIFY] [CAScreen.kt](file:///D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/ui/home/ca/CAScreen.kt)
- Implement a placeholder `CAScreen` Composable.

#### [NEW] [LegalHomeScreen.kt](file:///D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/ui/home/legal/LegalHomeScreen.kt)
- Create this file and implement a placeholder `LegalHomeScreen` Composable.

#### [DELETE] [LegalHelpScreen.kt](file:///D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/ui/home/legal/LegalHelpScreen.kt)
- Remove this empty file if it's redundant.

## Verification Plan

### Automated Tests
- Run `./gradlew :app:assembleDebug` to verify that the project builds without unresolved references.

### Manual Verification
- Deploy the app to a device/emulator.
- Navigate to the Member Home screen.
- Tap on "Legal Help" and "CA / Finance" to ensure they navigate to the new screens.
