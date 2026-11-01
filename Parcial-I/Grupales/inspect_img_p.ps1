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
$ns.AddNamespace('wp', 'http://schemas.openxmlformats.org/drawingml/2006/wordprocessingDrawing')
$ns.AddNamespace('a', 'http://schemas.openxmlformats.org/drawingml/2006/main')

$f1 = $doc.SelectSingleNode("//w:p[w:pPr/w:pStyle/@w:val='Heading4']", $ns)
Write-Output "Caption 1 XML:"
Write-Output $f1.OuterXml

$imgP = $f1.NextSibling
Write-Output "`nImage P XML (Full):"
Write-Output $imgP.OuterXml
