package org.jenkinsci.plugins.googlelogin;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.cloudidentity.v1.CloudIdentity;
import com.google.api.services.cloudidentity.v1.CloudIdentityScopes;
import com.google.api.services.cloudidentity.v1.model.GroupRelation;
import com.google.api.services.cloudidentity.v1.model.SearchTransitiveGroupsResponse;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class GoogleIdentityGroups {

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    // public static void main(String[] args) throws IOException,
    // GeneralSecurityException {
    // String userEmail = "user@yourdomain.com"; // Replace with the actual user
    // email

    // List<GroupRelation> transitiveGroups = getTransitiveGroupsForUser(userEmail);

    // if (transitiveGroups != null && !transitiveGroups.isEmpty()) {
    // System.out.println("Groups " + userEmail + " is a member of (including
    // nested):");
    // for (GroupRelation group : transitiveGroups) {
    // String groupId = group.getGroupKey() != null ? group.getGroupKey().getId() :
    // "N/A";
    // String groupDisplayName = group.getDisplayName() != null ?
    // group.getDisplayName() : "N/A";
    // String relationType = group.getRelationType() != null ?
    // group.getRelationType() : "N/A";
    // System.out.println("- " + groupDisplayName + " (ID: " + groupId + ") -
    // Relation: " + relationType);
    // }
    // } else {
    // System.out.println("No groups found for " + userEmail + " or an error
    // occurred.");
    // }
    // }

    public static CloudIdentity buildCloudIdentityService(GoogleCredential credential)
            throws IOException, GeneralSecurityException {

        return new CloudIdentity.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                .setApplicationName("google-login Jenkins plugin").build();

    }

    public static List<GroupRelation> getTransitiveGroupsForUser(GoogleCredential credential, String userEmail) {
        List<GroupRelation> allTransitiveGroups = new ArrayList<>();
        try {
            CloudIdentity service = buildCloudIdentityService(credential);
            String pageToken = null;

            do {
                CloudIdentity.Groups.Memberships.SearchTransitiveGroups request = service.groups().memberships()
                        .searchTransitiveGroups("groups/-")
                        .setQuery("member_key_id == \"" + userEmail + "\"");

                if (pageToken != null) {
                    request.setPageToken(pageToken);
                }

                SearchTransitiveGroupsResponse response = request.execute();

                if (response.getMemberships() != null) {
                    allTransitiveGroups.addAll(response.getMemberships());
                }

                pageToken = response.getNextPageToken();

            } while (pageToken != null);

        } catch (IOException | GeneralSecurityException e) {
            System.err.println("Error retrieving transitive groups: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return allTransitiveGroups;
    }
}
