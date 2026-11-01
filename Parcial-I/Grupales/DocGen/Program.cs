using System;
using System.Collections.Generic;
using System.IO;
using System.IO.Compression;
using System.Linq;
using System.Xml.Linq;

namespace DocGen
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("=== GENERADOR DE MANUALES DE USUARIO Y CONFIGURACION (GR04) ===");

            string target = args.Length > 0 ? args[0].ToLowerInvariant() : "soap-dotnet";
            string basePath = @"C:\Users\MeatPuppets\Desktop\Grupo4-NRC36884\Parcial-I\Grupales";
            string templateDocx = Path.Combine(basePath, @"TG.1.1 PAO202651_SOAP_JAVA_CONBDD_NRC36884_GR04\05. DOCUMENTACION\MANUAL_SOAP_JAVA_SINBDD_GR04.docx");

            if (target == "soap-dotnet" || target == "all")
            {
                string dst = Path.Combine(basePath, @"TG.1.2 PAO202651_SOAP_DOTNET_CONBDD_NRC36884_GR04\05. DOCUMENTACION\MANUAL_SOAP_DOTNET_CONBDD_GR04.docx");
                string titulo = "MANUAL DE CONFIGURACIÓN Y USUARIO DEL SISTEMA BANCARIO EUREKABANK CON SERVICIOS SOAP Y BASE DE DATOS EN .NET (C# - COREWCF / ASP.NET CORE / MAUI)";
                GenerarDocumento(templateDocx, dst, titulo, ManualSoapDotNetContent.BuildElements);
            }
            if (target == "soap-java" || target == "all")
            {
                string dst = Path.Combine(basePath, @"TG.1.1 PAO202651_SOAP_JAVA_CONBDD_NRC36884_GR04\05. DOCUMENTACION\MANUAL_SOAP_JAVA_CONBDD_GR04.docx");
                string titulo = "MANUAL DE CONFIGURACIÓN Y USUARIO DEL SISTEMA BANCARIO EUREKABANK CON SERVICIOS SOAP Y BASE DE DATOS EN JAVA (CON BDD - MASHUP DE CONCURRENCIA ACID)";
                GenerarDocumento(templateDocx, dst, titulo, ManualSoapJavaContent.BuildElements);
            }
            if (target == "rest-java" || target == "all")
            {
                string dst = Path.Combine(basePath, @"TG.1.3 PAO202651_RESTFUL_JAVA_CONBDD_NRC36884_GR04\05. DOCUMENTACION\MANUAL_RESTFUL_JAVA_CONBDD_GR04.docx");
                string titulo = "MANUAL DE CONFIGURACIÓN Y USUARIO DEL SISTEMA BANCARIO EUREKABANK CON SERVICIOS RESTFUL Y BASE DE DATOS EN JAVA (JAKARTA EE / JERSEY / PAYARA)";
                GenerarDocumento(templateDocx, dst, titulo, ManualRestJavaContent.BuildElements);
            }
            if (target == "rest-dotnet" || target == "all")
            {
                string dst = Path.Combine(basePath, @"TG.1.4 PAO202651_RESTFUL_DOTNET_CONBDD_NRC36884_GR04\05. DOCUMENTACION\MANUAL_RESTFUL_DOTNET_CONBDD_GR04.docx");
                string titulo = "MANUAL DE CONFIGURACIÓN Y USUARIO DEL SISTEMA BANCARIO EUREKABANK CON SERVICIOS RESTFUL Y BASE DE DATOS EN .NET (ASP.NET CORE WEB API / MAUI)";
                GenerarDocumento(templateDocx, dst, titulo, ManualRestDotNetContent.BuildElements);
            }
        }

        public static void GenerarDocumento(string srcDocx, string dstDocx, string tituloCover, Func<List<XElement>> contentBuilder)
        {
            Console.WriteLine($"\n--- GENERANDO: {Path.GetFileName(dstDocx)} ---");
            Console.WriteLine($"Origen referencia : {srcDocx}");
            Console.WriteLine($"Destino nuevo     : {dstDocx}");

            if (!File.Exists(srcDocx))
            {
                Console.WriteLine($"ERROR: No se encontró el archivo de referencia: {srcDocx}");
                return;
            }

            // 1. Copiar archivo base para preservar estilos, fuentes, bordes de página y recursos
            File.Copy(srcDocx, dstDocx, true);
            Console.WriteLine("[1/4] Plantilla base copiada preservando carátula, márgenes y bordes twistedLines1.");

            // 2. Modificar word/document.xml en el archivo ZIP
            XDocument docXml;
            XNamespace w = "http://schemas.openxmlformats.org/wordprocessingml/2006/main";

            using (var zip = ZipFile.Open(dstDocx, ZipArchiveMode.Update))
            {
                var entry = zip.GetEntry("word/document.xml");
                if (entry == null)
                {
                    Console.WriteLine("ERROR: No se encontró word/document.xml en el documento.");
                    return;
                }

                using (var reader = new StreamReader(entry.Open()))
                {
                    docXml = XDocument.Load(reader);
                }

                var body = docXml.Root?.Element(w + "body");
                if (body == null)
                {
                    Console.WriteLine("ERROR: No se encontró el elemento w:body en el XML.");
                    return;
                }

                // Node 0: Carátula (Shapes, Logos, Tabla de metadatos)
                var node0 = body.Elements().FirstOrDefault();
                if (node0 != null)
                {
                    // Actualizar textos de Tema y Fecha en la carátula
                    foreach (var t in node0.Descendants(w + "t"))
                    {
                        if (t.Value.Contains("CONVERSION UNIDADES") || t.Value.Contains("CONVERSION DE UNIDADES") || t.Value.Contains("SISTEMA BANCARIO"))
                        {
                            t.Value = tituloCover;
                        }
                        if (t.Value.Contains("28/10/2026") || t.Value.Contains("27/10/2026"))
                        {
                            t.Value = "30/10/2026";
                        }
                    }
                }

                // Node 1: Título superior tras la carátula
                var node1 = body.Elements().Skip(1).FirstOrDefault();
                if (node1 != null)
                {
                    var t1 = node1.Descendants(w + "t").FirstOrDefault();
                    if (t1 != null)
                    {
                        t1.Value = tituloCover;
                    }
                }

                // Preservar los primeros 9 elementos (Carátula, Título, TOC general, spacers, Índice de Ilustraciones, Índice de Tablas)
                var preservedNodes = body.Elements().Take(9).ToList();
                var finalSectPr = body.Elements(w + "sectPr").LastOrDefault();

                // Actualizar instrucciones de campos TOC para usar niveles de esquema universales
                foreach (var instr in preservedNodes.SelectMany(p => p.Descendants(w + "instrText")))
                {
                    if (instr.Value.Contains("Heading 1,1"))
                    {
                        instr.Value = @" TOC \o ""1-3"" \h \z \u ";
                    }
                    else if (instr.Value.Contains("Heading 4,1"))
                    {
                        instr.Value = @" TOC \o ""4-4"" \h \z ";
                    }
                    else if (instr.Value.Contains("Heading 5,1"))
                    {
                        instr.Value = @" TOC \o ""5-5"" \h \z ";
                    }
                }

                Console.WriteLine($"Nodos preservados (Carátula + Índices): {preservedNodes.Count}");

                // Generar los nuevos elementos del contenido técnico
                var newElements = contentBuilder();
                Console.WriteLine($"Nuevos elementos de contenido generados: {newElements.Count}");

                // Reconstruir el cuerpo del documento
                body.RemoveAll();
                foreach (var pNode in preservedNodes)
                {
                    body.Add(pNode);
                }
                foreach (var newElem in newElements)
                {
                    body.Add(newElem);
                }
                if (finalSectPr != null)
                {
                    body.Add(finalSectPr);
                }

                // Guardar el XML actualizado en el ZIP
                entry.Delete();
                var newEntry = zip.CreateEntry("word/document.xml", CompressionLevel.Optimal);
                using (var writer = new StreamWriter(newEntry.Open(), System.Text.Encoding.UTF8))
                {
                    docXml.Save(writer);
                }

                // Ajustar tamaños de fuente en styles.xml a 11 pt
                AjustarEstilosBase(zip, w);
            }

            Console.WriteLine("[2/4] word/document.xml y styles.xml actualizados exitosamente con la nueva estructura técnica.");

            // 3. Actualizar campos automáticos de Word (TOC, Ilustraciones, Tablas, Páginas)
            Console.WriteLine("[3/4] Invocando automatización de Microsoft Word para actualizar campos e índices...");
            ActualizarCamposWord(dstDocx);

            Console.WriteLine("[4/4] ¡Proceso completado exitosamente!");
            Console.WriteLine($"Archivo generado: {dstDocx}");
        }

        static void ActualizarCamposWord(string docxPath)
        {
            try
            {
                Type? wordType = Type.GetTypeFromProgID("Word.Application");
                if (wordType == null)
                {
                    Console.WriteLine("ADVERTENCIA: Word.Application COM no disponible. El documento fue generado con estructura válida.");
                    return;
                }

                dynamic? word = Activator.CreateInstance(wordType);
                if (word == null) return;

                word.Visible = false;
                word.DisplayAlerts = 0; // wdAlertsNone
                dynamic doc = word.Documents.Open(docxPath, ReadOnly: false);

                try
                {
                    Console.WriteLine("    -> Actualizando Tablas de Contenido e Índices (TOC)...");
                    int tocIndex = 1;
                    foreach (dynamic toc in doc.TablesOfContents)
                    {
                        try 
                        { 
                            Console.WriteLine($"       * Actualizando TOC {tocIndex++}...");
                            toc.Update(); 
                        } 
                        catch (Exception ex)
                        {
                            Console.WriteLine($"       ! Error en TOC: {ex.Message}");
                        }
                    }

                    // Segunda sincronización de páginas
                    foreach (dynamic toc in doc.TablesOfContents)
                    {
                        try { toc.UpdatePageNumbers(); } catch { }
                    }

                    doc.Save();
                    Console.WriteLine("    -> Documento guardado con índices actualizados.");
                }
                finally
                {
                    doc.Close(false);
                    word.Quit();
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine($"Nota de actualización de índices: {ex.Message}");
            }
        }

        static void AjustarEstilosBase(ZipArchive zip, XNamespace w)
        {
            var stylesEntry = zip.GetEntry("word/styles.xml");
            if (stylesEntry == null) return;

            XDocument stylesXml;
            using (var reader = new StreamReader(stylesEntry.Open()))
            {
                stylesXml = XDocument.Load(reader);
            }

            // 1. Modificar Normal style
            var normalStyle = stylesXml.Descendants(w + "style").FirstOrDefault(s => (string?)s.Attribute(w + "styleId") == "Normal");
            if (normalStyle != null)
            {
                var rPr = normalStyle.Element(w + "rPr");
                if (rPr == null)
                {
                    rPr = new XElement(w + "rPr");
                    normalStyle.Add(rPr);
                }
                rPr.SetElementValue(w + "sz", null);
                rPr.Add(new XElement(w + "sz", new XAttribute(w + "val", "22")));
                rPr.SetElementValue(w + "szCs", null);
                rPr.Add(new XElement(w + "szCs", new XAttribute(w + "val", "22")));
            }

            // 2. Modificar docDefaults
            var docDefRpr = stylesXml.Descendants(w + "docDefaults").Descendants(w + "rPrDefault").Descendants(w + "rPr").FirstOrDefault();
            if (docDefRpr != null)
            {
                docDefRpr.SetElementValue(w + "sz", null);
                docDefRpr.Add(new XElement(w + "sz", new XAttribute(w + "val", "22")));
                docDefRpr.SetElementValue(w + "szCs", null);
                docDefRpr.Add(new XElement(w + "szCs", new XAttribute(w + "val", "22")));
            }

            stylesEntry.Delete();
            var newStylesEntry = zip.CreateEntry("word/styles.xml", CompressionLevel.Optimal);
            using (var writer = new StreamWriter(newStylesEntry.Open(), System.Text.Encoding.UTF8))
            {
                stylesXml.Save(writer);
            }
            Console.WriteLine("[INFO] Estilos base (Normal y docDefaults) configurados a 11 pt (22 half-points).");
        }
    }
}
