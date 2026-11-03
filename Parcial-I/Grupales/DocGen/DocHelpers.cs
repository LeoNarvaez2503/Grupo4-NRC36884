using System;
using System.Collections.Generic;
using System.Xml.Linq;

namespace DocGen
{
    public static class DocHelpers
    {
        public static readonly XNamespace W = "http://schemas.openxmlformats.org/wordprocessingml/2006/main";

        public static XElement Paragraph(string text, string? style = null, bool bold = false, bool italic = false, string? color = null, int? size = null, string jc = "both")
        {
            var pPr = new XElement(W + "pPr",
                new XElement(W + "spacing", new XAttribute(W + "after", "120"), new XAttribute(W + "line", "240"), new XAttribute(W + "lineRule", "auto")),
                new XElement(W + "jc", new XAttribute(W + "val", jc))
            );

            if (!string.IsNullOrEmpty(style))
            {
                pPr.AddFirst(new XElement(W + "pStyle", new XAttribute(W + "val", style)));
            }

            int actualSize = size ?? 22; // 11 pt estándar institucional
            var rPr = new XElement(W + "rPr",
                new XElement(W + "rFonts", new XAttribute(W + "ascii", "Calibri"), new XAttribute(W + "hAnsi", "Calibri"), new XAttribute(W + "cs", "Calibri")),
                new XElement(W + "sz", new XAttribute(W + "val", actualSize.ToString())),
                new XElement(W + "szCs", new XAttribute(W + "val", actualSize.ToString()))
            );
            if (bold) rPr.Add(new XElement(W + "b"));
            if (italic) rPr.Add(new XElement(W + "i"));
            if (!string.IsNullOrEmpty(color)) rPr.Add(new XElement(W + "color", new XAttribute(W + "val", color)));

            var r = new XElement(W + "r", rPr, new XElement(W + "t", new XAttribute(XNamespace.Xml + "space", "preserve"), text));
            return new XElement(W + "p", pPr, r);
        }

        public static XElement BulletParagraph(string boldPrefix, string text)
        {
            var pPr = new XElement(W + "pPr",
                new XElement(W + "spacing", new XAttribute(W + "after", "80"), new XAttribute(W + "line", "240"), new XAttribute(W + "lineRule", "auto")),
                new XElement(W + "ind", new XAttribute(W + "left", "720"), new XAttribute(W + "hanging", "360")),
                new XElement(W + "jc", new XAttribute(W + "val", "both"))
            );

            var rBullet = new XElement(W + "r",
                new XElement(W + "rPr",
                    new XElement(W + "rFonts", new XAttribute(W + "ascii", "Calibri"), new XAttribute(W + "hAnsi", "Calibri")),
                    new XElement(W + "b"),
                    new XElement(W + "sz", new XAttribute(W + "val", "22")),
                    new XElement(W + "szCs", new XAttribute(W + "val", "22")),
                    new XElement(W + "color", new XAttribute(W + "val", "1E3A8A"))),
                new XElement(W + "t", new XAttribute(XNamespace.Xml + "space", "preserve"), "• ")
            );

            var rBold = new XElement(W + "r",
                new XElement(W + "rPr",
                    new XElement(W + "rFonts", new XAttribute(W + "ascii", "Calibri"), new XAttribute(W + "hAnsi", "Calibri")),
                    new XElement(W + "b"),
                    new XElement(W + "sz", new XAttribute(W + "val", "22")),
                    new XElement(W + "szCs", new XAttribute(W + "val", "22")),
                    new XElement(W + "color", new XAttribute(W + "val", "0F172A"))),
                new XElement(W + "t", new XAttribute(XNamespace.Xml + "space", "preserve"), boldPrefix + " ")
            );

            var rText = new XElement(W + "r",
                new XElement(W + "rPr",
                    new XElement(W + "rFonts", new XAttribute(W + "ascii", "Calibri"), new XAttribute(W + "hAnsi", "Calibri")),
                    new XElement(W + "sz", new XAttribute(W + "val", "22")),
                    new XElement(W + "szCs", new XAttribute(W + "val", "22")),
                    new XElement(W + "color", new XAttribute(W + "val", "334155"))),
                new XElement(W + "t", new XAttribute(XNamespace.Xml + "space", "preserve"), text)
            );

            return new XElement(W + "p", pPr, rBullet, rBold, rText);
        }

