# pax-ecr-on-device
A demo _ECR on Device_ Android-application that runs on the PAX-family of payment terminals.

## Getting started

Running this application requires a payment terminal, as well as a version of the payment application
that supports receiving NEXO-retailer messages via Intent, and the _ECR on Device_-feature being enabled via the dev-menu on the payment application. 
Currently, such a version can be found, for 
PayEx developers, in the pax-payment-app repository on the branch 
[PAX-3262-create-initial-application-for-ecr-on-device-testing](https://github.com/PayEx/pax-payment-app/tree/PAX-3262-create-initial-application-for-ecr-on-device-testing).
Partners can download a packaged version via the TMS.

## Usage

When running the _ECR on Device_ application, the initial boot will prompt for a config. Here, 
you must add a POI-ID and the currency associated with that POI. If you're debugging, you
might want to turn on the _Response-screen_-flag in order to see the responses from the terminal.

To verify that everything works, you can try to send a login-request, or entering the payment
application from the footer menu. If either application stalls, or seems unresponsive, try to restart
the terminal.