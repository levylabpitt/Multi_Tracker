
package com.synapse.model.task_detail;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CustomField {

    @SerializedName("gid")
    @Expose
    private String gid;
    @SerializedName("enabled")
    @Expose
    private Boolean enabled;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("resource_subtype")
    @Expose
    private String resourceSubtype;
    @SerializedName("resource_type")
    @Expose
    private String resourceType;
    @SerializedName("text_value")
    @Expose
    private String textValue;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("enum_options")
    @Expose
    private List<EnumOption> enumOptions = null;
    @SerializedName("enum_value")
    @Expose
    private Object enumValue;
    @SerializedName("number_value")
    @Expose
    private Integer numberValue;
    @SerializedName("precision")
    @Expose
    private Integer precision;

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResourceSubtype() {
        return resourceSubtype;
    }

    public void setResourceSubtype(String resourceSubtype) {
        this.resourceSubtype = resourceSubtype;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getTextValue() {
        return textValue;
    }

    public void setTextValue(String textValue) {
        this.textValue = textValue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<EnumOption> getEnumOptions() {
        return enumOptions;
    }

    public void setEnumOptions(List<EnumOption> enumOptions) {
        this.enumOptions = enumOptions;
    }

    public Object getEnumValue() {
        return enumValue;
    }

    public void setEnumValue(Object enumValue) {
        this.enumValue = enumValue;
    }

    public Integer getNumberValue() {
        return numberValue;
    }

    public void setNumberValue(Integer numberValue) {
        this.numberValue = numberValue;
    }

    public Integer getPrecision() {
        return precision;
    }

    public void setPrecision(Integer precision) {
        this.precision = precision;
    }

}
