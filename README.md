Original App Design Project
===

# Open Sorcerer

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description

Open Sorcerer is a social network dedicated to connecting developers with Open Source projects by allowing the user to browse a growing catalogue of projects they can participate in or upload their own ideas in order to find developers willing to help. The main browsing experience is shaped by the user's set of preferences and technical skills, and the recommendation algorithm gradually understands their interests and brings the most relevant projects to their attention.

### App Evaluation

- **Category:** Social network / Browser
- **Mobile:** Incorporates swiping and double tapping mechanisms for liking and discarding projects
- **Story:** The app creates a link between developers looking to work in open source projects and project leaders looking to find members of their team. It uses recommendation algorithms to present developers with interesting projects that adjust to their abilities. 
- **Market:** Independent developers that want to expand their skills and project leaders looking for teams.
- **Habit:** Have a place to find, save and like the projects you're interested in, keep track of your experience and give feedback / Use the app to find developers for your project quickly, receive feedback and create teams easily
- **Scope:** Users can like and save projects they find interesting and the algorithm will recommend more based on that. Chat to communicate developers with project leaders. Project leaders can see who is interested in their projects. Integration with GitHub for easy access to repositories and ReadMe's. Integration with Google's and other's open source libraries. 

## Product Spec

### 1. User Stories (Required and Optional)

### **Required Must-have Stories**

* **Developers**
    * Login (with GitHub)
    * Set skills and interests
    * Project browsing based on skills and interests
    * Viewing project's repo and ReadMe
    * Liking and saving projects
    * Contacting project leaders
    * Profile with experience
    * See projects uploaded to the app and to other libraries

* **Project leaders**
    * Login (with GitHub)
    * Create a new project
    * Connect project to GitHub
    * See who is interested in the project
    * Contacting developers
    * Profile with experience

### **Optional Nice-to-have Stories**
* Add developers to teams
* Clone project to GitHub
* Boosting project reach through pay
* Add comments and grading system
* Project sharing
* Liked projects organization 
* Get statistics


### 2. Screen Archetypes

* Login and signup
    * Login/signup as developer
    * Login/signup as project leader
    * Connect to GitHub
   
* Developer account setup
    * Set skills
    * Set interests
    * Set experience
    * Set description
   
* Project browser
    * Card view with project info
    * Recommendation algorithm based on skills and interests
    * Like or dislike project
    * Projects obtained both from the uploaded directly to the app and from libraries like Google's Open Source Library 
    
* Developer project info page
    * View project's repo and readme
    * Save project
    * Contact project leader
    * Comment and score

* Chat
    * Communication between project leaders and developers
    * Saved chats

* Developer Profile view
    * View and edit personal information
    * View liked and saved projects
    * View experience

* Project creation
    * Add project information
    * Connect project to GitHub

* Project leader project info page
    * View interested developers
    * View comments and scores
    * View project views
    * Contact interested developers


### 3. Navigation

**Tab Navigation** (Tab to Screen)

**Developer**
* Home (Main browser)
* Profile view
* Chats
* Liked projects

**Project leader**
* Home (My projects)
* Profile view
* Chats
* Create project

**Flow Navigation** (Screen to Screen)

** Developer** 
* Login or signup
    * Login
    * Signup
   
* Login
    * Project card browser
    
* Signup
    * Account setup
    * Connect with github
    
* Home - Project card browser
    * Project information
    * Profile view
    * Chats
    * Liked projects
    
* Project information
    * Chat with project leader
    * Project GitHub page (external)
    
* Profile view
    * Liked projects
    * Profile edit
    
**Project leader**
* Login or signup
    * Login
    * Signup
    
* Signup
    * Account setup
    * Connect with github

* Login
    * My projects
    
* Home - My projects
    * Project information
    * Profile view
    * Chats
    * Create project
    
* Project information
    * Project GitHub page (external)
    * Chat with interested developers
    



## Wireframes
[Add picture of your hand sketched wireframes in this section]
<img src="YOUR_WIREFRAME_IMAGE_URL" width=600>

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 
[This section will be completed in Unit 9]
### Models
[Add table of models]
### Networking
- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]
