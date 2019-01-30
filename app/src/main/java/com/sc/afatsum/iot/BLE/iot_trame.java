package com.sc.afatsum.iot.BLE;

/**
 * Created by Mustafa on 13/07/2017.
 */

public class iot_trame {
    private String trame;
    private String fonctionCode = "";
    private String parametreCode = "";
    private String modeCode = "";
    private int Value = 0;

    public String getFonctionCode() {
        return fonctionCode;
    }

    public String getParametreCode() {
        return parametreCode;
    }

    public String getModeCode() {
        return modeCode;
    }

    public int getValue() {
        return Value;
    }

    public void setTrame(String trame) {
        this.trame = trame;
        AnalyseTrame();
    }

    public void AnalyseTrame() {
        String trame = "", fonction = "", parametre = "", mode = "", value = "", x = "";
        trame = this.trame;

        fonction = trame.substring(4, 6);
        parametre = trame.substring(7, 9);
        mode = trame.substring(10, 11);
        value = trame.substring(12, trame.indexOf('z'));

        this.fonctionCode = fonction;
        this.parametreCode = parametre;
        this.modeCode = mode;
        this.Value = Integer.parseInt(value);
    }
}
