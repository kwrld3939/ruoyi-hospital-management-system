package com.ruoyi.hospital.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 医生工作台预约明细
 *
 * @author ruoyi
 */
public class HospitalDoctorWorkbenchRegistrationItem
{
    private Long registrationId;

    private String registrationNo;

    private String patientName;

    private String patientCode;

    private String patientPhone;

    private String departmentName;

    private String doctorName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date registrationTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date visitDate;

    private String timeSlot;

    private String location;

    private String status;

    public Long getRegistrationId()
    {
        return registrationId;
    }

    public void setRegistrationId(Long registrationId)
    {
        this.registrationId = registrationId;
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

    public String getPatientCode()
    {
        return patientCode;
    }

    public void setPatientCode(String patientCode)
    {
        this.patientCode = patientCode;
    }

    public String getPatientPhone()
    {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone)
    {
        this.patientPhone = patientPhone;
    }

    public String getDepartmentName()
    {
        return departmentName;
    }

    public void setDepartmentName(String departmentName)
    {
        this.departmentName = departmentName;
    }

    public String getDoctorName()
    {
        return doctorName;
    }

    public void setDoctorName(String doctorName)
    {
        this.doctorName = doctorName;
    }

    public Date getRegistrationTime()
    {
        return registrationTime;
    }

    public void setRegistrationTime(Date registrationTime)
    {
        this.registrationTime = registrationTime;
    }

    public Date getVisitDate()
    {
        return visitDate;
    }

    public void setVisitDate(Date visitDate)
    {
        this.visitDate = visitDate;
    }

    public String getTimeSlot()
    {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot)
    {
        this.timeSlot = timeSlot;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
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
