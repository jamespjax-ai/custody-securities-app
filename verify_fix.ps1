$baseUrl = "http://localhost:8080/api"

Write-Output "--- Test: Ingesting SESE.023 ---"
$xml = @"
<Document xmlns="urn:iso:std:iso:20022:tech:xsd:sese.023.001.09">
  <SctiesSttlmTxInstr>
    <TxId>TEST-INGEST-001</TxId>
    <SttlmTpAndAddtlParams>
      <SctiesMvmntTp>RECE</SctiesMvmntTp>
      <Pmt>APMT</Pmt>
    </SttlmTpAndAddtlParams>
    <FinInstrmId>
      <ISIN>US0378331005</ISIN>
    </FinInstrmId>
    <QtyAndAcctDtls>
      <SttlmQty>
        <Qty>
          <Unit>400</Unit>
        </Qty>
      </SttlmQty>
      <SfkpgAcct>
        <Id>ACCT_ABC_123</Id>
      </SfkpgAcct>
    </QtyAndAcctDtls>
  </SctiesSttlmTxInstr>
</Document>
"@

try {
    $resp = Invoke-RestMethod -Uri "$baseUrl/ingest" -Method Post -Body $xml -ContentType "application/xml"
    Write-Output "Server Response: $resp"

    Write-Output "`n--- Verifying Position Update ---"
    $positions = Invoke-RestMethod -Uri "$baseUrl/positions" -Method Get
    $positions | Where-Object { $_.accountId -eq "ACCT_ABC_123" -and $_.isin -eq "US0378331005" } | Format-Table
}
catch {
    Write-Output "Error: $_"
}
