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

        //Assert
        assertEquals(true, Utilidades.esEmailValido(emailValido));
        assertEquals(false, Utilidades.esEmailValido(emailIncorrecto));
    }

    @Test
    public void utilidades_esTelefonoCorrecto() {
        //Arrange
        String telefonoValido1 = "666 521 178";
        String telefonoValido2 = "666521178";
        String telefonoIncorrecto1 = "66178";
        String telefonoIncorrecto2 = "aaa";

        //Act

        //Assert
        assertEquals(true, Utilidades.esTelefonoValido(telefonoValido1));
        assertEquals(true, Utilidades.esTelefonoValido(telefonoValido2));
        assertEquals(false, Utilidades.esEmailValido(telefonoIncorrecto1));
        assertEquals(false, Utilidades.esEmailValido(telefonoIncorrecto2));
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
