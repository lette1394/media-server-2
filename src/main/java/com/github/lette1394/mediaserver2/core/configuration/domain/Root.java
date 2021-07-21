package com.github.lette1394.mediaserver2.core.configuration.domain;

import com.github.lette1394.mediaserver2.core.configuration.infrastructure.MultiFileResource;
import java.util.Date;
import java.util.List;
import lombok.Value;

@Value
class Actor {
  public int id;
  public String login;
  public String display_login;
  public String gravatar_id;
  public String url;
  public String avatar_url;
}

@Value
class Repo {
  public int id;
  public String name;
  public String url;
  public String node_id;
  public String full_name;
  public Owner owner;
  public String html_url;
  public String description;
  public boolean fork;
  public String forks_url;
  public String keys_url;
  public String collaborators_url;
  public String teams_url;
  public String hooks_url;
  public String issue_events_url;
  public String events_url;
  public String assignees_url;
  public String branches_url;
  public String tags_url;
  public String blobs_url;
  public String git_tags_url;
  public String git_refs_url;
  public String trees_url;
  public String statuses_url;
  public String languages_url;
  public String stargazers_url;
  public String contributors_url;
  public String subscribers_url;
  public String subscription_url;
  public String commits_url;
  public String git_commits_url;
  public String comments_url;
  public String issue_comment_url;
  public String contents_url;
  public String compare_url;
  public String merges_url;
  public String archive_url;
  public String downloads_url;
  public String issues_url;
  public String pulls_url;
  public String milestones_url;
  public String notifications_url;
  public String labels_url;
  public String releases_url;
  public String deployments_url;
  public Date created_at;
  public Date updated_at;
  public Date pushed_at;
  public String git_url;
  public String ssh_url;
  public String clone_url;
  public String svn_url;
  public String homepage;
  public int size;
  public int stargazers_count;
  public int watchers_count;
  public String language;
  public boolean has_issues;
  public boolean has_projects;
  public boolean has_downloads;
  public boolean has_wiki;
  public boolean has_pages;
  public int forks_count;
  public Object mirror_url;
  public boolean archived;
  public boolean disabled;
  public int open_issues_count;
  public License license;
  public int forks;
  public int open_issues;
  public int watchers;
  public String default_branch;
}

@Value
class Author {
  public String email;
  public String name;
}

@Value
class Commit {
  public String sha;
  public Author author;
  public String message;
  public boolean distinct;
  public String url;
  public String href;
}

@Value
class User {
  public String login;
  public int id;
  public String node_id;
  public String avatar_url;
  public String gravatar_id;
  public String url;
  public String html_url;
  public String followers_url;
  public String following_url;
  public String gists_url;
  public String starred_url;
  public String subscriptions_url;
  public String organizations_url;
  public String repos_url;
  public String events_url;
  public String received_events_url;
  public String type;
  public boolean site_admin;
}

@Value
class RequestedReviewer {
  public String login;
  public int id;
  public String node_id;
  public String avatar_url;
  public String gravatar_id;
  public String url;
  public String html_url;
  public String followers_url;
  public String following_url;
  public String gists_url;
  public String starred_url;
  public String subscriptions_url;
  public String organizations_url;
  public String repos_url;
  public String events_url;
  public String received_events_url;
  public String type;
  public boolean site_admin;
}

@Value
class Label {
  public int id;
  public String node_id;
  public String url;
  public String name;
  public String color;
  public String description;
}

@Value
class Creator {
  public String login;
  public int id;
  public String node_id;
  public String avatar_url;
  public String gravatar_id;
  public String url;
  public String html_url;
  public String followers_url;
  public String following_url;
  public String gists_url;
  public String starred_url;
  public String subscriptions_url;
  public String organizations_url;
  public String repos_url;
  public String events_url;
  public String received_events_url;
  public String type;
  public boolean site_admin;
}

@Value
class Milestone {
  public String url;
  public String html_url;
  public String labels_url;
  public int id;
  public String node_id;
  public int number;
  public String title;
  public String description;
  public Creator creator;
  public int open_issues;
  public int closed_issues;
  public String state;
  public Date created_at;
  public Date updated_at;
  public Object due_on;
  public Object closed_at;
}

@Value
class Owner {
  public String login;
  public int id;
  public String node_id;
  public String avatar_url;
  public String gravatar_id;
  public String url;
  public String html_url;
  public String followers_url;
  public String following_url;
  public String gists_url;
  public String starred_url;
  public String subscriptions_url;
  public String organizations_url;
  public String repos_url;
  public String events_url;
  public String received_events_url;
  public String type;
  public boolean site_admin;
}

