package com.app.ashesha.v3care.services;

public class ServicesListModel {
    String serviceId;
    String serviceName;
    String serviceImage;
    String inclusion;
    String exclusion;


    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceImage() {
        return serviceImage;
    }

    public void setServiceImage(String serviceImage) {
        this.serviceImage = serviceImage;
    }

    public String getInclusion()
    {
        return  inclusion;
    }
    public void setInclusion(String inclusion) {
        this.inclusion = inclusion;
    }
    public String getExclusion()
    {
        return  exclusion;
    }
    public void setExclusion(String exclusion) {
        this.exclusion = exclusion;
    }
}
