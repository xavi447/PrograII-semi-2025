package com.example.miprimeraplicacion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor; // <<<< Añadir esta importación
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB_multas extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "multas_app.db";
    private static final int DATABASE_VERSION = 1;

    // Nombre de la tabla
    private static final String TABLE_MULTAS = "multas";

    // Columnas de la tabla multas
    private static final String COLUMN_ID = "id_multa";
    private static final String COLUMN_FECHA_HORA = "fecha_hora"; // Formato TEXT (timestamp o string)
    private static final String COLUMN_TIPO_INFRACCION = "tipo_infraccion"; // Corresponde a multaCategoria
    private static final String COLUMN_MONTO = "monto";
    private static final String COLUMN_UBICACION_DEPARTAMENTO = "ubicacion_departamento";
    private static final String COLUMN_UBICACION_MUNICIPIO = "ubicacion_municipio";
    private static final String COLUMN_UBICACION_CALLE = "ubicacion_calle";
    private static final String COLUMN_EVIDENCIA_FOTOS_PATHS = "evidencia_fotos_paths"; // Rutas a las imágenes, separadas por coma

    // Datos del Agente (referencia o copia de datos importantes)
    private static final String COLUMN_AGENTE_USUARIO = "agente_usuario"; // Usuario que logueó
    private static final String COLUMN_AGENTE_ONI = "agente_oni";
    private static final String COLUMN_AGENTE_PUESTO = "agente_puesto";

    // Datos del Conductor
    private static final String COLUMN_CONDUCTOR_LICENCIA = "conductor_licencia"; // Clave foránea o simplemente el dato
    private static final String COLUMN_CONDUCTOR_APELLIDO1 = "conductor_apellido1";
    private static final String COLUMN_CONDUCTOR_APELLIDO2 = "conductor_apellido2";
    private static final String COLUMN_CONDUCTOR_APELLIDO3 = "conductor_apellido3"; // <<<< Añadido
    private static final String COLUMN_CONDUCTOR_NOMBRE1 = "conductor_nombre1";
    private static final String COLUMN_CONDUCTOR_NOMBRE2 = "conductor_nombre2";   // <<<< Añadido
    private static final String COLUMN_CONDUCTOR_NOMBRE3 = "conductor_nombre3";   // <<<< Añadido
    private static final String COLUMN_CONDUCTOR_CLASE_LICENCIA = "conductor_clase_licencia";

    // Datos del Vehículo
    private static final String COLUMN_VEHICULO_TIPO_PLACA = "vehiculo_tipo_placa";
    private static final String COLUMN_VEHICULO_NUMERO_PLACA = "vehiculo_numero_placa"; // Clave foránea o simplemente el dato
    private static final String COLUMN_VEHICULO_CODIGO_RUTA = "vehiculo_codigo_ruta";
    private static final String COLUMN_VEHICULO_PLACA_EXTRANJERA = "vehiculo_placa_extranjera";
    private static final String COLUMN_VEHICULO_CLASE = "vehiculo_clase"; // Clase de vehículo (Automóvil, Bus, etc.)
    private static final String COLUMN_VEHICULO_MARCA = "vehiculo_marca";
    private static final String COLUMN_VEHICULO_MODELO = "vehiculo_modelo";
    private static final String COLUMN_VEHICULO_COLOR = "vehiculo_color"; // <<<< Añadido

    // Sentencia SQL para crear la tabla de multas
    private static final String CREATE_TABLE_MULTAS =
            "CREATE TABLE " + TABLE_MULTAS + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_FECHA_HORA + " TEXT NOT NULL," +
                    COLUMN_TIPO_INFRACCION + " TEXT NOT NULL," +
                    COLUMN_MONTO + " REAL NOT NULL," + // Uso REAL para el monto para permitir decimales
                    COLUMN_UBICACION_DEPARTAMENTO + " TEXT," +
                    COLUMN_UBICACION_MUNICIPIO + " TEXT," +
                    COLUMN_UBICACION_CALLE + " TEXT," +
                    COLUMN_EVIDENCIA_FOTOS_PATHS + " TEXT," + // Guardará las rutas a las fotos
                    COLUMN_AGENTE_USUARIO + " TEXT NOT NULL," +
                    COLUMN_AGENTE_ONI + " TEXT," +
                    COLUMN_AGENTE_PUESTO + " TEXT," +
                    COLUMN_CONDUCTOR_LICENCIA + " TEXT," +
                    COLUMN_CONDUCTOR_APELLIDO1 + " TEXT," +
                    COLUMN_CONDUCTOR_APELLIDO2 + " TEXT," +
                    COLUMN_CONDUCTOR_APELLIDO3 + " TEXT," + // <<<< Añadido a la tabla
                    COLUMN_CONDUCTOR_NOMBRE1 + " TEXT," +
                    COLUMN_CONDUCTOR_NOMBRE2 + " TEXT," +   // <<<< Añadido a la tabla
                    COLUMN_CONDUCTOR_NOMBRE3 + " TEXT," +   // <<<< Añadido a la tabla
                    COLUMN_CONDUCTOR_CLASE_LICENCIA + " TEXT," +
                    COLUMN_VEHICULO_TIPO_PLACA + " TEXT," +
                    COLUMN_VEHICULO_NUMERO_PLACA + " TEXT," +
                    COLUMN_VEHICULO_CODIGO_RUTA + " TEXT," +
                    COLUMN_VEHICULO_PLACA_EXTRANJERA + " INTEGER," +
                    COLUMN_VEHICULO_CLASE + " TEXT," +
                    COLUMN_VEHICULO_MARCA + " TEXT," +
                    COLUMN_VEHICULO_MODELO + " TEXT," +
                    COLUMN_VEHICULO_COLOR + " TEXT" +      // <<<< Añadido a la tabla
                    ")";

    public DB_multas(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MULTAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // En una aplicación real, aquí deberías manejar las migraciones de datos
        // Para fines de desarrollo, es común simplemente eliminar y recrear
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MULTAS);
        onCreate(db);
    }

    /**
     * Inserta una nueva multa en la base de datos con todos los detalles.
     * @return El ID de la nueva fila insertada, o -1 si hubo un error.
     */
    public long insertarMulta(
            String usuarioLogueado, String agenteOni, String agentePuesto,
            String conductorLicencia, String conductorApellido1, String conductorApellido2, String conductorApellido3,
            String conductorNombre1, String conductorNombre2, String conductorNombre3, String conductorClaseLicencia,
            String vehiculoTipoPlaca, String vehiculoNumeroPlaca, String vehiculoCodigoRuta, int vehiculoPlacaExtranjera,
            String vehiculoClase, String vehiculoMarca, String vehiculoModelo, String vehiculoColor,
            String fechaHoraInfraccion, String fotosEvidenciaPaths, String multaMonto, String multaCategoria,
            String ubicacionDepartamento, String ubicacionMunicipio, String ubicacionCalle) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Datos de la Multa principal
        values.put(COLUMN_FECHA_HORA, fechaHoraInfraccion);
        values.put(COLUMN_TIPO_INFRACCION, multaCategoria);
        values.put(COLUMN_MONTO, Double.parseDouble(multaMonto)); // Convertir a Double para REAL

        // Datos de Ubicación
        values.put(COLUMN_UBICACION_DEPARTAMENTO, ubicacionDepartamento);
        values.put(COLUMN_UBICACION_MUNICIPIO, ubicacionMunicipio);
        values.put(COLUMN_UBICACION_CALLE, ubicacionCalle);
        values.put(COLUMN_EVIDENCIA_FOTOS_PATHS, fotosEvidenciaPaths);

        // Datos del Agente
        values.put(COLUMN_AGENTE_USUARIO, usuarioLogueado);
        values.put(COLUMN_AGENTE_ONI, agenteOni);
        values.put(COLUMN_AGENTE_PUESTO, agentePuesto);

        // Datos del Conductor
        values.put(COLUMN_CONDUCTOR_LICENCIA, conductorLicencia);
        values.put(COLUMN_CONDUCTOR_APELLIDO1, conductorApellido1);
        values.put(COLUMN_CONDUCTOR_APELLIDO2, conductorApellido2);
        values.put(COLUMN_CONDUCTOR_APELLIDO3, conductorApellido3);
        values.put(COLUMN_CONDUCTOR_NOMBRE1, conductorNombre1);
        values.put(COLUMN_CONDUCTOR_NOMBRE2, conductorNombre2);
        values.put(COLUMN_CONDUCTOR_NOMBRE3, conductorNombre3);
        values.put(COLUMN_CONDUCTOR_CLASE_LICENCIA, conductorClaseLicencia);

        // Datos del Vehículo
        values.put(COLUMN_VEHICULO_TIPO_PLACA, vehiculoTipoPlaca);
        values.put(COLUMN_VEHICULO_NUMERO_PLACA, vehiculoNumeroPlaca);
        values.put(COLUMN_VEHICULO_CODIGO_RUTA, vehiculoCodigoRuta);
        values.put(COLUMN_VEHICULO_PLACA_EXTRANJERA, vehiculoPlacaExtranjera); // int (0 o 1)
        values.put(COLUMN_VEHICULO_CLASE, vehiculoClase);
        values.put(COLUMN_VEHICULO_MARCA, vehiculoMarca);
        values.put(COLUMN_VEHICULO_MODELO, vehiculoModelo);
        values.put(COLUMN_VEHICULO_COLOR, vehiculoColor);

        long id = db.insert(TABLE_MULTAS, null, values);
        db.close();
        return id;
    }

    // Puedes añadir otros métodos aquí para consultar multas si los necesitas en el futuro
    public Cursor obtenerTodasLasMultas() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_MULTAS, null, null, null, null, null, COLUMN_ID + " DESC");
    }
}