        public static XElement Heading1(string title) =>
            new XElement(W + "p",
                new XElement(W + "pPr",
                    new XElement(W + "pStyle", new XAttribute(W + "val", "Heading1")),
                    new XElement(W + "outlineLvl", new XAttribute(W + "val", "0")),
                    new XElement(W + "spacing", new XAttribute(W + "before", "280"), new XAttribute(W + "after", "140")),
                    new XElement(W + "jc", new XAttribute(W + "val", "left"))
                ),
                new XElement(W + "r",
                    new XElement(W + "rPr",
                        new XElement(W + "rFonts", new XAttribute(W + "ascii", "Calibri"), new XAttribute(W + "hAnsi", "Calibri"), new XAttribute(W + "cs", "Calibri")),
                        new XElement(W + "b"),
                        new XElement(W + "sz", new XAttribute(W + "val", "28")),
                        new XElement(W + "szCs", new XAttribute(W + "val", "28")),
                        new XElement(W + "color", new XAttribute(W + "val", "0F172A"))
                    ),
                    new XElement(W + "t", title)
                )
            );

        public static XElement Heading2(string title) =>
            new XElement(W + "p",
                new XElement(W + "pPr",
                    new XElement(W + "pStyle", new XAttribute(W + "val", "Heading2")),
                    new XElement(W + "outlineLvl", new XAttribute(W + "val", "1")),
                    new XElement(W + "spacing", new XAttribute(W + "before", "220"), new XAttribute(W + "after", "100")),
                    new XElement(W + "jc", new XAttribute(W + "val", "left"))
                ),
                new XElement(W + "r",
                    new XElement(W + "rPr",
                        new XElement(W + "rFonts", new XAttribute(W + "ascii", "Calibri"), new XAttribute(W + "hAnsi", "Calibri"), new XAttribute(W + "cs", "Calibri")),
                        new XElement(W + "b"),
                        new XElement(W + "sz", new XAttribute(W + "val", "24")),
                        new XElement(W + "szCs", new XAttribute(W + "val", "24")),
                        new XElement(W + "color", new XAttribute(W + "val", "1E3A8A"))
                    ),
                    new XElement(W + "t", title)
                )
            );

        public static XElement Heading3(string title) =>
            new XElement(W + "p",
                new XElement(W + "pPr",
                    new XElement(W + "pStyle", new XAttribute(W + "val", "Heading3")),
                    new XElement(W + "outlineLvl", new XAttribute(W + "val", "2")),
                    new XElement(W + "spacing", new XAttribute(W + "before", "180"), new XAttribute(W + "after", "80")),
                    new XElement(W + "jc", new XAttribute(W + "val", "left"))
                ),
                new XElement(W + "r",
                    new XElement(W + "rPr",
                        new XElement(W + "rFonts", new XAttribute(W + "ascii", "Calibri"), new XAttribute(W + "hAnsi", "Calibri"), new XAttribute(W + "cs", "Calibri")),
                        new XElement(W + "b"),
                        new XElement(W + "sz", new XAttribute(W + "val", "22")),
                        new XElement(W + "szCs", new XAttribute(W + "val", "22")),
                        new XElement(W + "color", new XAttribute(W + "val", "0369A1"))
                    ),
                    new XElement(W + "t", title)
                )
            );

