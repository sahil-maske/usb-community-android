package com.dev.usbdigitalcommunityplatform.ui.localization


object TranslationManager {

    var currentLanguage = "en"

    private val translations = mapOf(

        "en" to mapOf(
            "select_language" to "Select Language",
            "continue" to "Continue",
            "welcome" to "Welcome",
            "Welcome Back" to "Welcome Back",
            "Please select your preferred language to continue" to "Please select your preferred language to continue",
            "Sign in with your mobile number to continue" to "Sign in with your mobile number to continue",
            "Mobile Number" to "Mobile Number",
            "Enter a valid mobile number" to "Enter a valid mobile number",
            "Enter a 10-digit number" to "Enter a 10-digit number",
            "Verify Phone Number" to "Verify Phone Number",
            "Enter the 6-digit code sent to your mobile number" to "Enter the 6-digit code sent to your mobile number",
            " ⚠ Invalid verification code" to "⚠ Invalid verification code",
            "Verify" to "Verify"
        ),

        "hi" to mapOf(
            "select_language" to "भाषा चुनें",
            "continue" to "जारी रखें",
            "welcome" to "स्वागत हे",
            "Welcome Back" to "आपका पुनः स्वागत है",
            "Please select your preferred language to continue" to "जारी रखने के लिए कृपया अपनी पसंदीदा भाषा चुनें.",
            "Sign in with your mobile number to continue" to "",
            "Mobile Number" to "मोबाइल नंबर",
            "Enter a valid mobile number" to "एक वैध मोबाइल नंबर दर्ज करें",
            "Activity not found" to "गतिविधि नहीं मिली",
            "Enter a 10-digit number" to "१० अंकों की संख्या दर्ज करें",
            "Verify Phone Number" to "फ़ोन नंबर सत्यापित करें",
            "Enter the 6-digit code sent to your mobile number" to "अपने मोबाइल नंबर पर भेजा गया 6 अंकों का कोड दर्ज करें",
            "⚠ Invalid verification code" to "⚠ अमान्य सत्यापन कोड",
            "Verify" to "सत्यापित करें"

        ),

        "mr" to mapOf(
            "select_language" to "भाषा निवडा",
            "continue" to "सुरू ठेवा",
            "welcome" to "स्वागत आहे",
            "Welcome Back" to "परत स्वागत आहे",
            "Please select your preferred language to continue" to "कृपया सुरू ठेवण्यासाठी आपली पसंतीची भाषा निवडा.",
            "Sign in with your mobile number to continue" to "सुरू ठेवण्यासाठी आपल्या मोबाइल नंबरसह साइन इन करा",
            "Mobile Number" to "मोबाइल क्रमांक",
            "Enter a valid mobile number" to "मोबाइल क्रमांक",
            "Activity not found" to "क्रियाकलाप आढळला नाही",
            "Enter a 10-digit number" to "10-अंकी क्रमांक प्रविष्ट करा",
            "Verify Phone Number" to "फोन नंबर सत्यापित करा",
            "Enter the 6-digit code sent to your mobile number" to "आपल्या मोबाइल नंबरवर पाठविलेले 6-अंकी कोड प्रविष्ट करा",
            "⚠ Invalid verification code" to "⚠ अवैध सत्यापन कोड",
            "Verify" to "सत्यापित करा"

        ),
    )
    fun getText(key : String): String{
        return translations[currentLanguage]?.get(key) ?: key
    }
}