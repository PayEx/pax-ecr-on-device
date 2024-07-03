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

## Contributing
We welcome contributions to enhance the ECR on Device application. To contribute, please follow these steps:

1. Fork the Repository: Create a fork of the repository on GitHub.
2. Clone the Repository: Clone your fork to your local machine using git clone.
3. Create a Branch: Create a new branch for your feature or bug fix using git checkout -b feature-name.
4. Make Changes: Implement your changes and ensure they are well-tested.
5. Commit Changes: Commit your changes with clear and descriptive commit messages.
6. Push Changes: Push your changes to your forked repository using git push origin feature-name.
7. Create a Pull Request: Open a pull request on the original repository, detailing your changes and any issues they address.

Please ensure your contributions adhere to the project's coding standards and include appropriate documentation. For significant changes, consider discussing them with the repository maintainers before starting work.
