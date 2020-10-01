package com.app.trueleap.gradebook.model;

public class GradeItem {
    String gradetype;
    double gradeweight;
    String gradename;
    boolean compulsary;
    double compulsarypassmark;
    String assessmentdate;
    double outof;
    double bestoutof;
    boolean partofmidtermgrade;

    public GradeItem(String gradetype, double gradeweight, String gradename, boolean compulsary, double compulsarypassmark, String assessmentdate, double outof, double bestoutof, boolean partofmidtermgrade) {
        this.gradetype = gradetype;
        this.gradeweight = gradeweight;
        this.gradename = gradename;
        this.compulsary = compulsary;
        this.compulsarypassmark = compulsarypassmark;
        this.assessmentdate = assessmentdate;
        this.outof = outof;
        this.bestoutof = bestoutof;
        this.partofmidtermgrade = partofmidtermgrade;
    }

    public String getGradetype() {
        return gradetype;
    }

    public void setGradetype(String gradetype) {
        this.gradetype = gradetype;
    }

    public double getGradeweight() {
        return gradeweight;
    }

    public void setGradeweight(double gradeweight) {
        this.gradeweight = gradeweight;
    }

    public String getGradename() {
        return gradename;
    }

    public void setGradename(String gradename) {
        this.gradename = gradename;
    }

    public boolean isCompulsary() {
        return compulsary;
    }

    public void setCompulsary(boolean compulsary) {
        this.compulsary = compulsary;
    }

    public double getCompulsarypassmark() {
        return compulsarypassmark;
    }

    public void setCompulsarypassmark(double compulsarypassmark) {
        this.compulsarypassmark = compulsarypassmark;
    }

    public String getAssessmentdate() {
        return assessmentdate;
    }

    public void setAssessmentdate(String assessmentdate) {
        this.assessmentdate = assessmentdate;
    }

    public double getOutof() {
        return outof;
    }

    public void setOutof(double outof) {
        this.outof = outof;
    }

    public double getBestoutof() {
        return bestoutof;
    }

    public void setBestoutof(double bestoutof) {
        this.bestoutof = bestoutof;
    }

    public boolean isPartofmidtermgrade() {
        return partofmidtermgrade;
    }

    public void setPartofmidtermgrade(boolean partofmidtermgrade) {
        this.partofmidtermgrade = partofmidtermgrade;
    }
}


