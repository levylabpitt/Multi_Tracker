
package com.synapse.model.task_detail;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("gid")
    @Expose
    private String gid;
    @SerializedName("assignee")
    @Expose
    private Object assignee;
    @SerializedName("assignee_status")
    @Expose
    private String assigneeStatus;
    @SerializedName("completed")
    @Expose
    private Boolean completed;
    @SerializedName("completed_at")
    @Expose
    private Object completedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("custom_fields")
    @Expose
    private List<CustomField> customFields = null;
    @SerializedName("due_at")
    @Expose
    private Object dueAt;
    @SerializedName("due_on")
    @Expose
    private Object dueOn;
    @SerializedName("followers")
    @Expose
    private List<Follower> followers = null;
    @SerializedName("hearted")
    @Expose
    private Boolean hearted;
    @SerializedName("hearts")
    @Expose
    private List<Object> hearts = null;
    @SerializedName("liked")
    @Expose
    private Boolean liked;
    @SerializedName("likes")
    @Expose
    private List<Object> likes = null;
    @SerializedName("memberships")
    @Expose
    private List<Membership> memberships = null;
    @SerializedName("modified_at")
    @Expose
    private String modifiedAt;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("num_hearts")
    @Expose
    private Integer numHearts;
    @SerializedName("num_likes")
    @Expose
    private Integer numLikes;
    @SerializedName("parent")
    @Expose
    private Object parent;
    @SerializedName("permalink_url")
    @Expose
    private String permalinkUrl;
    @SerializedName("projects")
    @Expose
    private List<Project_> projects = null;
    @SerializedName("resource_type")
    @Expose
    private String resourceType;
    @SerializedName("start_on")
    @Expose
    private Object startOn;
    @SerializedName("tags")
    @Expose
    private List<Tag> tags = null;
    @SerializedName("resource_subtype")
    @Expose
    private String resourceSubtype;
    @SerializedName("workspace")
    @Expose
    private Workspace workspace;

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public Object getAssignee() {
        return assignee;
    }

    public void setAssignee(Object assignee) {
        this.assignee = assignee;
    }

    public String getAssigneeStatus() {
        return assigneeStatus;
    }

    public void setAssigneeStatus(String assigneeStatus) {
        this.assigneeStatus = assigneeStatus;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Object getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(Object completedAt) {
        this.completedAt = completedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<CustomField> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<CustomField> customFields) {
        this.customFields = customFields;
    }

    public Object getDueAt() {
        return dueAt;
    }

    public void setDueAt(Object dueAt) {
        this.dueAt = dueAt;
    }

    public Object getDueOn() {
        return dueOn;
    }

    public void setDueOn(Object dueOn) {
        this.dueOn = dueOn;
    }

    public List<Follower> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Follower> followers) {
        this.followers = followers;
    }

    public Boolean getHearted() {
        return hearted;
    }

    public void setHearted(Boolean hearted) {
        this.hearted = hearted;
    }

    public List<Object> getHearts() {
        return hearts;
    }

    public void setHearts(List<Object> hearts) {
        this.hearts = hearts;
    }

    public Boolean getLiked() {
        return liked;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public List<Object> getLikes() {
        return likes;
    }

    public void setLikes(List<Object> likes) {
        this.likes = likes;
    }

    public List<Membership> getMemberships() {
        return memberships;
    }

    public void setMemberships(List<Membership> memberships) {
        this.memberships = memberships;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
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

    public Integer getNumHearts() {
        return numHearts;
    }

    public void setNumHearts(Integer numHearts) {
        this.numHearts = numHearts;
    }

    public Integer getNumLikes() {
        return numLikes;
    }

    public void setNumLikes(Integer numLikes) {
        this.numLikes = numLikes;
    }

    public Object getParent() {
        return parent;
    }

    public void setParent(Object parent) {
        this.parent = parent;
    }

    public String getPermalinkUrl() {
        return permalinkUrl;
    }

    public void setPermalinkUrl(String permalinkUrl) {
        this.permalinkUrl = permalinkUrl;
    }

    public List<Project_> getProjects() {
        return projects;
    }

    public void setProjects(List<Project_> projects) {
        this.projects = projects;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Object getStartOn() {
        return startOn;
    }

    public void setStartOn(Object startOn) {
        this.startOn = startOn;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public String getResourceSubtype() {
        return resourceSubtype;
    }

    public void setResourceSubtype(String resourceSubtype) {
        this.resourceSubtype = resourceSubtype;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Workspace workspace) {
        this.workspace = workspace;
    }

}
