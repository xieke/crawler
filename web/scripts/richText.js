function PC_RT_Init(richText) {
 richText.value = richText.eosData;
 document.getElementById(richText.eosRichTextDataID).value = richText.value;
}

function PC_RT_Update(richText) {
	document.getElementById(richText.eosRichTextDataID).value = richText.value;
}