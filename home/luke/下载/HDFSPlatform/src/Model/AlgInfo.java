package Model;

//algorithm info
public class AlgInfo {
    private int paramK;
    private int paramN;
    private int zeroNum;
    private String algName;

    public AlgInfo(int paramK, int paramN, int zeroNum, String algName){
        this.paramK = paramK;
        this.paramN = paramN;
        this.zeroNum = zeroNum;
        this.algName = algName;
    }

    public int getParamK() {
        return paramK;
    }

    public int getParamN() {
        return paramN;
    }

    public String getAlgName() {
        return algName;
    }

    public void setParamK(int paramK) {
        this.paramK = paramK;
    }

    public void setParamN(int paramN) {
        this.paramN = paramN;
    }

    public void setAlgName(String algName) {
        this.algName = algName;
    }
}
