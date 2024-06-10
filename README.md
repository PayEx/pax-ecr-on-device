# pax-ecr-on-device

A demo _ECR on Device_ Android application designed to run on the PAX family of payment terminals.

## Getting Started

To run this application, you will need:
- A compatible payment terminal
- A version of the payment application that supports receiving NEXO-retailer messages via Intent
- The _ECR on Device_ feature enabled via the dev-menu in the payment application

PayEx developers can find the necessary version of the payment application in the `pax-payment-app` repository on the branch [PAX-3262-create-initial-application-for-ecr-on-device-testing](https://github.com/PayEx/pax-payment-app/tree/PAX-3262-create-initial-application-for-ecr-on-device-testing). Partners can download a packaged version via the TMS.

## Usage

Upon launching the _ECR on Device_ application, you will be prompted to configure the app. Enter the POI-ID and the currency associated with that POI. If you are debugging, consider enabling the _Response-screen_ flag to view responses from the terminal.

To verify that everything is functioning correctly, you can:
- Send a login request
- Access the payment application from the footer menu

If the application stalls or becomes unresponsive, try restarting the terminal.