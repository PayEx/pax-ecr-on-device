package com.pax.ecr.app

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
            <MessageHeader MessageCategory="Reversal" MessageClass="Service" MessageType="Request" POIID="${config.poiId}" SaleID="${config.saleId}" ServiceID="${randomServiceId()}"/>
            <ReversalRequest ReversalReason="MerchantCancel">
                <OriginalPOITransaction POIID="${config.poiId}" SaleID="${config.saleId}">
                    <POITransactionID TimeStamp="$lastTransactionDatetime" TransactionID="$lastResponseTransactionId" />
                </OriginalPOITransaction>
            </ReversalRequest>
        </SaleToPOIRequest>
        """.trimIndent().toByteArray(Charset.defaultCharset())

    private fun randomServiceId() = Random.nextInt(0, Int.MAX_VALUE)

    private fun randomTransactionId() =
        Random.nextInt(0, Int.MAX_VALUE).also {
            lastTransactionId = it.toString()
        }

    private fun now() = ZonedDateTime.now(ZoneId.of("Europe/Oslo")).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
}
