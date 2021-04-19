package com.sdainfo.firebase.act;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.sdainfo.firebase.config.ConfiguraFirebase;
import com.sdainfo.firebase.helper.Mensagens;
import com.sdainfo.firebase.R;
import com.sdainfo.firebase.model.Usuario;

public class MainActivity extends AppCompatActivity {
    private EditText et_email, et_senha;
    private Button bt_logar;

    private FirebaseAuth autenticacao;
    private Usuario usuario;
    private Mensagens mensagens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniciaComponentes();
    }

    public void onclickMain(View view) {
        logarUsuario();
    }

    private void iniciaComponentes() {
        et_email = findViewById(R.id.email);
        et_senha = findViewById(R.id.senha);
        bt_logar = findViewById(R.id.button);
        usuario = new Usuario();
        mensagens = new Mensagens(getApplicationContext());
    }

    private void logarUsuario() {
        autenticacao = ConfiguraFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                iniciaPrincipal();
            } else {
                String msgExcecao = "";
                try {
                    throw task.getException();
                } catch (FirebaseAuthInvalidCredentialsException e) {
                    msgExcecao = "Dados inválidos.";
                } catch (FirebaseAuthInvalidUserException e) {
                    msgExcecao = "Usuário não cadastrado.";
                } catch (Exception e) {
                    msgExcecao = "Erro ao cadastrar usuário : " + e.getMessage();
                    e.printStackTrace();
                }
                mensagens.msgCurta(msgExcecao);
            }
        });
    }

    private void iniciaPrincipal() {
        limpaCampos();
        startActivity(new Intent(MainActivity.this, PrincipalActivity.class));
        finish();
    }

    private void limpaCampos() {
        et_email.setText("");
        et_senha.setText("");
    }

    public void criarUsuario(View view) {
        startActivity(new Intent(MainActivity.this, PrincipalActivity.class));
        limpaCampos();
    }
}