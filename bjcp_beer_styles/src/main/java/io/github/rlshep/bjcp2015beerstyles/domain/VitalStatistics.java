package io.github.rlshep.bjcp2015beerstyles.domain;

public class VitalStatistics {

    private long id;
    private long categoryId;
    private String ogStart;
    private String ogEnd;
    private String fgStart;
    private String fgEnd;
    private String ibuStart;
    private String ibuEnd;
    private String srmStart;
    private String srmEnd;
    private String abvStart;
    private String abvEnd;
    private String header = "";

    public VitalStatistics() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getOgStart() {
        return ogStart;
    }

    public void setOgStart(String ogStart) {
        this.ogStart = ogStart;
    }

    public String getOgEnd() {
        return ogEnd;
    }

    public void setOgEnd(String ogEnd) {
        this.ogEnd = ogEnd;
    }

    public String getFgStart() {
        return fgStart;
    }

    public void setFgStart(String fgStart) {
        this.fgStart = fgStart;
    }

    public String getFgEnd() {
        return fgEnd;
    }

    public void setFgEnd(String fgEnd) {
        this.fgEnd = fgEnd;
    }

    public String getIbuStart() {
        return ibuStart;
    }

    public void setIbuStart(String ibuStart) {
        this.ibuStart = ibuStart;
    }

    public String getIbuEnd() {
        return ibuEnd;
    }

    public void setIbuEnd(String ibuEnd) {
        this.ibuEnd = ibuEnd;
    }

    public String getSrmStart() {
        return srmStart;
    }

    public void setSrmStart(String srmStart) {
        this.srmStart = srmStart;
    }

    public String getSrmEnd() {
        return srmEnd;
    }

    public void setSrmEnd(String srmEnd) {
        this.srmEnd = srmEnd;
    }

    public String getAbvStart() {
        return abvStart;
    }

    public void setAbvStart(String abvStart) {
        this.abvStart = abvStart;
    }

    public String getAbvEnd() {
        return abvEnd;
    }

    public void setAbvEnd(String abvEnd) {
        this.abvEnd = abvEnd;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}