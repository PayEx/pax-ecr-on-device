package com.pax.ecr.app

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.nio.charset.Charset
import java.text.DecimalFormat
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

object NexoMessages {
    fun loginRequest() =
        """
        <SaleToPOIRequest>
            <MessageHeader MessageCategory="Login" MessageClass="Service" MessageType="Request" POIID="${config.poiId}" ProtocolVersion="3.1" SaleID="${config.saleId}" ServiceID="${randomServiceId()}"/>
            <LoginRequest OperatorID="Cashier1" OperatorLanguage="en" ShiftNumber="2">
                <DateTime>${now()}</DateTime>
                <SaleSoftware ApplicationName="TestScripts" CertificationCode="ECTS2PS001" ProviderIdentification="swedbankpay" SoftwareVersion="1.0"/>
                <SaleTerminalData TerminalEnvironment="Attended">
                    <SaleCapabilities>PrinterReceipt</SaleCapabilities>
                    <SaleProfile/>
                </SaleTerminalData>
            </LoginRequest>
        </SaleToPOIRequest>
        """.trimIndent().toByteArray(Charset.defaultCharset())

    fun logout() =
        """
        <SaleToPOIRequest>
            <MessageHeader MessageCategory="Logout" MessageClass="Service" MessageType="Request" POIID="${config.poiId}" ProtocolVersion="3.1" SaleID="${config.saleId}" ServiceID="${randomServiceId()}"/>
            <LogoutRequest MaintenanceAllowed="true"/>
        </SaleToPOIRequest>
        """.trimIndent().toByteArray(Charset.defaultCharset())

    fun payment(amount: BigDecimal) =
        """
        <SaleToPOIRequest>
            <MessageHeader MessageCategory="Payment" MessageClass="Service" MessageType="Request" POIID="${config.poiId}" ProtocolVersion="3.1" SaleID="${config.saleId}" ServiceID="${randomServiceId()}"/>
            <PaymentRequest>
                <SaleData TokenRequestedType="Customer">
                    <SaleTransactionID TimeStamp="${now()}" TransactionID="${randomTransactionId()}"/>
                </SaleData>
                <PaymentTransaction>
                    <AmountsReq CashBackAmount="0" Currency="${config.currencyCode}"  RequestedAmount="${
            DecimalFormat(
                "#0.##",
            ).format(amount)}"/>
                </PaymentTransaction>
            </PaymentRequest>
        </SaleToPOIRequest>
        """.trimIndent().toByteArray(Charset.defaultCharset())

    fun paymentWithCashback() =
        """
        <SaleToPOIRequest>
            <MessageHeader MessageCategory="Payment" MessageClass="Service" MessageType="Request" POIID="${config.poiId}" ProtocolVersion="3.1" SaleID="${config.saleId}" ServiceID="${randomServiceId()}"/>
            <PaymentRequest>
                <SaleData TokenRequestedType="Customer">
                    <SaleTransactionID TimeStamp="${now()}" TransactionID="${randomTransactionId()}"/>
                </SaleData>
                <PaymentTransaction>
                    <AmountsReq CashBackAmount="50" Currency="${config.currencyCode}"  RequestedAmount="100"/>
                </PaymentTransaction>
            </PaymentRequest>
        </SaleToPOIRequest>
        """.trimIndent().toByteArray(Charset.defaultCharset())

    fun refund() =
        """
        <SaleToPOIRequest>
            <MessageHeader MessageCategory="Payment" MessageClass="Service" MessageType="Request" POIID="${config.poiId}" ProtocolVersion="3.1" SaleID="${config.saleId}" ServiceID="${randomServiceId()}"/>
            <PaymentRequest>
                <SaleData TokenRequestedType="Customer">
                    <SaleTransactionID TimeStamp="${now()}" TransactionID="${randomTransactionId()}"/>
                </SaleData>
                <PaymentTransaction>
                    <AmountsReq Currency="${config.currencyCode}"  RequestedAmount="100"/>
                </PaymentTransaction>
                <PaymentData PaymentType="Refund"/>
            </PaymentRequest>
        </SaleToPOIRequest>
        """.trimIndent().toByteArray(Charset.defaultCharset())

    fun reversal() =
        """
        <SaleToPOIRequest>
            <MessageHeader MessageCategory="Reversal" MessageClass="Service" MessageType="Request" POIID="${config.poiId}" ProtocolVersion="3.1" SaleID="${config.saleId}" ServiceID="${randomServiceId()}"/>
            <ReversalRequest ReversalReason="MerchantCancel">
                <OriginalPOITransaction POIID="${config.poiId}" SaleID="${config.saleId}">
                    <POITransactionID TimeStamp="$lastTransactionDatetime" TransactionID="$lastResponseTransactionId" />
                </OriginalPOITransaction>
            </ReversalRequest>
        </SaleToPOIRequest>
        """.trimIndent().toByteArray(Charset.defaultCharset())

    fun receipt(receiptData: String) =
        """
        <SaleToPOIRequest>
            <MessageHeader MessageClass="Device" MessageCategory="Print" MessageType="Request" POIID="${config.poiId}" ProtocolVersion="3.1" SaleID="${config.saleId}" ServiceID="${randomServiceId()}" DeviceID="DEMO"/>
            <PrintRequest>
                <PrintOutput DocumentQualifier="SaleReceipt" ResponseMode="PrintEnd">
                    <OutputContent OutputFormat="Text">
                        $receiptData
                    </OutputContent>
                </PrintOutput>
            </PrintRequest>
        </SaleToPOIRequest>
        """.trimIndent().toByteArray(Charset.defaultCharset())

    private fun randomServiceId() = Random.nextInt(0, Int.MAX_VALUE)

