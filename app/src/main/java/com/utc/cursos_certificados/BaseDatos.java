package com.utc.cursos_certificados;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/*
@autores:Sandoval,sanchez,Robayo
@creación/ 19/06/2021
@fModificación 22/06/2021
@descripción: Base de Datos
*/
public class BaseDatos extends SQLiteOpenHelper {
    private static final String nombreBdd = "bdd_certificados"; //definiendo el nombre dela Bdd
    private static final int versionBdd = 5; //definiendo la version de la BDD

    //estructura de la tabla usuarios
    private static final String tablaUsuario = "create table usuario(id_usu integer primary key autoincrement," +
            "cedula_usu text, nombre_usu text, apellido_usu text, telefono_usu text, email_usu text, password_usu text)"; // definiendo estructura de la tabla usuarios

    //estructura de la tabla Curso
    private static final String tablaCurso = "create table curso(id_cur integer primary key autoincrement," +
            "nombre_cur text, fecha_inicio_cur text, fecha_fin_cur text, duracion_cur integer, precio_cur float);";

    //estructura de la tabla Estudiantes /esta relacionada con los cursos para que indique en que curso quiere inscribirse
    private static final String tablaEstudiante = "create table estudiante(id_est integer primary key autoincrement," +
            "cedula_est text, nombre_est text, apellido_est text, telefono_est text, email_est text, fk_id_curso integer, foreign key (fk_id_curso) references curso (id_cur))";

    //Constructor
    public BaseDatos(Context contexto) {
        super(contexto, nombreBdd, null, versionBdd);
    }

