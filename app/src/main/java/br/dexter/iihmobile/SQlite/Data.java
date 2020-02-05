package br.dexter.iihmobile.SQlite;

public class Data
{
    private long id;
    private String local;
    private String localidade;
    private String city;
    private String state;
    private String data;
    private float ScoreFinal;
    private float Q1, Q2, Q3, Q4, Q5, Q6, Q7, Q8, Q9, Q10, Q11, Q12;
    private float latitude, longitude;

    float getQ1() {return Q1;}
    public void setQ1(float Q1) {this.Q1 = Q1;}

    float getQ2() {return Q2;}
    public void setQ2(float Q2) {this.Q2 = Q2;}

    float getQ3() {return Q3;}
    public void setQ3(float Q3) {this.Q3 = Q3;}

    float getQ4() {return Q4;}
    public void setQ4(float Q4) {this.Q4 = Q4;}

    float getQ5() {return Q5;}
    public void setQ5(float Q5) {this.Q5 = Q5;}

    float getQ6() {return Q6;}
    public void setQ6(float Q6) {this.Q6 = Q6;}

    float getQ7() {return Q7;}
    public void setQ7(float Q7) {this.Q7 = Q7;}

    float getQ8() {return Q8;}
    public void setQ8(float Q8) {this.Q8 = Q8;}

    float getQ9() {return Q9;}
    public void setQ9(float Q9) {this.Q9 = Q9;}

    float getQ10() {return Q10;}
    public void setQ10(float Q10) {this.Q10 = Q10;}

    float getQ11() {return Q11;}
    public void setQ11(float Q11) {this.Q11 = Q11;}

    float getQ12() {return Q12;}
    public void setQ12(float Q12) {this.Q12 = Q12;}

    float getScoreFinal() {return ScoreFinal;}
    public void setScoreFinal(float ScoreFinal) {this.ScoreFinal = ScoreFinal;}

    float getLatitude() {return latitude;}
    public void setLatitude(float latitude) {this.latitude = latitude;}

    float getLongitude() {return longitude;}
    public void setLongitude(float longitude) {this.longitude = longitude;}

    String getCity() {return city;}
    public void setCity(String city) {this.city = city;}

    String getState() {return state;}
    public void setState(String state) {this.state = state;}

    String getData() {return data;}
    public void setData(String data) {this.data = data;}

    String getLocal() {return local;}
    public void setLocal(String local) {this.local = local;}

    String getLocalidade() {return localidade;}
    public void setLocalidade(String localidade) {this.localidade = localidade;}

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}
}
