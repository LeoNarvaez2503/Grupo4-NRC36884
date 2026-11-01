Add-Type -AssemblyName System.IO.Compression.FileSystem
$docPath = 'C:\Users\MeatPuppets\Desktop\Grupo4-NRC36884\Parcial-I\Grupales\TG.1.1 PAO202651_SOAP_JAVA_CONBDD_NRC36884_GR04\05. DOCUMENTACION\MANUAL_SOAP_JAVA_SINBDD_GR04.docx'
$zip = [System.IO.Compression.ZipFile]::OpenRead($docPath)
$entry = $zip.GetEntry('word/document.xml')
$reader = New-Object System.IO.StreamReader($entry.Open())
$xml = $reader.ReadToEnd()
$reader.Close()
$zip.Dispose()
[xml]$doc = $xml
$ns = New-Object System.Xml.XmlNamespaceManager($doc.NameTable)
$ns.AddNamespace('w', 'http://schemas.openxmlformats.org/wordprocessingml/2006/main')

$figCaptions = $doc.SelectNodes("//w:p[w:pPr/w:pStyle/@w:val='Heading4']", $ns)
Write-Output ("Total Figures in reference: " + $figCaptions.Count)
for ($i=0; $i -lt [Math]::Min(5, $figCaptions.Count); $i++) {
    $cap = $figCaptions[$i]
    Write-Output ("--- Figure Caption $i ---")
    Write-Output ("Text: " + $cap.InnerText)
    Write-Output ("Caption XML: " + $cap.OuterXml)
    $prev = $cap.PreviousSibling
    if ($prev) {
        Write-Output ("  Prev element: " + $prev.Name + " | Prev XML (first 400): " + $prev.OuterXml.Substring(0, [Math]::Min(400, $prev.OuterXml.Length)))
    }
    $next = $cap.NextSibling
    if ($next) {
        Write-Output ("  Next element: " + $next.Name + " | Next XML (first 400): " + $next.OuterXml.Substring(0, [Math]::Min(400, $next.OuterXml.Length)))
    }
}
