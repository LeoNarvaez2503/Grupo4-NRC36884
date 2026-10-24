namespace ec.edu.monster.vista
{
    partial class ConversorView
    {
        private System.ComponentModel.IContainer components = null;

        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        private void InitializeComponent()
        {
            picSulley = new PictureBox();
            lblTitulo = new Label();
            lblTipo = new Label();
            cmbMagnitud = new ComboBox();
            txtValor = new TextBox();
            cmbUndInit = new ComboBox();
            lblA = new Label();
            cmbUndFinal = new ComboBox();
            btnConvertir = new Button();
            lblResultado = new Label();
            conversionResultado = new Label();
            ((System.ComponentModel.ISupportInitialize)picSulley).BeginInit();
            SuspendLayout();
            // 
            // picSulley
            // 
            picSulley.Location = new Point(230, 8);
            picSulley.Name = "picSulley";
            picSulley.Size = new Size(70, 70);
            picSulley.SizeMode = PictureBoxSizeMode.Zoom;
            picSulley.TabIndex = 0;
            picSulley.TabStop = false;
            // 
            // lblTitulo
            // 
            lblTitulo.AutoSize = true;
            lblTitulo.Font = new Font("Segoe UI", 16F, FontStyle.Bold, GraphicsUnit.Point);
            lblTitulo.Location = new Point(140, 85);
            lblTitulo.Name = "lblTitulo";
            lblTitulo.Size = new Size(250, 30);
            lblTitulo.TabIndex = 1;
            lblTitulo.Text = "Conversor de Unidades";
            lblTitulo.TextAlign = ContentAlignment.MiddleCenter;
            // 
            // lblTipo
            // 
            lblTipo.AutoSize = true;
            lblTipo.Font = new Font("Segoe UI", 12F, FontStyle.Bold, GraphicsUnit.Point);
            lblTipo.Location = new Point(25, 138);
            lblTipo.Name = "lblTipo";
            lblTipo.Size = new Size(89, 21);
            lblTipo.TabIndex = 2;
            lblTipo.Text = "Magnitud:";
            // 
            // cmbMagnitud
            // 
            cmbMagnitud.DropDownStyle = ComboBoxStyle.DropDownList;
            cmbMagnitud.Font = new Font("Segoe UI", 11F, FontStyle.Regular, GraphicsUnit.Point);
            cmbMagnitud.FormattingEnabled = true;
            cmbMagnitud.Items.AddRange(new object[] { "LONGITUD", "MASA", "TEMPERATURA" });
            cmbMagnitud.Location = new Point(120, 135);
            cmbMagnitud.Name = "cmbMagnitud";
            cmbMagnitud.Size = new Size(160, 28);
            cmbMagnitud.TabIndex = 3;
            cmbMagnitud.SelectedIndexChanged += cmbMagnitud_SelectedIndexChanged;
            // 
            // txtValor
            // 
            txtValor.Font = new Font("Segoe UI", 12F, FontStyle.Regular, GraphicsUnit.Point);
            txtValor.Location = new Point(300, 135);
            txtValor.Name = "txtValor";
            txtValor.Size = new Size(195, 29);
            txtValor.TabIndex = 4;
            txtValor.KeyDown += txtValor_KeyDown;
            // 
            // cmbUndInit
            // 
            cmbUndInit.DropDownStyle = ComboBoxStyle.DropDownList;
            cmbUndInit.Font = new Font("Segoe UI", 10.5F, FontStyle.Regular, GraphicsUnit.Point);
            cmbUndInit.FormattingEnabled = true;
            cmbUndInit.Location = new Point(25, 185);
            cmbUndInit.Name = "cmbUndInit";
            cmbUndInit.Size = new Size(155, 27);
            cmbUndInit.TabIndex = 5;
            // 
            // lblA
            // 
            lblA.AutoSize = true;
            lblA.Font = new Font("Segoe UI", 14F, FontStyle.Bold, GraphicsUnit.Point);
            lblA.Location = new Point(195, 185);
            lblA.Name = "lblA";
            lblA.Size = new Size(22, 25);
            lblA.TabIndex = 6;
            lblA.Text = "a";
            // 
            // cmbUndFinal
            // 
            cmbUndFinal.DropDownStyle = ComboBoxStyle.DropDownList;
            cmbUndFinal.Font = new Font("Segoe UI", 10.5F, FontStyle.Regular, GraphicsUnit.Point);
            cmbUndFinal.FormattingEnabled = true;
            cmbUndFinal.Location = new Point(230, 185);
            cmbUndFinal.Name = "cmbUndFinal";
            cmbUndFinal.Size = new Size(155, 27);
            cmbUndFinal.TabIndex = 7;
            // 
            // btnConvertir
            // 
            btnConvertir.Font = new Font("Segoe UI", 11F, FontStyle.Bold, GraphicsUnit.Point);
            btnConvertir.Location = new Point(400, 183);
            btnConvertir.Name = "btnConvertir";
            btnConvertir.Size = new Size(105, 32);
            btnConvertir.TabIndex = 8;
            btnConvertir.Text = "Convertir";
            btnConvertir.UseVisualStyleBackColor = true;
            btnConvertir.Click += btnConvertir_Click;
            // 
            // lblResultado
            // 
            lblResultado.AutoSize = true;
            lblResultado.Font = new Font("Segoe UI", 13F, FontStyle.Regular, GraphicsUnit.Point);
            lblResultado.Location = new Point(80, 245);
            lblResultado.Name = "lblResultado";
            lblResultado.Size = new Size(94, 25);
            lblResultado.TabIndex = 9;
            lblResultado.Text = "Resultado:";
            // 
            // conversionResultado
            // 
            conversionResultado.AutoSize = true;
            conversionResultado.Font = new Font("Segoe UI", 14F, FontStyle.Bold, GraphicsUnit.Point);
            conversionResultado.Location = new Point(185, 245);
            conversionResultado.Name = "conversionResultado";
            conversionResultado.Size = new Size(0, 25);
            conversionResultado.TabIndex = 10;
            // 
            // ConversorView
            // 
            AutoScaleDimensions = new SizeF(7F, 15F);
            AutoScaleMode = AutoScaleMode.Font;
            ClientSize = new Size(530, 310);
            Controls.Add(conversionResultado);
            Controls.Add(lblResultado);
            Controls.Add(btnConvertir);
            Controls.Add(cmbUndFinal);
            Controls.Add(lblA);
            Controls.Add(cmbUndInit);
            Controls.Add(txtValor);
            Controls.Add(cmbMagnitud);
            Controls.Add(lblTipo);
            Controls.Add(lblTitulo);
            Controls.Add(picSulley);
            FormBorderStyle = FormBorderStyle.FixedSingle;
            MaximizeBox = false;
            Name = "ConversorView";
            StartPosition = FormStartPosition.CenterScreen;
            Text = "Conversor de Unidades";
            FormClosed += ConversorView_FormClosed;
            ((System.ComponentModel.ISupportInitialize)picSulley).EndInit();
            ResumeLayout(false);
            PerformLayout();
        }

        private PictureBox picSulley;
        private Label lblTitulo;
        private Label lblTipo;
        private ComboBox cmbMagnitud;
        private TextBox txtValor;
        private ComboBox cmbUndInit;
        private Label lblA;
        private ComboBox cmbUndFinal;
        private Button btnConvertir;
        private Label lblResultado;
        private Label conversionResultado;
    }
}
