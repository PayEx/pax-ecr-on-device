package com.pax.ecr.app

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
    val Cardholder: Cardholder,
) {
    fun toReceiptData(items: Map<String, Int> = emptyMap()): String {
        return """
                                                                        <OutputText StartRow="1">Customer Receipt</OutputText>
                                                                        <OutputText StartRow="2">Check out the integration guide for examples</OutputText>
                                                                        <OutputText StartRow="4">${Cardholder.Mandatory.Outcome.AuthorisationResponder} ${Cardholder.Mandatory.Outcome.DebitStatus} ${Cardholder.Mandatory.Outcome.ApprovalCode} ${Cardholder.Mandatory.Outcome.AuthorisationResponseCode}</OutputText>
                                                                        <OutputText StartRow="5">${Cardholder.Mandatory.TimeStamp.TimeOfPayment} ${Cardholder.Mandatory.TimeStamp.DateOfPayment}
                                                                        
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
                                                                        <OutputText StartRow="${items.size + 8}">${Cardholder.Mandatory.Payment.PaymentAmount} ${Cardholder.Mandatory.Payment.Currency}
                                                                            
                                                                            
                                                                            
                                                                            
                                                                            
                                                                                  
                                                                        </OutputText>
            """.trimIndent()
    }
}

@Serializable
data class Cardholder(
    val Mandatory: Mandatory,
    val Optional: Optional? = null,
)

@Serializable
data class Mandatory(
    val Acquirer: Acquirer,
    val CardAcceptor: CardAcceptor,
    val CardDetails: CardDetails,
    val Outcome: Outcome,
    val Payment: Payment,
    val TimeStamp: TimeStamp,
)

@Serializable
data class Acquirer(
    val CardAcceptorNumber: String,
    val TerminalID: String,
)

@Serializable
data class CardAcceptor(
    val Address1: String,
    val BankAgentName: String,
    val Name: String,
    val OperatorNumber: String,
    val OrganisationNumber: String,
    val PostZipCode: String,
    val TownCity: String,
)

@Serializable
data class CardDetails(
    val ApplicationIdentifier: String,
    val CardSchemeName: CardSchemeName,
    val PrimaryAccountNumber: String,
    val TerminalVerificationResult: String,
    val TransactionStatusInformation: String,
)

@Serializable
data class CardSchemeName(
    val ApplicationLabel: String,
)

@Serializable
data class Outcome(
    val ApprovalCode: String? = null,
    val AuthorisationResponder: String,
    val AuthorisationResponseCode: String? = null,
    val DebitStatus: String,
)

@Serializable
data class Payment(
    val AuthorisationChannel: String,
    val CardholderVerificationMethod: String,
    val Currency: String,
    val FinancialInstitution: String,
    val PaymentAmount: String,
    val ReceiptNumber: String,
    val SignatureBlock: Boolean,
    val TotalAmount: String,
    val TransactionSource: String,
    val TransactionType: String,
)

@Serializable
data class TimeStamp(
    val DateOfPayment: String,
    val TimeOfPayment: String,
)

@Serializable
data class Optional(
    val CardAcceptor: OptionalCardAcceptor? = null,
    val CardDetails: OptionalCardDetails? = null,
    val Payment: OptionalPayment? = null,
    val ReceiptString: List<String>? = null,
)

@Serializable
data class OptionalCardAcceptor(
    val CountryName: String,
)

@Serializable
data class OptionalCardDetails(
    val CardIssuerNumber: String,
    val CardSchemeName: CardSchemeName,
)

@Serializable
data class OptionalPayment(
    val Reference: String,
)
