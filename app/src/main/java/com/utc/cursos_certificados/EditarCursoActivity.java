package com.utc.cursos_certificados;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*
@autores:Sandoval,Sanchez,Robayo
@creación: 19/06/2021
@Modificación: 19/06/2021
@descripción: Editar Curso
*/

public class EditarCursoActivity extends AppCompatActivity implements View.OnClickListener {

    //definicion de variables: para capturar los valores de elegir un ccurso en registro de curso
    String idC, nombreC, fechaInicioC, fechaFinalC, duracionC, precioC;

    TextView txtIdCursoEditar;
    EditText txtNombreCursoEditar, txtFechaInicioCursoEditar, txtFechaFinCursoEditar, txtDuracionCursoEditar, txtPrecioCursoEditar;
    Button btnFechaInicioEditar, btnFechaFinEditar;

    BaseDatos miBdd;// creando un objeto para acceder a los procesos de la BDD SQlite

    // Capturar fecha actual y fecha ingresada
    private int Anio_act, Mes_act, Dia_act;
    private int Anio_inicio, Mes_inicio, Dia_inicio;
    private int Anio_fin, Mes_fin, Dia_fin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_curso);

        //Mapeo de elementos
        txtIdCursoEditar=(TextView) findViewById(R.id.txtIdCursoEditar);
        txtNombreCursoEditar=(EditText) findViewById(R.id.txtNombreCursoEditar);
        txtFechaInicioCursoEditar=(EditText) findViewById(R.id.txtFechaInicioCursoEditar);
        txtFechaFinCursoEditar=(EditText) findViewById(R.id.txtFechaFinCursoEditar);
        txtDuracionCursoEditar=(EditText) findViewById(R.id.txtDuracionCursoEditar);
        txtPrecioCursoEditar=(EditText) findViewById(R.id.txtPrecioCursoEditar);

        btnFechaInicioEditar=(Button) findViewById(R.id.btnFechaInicioEditar);
        btnFechaInicioEditar.setOnClickListener(this); //al dar click
        btnFechaFinEditar=(Button) findViewById(R.id.btnFechaFinEditar);
        btnFechaFinEditar.setOnClickListener(this); //al dar click

        //objeto tipo Bundle que captura los parametros que se han pasado a esta actividad
        Bundle parametrosExtra = getIntent().getExtras();
        if (parametrosExtra != null){
            try {
                //usamos las variables declaradas
                idC = parametrosExtra.getString("idCurso");
                nombreC = parametrosExtra.getString("nombreCurso");
                fechaInicioC = parametrosExtra.getString("fechaInicioCurso");
                fechaFinalC = parametrosExtra.getString("fechaFinCurso");
                duracionC = parametrosExtra.getString("duracionCurso");
                precioC = parametrosExtra.getString("precioCurso");

            }catch (Exception ex){ //ex recibe el tipo de error
                Toast.makeText(getApplicationContext(), "Error al procesar la solicitud "+ex.toString(),
                        Toast.LENGTH_LONG).show();
            }
        }

        //presentar los datos recibidos de curso en pantalla
        txtIdCursoEditar.setText(idC);
        txtNombreCursoEditar.setText(nombreC);
        txtFechaInicioCursoEditar.setText(fechaInicioC);
        txtFechaFinCursoEditar.setText(fechaFinalC);
        txtDuracionCursoEditar.setText(duracionC);
        txtPrecioCursoEditar.setText(precioC);

        //instanciar /construir la base de datos en el objeto mi bdd
        miBdd= new BaseDatos(getApplicationContext());
    }

    //Boton Cancelar
    public void cancelarEditarCurso(View vista){
        finish();
        Intent ventanaRegistroCursos = new Intent(getApplicationContext(),RegistroCursosActivity.class);
        startActivity(ventanaRegistroCursos); //solicitando que se abra la ventana de gestion clientes
    }

    //Boton Actualizar
    public void actualizarCurso(View vista){
        //capturando los nuevos valores ingresados por el usuario
        String nombreCurso = txtNombreCursoEditar.getText().toString();
        String fechaInicio = txtFechaInicioCursoEditar.getText().toString();
        String fechaFinal = txtFechaFinCursoEditar.getText().toString();
        String duracion = txtDuracionCursoEditar.getText().toString();
        String precio = txtPrecioCursoEditar.getText().toString();

        //validaciones
        //campos vacios
        if (nombreCurso.isEmpty() ||fechaInicio.isEmpty() ||fechaFinal.isEmpty() ||duracion.isEmpty() || precio.isEmpty()){
            Toast.makeText(getApplicationContext(), "Para continuar con el registro llene todos los campos solicitados",
                    Toast.LENGTH_LONG).show(); //mostrando mensaje de campo vacio a traves de un toast
        } else {
            if (contieneSoloLetras(nombreCurso) == false) {
                Toast.makeText(getApplicationContext(), "El nombre no debe contener numeros",
                        Toast.LENGTH_LONG).show(); //mostrando error de nombre
            }  else { //DURACION diferente de 0
                int duracion2 = Integer.parseInt(duracion);
                if (duracion2 <= 0) {
                    Toast.makeText(getApplicationContext(), "La duración del curso debe ser mayor a 0 horas",
                            Toast.LENGTH_LONG).show();
                } else { //PRECIO diferente de 0
                    float precio2 = Float.parseFloat(precio);
                    if (precio2 > 0) {
                        miBdd.actualizarCurso(nombreCurso, fechaInicio,fechaFinal, duracion2, precio2, idC);
                        Toast.makeText(getApplicationContext(), "Actualizacion de curso exitosa",
                                Toast.LENGTH_LONG).show();
                        cancelarEditarCurso(null); //invocando al metodo volverCliente

                    }else{
                        Toast.makeText(getApplicationContext(), "El Precio debe ser un valor aceptable",
                                Toast.LENGTH_LONG).show();
                    }
                }

            }
        }
    }

    public boolean contieneSoloLetras(String cadena) {
        for (int x = 0; x < cadena.length(); x++) {
            char c = cadena.charAt(x);
            // Si no está entre a y z, ni entre A y Z, ni es un espacio
            if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == ' ' || c =='ñ' || c=='Ñ'
                    || c=='á' || c=='é' || c=='í' || c=='ó' || c=='ú'
                    || c=='Á' || c=='É' || c=='Í' || c=='Ó' || c=='Ú')) {
                return false;
            }
        }
        return true;
    }

    //METODOS PARA OBTENER Y VALIDAR LA FECHA DE CADUCIDAD
    @Override
    public void onClick(View v) {
        // obtenemos la fecha actual
        final Calendar calendario = Calendar.getInstance();
        Anio_act = calendario.get(Calendar.YEAR);
        Mes_act = calendario.get(Calendar.MONTH);
        Dia_act = calendario.get(Calendar.DAY_OF_MONTH);

        if (v == btnFechaInicioEditar) {
            //creamos la instancia de tipo date
            //this indicamos que se despliegue en el mismo formulario
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                //captura la fecha seleccionada por el usuario
                @Override
                public void onDateSet(DatePicker view, int anio, int mes, int dia) {
                    //las variables declaradas para fecha inicio toman la fecha seleccionada
                    Anio_inicio = anio;
                    Mes_inicio = mes;
                    Dia_inicio = dia;

                    String fechaDeInicioEditar = (Dia_inicio+"/"+(Mes_inicio + 1)+"/"+Anio_inicio);
                    validarFechaInicioCursoEditar(fechaDeInicioEditar);
                }
            }, Anio_act, Mes_act, Dia_act); //presenta picker con la fecha actual
            datePickerDialog.show(); //mostrando el date picker
        }

        if (v == btnFechaFinEditar) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                //captura la fecha seleccionada por el usuario
                @Override
                public void onDateSet(DatePicker view, int anio, int mes, int dia) {
                    Anio_fin = anio;
                    Mes_fin = mes;
                    Dia_fin = dia;

                    String fechaDeFinEditar = (Dia_fin+"/"+(Mes_fin + 1)+"/"+Anio_fin);
                    validarFechaFinCursoEditar(fechaDeFinEditar);
                }
            }, Anio_act, Mes_act, Dia_act); //presenta picker con la fecha actual
            datePickerDialog.show(); //mostrando el date picker
        }
    }

    public boolean validarFechaInicioCursoEditar(String fechaInicioCursoString){
        //formato de  fecha
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            //obtener fecha de hoy y se lo guarda en stringFechaHoy con tipo fecha
            String stringFechaHoy = df.format(Calendar.getInstance().getTime());

            //se obtiene la fecha del editText
            String fechaFinEditar = txtFechaFinCursoEditar.getText().toString();

            //casting de fechas a tipo Date
            Date fechaHoy =df.parse(stringFechaHoy);
            Date stringFechaFin =df.parse(fechaFinEditar);
            Date fechaInicioEditar = df.parse(fechaInicioCursoString);

            //si la fecha de incio editada es mayor a la actual y menor a la fecha final
            //si fecha inicio esta despues de la fecha actual devuelve true
            if(fechaInicioEditar.after(fechaHoy) && fechaInicioEditar.before(stringFechaFin) || fechaInicioEditar.equals(stringFechaFin)){
                txtFechaInicioCursoEditar.setText(Dia_inicio+"/"+(Mes_inicio + 1)+"/"+Anio_inicio);
                Toast.makeText(getApplicationContext(), "Fecha de inicio editada correctamente", Toast.LENGTH_LONG).show();
                return false;
            }else{
                Toast.makeText(getApplicationContext(), "Fecha seleccionada es incorrecta", Toast.LENGTH_LONG).show();
                txtFechaInicioCursoEditar.setText(fechaInicioC);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error de formato en la fecha", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public boolean validarFechaFinCursoEditar(String fechaFinCursoString){
        //formato de  fecha
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            //se obtiene la fecha del editText
            String fechaInicioEditar = txtFechaInicioCursoEditar.getText().toString();

            //casting de fechas a tipo Date
            Date stringFechaInicio =df.parse(fechaInicioEditar);
            Date fechaFinEditar = df.parse(fechaFinCursoString);

            //si la fecha final editada es mayor a la fecha de inicio
            //si fecha final esta despues de la fecha inicio o es igual devuelve true
            if(fechaFinEditar.after(stringFechaInicio) || fechaFinEditar.equals(stringFechaInicio)){
                txtFechaFinCursoEditar.setText(Dia_fin+"/"+(Mes_fin + 1)+"/"+Anio_fin);
                Toast.makeText(getApplicationContext(), "Fecha de finalizacion editada correctamente", Toast.LENGTH_LONG).show();
                return false;
            }else{
                Toast.makeText(getApplicationContext(), "Seleccione una fecha mayor a la fecha de inicio", Toast.LENGTH_LONG).show();
                txtFechaFinCursoEditar.setText(fechaFinalC);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error de formato en la fecha", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}