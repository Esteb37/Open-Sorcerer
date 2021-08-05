import json
import requests
import urllib
from flask import Flask
from flask_restful import Api, Resource
from requests.structures import CaseInsensitiveDict
from selenium import webdriver

app = Flask(__name__)
api = Api(app)

# Parse API data
parseHeaders = CaseInsensitiveDict()
parseHeaders["X-Parse-Application-Id"] = "pxRxTLlBZdZPk8Tt7SyhgruAop4vl8rRQR2DXYUh"
parseHeaders["X-Parse-REST-API-Key"] = "AtJu505v2cs2mtj3XlHipjGqAeYfyCODZ7rodjrv"
serverURL = "https://parseapi.back4app.com//parse/"

# Github API data
githubURL = "github.com/"
githubHeaders = CaseInsensitiveDict()
githubHeaders["Authorization"] = "Basic ZXN0ZWIzNzpnaHBfZmRYWnRodTV3c085emt6c2NDd3FTcDJpTWtBOGJZMmNwcW55"
githubHeaders["Accept"] = "application/vnd.github.mercy-preview+json"
githubAPI = "https://api.github.com/repos/"

"""
    Class for handling the project scraper
"""
class ScrapeProjects(Resource):

    """
        Post function for starting the scraper
    """
    def post(self):

        # Launch the chrome web driver
        path = r'/Users/estebanpadilla/Desktop/Open Sorcerer REST/chromedriver'
        driver = webdriver.Chrome(executable_path=path)

        categories = ["featured", "cloud", "analytics-visualization", "databases",
                      "developer-tools", "education", "enterprise", "games", "graphics-video-audio",
                      "i18n", "iot", "mobile", "machine-learning", "geo", "networking",
                      "programming", "samples", "security", "testing", "utilities", "web"]

        # For each category
        for category in categories:
            print("----" + category + "----")

            # Load the catalogue
            driver.get('https://opensource.google/projects/list/' + category)

            # Get a list of project card items
            projects = []
            while (len(projects) == 0):
                projects = driver.find_elements_by_class_name("project-card")

            # Get the links to each project
            links = [project.get_attribute('href') for project in projects]

            # For each project link
            for link in links:

                # Load the project's page
                driver.get(link)

                repo = ""
                image = ""
                while (repo == ""):

                    # Find all table cells
                    tds = driver.find_elements_by_tag_name("td")

                    if (len(tds) > 0):

                        # Get the link from the first table cell, which contains the repository link
                        repo = tds[0].find_element_by_tag_name("a").get_attribute('href')

                        # Try to get the image from the logo, if there is none, assign Google Open Source's logo
                        try:
                            image = driver.find_element_by_class_name(
                                "logo").find_element_by_tag_name("img").get_attribute("src")
                        except:
                            image = "https://www.mathieuchartier.com/wp-content/uploads/blog_image_1.jpg"

                # If the repository link is from github
                if (githubURL in repo):

                    # Get the repository name
                    reponame = repo.split(githubURL)[1]

                    if (projectIsNotCreated(reponame)):

                        try:

                            # Create a project from the repository data
                            data = requests.get(githubAPI + reponame, headers=githubHeaders).json()
                            createProject(data, repo, image)

                        except ValueError:
                            print("No data", repo)

                    else:
                        print("Project already exists.")

        return {"data": "Completed"}

    """
        Determines if the project is already in the database
    """
    def projectIsNotCreated(reponame):

        try:

            # Make a query for the project by its repository name
            params = urllib.urlencode({"where": json.dumps({
                "githubName": reponame
            })})

            url = serverURL + "classes/Project/?" + params

            resp = requests.get(url, headers=parseHeaders)

            # Return if the query threw any results
            return (resp.json()["results"] == [])

        except KeyError:
            return True

    """
        Creates a project in the Parse Database from the repository information
    """
    def createProject(data, repo, image):

        reponame = repo.split(githubURL)[1]

        # Get the project's information
        try:
            title = data["name"].replace("_", " ").capitalize()

        except KeyError:
            print("No name", repo)
            return

        try:
            description = data["description"]
        except:
            description = ""

        try:
            homepage = data["homepage"]
            if (len(homepage) == 0):
                homepage = repo
        except:
            homepage = repo

        try:
            resp = requests.get(githubAPI + reponame + "/topics", headers=githubHeaders)
            tags = resp.json()["names"]
        except:
            tags = []

        try:
            resp = requests.get(githubAPI + reponame + "/languages", headers=githubHeaders)
            languages = resp.json().keys()
        except:
            languages = []

        # Set the project's information
        project = CaseInsensitiveDict()
        project["title"] = title
        project["description"] = description
        project["website"] = homepage
        project["tags"] = tags
        project["languages"] = languages
        project["repository"] = repo
        project["logoUrl"] = image
        project["githubName"] = reponame

        # Save the project in the database
        url = serverURL + "classes/Project/"
        resp = requests.post(url, headers=parseHeaders, data=project)


api.add_resource(ScrapeProjects, "/scrape_projects")

if __name__ == "__main__":
    app.run(debug=False, host='0.0.0.0')
