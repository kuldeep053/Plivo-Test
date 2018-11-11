# Plivo-Test
Test Automation for Plivo APIs

The code contains test cases based on testNG framework.
Here is how it works:
1. The test case starts from AllTestSuite.xml which specifies class and method to run.
2. Test data is in the form of json files(placed in resources folder) passed through data providers to the test method.
3. Json file specifies the task to be carried out by the test method
4. Test class(PlivoTest) inherits from PlivoBaseTest.
5. PlivoBaseTest has objects of Validator class and PlivoClient class(makes Plivo API calls) and utility methods.
6. PlivoClient uses defaultHttpClient class which internally uses using okHttp to make Http calls.
