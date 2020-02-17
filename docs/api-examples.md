# Fisheye API examples
Call:
```
curl -H "Accept: application/json" "https://fisheye.com/rest-service/reviews-v1/filter?states=Review&project=CR-EXAMPLE&fromDate=1581000000000&FEAUTH=XXX"
```

Response:
```json
{
  "reviewData": [
    {
      "projectKey": "CR-EXAMPLE",
      "name": "[EXAMPLE-2544] Included test for feature disabling in UserProfileManager",
      "description": "* [EXAMPLE-2544] Included test for feature disabling in UserProfileManager",
      "author": {
        "userName": "jsmith",
        "displayName": "John Smith",
        "avatarUrl": "https://fisheye.com/avatar/jsmith?s=48",
        "url": "/user/jsmith"
      },
      "creator": {
        "userName": "jsmith",
        "displayName": "John Smith",
        "avatarUrl": "https://fisheye.com/avatar/jsmith?s=48",
        "url": "/user/jsmith"
      },
      "permaId": {
        "id": "CR-EXAMPLE-1137"
      },
      "permaIdHistory": [
        "CR-EXAMPLE-1137"
      ],
      "state": "Review",
      "type": "REVIEW",
      "allowReviewersToJoin": true,
      "metricsVersion": 1,
      "createDate": "2020-02-06T14:13:33.140+0000",
      "dueDate": "2020-02-10T14:00:00.000+0000",
      "reminderDate": "2020-02-07T14:00:00.000+0000",
      "linkedIssues": [
        "EXAMPLE-2544"
      ],
      "jiraIssueKey": "EXAMPLE-2544"
    }
  ]
}
```
See [Official documentation](https://docs.atlassian.com/fisheye-crucible/latest/wadl/crucible.html#rest-service:reviews-v1:filter:filter)
