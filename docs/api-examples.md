# Fisheye API examples
Call:
```
curl "https://fisheye.com/rest-service/reviews-v1/filter?states=Review&project=CR-EXAMPLE&fromDate=1581000000000&FEAUTH=XXX"
```

Response:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<reviews>
   <reviewData>
      <allowReviewersToJoin>true</allowReviewersToJoin>
      <author>
         <avatarUrl>https://fisheye.com/avatar/jsmith?s=48</avatarUrl>
         <displayName>John Smith</displayName>
         <userName>jsmith</userName>
      </author>
      <createDate>2020-02-06T14:13:33.140+0000</createDate>
      <creator>
         <avatarUrl>https://fisheye.com/avatar/jsmith?s=48</avatarUrl>
         <displayName>John Smith</displayName>
         <userName>jsmith</userName>
      </creator>
      <description>* [EXAMPLE-2544] Updating docs</description>
      <dueDate>2020-02-10T14:00:00.000+0000</dueDate>
      <jiraIssueKey>EXAMPLE-2544</jiraIssueKey>
      <linkedIssues>EXAMPLE-2544</linkedIssues>
      <metricsVersion>1</metricsVersion>
      <name>[EXAMPLE-2544] Updating docs</name>
      <permaId>
         <id>CR-EXAMPLE-1137</id>
      </permaId>
      <permaIdHistory>CR-EXAMPLE-1137</permaIdHistory>
      <projectKey>CR-EXAMPLE</projectKey>
      <reminderDate>2020-02-07T14:00:00.000+0000</reminderDate>
      <state>Review</state>
      <type>REVIEW</type>
   </reviewData>
   <!-- More reviews ... -->
</reviews>
```