    //proceso 1 crear la BDD
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tablaUsuario); // ejecutando el query DDl(sentencia de definicion de datos) para crear la tabla usuarios con sus atributos
        db.execSQL(tablaEstudiante);// ejecutando el query DDl(sentencia de definicion de datos) para crear la tabla clientes
        db.execSQL(tablaCurso);
    }

    //proceso 2: metodo que se ejecuta automaticamente cuando se detectan cambios en la version de nuestra bdd
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //cuando se actualice
        db.execSQL("DROP TABLE IF EXISTS usuario");//elimincacion de la version anterior de la tabla usuarios se puee usar otro comando Dll como alter table
        db.execSQL(tablaUsuario); //Ejecucion del codigo para crear la tabla usuaios con su nueva estructura

        db.execSQL("DROP TABLE IF EXISTS curso");//elimincacion de la version anterior de la tabla usuarios se puee usar otro comando Dll como alter table
        db.execSQL(tablaCurso); //Ejecucion del codigo para crear la tabla usuaios con su nueva estructura

        db.execSQL("DROP TABLE IF EXISTS estudiante");//elimincacion de la version anterior de la tabla estudiante se puee usar otro comando Dll como alter table
        db.execSQL(tablaEstudiante); //Ejecucion del codigo para crear la tabla cliente con su nueva estructura

    }

    /*crud
         c -> create
         r -> leer
         U -> ACTUALIZAR
         D -> eliminar
     */

    //proceso 3: metodo para insertar datos de la tabla usuarios , retorno true cuando inserto y false cuando hay algun error
    public boolean agregarUsuario(String cedula, String nombre, String apellido, String telefono, String email, String password) {
        SQLiteDatabase miBdd = getWritableDatabase(); //llamando a la base de datos en el objeto mi Ddd
        if (miBdd != null) { //validando que la base de datos exista(q no sea nula)
            miBdd.execSQL("insert into usuario(cedula_usu, nombre_usu, apellido_usu, telefono_usu, email_usu, password_usu) " +
                    "values('"+ cedula + "','" + nombre + "','" + apellido + "','" + telefono +"','" + email + "','" + password + "')");    //ejecutando la sentencia de insercion de SQL
            miBdd.close(); //cerrando la conexion a la base de datos.
            return true; // valor de retorno si se inserto exitosamente.
        }
        return false; //retorno cuando no existe la BDD
    }

    //Proceso 4 para consultar usuarios por email y password.
    public Cursor obtenerUsuarioPorEmailPassword(String email, String password) {
        SQLiteDatabase miBdd = getWritableDatabase(); // llamado a la base de datos
        //crear un cursor donde inserto la consulta sql y almaceno los resultados en el objeto usuario
        Cursor usuario = miBdd.rawQuery("select * from usuario where " +
                "email_usu='" + email + "' and password_usu = '" + password + "';", null);

        //validar si existe o no la consulta
        if (usuario.moveToFirst()) { //metodo movetofirst nueve al primer elemento encontrado si hay el usuario
            return usuario; //retornamos los datos encontrados
        } else {
            //no se encuentra informacion de usuaio -> no existe o porque el email y password son incorrectos
            return null; //devuelvo null si no hay
        }
    }

    /*
    public Cursor obtenerEstudiantes(){
        SQLiteDatabase miBdd = getWritableDatabase(); //objeto para manejar la base de datos

        //consultando los productos en la base de datos y guardando en un cursor
        Cursor cursos=miBdd.rawQuery("select * from usuario;", null);
        //validar si se encontro o no clientes
        if (cursos.moveToFirst()){
            miBdd.close();
            //retornar el cursor que contiene el listado de cliente
            return cursos; // retornar el cursor que contiene el listado de productos
        }else{
            return null; //se retorna nulo cuando no hay productos dentro de la tabla
        }
    }

     */

    //CURSOS
    public boolean agregarCurso(String nombreCurso, String fechaInicio, String fechaFin, Integer duracionCurso,
                                Float precioCurso){
        SQLiteDatabase miBdd =getWritableDatabase();
        if (miBdd != null) { //validando que la base de datos exista(q no sea nula)
            miBdd.execSQL("insert into curso(nombre_cur, fecha_inicio_cur, fecha_fin_cur,duracion_cur, precio_cur) " +
                    "values  ('"+nombreCurso+"','"+fechaInicio+"','"+fechaFin+"','"+duracionCurso+"','"+precioCurso+"');");
            //ejecutando la sentencia de insercion de SQL
            miBdd.close(); //cerrando la conexion a la base de datos.
            return true; // valor de retorno si se inserto exitosamente.
        }
        return false; //retorno cuando no existe la BDD
    }


    //Metodo para consultar productos existentes en la base ded datos
    public Cursor obtenerCursos(){
        SQLiteDatabase miBdd = getWritableDatabase(); //objeto para manejar la base de datos

        //consultando los productos en la base de datos y guardando en un cursor
        Cursor cursos=miBdd.rawQuery("select * from curso;", null);

        if (cursos.moveToFirst()){ //validar si se encontro o no clientes
            miBdd.close();
            //retornar el cursor que contiene el listado de cliente
            return cursos; // retornar el cursor que contiene el listado de productos
        }else{
            return null; //se retorna nulo cuando no hay productos dentro de la tabla
        }
    }

    public Cursor obtenerCursoPorId(String id) {
        SQLiteDatabase miBdd = getWritableDatabase(); // llamado a la base de datos
        //crear un cursor donde inserto la consulta sql y almaceno los resultados en el objeto usuario
        Cursor unCurso = miBdd.rawQuery("select * from curso where  id_cur='"+id+"';", null);
        //validar si existe o no la consulta
        if (unCurso.moveToFirst()) { //metodo movetofirst nueve al primer elemento encontrado si hay el usuario
            return unCurso; //retornamos los datos encontrados
        } else {
            //no se encuentra informacion del curso -> no existe
            return null; //devuelvo null si no hay
        }
    }

    public Cursor obtenerEstudiantesPorCurso(String id) {
        SQLiteDatabase miBdd = getWritableDatabase(); // llamado a la base de datos
        //crear un cursor donde inserto la consulta sql y almaceno los resultados en el objeto usuario
        Cursor EstudiantesCurso = miBdd.rawQuery("select " +
                "curso.nombre_cur , curso.fecha_inicio_cur, curso.fecha_fin_cur , curso.duracion_cur, curso.precio_cur, " +
                "estudiante.id_est, estudiante.cedula_est , estudiante.nombre_est, estudiante.apellido_est, estudiante.telefono_est, estudiante.email_est " +
                "from curso inner join estudiante " +
                "on estudiante.fk_id_curso = curso.id_cur " +
                "where curso.id_cur =  '"+id+"';", null);
        //validar si existe o no la consulta
        if (EstudiantesCurso.moveToFirst()) { //metodo movetofirst nueve al primer elemento encontrado si hay el usuario
            return EstudiantesCurso; //retornamos los datos encontrados
        } else {
            //no se encuentra informacion del curso -> no existe
            return null; //devuelvo null si no hay
        }
    }

    public boolean actualizarCurso(String nombreCurso, String fechaInicio, String fechaFin, Integer duracionCurso,
                                   Float precioCurso, String id){
        SQLiteDatabase miBdd = getWritableDatabase(); // objeto para manejar la base de datos
        if(miBdd != null){
            //proceso de actualizacion
            miBdd.execSQL("update curso set nombre_cur='"+nombreCurso+"', " +
                    "fecha_inicio_cur='"+fechaInicio+"', fecha_fin_cur='"+fechaFin+"', " +
                    "duracion_cur='"+duracionCurso+"',precio_cur='"+precioCurso+"' where id_cur ="+id);
            miBdd.close(); //cerrando la conexion coon la BDD
            return true; //retornamos verdero ya que el proceso de actulaicacion fue exitoso
        }
        return false; // se retorna falso cuando no existe la base de datos
    }

    // CRUD para Estudiantes

    //agregar un nuevo estudiante
    public boolean agregarEstudiante(String cedula, String nombre, String apellido, String telefono, String email, Integer id_curso){
        SQLiteDatabase miBdd =getWritableDatabase();
        if (miBdd != null) { //validando que la base de datos exista(q no sea nula)
            miBdd.execSQL("insert into estudiante(cedula_est, nombre_est, apellido_est,telefono_est, email_est, fk_id_curso) " +
                    "values  ('"+cedula+"','"+nombre+"','"+apellido+"','"+telefono+"','"+email+"','"+id_curso+"');");
              //ejecutando la sentencia de insercion de SQL
            miBdd.close(); //cerrando la conexion a la base de datos.
            return true; // valor de retorno si se inserto exitosamente.
        }
        return false; //retorno cuando no existe la BDD
    }

    //Metodo para consultar estudiantes existentes en la base ded datos
    public Cursor obtenerEstudiantes(){
        SQLiteDatabase miBdd = getWritableDatabase(); //objeto para manejar la base de datos
        //consultando los clientes en la base de datos y guardando en un cursor
        Cursor estudiantes=miBdd.rawQuery("select * from estudiante;", null);
        //validar si se encontro o no clientes
        if (estudiantes.moveToFirst()){
            miBdd.close();
            //retornar el cursor que contiene el listado de cliente
            return estudiantes; // retornar el cursor que contiene el listado de clietnes
        }else{
            return null; //se retorna nulo cuando no hay clientes dentro de la tabla
        }
    }

    //metodo para actializar un estudiante
    public boolean actualizarEstudiante(String cedula, String nombre, String apellido, String telefono, String email, Integer id_curso, String id){
        SQLiteDatabase miBdd = getWritableDatabase(); // objeto para manejar la base de datos
        if(miBdd != null){
            //proceso de actualizacion
            miBdd.execSQL("update estudiante set cedula_est='"+cedula+"', " +
                    "nombre_est='"+nombre+"', apellido_est='"+apellido+"', " +
                    "telefono_est='"+telefono+"',email_est='"+email+"', fk_id_curso='"+id_curso+"' where id_est="+id);
            miBdd.close(); //cerrando la conexion coon la BDD
            return true; //retornamos verdero ya que el proceso de actulaicacion fue exitoso
        }
        return false; // se retorna falso cuando no existe la base de datos
    }

    //Metodo para eliminar un registro de un estudiante
    public boolean eliminarEstudiante(String id){
        SQLiteDatabase miBdd = getWritableDatabase(); // objeto para manejar la base de datos
        if(miBdd != null){ //validando que la bdd realmente exista
            miBdd.execSQL("delete from estudiante where id_est="+id); //ejecucion de la sentencia Sql para eliminar
            miBdd.close();
            return true; // //retornamos verdero ya que el proceso de eliminacion fue exitoso
        }
        return false; // se retorna falso cuando no existe la base de datos
    }


}




