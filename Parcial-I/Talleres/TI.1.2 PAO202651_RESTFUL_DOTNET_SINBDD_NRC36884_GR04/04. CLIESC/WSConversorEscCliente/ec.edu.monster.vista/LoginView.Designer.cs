namespace ec.edu.monster.vista
{
    partial class LoginView
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
            lblUsuario = new Label();
            txtUsuario = new TextBox();
            lblPassword = new Label();
            txtPassword = new TextBox();
            chkMostrar = new CheckBox();
            btnIngresar = new Button();
            btnLimpiar = new Button();
            ((System.ComponentModel.ISupportInitialize)picSulley).BeginInit();
            SuspendLayout();
            // 
            // picSulley
            // 
            picSulley.Location = new Point(165, 12);
            picSulley.Name = "picSulley";
            picSulley.Size = new Size(95, 95);
            picSulley.SizeMode = PictureBoxSizeMode.Zoom;
            picSulley.TabIndex = 0;
            picSulley.TabStop = false;
            // 
            // lblTitulo
            // 
            lblTitulo.AutoSize = true;
            lblTitulo.Font = new Font("Segoe UI", 18F, FontStyle.Bold, GraphicsUnit.Point);
            lblTitulo.Location = new Point(115, 115);
            lblTitulo.Name = "lblTitulo";
            lblTitulo.Size = new Size(198, 32);
            lblTitulo.TabIndex = 1;
            lblTitulo.Text = "INICIAR SESIÓN";
            lblTitulo.TextAlign = ContentAlignment.MiddleCenter;
            // 
            // lblUsuario
            // 
            lblUsuario.AutoSize = true;
            lblUsuario.Font = new Font("Segoe UI", 12F, FontStyle.Bold, GraphicsUnit.Point);
            lblUsuario.Location = new Point(35, 168);
            lblUsuario.Name = "lblUsuario";
            lblUsuario.Size = new Size(73, 21);
            lblUsuario.TabIndex = 2;
            lblUsuario.Text = "Usuario:";
            // 
            // txtUsuario
            // 
            txtUsuario.Font = new Font("Segoe UI", 12F, FontStyle.Regular, GraphicsUnit.Point);
            txtUsuario.Location = new Point(145, 165);
            txtUsuario.Name = "txtUsuario";
            txtUsuario.Size = new Size(225, 29);
            txtUsuario.TabIndex = 3;
            txtUsuario.KeyDown += txtUsuario_KeyDown;
            // 
            // lblPassword
            // 
            lblPassword.AutoSize = true;
            lblPassword.Font = new Font("Segoe UI", 12F, FontStyle.Bold, GraphicsUnit.Point);
            lblPassword.Location = new Point(35, 218);
            lblPassword.Name = "lblPassword";
            lblPassword.Size = new Size(100, 21);
            lblPassword.TabIndex = 4;
            lblPassword.Text = "Contraseña:";
            // 
            // txtPassword
            // 
            txtPassword.Font = new Font("Segoe UI", 12F, FontStyle.Regular, GraphicsUnit.Point);
            txtPassword.Location = new Point(145, 215);
            txtPassword.Name = "txtPassword";
            txtPassword.PasswordChar = '•';
            txtPassword.Size = new Size(225, 29);
            txtPassword.TabIndex = 5;
            txtPassword.KeyDown += txtPassword_KeyDown;
            // 
            // chkMostrar
            // 
            chkMostrar.AutoSize = true;
            chkMostrar.Font = new Font("Segoe UI", 9.75F, FontStyle.Regular, GraphicsUnit.Point);
            chkMostrar.Location = new Point(145, 255);
            chkMostrar.Name = "chkMostrar";
            chkMostrar.Size = new Size(140, 21);
            chkMostrar.TabIndex = 6;
            chkMostrar.Text = "Mostrar contraseña";
            chkMostrar.UseVisualStyleBackColor = true;
            chkMostrar.CheckedChanged += chkMostrar_CheckedChanged;
            // 
            // btnIngresar
            // 
            btnIngresar.Font = new Font("Segoe UI", 11.25F, FontStyle.Bold, GraphicsUnit.Point);
            btnIngresar.Location = new Point(115, 295);
            btnIngresar.Name = "btnIngresar";
            btnIngresar.Size = new Size(120, 38);
            btnIngresar.TabIndex = 7;
            btnIngresar.Text = "Ingresar";
            btnIngresar.UseVisualStyleBackColor = true;
            btnIngresar.Click += btnIngresar_Click;
            // 
            // btnLimpiar
            // 
            btnLimpiar.Font = new Font("Segoe UI", 11.25F, FontStyle.Regular, GraphicsUnit.Point);
            btnLimpiar.Location = new Point(250, 295);
            btnLimpiar.Name = "btnLimpiar";
            btnLimpiar.Size = new Size(120, 38);
            btnLimpiar.TabIndex = 8;
            btnLimpiar.Text = "Limpiar";
            btnLimpiar.UseVisualStyleBackColor = true;
            btnLimpiar.Click += btnLimpiar_Click;
            // 
            // LoginView
            // 
            AutoScaleDimensions = new SizeF(7F, 15F);
            AutoScaleMode = AutoScaleMode.Font;
            ClientSize = new Size(410, 360);
            Controls.Add(btnLimpiar);
            Controls.Add(btnIngresar);
            Controls.Add(chkMostrar);
            Controls.Add(txtPassword);
            Controls.Add(lblPassword);
            Controls.Add(txtUsuario);
            Controls.Add(lblUsuario);
            Controls.Add(lblTitulo);
            Controls.Add(picSulley);
            FormBorderStyle = FormBorderStyle.FixedSingle;
            MaximizeBox = false;
            Name = "LoginView";
            StartPosition = FormStartPosition.CenterScreen;
            Text = "Iniciar Sesión";
            ((System.ComponentModel.ISupportInitialize)picSulley).EndInit();
            ResumeLayout(false);
            PerformLayout();
        }

        private PictureBox picSulley;
        private Label lblTitulo;
        private Label lblUsuario;
        private TextBox txtUsuario;
        private Label lblPassword;
        private TextBox txtPassword;
        private CheckBox chkMostrar;
        private Button btnIngresar;
        private Button btnLimpiar;
    }
}
