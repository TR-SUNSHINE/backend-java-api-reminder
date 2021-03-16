# backend-java-api-reminder

A Java backend api created to serve the Sunshine frontend repository. The project uses the 
serverless framework to deploy the project to AWS & create AWS Lambda functions. The Lambda 
functions serve GET, POST, PUT & DELETE requests for reminders and interact with the Sunshine 
RDS database to retrieve, add, amend and delete data from the reminders table. 

To set up the project code:
* Install the serverless cli on your machine: [install serverless](https://www.serverless.com/framework/docs/getting-started/)
* Set up an AWS IAM user called 'serverless' with programmatic access [set up IAM user](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_users_create.html)
* Copy the AWS key & secret or download the CSV file from the AWS IAM console
* Create the following file:
  ~/.aws/credentials
* Add the serverless credentials to the .aws/credentials file [add serverless credentials](https://www.serverless.com/framework/docs/providers/aws/guide/credentials/#setup-with-serverless-config-credentials-command)
  `serverless config credentials --provider aws --key AKIAIOSFODNN7EXAMPLE --secret wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY --profile serverless`
* When you input `cat ~/.aws/credentials`, it should output \
  `[serverless]`\
  `aws_access_key_id=AKIAIOSFODNN7EXAMPLE`\
  `aws_secret_access_key=wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY`
* create a config.dev.json and store your database credentials in the following format:
  `DB_HOST: <hostname>
  DB_NAME: <databasename>
  DB_USER: <database username>
  DB_PASSWORD: <database password>`

To build & deploy:
* `mvn install`
* `serverless deploy`