        public static XElement FigureCaption(int figNum, string title) =>
            new XElement(W + "p",
                new XElement(W + "pPr",
                    new XElement(W + "pStyle", new XAttribute(W + "val", "Heading4")),
                    new XElement(W + "outlineLvl", new XAttribute(W + "val", "3")),
                    new XElement(W + "spacing", new XAttribute(W + "before", "80"), new XAttribute(W + "after", "180")),
                    new XElement(W + "jc", new XAttribute(W + "val", "center"))
                ),
                new XElement(W + "r",
                    new XElement(W + "rPr",
                        new XElement(W + "rFonts", new XAttribute(W + "ascii", "Calibri"), new XAttribute(W + "hAnsi", "Calibri"), new XAttribute(W + "cs", "Calibri")),
                        new XElement(W + "b"),
                        new XElement(W + "sz", new XAttribute(W + "val", "20")),
                        new XElement(W + "szCs", new XAttribute(W + "val", "20")),
                        new XElement(W + "color", new XAttribute(W + "val", "334155"))
                    ),
                    new XElement(W + "t", $"Figura {figNum}")
                ),
                new XElement(W + "r",
                    new XElement(W + "rPr",
                        new XElement(W + "rFonts", new XAttribute(W + "ascii", "Calibri"), new XAttribute(W + "hAnsi", "Calibri"), new XAttribute(W + "cs", "Calibri")),
                        new XElement(W + "b"),
                        new XElement(W + "sz", new XAttribute(W + "val", "20")),
                        new XElement(W + "szCs", new XAttribute(W + "val", "20")),
                        new XElement(W + "color", new XAttribute(W + "val", "334155"))
                    ),
                    new XElement(W + "br"),
                    new XElement(W + "t", title)
                )
            );

        public static XElement TableCaption(int tableNum, string title) =>
            new XElement(W + "p",
                new XElement(W + "pPr",
                    new XElement(W + "pStyle", new XAttribute(W + "val", "Heading5")),
                    new XElement(W + "outlineLvl", new XAttribute(W + "val", "4")),
                    new XElement(W + "spacing", new XAttribute(W + "before", "140"), new XAttribute(W + "after", "80")),
                    new XElement(W + "jc", new XAttribute(W + "val", "center"))
                ),
                new XElement(W + "r",
                    new XElement(W + "rPr",
                        new XElement(W + "rFonts", new XAttribute(W + "ascii", "Calibri"), new XAttribute(W + "hAnsi", "Calibri"), new XAttribute(W + "cs", "Calibri")),
                        new XElement(W + "b"),
                        new XElement(W + "sz", new XAttribute(W + "val", "20")),
                        new XElement(W + "szCs", new XAttribute(W + "val", "20")),
                        new XElement(W + "color", new XAttribute(W + "val", "334155"))
                    ),
                    new XElement(W + "t", $"Tabla {tableNum}")
                ),
                new XElement(W + "r",
                    new XElement(W + "rPr",
                        new XElement(W + "rFonts", new XAttribute(W + "ascii", "Calibri"), new XAttribute(W + "hAnsi", "Calibri"), new XAttribute(W + "cs", "Calibri")),
                        new XElement(W + "b"),
                        new XElement(W + "sz", new XAttribute(W + "val", "20")),
                        new XElement(W + "szCs", new XAttribute(W + "val", "20")),
                        new XElement(W + "color", new XAttribute(W + "val", "334155"))
                    ),
                    new XElement(W + "br"),
                    new XElement(W + "t", title)
                )
            );

