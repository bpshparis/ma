# Mailbox Analyzer

MailBox Analyzer is an application using [Watson Developer Cloud Java SDK](https://github.com/watson-developer-cloud/java-sdk) to demonstrate how to use the [Watson Developer Cloud services](https://www.ibm.com/watson/products-services/), a collection of REST APIs and SDKs that use cognitive computing to solve complex problems.

<br>

## Table of Contents

* [Overview of the application](#overview-of-the-application)
* [Application Flow](#application-flow)
* [Prerequisite](#prerequisite)
* [Login to IBM Cloud](#login-to-ibm-cloud)
* [Setup environment](#setup-environment)
* [Check environment is setup correctly](#check-environment-is-setup-correctly)
* [Setup application](#setup-application)
* [Deploy application](#deploy-application)
* [Run application](#run-application)
* [Send your own datas for analysis](#send-your-own-datas-for-analysis)
* [About Watson Developer Cloud services being used in the application](#about-watson-developer-cloud-services-being-used-in-the-application)
* [About other Watson Developer Cloud services](#about-other-watson-developer-cloud-services)

<br>

### Overview of the application

A sample demo of the application with a mailbox analysis *may be* available [here](http://ma.bpshparis.eu-de.mybluemix.net).

<br>

### Application Flow
![Flow](images/mailbox.analyzer.flow.jpg)

<br>

### Prerequisite

<br>

#### Install needed softwares

> :bulb: Ctrl + Click on links below to open them in new tab and keep the tutorial tab opened.

![](res/mac.png)

* Download and install [IBM Cloud CLI](https://console.bluemix.net/docs/cli/reference/ibmcloud/download_cli.html)  
* Download and install [curl](https://curl.haxx.se/dlwiz/?type=bin&os=Mac+OS+X&flav=-&ver=-&cpu=i386)
* Download and install [jq](https://github.com/stedolan/jq/releases/download/jq-1.5/jq-osx-amd64)

![](res/tux.png)

* Download and install [IBM Cloud CLI](https://console.bluemix.net/docs/cli/reference/ibmcloud/download_cli.html)  
* Get **curl** from your distribution repository or download and install it from [here](https://curl.haxx.se/dlwiz/?type=bin&os=Linux).
* Get **jq** from your distribution repository or download and install it from [here](https://github.com/stedolan/jq/releases/download/jq-1.5/jq-linux64).

<br>

#### Check everything is installed properly

![](res/mac.png) ![](res/tux.png)

Inside a terminal &nbsp; ![](res/term.png) 

Check ibmcloud command is available:

	ibmcloud -v

Check curl command is available:

	curl -V

Check jq command is available:

	jq

<br>

#### Check your IBM Cloud account

Before being able to work with IBM Cloud you should be aware of **2** things:
  1. the name of your **organization**, which is the same among all Regions (Germany, Sydney, United Kingdom, US South and US East).
  2. the name of one **space** - which is assigned to one Region only - in one Region (Germany, Sydney, United Kingdom, US South and US East) in your organization.

> At least one organization has been created automatically, and one space called **dev** is created for you.
If not sure about organization name and if a space is available then log in [IBM Cloud console](https://console.bluemix.net/account/manage-orgs), click 'Cloud Foundry Orgs' then view details, check that 'Cloud Foundry Spaces in Region' is not empty and if so then Add a Cloud Foundry Space.

:checkered_flag: Now you should know both your organization and your space in one Region and your are ready to setup your environment in IBM Cloud.

<br>

#### Add some environment variables and aliases

![](res/mac.png) ![](res/tux.png)

Inside a terminal &nbsp; ![](res/term.png) 

:bulb: **ORG**, **USERID** and **SPACE** have to be substituted with your own environment variables

Set your IBM Cloud Organization

	export ORG=teatcher0@bpshparis.com

Set your IBM Cloud userid
	
	export USERID=teatcher0@bpshparis.com

Set your IBM Cloud space
	
	export SPACE=dev

Add some aliases

	cat >> aliases << EOF
	alias iclus='/usr/local/bin/ibmcloud login -a ${US_ENDPOINT} -u ${USERID} --skip-ssl-validation -s ${SPACE} -o ${ORG}' 
	alias iclgb='/usr/local/bin/ibmcloud login -a ${GB_ENDPOINT} -u ${USERID} --skip-ssl-validation -s ${SPACE} -o ${ORG}' 
	alias iclde='/usr/local/bin/ibmcloud login -a ${DE_ENDPOINT -u ${USERID} --skip-ssl-validation -s ${SPACE} -o ${ORG}' 
	alias iclsso='/usr/local/bin/ibmcloud login -u ${USERID} --sso' 
	alias ic='/usr/local/bin/ibmcloud'
	alias iclo='/usr/local/bin/ibmcloud logout'
	alias l='ls -Alhtr' 
	export US_ENDPOINT=https://api.ng.bluemix.net
	export GB_ENDPOINT=https://api.eu-gb.bluemix.net
	export DE_ENDPOINT=https://api.eu-de.bluemix.net
	export US_REGION=us-south
	export GB_REGION=eu-gb
	export DE_REGION=eu-de
	EOF

Add aliases to your environment

	. aliases
	
:bulb: To display what's hiding behind aliases use **command -v**

	command -v iclde
	
will display

	alias iclde='/usr/local/bin/ibmcloud login -a https://api.eu-de.bluemix.net -u teatcher0@bpshparis.com --skip-ssl-validation -s dev -o teatcher0@bpshparis.com'			

<br>

### Login to IBM Cloud

Let's connect to :de:

Inside a terminal &nbsp; ![](res/term.png) 

	iclde	

> :no_entry: If **login failed** because of logging in with a federated ID, then browse one a the following url:

> :bulb: Ctrl + Click on links below to open them in new tab and keep the tutorial tab opened.

 * https://login.ng.bluemix.net/UAALoginServerWAR/passcode
 * https://login.eu-gb.bluemix.net/UAALoginServerWAR/passcode
 * https://login.eu-de.bluemix.net/UAALoginServerWAR/passcode
 
> and get a one-time passcode.

> Then back to your terminal &nbsp; ![](res/term.png), login with **--sso**,

	iclsso
	
> paste the one-time passcode when prompt

	One Time Code (Get one at https://login.eu-gb.bluemix.net/UAALoginServerWAR/passcode)>
	
> and hit enter.

<br>


### Setup environment

#### Dump marketplace to get service name, plan and description

![](res/mac.png) ![](res/tux.png)

> It may take a minute to display :zzz: 

	ibmcloud service offerings | tee marketplace

<br>

#### Setup Tone Analyzer service

![](res/ta50x.png) **Tone Analyzer** uses linguistic analysis to detect three types of tones from communications: emotion, social, and language.  This insight can then be used to drive high impact communications.

##### Get name and plan for Tone Analyzer service

![](res/mac.png) ![](res/tux.png)

	grep -i tone marketplace

##### Create Tone Analyzer service
	ibmcloud service create tone_analyzer lite ta0

##### Create service key (credential) for Tone Analyzer service
	ibmcloud service key-create ta0 user0

<br>

#### Setup Natural Language Understanding

![](res/nlu50x.png) **Natural Language Understanding** analyze text to extract meta-data from content such as concepts, entities, emotion, relations, sentiment and more.

##### Get name and plan for Natural Language Understanding service

![](res/mac.png) ![](res/tux.png)

	grep -i language marketplace

##### Create Natural Language Understanding service
	ibmcloud service create natural-language-understanding free nlu0

##### Create service key (credential) for Natural Language Understanding service
	ibmcloud service key-create nlu0 user0

<br>

#### Setup Discovery service

![](res/dsc50x.png) **Discovery** add a cognitive search and content analytics engine to applications.

##### Get name and plan for Discovery service

![](res/mac.png) ![](res/tux.png)

	grep -i discovery marketplace

##### Create Discovery service

	ibmcloud service create discovery lite dsc0

##### Create service key (credential) for Discovery service

	ibmcloud service key-create dsc0 user0

#### Create Discovery Collection

To make life easier for further steps we will set some Discovery service variable

##### Store Discovery url in URL environment variable

![](res/mac.png) ![](res/tux.png)

	URL=$(ic service key-show dsc0 user0 | awk 'NR >= 4 {print}' | jq -r '.url')

##### Store Discovery credential in CRED environment variable

![](res/mac.png) ![](res/tux.png)

	CRED=$(ic service key-show dsc0 user0 | awk 'NR >= 4 {print}' | jq -r '.username + ":" + .password')

##### Store Discovery version in VERSION environment variable

![](res/mac.png) ![](res/tux.png)

	DSC_VERSION=2018-03-05

##### Store Discovery language in LANG environment variable

> Choose a language model among this list:

> * en
> * es
> * de
> * ar
> * **fr**
> * it
> * ja
> * ko
> * pt
> * nl

![](res/mac.png) ![](res/tux.png)

	DSC_LANG=fr

Before being able to create a collection **2** steps have to be completed:

##### Create **env0** environment for Discovery service and store its id in ENVID

![](res/mac.png) ![](res/tux.png)

	ENVID=$(curl  -X POST -u ${CRED} -H 'Content-Type: application/json' -d '{"name": "env0"}' ${URL}'/v1/environments?version='${DSC_VERSION} | jq -r '.environment_id')

<!--
Get **environment_id** for Discovery service
	curl -u ${CRED} '${url}/v1/environments?version=${DSC_VERSION}' | jq -r --arg ENV env0 '.environments[] | select(.name == $ENV) | .environment_id'
	curl -X POST -u ${CRED} -H 'Content-Type: application/json' -X DELETE ${URL}'/v1/environments/${ENVID}?version='${DSC_VERSION}
-->

##### Create **configuration** for Discovery service and store its id in CONFID

![](res/mac.png) ![](res/tux.png)

	CONFID=$(curl -u ${CRED} ${URL}/v1/environments/${ENVID}/configurations?version=${DSC_VERSION} | jq -r '.configurations[].configuration_id')

<!--
Get **configuration_id** for Discovery service
	curl -u ${username}:${password} '${url}/v1/environments/${ENVID}/configurations?version=${DSC_VERSION}' | jq -r '.configurations[] | .configuration_id'
-->

:checkered_flag: Now, you should be ready to create the collection.

##### Create collection **coll0** for Discovery service and and store its id in COLLID

![](res/mac.png) ![](res/tux.png)

	curl -X POST -H 'Content-Type: application/json' -u ${CRED} -d '{"name": "coll0", "configuration_id":"'${CONFID}'" , "language": "'${DSC_LANG}'"}' ${URL}/v1/environments/${ENVID}/collections?version=${DSC_VERSION}

> :bulb: You won't need neither environment_id nor configuration_id for further use but keep **env0** and **coll0** in mind.

<br>

#### Create Visual Recognition service

![](res/wvc50x.png) **Visual Recognition** find meaning in visual content! Analyze images for scenes, objects, faces, and other content. Choose a default model off the shelf, or create your own custom classifier. Develop smart applications that analyze the visual content of images or video frames to understand what is happening in a scene.

##### Get name and plan for Visual Recognition service

![](res/mac.png) ![](res/tux.png)

	grep -i visual marketplace

##### Create Visual Recognition service

	ibmcloud resource service-instance-create wvc0 watson-vision-combined lite us-south	
	
	ibmcloud  resource service-alias-create wvc0 --instance-name wvc0

##### Create service key (credential) for Visual Recognition service

	ibmcloud service key-create wvc0 user0

<br>

#### Check environment is setup correctly

> :thumbsup: You are done with environment setup. Now at least four Watson services should be created (**ta0, nlu0, dsc0 and wvc0**) in your space.

>Check it with

	ibmcloud service list

<br>
	
### Setup application

#### Get application code

Download code

	curl -LO  https://github.com/bpshparis/ma/archive/master.zip

Unzip it

	unzip master.zip

#### Prepare for application deployment

Change to code directory

	cd ma-master

> Now if you stand in the correct directory, you should be able to list directory such as **WebContent** and file such as **manifest.yml**.

>Before deploying the application you need to choose **3** things:

> :skull: **Don't use special characters. Use [a-z],[A-Z],[0-9] and [-] only.**
> * A **host** (must be unique in a region or domain) for your application (e.g.: **mylastname-mycompagny**)
> * A **name** (must be unique in your space) for your application (e.g.: **myapp0**)
> * A **domain** available thanks to the following command

	ic app domains | awk 'NR==5{print $1}'

Edit the **manifest.yml** and update it accordingly by substituting **${host}**, **${name}** and **${domain}**:

	applications:
	- host: ${host}
	  disk: 256M
	  name: ${name}
	  path: ./WebContent
	  domain: ${domain}
	  mem: 256M
	  instances: 1
	  services:
	  - dsc0  
	  - ta0
	  - nlu0
	  - wvc0

<br>

### Deploy application

> :skull: For deployment to work you need to push your code from the same directory as **manifest.yml**.

:checkered_flag: Now you are ready to deploy the application :

	ic app push

Once staging has completed you should be able to run the application *on your own IBM Cloud environment*.

<br>

### Run application

Display your application state :

	ic app list
	
Copy urls columns content. It should be **${host}.${domain}** (e.g.:**mylastname-mycompagny.eu-gb.mybluemix.net**).

Paste it in a web browser and check application is running :

Click on ![](res/envelope.png) to upload sample attached documents in Discovery Collection and get sample mails.

Once mails are displayed, click ![](res/cogwheels.png) to send sample mails for analysis.

When Watson returned, **4 new tabs** (one per service) should appear and are ready to browse. 

### Send your own datas for analysis

Edit a json file of this form :

```
[
  {
    "subject": ${paste some text between double quotation marks or set to null},
    "content": ${paste some text between double quotation marks or set to null},
    "attached": ${paste a doc|docx|pdf file name between double quotation marks or set to null},
    "picture": ${paste a picture file name between double quotation marks or set to null},
    "face": ${paste a picture file name between double quotation marks or set to null},
    "tip": ${paste a picture file name between double quotation marks or set to null}
  }
]
```

An example for 2 mails with documents and pictures attached :

```
[
  {
      "subject": "At UEFA, Mounting Concern About A.C. Milan’s Murky Finances",
      "content": null,
      "attached": "3.pdf",
      "picture": "pic3.jpg",
      "face": null,
      "tip": null
  },
  {
      "subject": null,
      "content": "At a flea market six years ago, a North Carolina lawyer named Frank Abrams unknowingly bought...",
      "attached": "4.doc",
      "picture": null,
      "face": "face4.jpg",
      "tip": "tip4.jpg"
  }
]
```

:bulb: Save this file as **mails.json** and test it with jq :

	jq . mails.json

The command should display pretty json without error.

Now **zip mails.json with all files set in attached, picture, face and tip fields from mails.json**.

Test you archive (e.g.: unzip -t mails0.zip):

```
Archive:  mails0.zip
    testing: 3.doc                    OK
    testing: 3.pdf                    OK
    testing: 4.doc                    OK
    testing: face4.jpg                OK
    testing: mails.json               OK
    testing: pic3.jpg                 OK
    testing: tip4.jpg                 OK
No errors detected in compressed data of mails0.zip.
```
Now go back to your application and click ![](res/compressed.png) to upload your datas.

Once your datas have been upload, click on ![](res/envelope.png) to upload your attached documents in Discovery Collection and get your mails.

Once your mails are displayed, click ![](res/cogwheels.png) to send your mails for analysis.

<br>

### About Watson Developer Cloud services being used in the application
![](res/ta50x.png) **Tone Analyzer** uses linguistic analysis to detect three types of tones from communications: emotion, social, and language.  This insight can then be used to drive high impact communications.

[Documentation](https://console.bluemix.net/docs/services/tone-analyzer/getting-started.html)
[Dashboard](https://www.ibm.com/watson/developercloud/dashboard/en/tone-analyzer-dashboard.html)
[Github](https://github.com/watson-developer-cloud)


![](res/nlu50x.png)

[Documentation](https://console.bluemix.net/docs/services/natural-language-understanding/getting-started.html)
[Dashboard](https://www.ibm.com/watson/developercloud/dashboard/en/natural-language-understanding-dashboard.html)
[Github](https://github.com/watson-developer-cloud)

> **Natural Language Understanding** analyze text to extract meta-data from content such as concepts, entities, emotion, relations, sentiment and more.

![](res/dsc50x.png)

[Documentation](https://console.bluemix.net/docs/services/discovery/getting-started.html)
[Dashboard](https://www.ibm.com/watson/developercloud/dashboard/en/discovery-dashboard.html)
[Github](https://github.com/watson-developer-cloud)
[Tool](https://watson-discovery.bluemix.net)

> **Discovery** add a cognitive search and content analytics engine to applications.

![](res/wvc50x.png)

[Documentation](https://console.bluemix.net/docs/services/visual-recognition/getting-started.html)
[Dashboard](https://www.ibm.com/smarterplanet/us/en/ibmwatson/developercloud/dashboard/en/visual-recognition-dashboard.html)
[Github](https://github.com/watson-developer-cloud)
[Tool](https://watson-visual-recognition.ng.bluemix.net/)

> **Visual Recognition** find meaning in visual content! Analyze images for scenes, objects, faces, and other content. Choose a default model off the shelf, or create your own custom classifier. Develop smart applications that analyze the visual content of images or video frames to understand what is happening in a scene.

### About other Watson Developer Cloud services

![](res/s2t50x.png)

[Documentation](https://console.bluemix.net/docs/services/speech-to-text/getting-started.html)
[Dashboard](https://www.ibm.com/watson/developercloud/dashboard/en/speech-to-text-dashboard.html)
[Github](https://github.com/watson-developer-cloud)

> **Speech to Text** Low-latency, streaming transcription.

![](res/t2s50x.png)

[Documentation](https://console.bluemix.net/docs/services/text-to-speech/getting-started.html)
[Dashboard](https://www.ibm.com/watson/developercloud/dashboard/en/text-to-speech-dashboard.html)
[Github](https://github.com/watson-developer-cloud)

> **Text to Speech** Synthesizes natural-sounding speech from text.

![](res/lt50x.png)

[Documentation](https://console.bluemix.net/docs/services/language-translator/getting-started.html)
[Dashboard](https://www.ibm.com/watson/developercloud/dashboard/en/language-translator-dashboard.html)
[Github](https://github.com/watson-developer-cloud)

> **Language Translator** Translate text from one language to another for specific domains.

![](res/pi50x.png)

[Documentation](https://console.bluemix.net/docs/services/personality-insights/getting-started.html)
[Dashboard](https://www.ibm.com/watson/developercloud/dashboard/en/personality-insights-dashboard.html)
[Github](https://github.com/watson-developer-cloud)

> **Personality Insights** The Watson Personality Insights derives insights from transactional and social media data to identify psychological traits

![](res/cvt50x.png)

[Documentation](https://console.bluemix.net/docs/services/conversation/getting-started.html)
[Dashboard](https://www.ibm.com/watson/developercloud/dashboard/en/conversation-dashboard.html)
[Github](https://github.com/watson-developer-cloud)
[Tool](https://watson-conversation.ng.bluemix.net)

> **Conversation** Add a natural language interface to your application to automate interactions with your end users. Common applications include virtual agents and chat bots that can integrate and communicate on any channel or device.

![](res/nlc50x.png)

[Documentation](https://console.bluemix.net/docs/services/natural-language-classifier/getting-started.html)
[Dashboard](https://www.ibm.com/watson/developercloud/dashboard/en/natural-language-classifier-dashboard.html)
[Github](https://github.com/watson-developer-cloud)
[Tool](https://natural-language-classifier-toolkit.eu-gb.bluemix.net)

> **Natural Language Classifier** performs natural language classification on question texts. A user would be able to train their data and the predict the appropriate class for a input question.
