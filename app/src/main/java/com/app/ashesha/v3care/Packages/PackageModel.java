package com.app.ashesha.v3care.Packages;

import java.io.Serializable;

public class PackageModel implements Serializable {

    String packageId;
    String packageImage;
    String service_id;
    String package_name;
    String service_slug;
    String package_price;
    String sub_package_status;

    String inclusion;
    String exclusionl;

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getService_slug() {
        return service_slug;
    }

    public void setService_slug(String service_slug) {
        this.service_slug = service_slug;
    }

    public String getPackage_price() {
        return package_price;
    }

    public void setPackage_price(String package_price) {
        this.package_price = package_price;
    }


    public String getSub_package_status() {
        return sub_package_status;
    }

    public void setSub_package_status(String sub_package_status) {
        this.sub_package_status = sub_package_status;
    }

    public String getPackageImage() {
        return packageImage;
    }

    public void setPackageImage(String packageImage) {
        this.packageImage = packageImage;
    }

    public String getInclusion() {
        return inclusion;
    }

    public void setInclusion(String inclusion) {
        this.inclusion = inclusion;
    }

    public String getExclusionl() {
        return exclusionl;
    }

    public void setExclusionl(String exclusionl) {
        this.exclusionl = exclusionl;
    }
}