        public static XElement ScreenshotBox(int figNum, string title, string entorno, string parametros, string accion, string resultado)
        {
            var pPr = new XElement(W + "pPr",
                new XElement(W + "spacing", new XAttribute(W + "before", "80"), new XAttribute(W + "after", "140"), new XAttribute(W + "line", "240"), new XAttribute(W + "lineRule", "auto")),
                new XElement(W + "jc", new XAttribute(W + "val", "left"))
            );

            var p = new XElement(W + "p", pPr,
                new XElement(W + "r",
                    new XElement(W + "rPr",
                        new XElement(W + "rFonts", new XAttribute(W + "ascii", "Calibri"), new XAttribute(W + "hAnsi", "Calibri")),
                        new XElement(W + "b"),
                        new XElement(W + "sz", new XAttribute(W + "val", "20")),
                        new XElement(W + "szCs", new XAttribute(W + "val", "20")),
                        new XElement(W + "color", new XAttribute(W + "val", "1E3A8A"))
                    ),
                    new XElement(W + "t", $"[ ESPACIO RESERVADO PARA CAPTURA: FIGURA {figNum} ]"),
                    new XElement(W + "br")
                ),
                CreateDescRun("• Entorno / Herramienta: ", entorno),
                new XElement(W + "r", new XElement(W + "br")),
                CreateDescRun("• Pantalla / Formulario: ", title),
                new XElement(W + "r", new XElement(W + "br")),
                CreateDescRun("• Parámetros / Datos: ", parametros),
                new XElement(W + "r", new XElement(W + "br")),
                CreateDescRun("• Acción a Realizar: ", accion),
                new XElement(W + "r", new XElement(W + "br")),
                CreateDescRun("• Resultado Visible: ", resultado)
            );

            return p;

            static IEnumerable<XElement> CreateDescRun(string lbl, string desc)
            {
                yield return new XElement(W + "r",
                    new XElement(W + "rPr",
                        new XElement(W + "rFonts", new XAttribute(W + "ascii", "Calibri"), new XAttribute(W + "hAnsi", "Calibri")),
                        new XElement(W + "b"),
                        new XElement(W + "sz", new XAttribute(W + "val", "19")),
                        new XElement(W + "szCs", new XAttribute(W + "val", "19")),
                        new XElement(W + "color", new XAttribute(W + "val", "1E3A8A"))
                    ),
                    new XElement(W + "t", new XAttribute(XNamespace.Xml + "space", "preserve"), lbl)
                );
                yield return new XElement(W + "r",
                    new XElement(W + "rPr",
                        new XElement(W + "rFonts", new XAttribute(W + "ascii", "Calibri"), new XAttribute(W + "hAnsi", "Calibri")),
                        new XElement(W + "sz", new XAttribute(W + "val", "19")),
                        new XElement(W + "szCs", new XAttribute(W + "val", "19")),
                        new XElement(W + "color", new XAttribute(W + "val", "334155"))
                    ),
                    new XElement(W + "t", desc)
                );
            }
        }

