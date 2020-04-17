google-login
============

A Jenkins plugin which lets you login to Jenkins with your Google account. Also allows you to restrict access
to accounts in a given Google Apps domain.

To use this plugin, you must obtain OAuth 2.0 credentials from the
[Google Developers Console](https://console.developers.google.com). These don't need to belong to a special account,
or even one associated with the domain you want to restrict logins to.

Instructions to create the Client ID and Secret:

 1. Login to the [Google Developers Console](https://console.developers.google.com)
 1. Create a new project
 1. Under APIs & Services -> Credentials, Create credentials, OAuth client ID
 1. The application type should be "Web Application"
 1. The authorized redirect URLs should contain ${JENKINS_ROOT_URL}/securityRealm/finishLogin
 1. Enter the created Client ID and secret in the Security Realm Configuration

Building:
=========

``` bash
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
mvn install -Dmaven.test.failure.ignore -Djava.level=8 -DskipTests -Dmaven.javadoc.skip=true -e
```

