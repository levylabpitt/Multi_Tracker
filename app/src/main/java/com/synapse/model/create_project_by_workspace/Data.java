
package com.synapse.model.create_project_by_workspace;

import java.util.List;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("gid")
    @Expose
    private String gid;
    @SerializedName("resource_type")
    @Expose
    private String resourceType;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("modified_at")
    @Expose
    private String modifiedAt;
    @SerializedName("due_date")
    @Expose
    private Object dueDate;
    @SerializedName("due_on")
    @Expose
    private Object dueOn;
    @SerializedName("current_status")
    @Expose
    private Object currentStatus;
    @SerializedName("public")
    @Expose
    private Boolean _public;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("archived")
    @Expose
    private Boolean archived;
    @SerializedName("workspace")
    @Expose
    private Workspace workspace;
    @SerializedName("team")
    @Expose
    private Team team;
    @SerializedName("permalink_url")
    @Expose
    private String permalinkUrl;
    @SerializedName("is_template")
    @Expose
    private Boolean isTemplate;
    @SerializedName("default_view")
    @Expose
    private String defaultView;
    @SerializedName("start_on")
    @Expose
    private Object startOn;
    @SerializedName("color")
    @Expose
    private String color;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("followers")
    @Expose
    private List<Object> followers = null;
    @SerializedName("owner")
    @Expose
    private Owner owner;
    @SerializedName("custom_field_settings")
    @Expose
    private List<Object> customFieldSettings = null;
    @SerializedName("members")
    @Expose
    private List<Member> members = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Data() {
    }

    /**
     * 
     * @param owner
     * @param workspace
     * @param gid
     * @param notes
     * @param dueOn
     * @param color
     * @param customFieldSettings
     * @param currentStatus
     * @param _public
     * @param modifiedAt
     * @param dueDate
     * @param icon
     * @param startOn
     * @param team
     * @param permalinkUrl
     * @param createdAt
     * @param archived
     * @param defaultView
     * @param followers
     * @param isTemplate
     * @param members
     * @param name
     * @param resourceType
     */
    public Data(String gid, String resourceType, String createdAt, String modifiedAt, Object dueDate, Object dueOn, Object currentStatus, Boolean _public, String name, String notes, Boolean archived, Workspace workspace, Team team, String permalinkUrl, Boolean isTemplate, String defaultView, Object startOn, String color, String icon, List<Object> followers, Owner owner, List<Object> customFieldSettings, List<Member> members) {
        super();
        this.gid = gid;
        this.resourceType = resourceType;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.dueDate = dueDate;
        this.dueOn = dueOn;
        this.currentStatus = currentStatus;
        this._public = _public;
        this.name = name;
        this.notes = notes;
        this.archived = archived;
        this.workspace = workspace;
        this.team = team;
        this.permalinkUrl = permalinkUrl;
        this.isTemplate = isTemplate;
        this.defaultView = defaultView;
        this.startOn = startOn;
        this.color = color;
        this.icon = icon;
        this.followers = followers;
        this.owner = owner;
        this.customFieldSettings = customFieldSettings;
        this.members = members;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public Object getDueDate() {
        return dueDate;
    }

    public void setDueDate(Object dueDate) {
        this.dueDate = dueDate;
    }

    public Object getDueOn() {
        return dueOn;
    }

    public void setDueOn(Object dueOn) {
        this.dueOn = dueOn;
    }

    public Object getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(Object currentStatus) {
        this.currentStatus = currentStatus;
    }

    public Boolean getPublic() {
        return _public;
    }

    public void setPublic(Boolean _public) {
        this._public = _public;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getPermalinkUrl() {
        return permalinkUrl;
    }

    public void setPermalinkUrl(String permalinkUrl) {
        this.permalinkUrl = permalinkUrl;
    }

    public Boolean getIsTemplate() {
        return isTemplate;
    }

    public void setIsTemplate(Boolean isTemplate) {
        this.isTemplate = isTemplate;
    }

    public String getDefaultView() {
        return defaultView;
    }

    public void setDefaultView(String defaultView) {
        this.defaultView = defaultView;
    }

    public Object getStartOn() {
        return startOn;
    }

    public void setStartOn(Object startOn) {
        this.startOn = startOn;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<Object> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Object> followers) {
        this.followers = followers;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public List<Object> getCustomFieldSettings() {
        return customFieldSettings;
    }

    public void setCustomFieldSettings(List<Object> customFieldSettings) {
        this.customFieldSettings = customFieldSettings;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }

}