        public static XElement CodeBox(string code)
        {
            var lines = code.Trim().Split(new[] { "\r\n", "\r", "\n" }, StringSplitOptions.None);
            var paras = new List<XElement>();

            foreach (var line in lines)
            {
                var r = new XElement(W + "r",
                    new XElement(W + "rPr",
                        new XElement(W + "rFonts", new XAttribute(W + "ascii", "Consolas"), new XAttribute(W + "hAnsi", "Consolas"), new XAttribute(W + "cs", "Consolas")),
                        new XElement(W + "sz", new XAttribute(W + "val", "17")),
                        new XElement(W + "color", new XAttribute(W + "val", "0F172A"))
                    ),
                    new XElement(W + "t", new XAttribute(XNamespace.Xml + "space", "preserve"), string.IsNullOrEmpty(line) ? " " : line)
                );

                var p = new XElement(W + "p",
                    new XElement(W + "pPr",
                        new XElement(W + "spacing", new XAttribute(W + "after", "0"), new XAttribute(W + "line", "200"), new XAttribute(W + "lineRule", "auto"))
                    ),
                    r
                );
                paras.Add(p);
            }

            var tc = new XElement(W + "tc",
                new XElement(W + "tcPr",
                    new XElement(W + "tcW", new XAttribute(W + "w", "8504"), new XAttribute(W + "type", "dxa")),
                    new XElement(W + "shd", new XAttribute(W + "val", "clear"), new XAttribute(W + "color", "auto"), new XAttribute(W + "fill", "F1F5F9")),
                    new XElement(W + "tcMar",
                        new XElement(W + "top", new XAttribute(W + "w", "100"), new XAttribute(W + "type", "dxa")),
                        new XElement(W + "left", new XAttribute(W + "w", "140"), new XAttribute(W + "type", "dxa")),
                        new XElement(W + "bottom", new XAttribute(W + "w", "100"), new XAttribute(W + "type", "dxa")),
                        new XElement(W + "right", new XAttribute(W + "w", "140"), new XAttribute(W + "type", "dxa"))
                    )
                ),
                paras
            );

            var tbl = new XElement(W + "tbl",
                new XElement(W + "tblPr",
                    new XElement(W + "tblW", new XAttribute(W + "w", "8504"), new XAttribute(W + "type", "dxa")),
                    new XElement(W + "tblBorders",
                        new XElement(W + "top", new XAttribute(W + "val", "single"), new XAttribute(W + "sz", "6"), new XAttribute(W + "space", "0"), new XAttribute(W + "color", "CBD5E1")),
                        new XElement(W + "left", new XAttribute(W + "val", "single"), new XAttribute(W + "sz", "6"), new XAttribute(W + "space", "0"), new XAttribute(W + "color", "CBD5E1")),
                        new XElement(W + "bottom", new XAttribute(W + "val", "single"), new XAttribute(W + "sz", "6"), new XAttribute(W + "space", "0"), new XAttribute(W + "color", "CBD5E1")),
                        new XElement(W + "right", new XAttribute(W + "val", "single"), new XAttribute(W + "sz", "6"), new XAttribute(W + "space", "0"), new XAttribute(W + "color", "CBD5E1")),
                        new XElement(W + "insideH", new XAttribute(W + "val", "none")),
                        new XElement(W + "insideV", new XAttribute(W + "val", "none"))
                    ),
                    new XElement(W + "jc", new XAttribute(W + "val", "center"))
                ),
                new XElement(W + "tblGrid", new XElement(W + "gridCol", new XAttribute(W + "w", "8504"))),
                new XElement(W + "tr", tc)
            );

            return tbl;
        }

