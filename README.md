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

When running the _ECR on Device_ application, the initial boot will look something like this:
![Screenshot 2024-05-21 at 13.28.25.png](https://i.imgur.com/3M2M3uT.png)

To verify that everything works as expected, try to open the menu, and select _Temporarily show payment app_.
![Screenshot 2024-05-21 at 13.28.25.png](https://i.imgur.com/2WeHLDy.png)

This should display the payment application for 5 seconds, before then displaying the _ECR on Device_ again.
Then, you can try to login, which should provide a response similar to
![Screenshot 2024-05-21 at 13.33.53.png](https://i.imgur.com/lXvolnr.png)

If either application stall, and nothing seem to happen, simply restart the terminal.