@Value
class License {
  public String key;
  public String name;
  public String spdx_id;
  public String url;
  public String node_id;
}

@Value
class Head {
  public String label;
  public String ref;
  public String sha;
  public User user;
  public Repo repo;
}

@Value
class Base {
  public String label;
  public String ref;
  public String sha;
  public User user;
  public Repo repo;
}

@Value
class Self {
  public String href;
}

@Value
class Html {
  public String href;
}

@Value
class Issue {
  public String href;
  public String url;
  public String repository_url;
  public String labels_url;
  public String comments_url;
  public String events_url;
  public String html_url;
  public int id;
  public String node_id;
  public int number;
  public String title;
  public User user;
  public List<Object> labels;
  public String state;
  public boolean locked;
  public Object assignee;
  public List<Object> assignees;
  public Object milestone;
  public int comments;
  public Date created_at;
  public Date updated_at;
  public Date closed_at;
  public String author_association;
  public Object active_lock_reason;
  public String body;
  public Object performed_via_github_app;
}

@Value
class Comments {
  public String href;
}

@Value
class ReviewComments {
  public String href;
}

@Value
class ReviewComment {
  public String href;
}

@Value
class Statuses {
  public String href;
}

@Value
class Links {
  public Self self;
  public Html html;
  public Issue issue;
  public Comments comments;
  public ReviewComments review_comments;
  public ReviewComment review_comment;
  public Statuses statuses;
  public PullRequest pull_request;
}

@Value
class MergedBy {
  public String login;
  public int id;
  public String node_id;
  public String avatar_url;
  public String gravatar_id;
  public String url;
  public String html_url;
  public String followers_url;
  public String following_url;
  public String gists_url;
  public String starred_url;
  public String subscriptions_url;
  public String organizations_url;
  public String repos_url;
  public String events_url;
  public String received_events_url;
  public String type;
  public boolean site_admin;
}

@Value
class PullRequest {
  public String url;
  public int id;
  public String node_id;
  public String html_url;
  public String diff_url;
  public String patch_url;
  public String issue_url;
  public int number;
  public String state;
  public boolean locked;
  public String title;
  public User user;
  public String body;
  public Date created_at;
  public Date updated_at;
  public Date closed_at;
  public Date merged_at;
  public String merge_commit_sha;
  public Object assignee;
  public List<Object> assignees;
  public List<RequestedReviewer> requested_reviewers;
  public List<Object> requested_teams;
  public List<Label> labels;
  public Milestone milestone;
  public boolean draft;
  public String commits_url;
  public String review_comments_url;
  public String review_comment_url;
  public String comments_url;
  public String statuses_url;
  public Head head;
  public Base base;
  public Links _links;
  public String author_association;
  public Object auto_merge;
  public Object active_lock_reason;
  public boolean merged;
  public boolean mergeable;
  public boolean rebaseable;
  public String mergeable_state;
  public MergedBy merged_by;
  public int comments;
  public int review_comments;
  public boolean maintainer_can_modify;
  public int commits;
  public int additions;
  public int deletions;
  public int changed_files;
  public String href;
}

@Value
class Review {
  public int id;
  public String node_id;
  public User user;
  public Object body;
  public String commit_id;
  public Date submitted_at;
  public String state;
  public String html_url;
  public String pull_request_url;
  public String author_association;
  public Links _links;
}

@Value
class Comment {
  public String url;
  public int pull_request_review_id;
  public int id;
  public String node_id;
  public String diff_hunk;
  public String path;
  public int position;
  public int original_position;
  public String commit_id;
  public String original_commit_id;
  public User user;
  public String body;
  public Date created_at;
  public Date updated_at;
  public String html_url;
  public String pull_request_url;
  public String author_association;
  public Links _links;
  public Object start_line;
  public Object original_start_line;
  public Object start_side;
  public int line;
  public int original_line;
  public String side;
}

@Value
class Payload {
  public Object push_id;
  public int size;
  public int distinct_size;
  public String ref;
  public String head;
  public String before;
  public List<Commit> commits;
  public String action;
  public int number;
  public PullRequest pull_request;
  public Issue issue;
  public String ref_type;
  public String master_branch;
  public Object description;
  public String pusher_type;
  public Review review;
  public Comment comment;
}

@Value
class Org {
  public int id;
  public String login;
  public String gravatar_id;
  public String url;
  public String avatar_url;
}

@Value
@SerializedType(List.class)
//@MultiFileResource(directoryPath = "/heavy")
public class Root {
  public String id;
  public String type;
  public Actor actor;
  public Repo repo;
  public Payload payload;
  public Date created_at;
  public Org org;
}


