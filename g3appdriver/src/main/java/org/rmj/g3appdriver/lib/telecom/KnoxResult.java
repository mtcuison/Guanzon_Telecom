package org.rmj.g3appdriver.lib.telecom;

public class KnoxResult {

    private String trbMsg;
    private String errMsg;
    private String logMsg;

    public String getTroubleShoot() {
        return trbMsg;
    }

    public String getErrorCause() {
        return errMsg;
    }

    public String getLogMessage() {
        return logMsg;
    }

    public String getErrorMessage(String ErrorCode){
        switch (ErrorCode){
            case "71102":
                logMsg = "No return log received.";
                trbMsg = "Try to re-enroll the device.";
                errMsg = "The Knox Configure client failed to start.";
                return "Unable to retrieve device return.";
            case "71202":
                logMsg = "No return log received.";
                trbMsg = "Try to re-enroll the device.";
                errMsg = "Device isn't configured for mobile enrollment. A system error occurred because the IMEI or serial number isn't registered with the device enrollment server.";
                return "Unable to enroll device. This device is not configured for Mobile Enrollment.";
            case "700107":
            case "700108":
                logMsg = "Device enrollment error. If the problem persists, contact Samsung and provide a device log to assist with troubleshooting.";
                trbMsg = "Try to re-enroll the device.";
                errMsg = "The admin is not activated. An error occurred while configuring the device.";
                return "An error occurred while configuring the device.";
            case "72001":
                logMsg = "Device enrollment error. If the problem persists, contact Samsung and provide a device log to assist with troubleshooting.";
                trbMsg = "Try to re-enroll the device.";
                errMsg = "The admin is not activated. An error occurred while configuring the device.";
                return "The operation failed due to invalid authentication credentials.";
            case "72002":
                logMsg = "Device enrollment error. If the problem persists, contact Samsung and provide a device log to assist with troubleshooting.";
                trbMsg = "Verify the credentials and try the credentials again or Please wait a few hours and try to enroll again.";
                errMsg = "Unable to find credentials. Enrollment failed due to invalid authentication credentials. The server returns HTTP code 404, not found.";
                return "Unable to find credentials.";
            case "72402":
            case "72502":
            case "72605":
            case "72626":
                logMsg = "Device enrollment error. If the problem persists, contact Samsung and provide a device log to assist with trouble shooting.";
                trbMsg = "Please check if the device has connection to server.";
                errMsg = "Unable to enroll device. The device wasn't able to connect to the Samsung server.";
                return "Unable to download and install customization agent. Check your connection and try again.";
            case "72701":
                logMsg = "Device enrollment error. If the problem persists, contact Samsung and provide a device log to assist with trouble shooting.";
                trbMsg = "An error occurred on the device enrollment server due to an invalid server certificate.";
                errMsg = "Server certificate error (Certificate pinning error). An invalid server certificate is preventing the device from enrolling successfully into Knox Configure.";
                return "Network error. Invalid server certificate.";
            case "72702":
                logMsg = "The device ID (IMEI or Serial Number) has been removed from the Knox Configure Portal. Add the Device ID and try again";
                trbMsg = "Register the IMEI on the server and retry the enrollment process again.";
                errMsg = "An error occurred on the device enrollment server because the IMEI or serial number was removed on the Knox Configure console.";
                return "Unable to enroll device or the enrollment couldn't be finished. You can retry by pressing the button. If the problem persists, please contact your IT admin.";
            case "0":
                logMsg = "";
                trbMsg = "On the device, swipe down from the status bar and tap Wi-Fi to check if the device connection is stable.\n" +
                        "\n" +
                        "If you're connecting the device to the internet via mobile network, go to: Settings > Connections > Mobile networks, check that you've selected the appropriate Network Mode and Network Operator for the device.\n" +
                        "\n" +
                        "Once Wi-Fi connectivity is verified, retry the enrollment process";
                errMsg = "Network connection error, an error occurred while getting the profile.";
                return "No Network Connection. No signal found for mobile networks. Check your network and retry.";
            case "10102":
                logMsg = "An unknown error occurred while downloading the profile. Contact Samsung and provide a device log.";
                trbMsg = "";
                errMsg = "Unknown error. The error occurred from an unknown or undetectable reason. The profile became corrupt during download.";
                return "An error occurred while customizing the device. Please retry.";
            case "11001":
                logMsg = "The profile became corrupted during the download. Contact Samsung for more information.";
                trbMsg = "Try to re-install the profile again.";
                errMsg = "Corrupt profile error. An issue occurred when syncing to the server when downloading the device profile.";
                return "Profile Error. The profile is not valid. Please retry.";
            case "11002":
                logMsg = "The profile became corrupted during the download. Contact Samsung for more information.";
                trbMsg = "Try to re-install the profile again.";
                errMsg = "Malformed enrollment response. A mismatch error occurred while the server was upgrading.";
                return "Profile Error. The profile is not valid. Please retry.";
            case "11003":
                logMsg = "The profile became corrupted during the download. Contact Samsung for more information.";
                trbMsg = "Try to re-install the profile again.";
                errMsg = "Malformed enrollment response. A license wasn't present in the response or only one license detected.";
                return "Profile Error. The profile is not valid. Please retry.";
            case "11004":
                logMsg = "The profile became corrupted during the download. Contact Samsung for more information.";
                trbMsg = "Try to re-install the profile again.";
                errMsg = "Unable to update KC agent.";
                return "Profile Error. The profile is not valid. Please retry.";
            case "11005":
                logMsg = "The profile became corrupted during the download. Contact Samsung for more information.";
                trbMsg = "Try to re-install the profile again.";
                errMsg = "Unable to download KC agent. The profile became corrupted during download.";
                return "Profile Error. The profile is not valid. Please retry.";
            case "11006":
                logMsg = "The profile became corrupted during the download. Contact Samsung for more information.";
                trbMsg = "Try to re-install the profile again.";
                errMsg = "KC agent package name and version code verification failure. The profile became corrupted during download.";
                return "Profile Error. The profile is not valid. Please retry.";
            case "11007":
                logMsg = "The profile became corrupted during the download. Contact Samsung for more information.";
                trbMsg = "Try to re-install the profile again.";
                errMsg = "The downloaded KC agent APK was deleted before it was properly installed. The profile became corrupted during download.";
                return "Profile Error. The profile is not valid. Please contact MIS or retry.";
            case "11103":
                logMsg = "An unknown error occurred while downloading the device profile. If the problem persists, contact Samsung and provide a device log to assist with troubleshooting.";
                trbMsg = "Check the network environment is stable and try again.";
                errMsg = "Unable to fetch registration ID.";
                return "Error. An error occurred while configuring the device. Please contact MIS or retry.";
            case "12000":
                logMsg = "A network error occurred while downloading the profile. Check the device network environment and retry.";
                trbMsg = "Please check if the device has stable network connection.";
                errMsg = "A network error is preventing the device from syncing with the profile.";
                return "A network error occurred while customizing the device. Check your network environment and retry.";
            case "13002":
                logMsg = "An unknown error occurred while downloading an app. If the problem persists, contact Samsung and provide a device log to assist with troubleshooting.";
                trbMsg = "Devices registered in the Samsung server must also be registered in Knox Configure before the device can be enrolled.\n" +
                        "\n" +
                        "Verify the device IMEI in the Knox Configure portal. If not, contact your reseller to upload the device.\n" +
                        "\n" +
                        "If the device appears in the portal, verify that the device is assigned a profile. Try reassigning the profile";
                errMsg = "Invalid Device ID. The device ID (IMEI or serial number) isn't registered in Knox Configure. This error may also occur if the IMEI or serial number is removed from Knox Configure while the device is in the process of being enrolled.";
                return "Invalid Device ID. Your device is not registered in the Knox system. Please contact MIS or retry.";
            case "20102":
                logMsg = "An unknown error occurred while downloading an app. If the problem persists, contact Samsung and provide a device log to assist with troubleshooting.";
                trbMsg = "";
                errMsg = "An unknown error occurred while downloading an app.";
                return "Error: An error occurred while customizing the device. Please contact MIS.";
            case "21001":
                logMsg = "The profile became corrupt during download. Contact Samsung for more information.";
                trbMsg = "Try to enroll the device again.";
                errMsg = "Device profile error. The Knox Configure profile format is corrupt, incorrect, or malformed.";
                return "Profile Error: The profile is invalid. Please contact or retry.";
            case "21004":
                logMsg = "Device storage limit exceeded. Ensure sufficient device storage space is available and retry.";
                trbMsg = "Check the available storage space on your device by navigating to Settings > System > Storage > Available Space.\n" +
                        "\n" +
                        "Remove apps by long pressing and tapping Remove.\n" +
                        "\n" +
                        "Remove content by going to My Files and long pressing files to delete.";
                errMsg = "Insufficient storage, the file download failed due to insufficient device storage capacity.";
                return "There is not enough space in your device storage. Ensure sufficient space is available on the device and retry.";
            case "21005":
                logMsg = "<application name> is invalid or a package with the same name has already been installed. Review the app and try again.";
                trbMsg = "If an app has the same package name, uninstall the existing app and try enrolling your device again.\n" +
                        "\n" +
                        "Try installing the APK file manually: Go to Settings > Lock screen and security. Under Unknown sources, tap to allow the installation of apps from sources other than Google Play. Transfer the APK file by connecting the device to a computer using a USB. Install by tapping My Files and navigate to the folder you've side-loaded the APK to. Tap on the package name and follow the installation prompts.";
                errMsg = "App installation failed. The app error occurred due to an error with the APK file previously uploaded to the Knox Configure portal. Alternatively, the device may have another app with the same package name already installed.";
                return "Application Installation Failed. An error occurred while installing applications. Please contact MIS or retry.";
            case "21006":
                logMsg = "<application name> is invalid or a package with the same name has already been installed. Review the app and try again.";
                trbMsg = "If an app has the same package name, uninstall the existing app and try enrolling your device again.";
                errMsg = "Application failed to install due to a check sum matching failure.";
                return "Application Installation Failed. An error occurred while installing applications. Please contact MIS or retry.";
            case "22000":
                logMsg = "<application name> download error. Check device network environment and retry.";
                trbMsg = "";
                errMsg = "App download failed. Download failed due to an error in the local network environment (firewall, proxy, gateway). Knox Configure couldn't establish a stable connection with the Samsung Server.";
                return "Application Download Failed. Check your network and retry.";
            case "23006":
                logMsg = "<application name> could not be downloaded. If this problem persists, contact Samsung.";
                trbMsg = "An error resulted from an invalid download URL.\n" +
                        "\n" +
                        "Please re-try downloading the app again.";
                errMsg = "Server error. The Samsung server failed to deliver the download URL to the device.";
                return "Error. An error occurred while customizing the device. Please contact MIS or retry.";
            case "23007":
                logMsg = "<application name> could not be downloaded. If this problem persists, contact Samsung.";
                trbMsg = "An error occurred due to network instability. On the device, swipe down from the status bar and tap Wi-Fi to check if the device connection is stable.\n" +
                        "\n" +
                        "Try to remove and upload the application again.";
                errMsg = "Server error. The Samsung server failed to deliver the download URL to the device.";
                return "<application name> download error. Check device network environment and retry. If the problem persists, contact Samsung.";
            case "23008":
                logMsg = "<application name> could not be downloaded. If this problem persists, contact Samsung.";
                trbMsg = "An error occurred due to network instability. On the device, swipe down from the status bar and tap Wi-Fi to check if the device connection is stable.\n" +
                        "\n" +
                        "Try to remove and upload the application again.";
                errMsg = "URL download failure, the app download failed from URL because the URL is malformed.";
                return "Error. An error occurred while customizing the device. Please contact MIS or retry.";
            case "23009":
                logMsg = "<application name> could not be downloaded. If this problem persists, contact Samsung.";
                trbMsg = "An error occurred due to network instability. On the device, swipe down from the status bar and tap Wi-Fi to check if the device connection is stable.\n" +
                        "\n" +
                        "Try to remove and upload the application again.";
                errMsg = "The server was unable to fetch EULA for an admin application.";
                return "Error. An error occurred while customizing the device. Please contact MIS or retry.";
            case "30102":
                logMsg = "An unknown error while activating your license. If the problem persists, contact Samsung and provide a device log to assist with troubleshooting.";
                trbMsg = "";
                errMsg = "An unknown error has occurred during the license activation process.";
                return "Error. An error occurred while customizing the device. Please contact MIS.";
            case "31001":
                logMsg = "The profile corrupted while activating a license. If the problem persists, contact Samsung.";
                trbMsg = "Try to re-load the profile.";
                errMsg = "Corrupted profile error. An error resulted from the use of a wrong profile schema. When parsing, the profile format wasn't correct.";
                return "Profile Error: The profile is not valid. Contact your IT admin or retry.";
            case "32105":
                logMsg = "The device time and/or date is incorrect. Set the correct time and/or date and try again.";
                trbMsg = "Reset the device date and time correctly to your geographic timezone: Go to Settings > System > Date and time. Enable Automatic date and time and Automatic time zone to use the settings from your mobile service provider.";
                errMsg = "Incorrect date or time on device. The device time is not synced with the license time stamp. An incorrect date or time on the device can cause a license activation error.";
                return "License Activation Failed. An error occurred while activating the license on the device. Set the date and time on the device correctly and retry.\t";
            case "32000":
            case "32502":
                logMsg = "License activation failed due to a network error. Check the device network environment and retry.";
                trbMsg = "PLease check if device has stable connection.";
                errMsg = "Network error occurred during license activation. The device wasn't able to activate the license.";
                return "License Activation Failed. An error occurred while activating the device license. Check your network and retry.";
            case "33401":
                logMsg = "License activation failed due to an error with the Samsung internal server. If the problem persists, contact Samsung.";
                trbMsg = "Wait a few hours and try enrolling your device again.";
                errMsg = "The Samsung server is currently experiencing an issue.";
                return "License Activation Failed. An error occurred while activating the device license. Please contact MIS.";
            case "34701":
                logMsg = "License expired. Purchase new licenses and try activating the device again.";
                trbMsg = "";
                errMsg = "License expired. Your license key has expired. If you are using a trial key which generated from SEAP, you can check your expiration date from License Keys. Log in to SEAP and see How to use license keys for more information.";
                return "License Activation Failed. An error occurred while activating the license on the device. Please contact MIS or retry.";
            case "34203":
                logMsg = "Invalid licenses. Purchase new licenses and try again.";
                trbMsg = "In the Knox Configure dashboard, go to the License tab. Check that you have valid licenses available. Check the expiry date.\n" +
                        "\n" +
                        "If you don't have valid licenses, you may need to obtain additional licenses and try again.";
                errMsg = "License terminated.";
                return "License Activation Failed. An error occurred while activating the license on the device. Please contact MIS or retry.";
            case "34702":
                logMsg = "Insufficient licenses to activate devices. Purchase new licenses and try again";
                trbMsg = "In the Knox Configure dashboard, go to the License tab. Check that you have valid licenses available. Check the expiry date.\n" +
                        "\n" +
                        "If you don't have valid licenses, you may need to obtain additional licenses and try again.";
                errMsg = "License quantity exceeded. You don't have enough licenses to enroll all of your devices.";
                return "An error occurred while activating the license on the device. Contact your IT admin or retry";
            case "700109":
            case "700110":
            case "700111":
            case "700112":
            case "700113":
                logMsg = "Insufficient licenses to activate devices. Purchase new licenses and try again.";
                trbMsg = "In the Knox Configure dashboard, go to the License tab. Check that you have valid licenses available. Check the expiry date.\n" +
                        "\n" +
                        "If you don't have valid licenses, you may need to obtain additional licenses and try again.";
                errMsg = "Invalid license key. Your license key couldn't be validated on the server and is currently invalid.";
                return "Configuration Failed. An error occurred while configuring the device. Contact your IT admin or retry.";
            case "400":
                return "Code 400 (Bad Request) : The client has issued an invalid request. ";
            case "401":
                return "Error Code 401 (Unauthorized) : Authorization for the API is required, but the request has not been authenticated.";

            case "403":
                return "Error Code 403 (Forbidden) : The request has been authenticated but does not have the appropriate permissions, or the requested resource cannot be found.";

            case "404":
                return "Error Code 404 (Not Found): Requested path does not exist.";

            case "406":
                return "Error Code 406 (Not Acceptable): The client has requested a MIME type via the Accept header for a value not supported by the server.";

            case "422":
                return "Error Code 422 (Unprocessable Entity): The client has made a valid request, but the server cannot process it. This is often returned for APIs when certain limits have been exceeded.";

            case "429":
                return "Error Code 429 (Too Many Requests): The client has exceeded the number of requests allowed for a given time window.";

            case "500":
                return "Error Code 500 (Internal Server Error): An unexpected error on the SmartThings servers has occurred. ";

            case "501":
                return "Error Code 422 (Not Implemented): The client request was valid and understood by the server, but the requested feature has yet to be implemented.";

            //return and description
            case "4010000":
                return "Error Code 4010000 (Authorization Fail): The API key is not valid or the restriction data does not match.";

            case "4002102":
                return "Error Code 4002102 (API Key Restriction Invalid): IP address or http referer format are incorrect.";

            case "4042101":
                return "Error Code 4002102 (API Key Restriction Not Found): The IP address or http referer are empty.";

            case "4040100":
                return "Error Code 4040100 (User Not Found): There is no user linked to the API key.";

            case "4040900":
                return "Error Code 4040900 (Tenant Not Found): There is no tenant linked to the API key.";

            case "4000000":
                return "Error Code 4000000 (Resource Invalid Parameters): Argument is invalid.";

            case "4001809":
                return "Error Code 4001809 (License Is Lack): There are no additional licenses that can be activated.";

            case "4040300":
                //return = "Error Code 4040300 (Device Not Found): A device was not found with the specified condition.";
                return "Specified Device IMEI was not found. Please contact MIS Support for uploading Device IMEI.";

            case "4000310":
                //return = "Error Code 4040300 (Device State Invalid): The operation is not permitted, in the current state.";
                return "Operation cannot proceed. Device IMEI was already activated. Please enroll your the device.";

            case "4000316":
                return "Error Code 4000316 (Device Bulk Operation Limit Exceeded): The bulk operation limit has been exceeded.";

            case "4040400":
                return "Error Code 4040400 (Profile Not Found): Internal profile not found.";

            case "4001805":
                return "Error Code 4001805 (License Name Already Exceed): License name already exists.";

            case "5001804":
                return "Error Code 5001804 (License Internal Server Failed): Error communication with the license server.";

            case "4001813":
                return "Error Code 4001813 (License Key Already Exist In Oher Region): This license key is already registered to another region (EU/US).";

            case "4001802":
                return "Error Code 4001802 (License Max Trial License Count Reached): Max trial license count reached.";

            case "4041800":
                return "Error Code 4041800 (License Not Found): License not found.";

            case "4001806":
                return "Error Code 4001806 (License Device Mapping Found): This device is already registered with a license.";

            case "4001812":
                return "Error Code 4001812 (License ALready Deleted): License already deleted.";

            default:
                return "default";
        }
    }
}
