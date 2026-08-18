package com.ruoyi.hospital.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 医生工作台就诊明细
 *
 * @author ruoyi
 */
public class HospitalDoctorWorkbenchVisitItem
{
    private Long visitId;

    private String registrationNo;

    private String patientName;

    private String patientPhone;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date visitTime;

    private String chiefComplaint;

    private String diagnosis;

    private String status;

    public Long getVisitId()
    {
        return visitId;
    }

    public void setVisitId(Long visitId)
    {
        this.visitId = visitId;
    }

    public String getRegistrationNo()
    {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo)
    {
        this.registrationNo = registrationNo;
    }

    public String getPatientName()
    {
        return patientName;
    }

    public void setPatientName(String patientName)
    {
        this.patientName = patientName;
    }

    public String getPatientPhone()
    {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone)
    {
        this.patientPhone = patientPhone;
    }

    public Date getVisitTime()
    {
        return visitTime;
    }

    public void setVisitTime(Date visitTime)
    {
        this.visitTime = visitTime;
    }

    public String getChiefComplaint()
    {
        return chiefComplaint;
    }

    public void setChiefComplaint(String chiefComplaint)
    {
        this.chiefComplaint = chiefComplaint;
    }

    public String getDiagnosis()
    {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis)
    {
        this.diagnosis = diagnosis;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }
}