    private fun randomTransactionId() =
        Random.nextInt(0, Int.MAX_VALUE).also {
            lastTransactionId = it.toString()
        }

    private fun now() = ZonedDateTime.now(ZoneId.of("Europe/Oslo")).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
}

@Serializable
data class CardholderReceipt(
    @SerialName("Cardholder") val cardholder: Cardholder,
) {
    fun toReceiptData(items: Map<String, Int> = emptyMap()): String {
        return """
                                                                                                <OutputText StartRow="1">Customer Receipt</OutputText>
                                                                                                <OutputText StartRow="2">Check out the integration guide for examples</OutputText>
                                                                                                <OutputText StartRow="4">${cardholder.mandatory.outcome.authorisationResponder} ${cardholder.mandatory.outcome.debitStatus} ${cardholder.mandatory.outcome.approvalCode} ${cardholder.mandatory.outcome.authorisationResponseCode}</OutputText>
                                                                                                <OutputText StartRow="5">${cardholder.mandatory.timeStamp.timeOfPayment} ${cardholder.mandatory.timeStamp.dateOfPayment}
                                                                                                
                                                                                                </OutputText>
                                                                                                ${if (items.isNotEmpty()) {
            items.keys.mapIndexed {
                    index,
                    key,
                ->
                "<OutputText StartRow=\"${index + 6}\">${items[key]} $key</OutputText>"
            }.joinToString("\n")
        } else {
            ""
        }}
                                                                    <OutputText StartRow="${items.size + 7}">
                                                                    
                                                                    
                                                                    </OutputText>
                                                                                                <OutputText StartRow="${items.size + 8}">${cardholder.mandatory.payment.paymentAmount} ${cardholder.mandatory.payment.currency}
                                                                                                    
                                                                                                    
                                                                                                    
                                                                                                          
                                                                                                </OutputText>
            """.trimIndent()
    }
}

@Serializable
data class Cardholder(
    @SerialName("Mandatory") val mandatory: Mandatory,
    @SerialName("Optional") val optional: Optional? = null,
)

@Serializable
data class Mandatory(
    @SerialName("Acquirer") val acquirer: Acquirer,
    @SerialName("CardAcceptor") val cardAcceptor: CardAcceptor,
    @SerialName("CardDetails") val cardDetails: CardDetails,
    @SerialName("Outcome") val outcome: Outcome,
    @SerialName("Payment") val payment: Payment,
    @SerialName("TimeStamp") val timeStamp: TimeStamp,
)

@Serializable
data class Acquirer(
    @SerialName("CardAcceptorNumber") val cardAcceptorNumber: String,
    @SerialName("TerminalID") val terminalID: String,
)

@Serializable
data class CardAcceptor(
    @SerialName("Address1") val address1: String,
    @SerialName("BankAgentName") val bankAgentName: String,
    @SerialName("Name") val name: String,
    @SerialName("OperatorNumber") val operatorNumber: String,
    @SerialName("OrganisationNumber") val organisationNumber: String,
    @SerialName("PostZipCode") val postZipCode: String,
    @SerialName("TownCity") val townCity: String,
)

@Serializable
data class CardDetails(
    @SerialName("ApplicationIdentifier") val applicationIdentifier: String,
    @SerialName("CardSchemeName") val cardSchemeName: CardSchemeName,
    @SerialName("PrimaryAccountNumber") val primaryAccountNumber: String,
    @SerialName("TerminalVerificationResult") val terminalVerificationResult: String,
    @SerialName("TransactionStatusInformation") val transactionStatusInformation: String,
)

@Serializable
data class CardSchemeName(
    @SerialName("ApplicationLabel") val applicationLabel: String,
)

@Serializable
data class Outcome(
    @SerialName("ApprovalCode") val approvalCode: String? = null,
    @SerialName("AuthorisationResponder") val authorisationResponder: String,
    @SerialName("AuthorisationResponseCode") val authorisationResponseCode: String? = null,
    @SerialName("DebitStatus") val debitStatus: String,
)

@Serializable
data class Payment(
    @SerialName("AuthorisationChannel") val authorisationChannel: String,
    @SerialName("CardholderVerificationMethod") val cardholderVerificationMethod: String,
    @SerialName("Currency") val currency: String,
    @SerialName("FinancialInstitution") val financialInstitution: String,
    @SerialName("PaymentAmount") val paymentAmount: String,
    @SerialName("ReceiptNumber") val receiptNumber: String,
    @SerialName("SignatureBlock") val signatureBlock: Boolean,
    @SerialName("TotalAmount") val totalAmount: String,
    @SerialName("TransactionSource") val transactionSource: String,
    @SerialName("TransactionType") val transactionType: String,
)

@Serializable
data class TimeStamp(
    @SerialName("DateOfPayment") val dateOfPayment: String,
    @SerialName("TimeOfPayment") val timeOfPayment: String,
)

@Serializable
data class Optional(
    @SerialName("CardAcceptor") val cardAcceptor: OptionalCardAcceptor? = null,
    @SerialName("CardDetails") val cardDetails: OptionalCardDetails? = null,
    @SerialName("Payment") val payment: OptionalPayment? = null,
    @SerialName("ReceiptString") val receiptString: List<String>? = null,
)

@Serializable
data class OptionalCardAcceptor(
    @SerialName("CountryName") val countryName: String,
)

@Serializable
data class OptionalCardDetails(
    @SerialName("CardIssuerNumber") val cardIssuerNumber: String,
    @SerialName("CardSchemeName") val cardSchemeName: CardSchemeName,
)

@Serializable
data class OptionalPayment(
    @SerialName("Reference") val reference: String,
)
