package com.example.vanapp;
import com.example.vanapp.Common.Utilidades;
import org.junit.Test;
import static org.junit.Assert.*;

public class UtilidadesTest {

    @Test
    public void utilidades_esEmailCorrecto() {
        //Arrange
        String emailValido = "david.santesteban@yahoo.es";
        String emailIncorrecto = "davidsantestebanyahooes";

        //Act
        Utilidades.esEmailValido(emailValido);

        //Assert
        assertEquals(true, Utilidades.esEmailValido(emailValido));
        assertEquals(false, Utilidades.esEmailValido(emailIncorrecto));
    }

    @Test
    public void utilidades_esPalabraLongitudExperada() {
        //Arrange
        int longitudPalabraEsperada = 10;
        String palabraDevuelta = "";

        //Act
        palabraDevuelta = Utilidades.generarRandomString(longitudPalabraEsperada);

        //Assert
        assertEquals(longitudPalabraEsperada, palabraDevuelta.length());
    }
}
