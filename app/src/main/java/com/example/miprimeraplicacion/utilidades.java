package com.example.miprimeraplicacion;

import android.util.Base64;

public class utilidades {
    static String url_consulta = "http://192.168.80.191:5984/agenda/_desing/agenda/_view/agenda";
    static String url_mto = "http://192.168.80.191:5984/agenda";
    static String user = "admin";
    static String passwd = "12/12/20";
    static String credencialesCodificadas = Base64.getEncoder().encodeToString((user + ":" + passwd).getBytes());

    public String generarUnicoId() {
        return java.util.UUID.randomUUID().toString();
    }
}