        public static XElement TechnicalTable(string[] colHeaders, int[] colWidths, List<string[]> rows)
        {
            int totalW = 0;
            foreach (var w in colWidths) totalW += w;

            var gridCols = new List<XElement>();
            foreach (var w in colWidths)
            {
                gridCols.Add(new XElement(W + "gridCol", new XAttribute(W + "w", w.ToString())));
            }

            var tblRows = new List<XElement>();

            // Header Row
            var hCells = new List<XElement>();
            for (int i = 0; i < colHeaders.Length; i++)
            {
                var p = new XElement(W + "p",
                    new XElement(W + "pPr",
                        new XElement(W + "spacing", new XAttribute(W + "before", "60"), new XAttribute(W + "after", "60")),
                        new XElement(W + "jc", new XAttribute(W + "val", "center"))
                    ),
                    new XElement(W + "r",
                        new XElement(W + "rPr", new XElement(W + "b"), new XElement(W + "color", new XAttribute(W + "val", "FFFFFF")), new XElement(W + "sz", new XAttribute(W + "val", "19"))),
                        new XElement(W + "t", colHeaders[i])
                    )
                );

                var tc = new XElement(W + "tc",
                    new XElement(W + "tcPr",
                        new XElement(W + "tcW", new XAttribute(W + "w", colWidths[i].ToString()), new XAttribute(W + "type", "dxa")),
                        new XElement(W + "shd", new XAttribute(W + "val", "clear"), new XAttribute(W + "color", "auto"), new XAttribute(W + "fill", "1E293B")),
                        new XElement(W + "tcMar",
                            new XElement(W + "top", new XAttribute(W + "w", "80"), new XAttribute(W + "type", "dxa")),
                            new XElement(W + "left", new XAttribute(W + "w", "100"), new XAttribute(W + "type", "dxa")),
                            new XElement(W + "bottom", new XAttribute(W + "w", "80"), new XAttribute(W + "type", "dxa")),
                            new XElement(W + "right", new XAttribute(W + "w", "100"), new XAttribute(W + "type", "dxa"))
                        )
                    ),
                    p
                );
                hCells.Add(tc);
            }
            tblRows.Add(new XElement(W + "tr", hCells));

            // Data Rows
            for (int rIdx = 0; rIdx < rows.Count; rIdx++)
            {
                var rowData = rows[rIdx];
                string fill = (rIdx % 2 == 1) ? "F8FAFC" : "FFFFFF";
                var dCells = new List<XElement>();

                for (int cIdx = 0; cIdx < colWidths.Length; cIdx++)
                {
                    string cellText = (cIdx < rowData.Length) ? rowData[cIdx] : "";
                    string align = (cIdx == 0) ? "left" : "left";

                    var p = new XElement(W + "p",
                        new XElement(W + "pPr",
                            new XElement(W + "spacing", new XAttribute(W + "before", "40"), new XAttribute(W + "after", "40")),
                            new XElement(W + "jc", new XAttribute(W + "val", align))
                        ),
                        new XElement(W + "r",
                            new XElement(W + "rPr", new XElement(W + "sz", new XAttribute(W + "val", "19")), new XElement(W + "color", new XAttribute(W + "val", "334155"))),
                            new XElement(W + "t", cellText)
                        )
                    );

                    var tc = new XElement(W + "tc",
                        new XElement(W + "tcPr",
                            new XElement(W + "tcW", new XAttribute(W + "w", colWidths[cIdx].ToString()), new XAttribute(W + "type", "dxa")),
                            new XElement(W + "shd", new XAttribute(W + "val", "clear"), new XAttribute(W + "color", "auto"), new XAttribute(W + "fill", fill)),
                            new XElement(W + "tcMar",
                                new XElement(W + "top", new XAttribute(W + "w", "70"), new XAttribute(W + "type", "dxa")),
                                new XElement(W + "left", new XAttribute(W + "w", "100"), new XAttribute(W + "type", "dxa")),
                                new XElement(W + "bottom", new XAttribute(W + "w", "70"), new XAttribute(W + "type", "dxa")),
                                new XElement(W + "right", new XAttribute(W + "w", "100"), new XAttribute(W + "type", "dxa"))
                            )
                        ),
                        p
                    );
                    dCells.Add(tc);
                }
                tblRows.Add(new XElement(W + "tr", dCells));
            }

            var tbl = new XElement(W + "tbl",
                new XElement(W + "tblPr",
                    new XElement(W + "tblW", new XAttribute(W + "w", totalW.ToString()), new XAttribute(W + "type", "dxa")),
                    new XElement(W + "tblBorders",
                        new XElement(W + "top", new XAttribute(W + "val", "single"), new XAttribute(W + "sz", "6"), new XAttribute(W + "space", "0"), new XAttribute(W + "color", "CBD5E1")),
                        new XElement(W + "left", new XAttribute(W + "val", "single"), new XAttribute(W + "sz", "6"), new XAttribute(W + "space", "0"), new XAttribute(W + "color", "CBD5E1")),
                        new XElement(W + "bottom", new XAttribute(W + "val", "single"), new XAttribute(W + "sz", "6"), new XAttribute(W + "space", "0"), new XAttribute(W + "color", "CBD5E1")),
                        new XElement(W + "right", new XAttribute(W + "val", "single"), new XAttribute(W + "sz", "6"), new XAttribute(W + "space", "0"), new XAttribute(W + "color", "CBD5E1")),
                        new XElement(W + "insideH", new XAttribute(W + "val", "single"), new XAttribute(W + "sz", "4"), new XAttribute(W + "space", "0"), new XAttribute(W + "color", "E2E8F0")),
                        new XElement(W + "insideV", new XAttribute(W + "val", "none"))
                    ),
                    new XElement(W + "jc", new XAttribute(W + "val", "center"))
                ),
                new XElement(W + "tblGrid", gridCols),
                tblRows
            );

            return tbl;
        }
    }
}
